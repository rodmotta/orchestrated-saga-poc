package com.github.rodmotta.inventory_service.service;

import com.github.rodmotta.inventory_service.messaging.KafkaProducer;
import com.github.rodmotta.inventory_service.messaging.event.InventoryReserveEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final KafkaProducer kafkaProducer;

    public void checkInventory(InventoryReserveEvent event) throws InterruptedException {
        Thread.sleep(3000);

        Random random = new Random();
        int randomNumber = random.nextInt(5);

        if (randomNumber == 0) {
            kafkaProducer.inventoryFailedEvent(event.orderId());
            return;
        }
        kafkaProducer.inventoryReservedEvent(event.orderId());
    }
}
