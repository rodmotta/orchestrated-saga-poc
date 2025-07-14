package com.github.rodmotta.inventory_service.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.rodmotta.inventory_service.messaging.event.InventoryReserveEvent;
import com.github.rodmotta.inventory_service.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaConsumer {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final InventoryService inventoryService;

    @KafkaListener(topics = "inventory.reserve", groupId = "inventory-service")
    public void checkInventory(String event) throws InterruptedException, JsonProcessingException {
        InventoryReserveEvent inventoryReserveEvent = objectMapper.readValue(event, InventoryReserveEvent.class);
        inventoryService.checkInventory(inventoryReserveEvent);
    }
}
