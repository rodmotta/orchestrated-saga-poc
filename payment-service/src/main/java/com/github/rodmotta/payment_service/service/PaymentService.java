package com.github.rodmotta.payment_service.service;

import com.github.rodmotta.payment_service.messaging.KafkaProducer;
import com.github.rodmotta.payment_service.messaging.event.CreateOrderEvent;
import com.github.rodmotta.payment_service.messaging.event.PaymentRefundEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final KafkaProducer kafkaProducer;

    public void pay(CreateOrderEvent event) throws InterruptedException {
        Thread.sleep(3000);

        Random random = new Random();
        int randomNumber = random.nextInt(5);

        if (randomNumber == 0) {
            kafkaProducer.paymentFailedEvent(event.orderId());
            return;
        }
        kafkaProducer.paymentApprovedEvent(event.orderId());
    }

    public void refund(PaymentRefundEvent event) throws InterruptedException {
        Thread.sleep(3000);

        kafkaProducer.paymentRefundedEvent(event.orderId());
    }
}
