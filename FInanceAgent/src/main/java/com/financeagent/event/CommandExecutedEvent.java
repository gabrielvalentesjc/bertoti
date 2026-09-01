package com.financeagent.event;

import java.time.Instant;

public record CommandExecutedEvent(
        String commandName,
        Object result,
        String correlationId,
        Instant executedAt
) {
}
