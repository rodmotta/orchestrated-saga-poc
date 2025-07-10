package com.github.rodmotta.payment_service.service;

import com.github.rodmotta.payment_service.event.OrderEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final KafkaTemplate<String, String> kafkaTemplate;

    @KafkaListener(topics = "order.created", groupId = "payment-service")
    public void pay(OrderEvent message) throws InterruptedException {
        Thread.sleep(3000);

        Random random = new Random();
        int randomNumber = random.nextInt(5);

        if (randomNumber == 0) {
            kafkaTemplate.send("payment.failed", message.id().toString());
            return;
        }
        kafkaTemplate.send("payment.approved", message.id().toString());
    }
}
