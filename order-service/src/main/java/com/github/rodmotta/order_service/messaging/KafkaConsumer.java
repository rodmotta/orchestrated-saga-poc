package com.github.rodmotta.order_service.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.rodmotta.order_service.messaging.event.*;
import com.github.rodmotta.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaConsumer {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final OrderService orderService;

    @KafkaListener(topics = "payment.approved", groupId = "order-service")
    public void paymentApprovedEvent(String event) throws JsonProcessingException {
        PaymentApprovedEvent paymentApprovedEvent = objectMapper.readValue(event, PaymentApprovedEvent.class);
        orderService.paymentApproved(paymentApprovedEvent);
    }

    @KafkaListener(topics = "payment.failed", groupId = "order-service")
    public void paymentFailedEvent(String event) throws JsonProcessingException {
        PaymentFailedEvent paymentFailedEvent = objectMapper.readValue(event, PaymentFailedEvent.class);
        orderService.paymentFailed(paymentFailedEvent);
    }

    @KafkaListener(topics = "inventory.reserved", groupId = "order-service")
    public void inventoryReservedEvent(String event) throws JsonProcessingException {
        InventoryReservedEvent inventoryReservedEvent = objectMapper.readValue(event, InventoryReservedEvent.class);
        orderService.inventoryReserved(inventoryReservedEvent);
    }

    @KafkaListener(topics = "inventory.failed", groupId = "order-service")
    public void inventoryFailedEvent(String event) throws JsonProcessingException {
        InventoryFailedEvent inventoryFailedEvent = objectMapper.readValue(event, InventoryFailedEvent.class);
        orderService.inventoryFailed(inventoryFailedEvent);
    }

    @KafkaListener(topics = "payment.refunded", groupId = "order-service")
    public void paymentRefundedEvent(String event) throws JsonProcessingException {
        PaymentRefundedEvent paymentRefundedEvent = objectMapper.readValue(event, PaymentRefundedEvent.class);
        orderService.paymentRefunded(paymentRefundedEvent);
    }
}
