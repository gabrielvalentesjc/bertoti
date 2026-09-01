package com.financeagent.strategy;

import com.financeagent.domain.risk.RiskAnalysisContext;
import com.financeagent.domain.risk.RiskAssessment;
import com.financeagent.domain.risk.RiskType;

public interface RiskAnalysisStrategy {

    RiskType getType();

    RiskAssessment analyze(RiskAnalysisContext context);
}
