package com.github.rodmotta.inventory_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final KafkaTemplate<String, String> kafkaTemplate;

    @KafkaListener(topics = "payment.approved", groupId = "inventory-service")
    public void checkInventory(String orderId) throws InterruptedException {
        orderId = orderId.replace("\"", "");
        Thread.sleep(3000);

        Random random = new Random();
        int randomNumber = random.nextInt(5);

        if (randomNumber == 0) {
            kafkaTemplate.send("inventory.failed", orderId);
            return;
        }
        kafkaTemplate.send("inventory.reserved", orderId);
    }
}
