package com.financeagent.command.impl;

import com.financeagent.command.Command;
import com.financeagent.domain.risk.RiskAnalysisContext;
import com.financeagent.domain.risk.RiskAssessment;
import com.financeagent.strategy.RiskAnalysisStrategy;

public class AnalyzeRiskCommand implements Command<RiskAssessment> {

    private final RiskAnalysisStrategy strategy;
    private final RiskAnalysisContext context;

    public AnalyzeRiskCommand(RiskAnalysisStrategy strategy, RiskAnalysisContext context) {
        this.strategy = strategy;
        this.context = context;
    }

    @Override
    public RiskAssessment execute() {
        return strategy.analyze(context);
    }

    @Override
    public String getName() {
        return "AnalyzeRiskCommand:" + context.type();
    }
}
