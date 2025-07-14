package com.github.rodmotta.inventory_service.messaging.event;

import java.util.UUID;

public record InventoryFailedEvent(
        UUID orderId
) {
}
