package com.github.rodmotta.payment_service.messaging.event;

import java.util.UUID;

public record PaymentFailedEvent(
        UUID orderId
) {
}
