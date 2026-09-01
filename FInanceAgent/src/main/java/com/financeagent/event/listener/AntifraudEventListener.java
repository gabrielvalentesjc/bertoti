package com.financeagent.event.listener;

import com.financeagent.event.CommandExecutedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class AntifraudEventListener {

    private static final Logger log = LoggerFactory.getLogger(AntifraudEventListener.class);

    @Async("agentEventExecutor")
    @EventListener(condition = "#event.commandName().startsWith('AnalyzeRiskCommand')")
    public void onRiskCommandExecuted(CommandExecutedEvent event) {
        log.warn("[ANTIFRAUDE] Avaliando resultado assincronamente: {} (correlationId={})",
                event.result(), event.correlationId());
    }
}
