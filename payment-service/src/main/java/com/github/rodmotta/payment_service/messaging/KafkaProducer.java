package com.github.rodmotta.payment_service.messaging;

import com.github.rodmotta.payment_service.messaging.event.PaymentApprovedEvent;
import com.github.rodmotta.payment_service.messaging.event.PaymentFailedEvent;
import com.github.rodmotta.payment_service.messaging.event.PaymentRefundEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class KafkaProducer {
    private final KafkaTemplate<String, PaymentApprovedEvent> paymentApprovedEventKafkaTemplate;
    private final KafkaTemplate<String, PaymentFailedEvent> paymentFailedEventKafkaTemplate;
    private final KafkaTemplate<String, PaymentRefundEvent> paymentRefundEventKafkaTemplate;

    public void paymentApprovedEvent(UUID orderId) {
        var event = new PaymentApprovedEvent(orderId);
        paymentApprovedEventKafkaTemplate.send("payment.approved", event);
    }

    public void paymentFailedEvent(UUID orderId) {
        var event = new PaymentFailedEvent(orderId);
        paymentFailedEventKafkaTemplate.send("payment.failed", event);
    }

    public void paymentRefundedEvent(UUID orderId) {
        var event = new PaymentRefundEvent(orderId);
        paymentRefundEventKafkaTemplate.send("payment.refunded", event);
    }
}
