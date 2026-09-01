package com.financeagent.agent.state;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Mantem o estado do ciclo de vida do agente por thread de requisicao,
 * evitando que uma sessao vaze estado para outra sob concorrencia.
 */
@Component
public class AgentStateHolder {

    private static final Logger log = LoggerFactory.getLogger(AgentStateHolder.class);

    private final ThreadLocal<AgentState> current = ThreadLocal.withInitial(() -> IdleState.INSTANCE);

    public AgentState getState() {
        return current.get();
    }

    public void transitionTo(AgentState newState) {
        log.info("[STATE] {} -> {}", current.get().getName(), newState.getName());
        current.set(newState);
    }

    public void reset() {
        current.remove();
    }
}
