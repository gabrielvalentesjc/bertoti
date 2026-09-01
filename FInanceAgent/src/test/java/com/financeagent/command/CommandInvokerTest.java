package com.financeagent.command;

import com.financeagent.agent.state.AgentStateHolder;
import com.financeagent.agent.state.ExecutingCommandState;
import com.financeagent.event.CommandExecutedEvent;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.context.ApplicationEventPublisher;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class CommandInvokerTest {

    private final ApplicationEventPublisher eventPublisher = mock(ApplicationEventPublisher.class);
    private final AgentStateHolder stateHolder = new AgentStateHolder();
    private final CommandInvoker invoker = new CommandInvoker(eventPublisher, stateHolder);

    @Test
    void rejeitaExecucaoQuandoAgenteEstaOcioso() {
        Command<String> command = fakeCommand("qualquer");

        assertThatThrownBy(() -> invoker.execute(command))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void executaEPublicaEventoQuandoEstadoPermite() {
        stateHolder.transitionTo(ExecutingCommandState.INSTANCE);
        Command<String> command = fakeCommand("resultado");

        String result = invoker.execute(command);

        assertThat(result).isEqualTo("resultado");
        verify(eventPublisher).publishEvent(ArgumentMatchers.any(CommandExecutedEvent.class));
    }

    private Command<String> fakeCommand(String result) {
        return new Command<>() {
            @Override
            public String execute() {
                return result;
            }

            @Override
            public String getName() {
                return "FakeCommand";
            }
        };
    }
}
