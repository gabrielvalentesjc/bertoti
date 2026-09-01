package com.financeagent.agent.state;

public final class IdleState implements AgentState {

    public static final IdleState INSTANCE = new IdleState();

    private IdleState() {
    }

    @Override
    public String getName() {
        return "IDLE";
    }

    @Override
    public boolean canExecuteCommand() {
        return false;
    }
}
