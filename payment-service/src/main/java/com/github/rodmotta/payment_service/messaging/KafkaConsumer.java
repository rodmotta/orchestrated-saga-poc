package com.github.rodmotta.payment_service.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.rodmotta.payment_service.messaging.event.CreateOrderEvent;
import com.github.rodmotta.payment_service.messaging.event.PaymentRefundEvent;
import com.github.rodmotta.payment_service.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaConsumer {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final PaymentService paymentService;

    @KafkaListener(topics = "order.created", groupId = "payment-service")
    public void pay(String event) throws InterruptedException, JsonProcessingException {
        CreateOrderEvent createOrderEvent = objectMapper.readValue(event, CreateOrderEvent.class);
        paymentService.pay(createOrderEvent);
    }

    @KafkaListener(topics = "payment.refund", groupId = "payment-service")
    public void paymentRefund(String event) throws JsonProcessingException, InterruptedException {
        PaymentRefundEvent paymentRefundEvent = objectMapper.readValue(event, PaymentRefundEvent.class);
        paymentService.refund(paymentRefundEvent);
    }
}
