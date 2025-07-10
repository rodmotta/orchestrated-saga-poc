package com.github.rodmotta.payment_service.event;

import java.time.LocalDateTime;
import java.util.UUID;

public record OrderEvent(
        UUID id,
        LocalDateTime createdAt,
        String status
) {
}
