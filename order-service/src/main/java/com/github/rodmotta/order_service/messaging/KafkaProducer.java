package com.github.rodmotta.order_service.messaging;

import com.github.rodmotta.order_service.messaging.event.CreateOrderEvent;
import com.github.rodmotta.order_service.messaging.event.InventoryReserveEvent;
import com.github.rodmotta.order_service.messaging.event.PaymentRefundEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class KafkaProducer {
    private final KafkaTemplate<String, CreateOrderEvent> createOrderEventKafkaTemplate;
    private final KafkaTemplate<String, InventoryReserveEvent> inventoryReserveEventKafkaTemplate;
    private final KafkaTemplate<String, PaymentRefundEvent> paymentRefundEventKafkaTemplate;

    public void createOrderEvent(UUID orderId) {
        var event = new CreateOrderEvent(orderId);
        createOrderEventKafkaTemplate.send("order.created", event);
    }

    public void inventoryReserveEvent(UUID orderId) {
        var event = new InventoryReserveEvent(orderId);
        inventoryReserveEventKafkaTemplate.send("inventory.reserve", event);
    }

    public void paymentRefund(UUID orderId) {
        var event = new PaymentRefundEvent(orderId);
        paymentRefundEventKafkaTemplate.send("payment.refund", event);
    }
}
