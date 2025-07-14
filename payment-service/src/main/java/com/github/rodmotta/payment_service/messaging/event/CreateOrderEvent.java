package com.github.rodmotta.payment_service.messaging.event;

import java.util.UUID;

public record CreateOrderEvent(
        UUID orderId
) {
}
