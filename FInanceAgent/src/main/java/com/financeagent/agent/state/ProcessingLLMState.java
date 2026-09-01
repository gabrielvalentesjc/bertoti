package com.financeagent.agent.state;

public final class ProcessingLLMState implements AgentState {

    public static final ProcessingLLMState INSTANCE = new ProcessingLLMState();

    private ProcessingLLMState() {
    }

    @Override
    public String getName() {
        return "PROCESSING_LLM";
    }

    @Override
    public boolean canExecuteCommand() {
        return false;
    }
}
