package com.financeagent.event.listener;

import com.financeagent.event.CommandExecutedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class ComplianceEventListener {

    private static final Logger log = LoggerFactory.getLogger(ComplianceEventListener.class);

    @Async("agentEventExecutor")
    @EventListener
    public void onCommandExecuted(CommandExecutedEvent event) {
        log.info("[COMPLIANCE] Registrando operacao '{}' (correlationId={}) para trilha de auditoria regulatoria",
                event.commandName(), event.correlationId());
    }
}
