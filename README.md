# Processamento de Pedidos (POC - Saga Orquestrada com Kafka)

Este repositório simula o fluxo de processamento de pedidos. O objetivo principal é aplicar meus estudos sobre comunicação assíncrona com Apache Kafka e implementar o padrão Saga Orquestrada. Nele, um orquestrador central (order-service) coordena o fluxo entre o serviço de pagamento (payment-service) e controle de estoque (inventory-service), ilustrando como diferentes estados de um pedido são gerenciados de forma consistente em um ambiente distribuído.

## 🧪 Como Testar

### Pré-requisitos

- Ter o docker instalado

### 1- Suba a aplicação

Abra seu terminal, navegue até o diretório onde o arquivo docker-compose.yml está localizado e execute o seguinte comando:

```bash
docker-compose up --build
```

### 2- Crie um pedido

Com a aplicação em execução, você pode criar um novo pedido. Abra o terminal e envie uma requisição POST para a API de pedidos:

```bash
curl --location --request POST 'http://localhost:8080/v1/orders'
```

Este comando simula a criação de um pedido, acionando o fluxo de processamento de pagamento e estoque.

### 3- Visualize os pedidos

Para verificar o status dos pedidos criados, faça uma requisição GET para a API de pedidos:

```bash
curl --location 'http://localhost:8080/v1/orders'
```

## 🎯 Possíveis Respostas

Ao visualizar o pedido, você pode encontrar diferentes cenários. Lembre-se que, por ser uma simulação, há um delay de 3 segundos entre as etapas de processamento.

### ✅ Caso de Sucesso

Se o pagamento for aprovado e o estoque reservado com sucesso, a resposta será similar a esta:

``` json
{
    "id": "03485bb3-a039-48f4-8ffa-22eccb764617",
    "createdAt": "2025-07-14T17:13:41.467006",
    "status": "COMPLETED",
    "paymentStatus": "APPROVED",
    "inventoryStatus": "RESERVED"
}
```

### ❌ Caso de Falha no Pagamento

Se o pagamento não for aprovado, o pedido será cancelado e o estoque não será afetado:

``` json
{
    "id": "cfa103ec-d6c9-4367-a3eb-2b1072271863",
    "createdAt": "2025-07-14T17:13:42.089668",
    "status": "CANCELLED",
    "paymentStatus": "FAILED",
    "inventoryStatus": null
}
```

### ⚠️ Caso de Falha no Estoque (Estorno do Pagamento)

Se o pagamento for aprovado, mas houver problemas com o estoque (por exemplo, falta de itens), o pagamento será estornado e o pedido cancelado:

``` json
{
    "id": "0af5133e-08f4-47f8-a65f-d683e128a158",
    "createdAt": "2025-07-14T17:13:45.302536",
    "status": "CANCELLED",
    "paymentStatus": "REFUNDED",
    "inventoryStatus": "OUT_OF_STOCK"
}
```

## 📊 Tópicos Kafka

| Tópico | Função | Produtor | Consumidor |
|-|-|--|-|
| `order.created`      | Evento de criação de pedido     | order-service     | payment-service     |
| `payment.approved`   | Evento de pagamento aprovado    | payment-service   | order-service       |
| `payment.failed`     | Evento de pagamento recusado    | payment-service   | order-service       |
| `inventory.reserve`  | Comando para reservar estoque   | order-service     | inventory-service   |
| `inventory.reserved` | Evento de estoque reservado     | inventory-service | order-service       |
| `inventory.failed`   | Evento de falha no estoque      | inventory-service | order-service       |
| `payment.refund`     | Comando de solicitar estorno    | order-service     | payment-service     |
| `payment.refunded`   | Evento de pagamento estornado   | payment-service   | order-service       |