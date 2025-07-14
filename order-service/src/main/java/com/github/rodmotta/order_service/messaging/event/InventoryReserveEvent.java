package com.github.rodmotta.order_service.messaging.event;

import java.util.UUID;

public record InventoryReserveEvent(
        UUID orderId
) {
}
