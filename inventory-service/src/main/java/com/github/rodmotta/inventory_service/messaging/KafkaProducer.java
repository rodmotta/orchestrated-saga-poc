package com.github.rodmotta.inventory_service.messaging;

import com.github.rodmotta.inventory_service.messaging.event.InventoryFailedEvent;
import com.github.rodmotta.inventory_service.messaging.event.InventoryReservedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class KafkaProducer {
    private final KafkaTemplate<String, InventoryReservedEvent> inventoryReservedEventKafkaTemplate;
    private final KafkaTemplate<String, InventoryFailedEvent> inventoryFailedEventKafkaTemplate;

    public void inventoryReservedEvent(UUID orderId) {
        var event = new InventoryReservedEvent(orderId);
        inventoryReservedEventKafkaTemplate.send("inventory.reserved", event);
    }

    public void inventoryFailedEvent(UUID orderId) {
        var event = new InventoryFailedEvent(orderId);
        inventoryFailedEventKafkaTemplate.send("inventory.failed", event);
    }
}
