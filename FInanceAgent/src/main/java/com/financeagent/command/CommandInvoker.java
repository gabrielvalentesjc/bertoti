package com.financeagent.command;

import com.financeagent.agent.state.AgentStateHolder;
import com.financeagent.event.CommandExecutedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
public class CommandInvoker {

    private static final Logger log = LoggerFactory.getLogger(CommandInvoker.class);

    private final ApplicationEventPublisher eventPublisher;
    private final AgentStateHolder stateHolder;

    public CommandInvoker(ApplicationEventPublisher eventPublisher, AgentStateHolder stateHolder) {
        this.eventPublisher = eventPublisher;
        this.stateHolder = stateHolder;
    }

    public <R> R execute(Command<R> command) {
        if (!stateHolder.getState().canExecuteCommand()) {
            log.warn("[COMMAND] '{}' REJEITADO: agente esta no estado '{}'",
                    command.getName(), stateHolder.getState().getName());
            throw new IllegalStateException(
                    "Comando '%s' rejeitado: agente esta no estado '%s', fora do ciclo de execucao autorizado"
                            .formatted(command.getName(), stateHolder.getState().getName()));
        }

        log.info("[COMMAND] Executando '{}' (estado: {})", command.getName(), stateHolder.getState().getName());
        R result = command.execute();

        String correlationId = UUID.randomUUID().toString();
        log.info("[COMMAND] '{}' concluido -> {} (correlationId={})", command.getName(), result, correlationId);

        eventPublisher.publishEvent(new CommandExecutedEvent(
                command.getName(),
                result,
                correlationId,
                Instant.now()));

        return result;
    }
}
