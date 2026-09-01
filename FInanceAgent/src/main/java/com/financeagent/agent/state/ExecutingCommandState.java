package com.financeagent.agent.state;

public final class ExecutingCommandState implements AgentState {

    public static final ExecutingCommandState INSTANCE = new ExecutingCommandState();

    private ExecutingCommandState() {
    }

    @Override
    public String getName() {
        return "EXECUTING_COMMAND";
    }

    @Override
    public boolean canExecuteCommand() {
        return true;
    }
}
