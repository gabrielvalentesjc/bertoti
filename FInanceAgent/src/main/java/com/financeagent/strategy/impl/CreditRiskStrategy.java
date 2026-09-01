package com.financeagent.strategy.impl;

import com.financeagent.domain.risk.RiskAnalysisContext;
import com.financeagent.domain.risk.RiskAssessment;
import com.financeagent.domain.risk.RiskLevel;
import com.financeagent.domain.risk.RiskType;
import com.financeagent.strategy.RiskAnalysisStrategy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CreditRiskStrategy implements RiskAnalysisStrategy {

    private static final BigDecimal LIMITE_ALTO_RISCO = new BigDecimal("50000");

    @Override
    public RiskType getType() {
        return RiskType.CREDIT;
    }

    @Override
    public RiskAssessment analyze(RiskAnalysisContext context) {
        boolean valorElevado = context.amount().compareTo(LIMITE_ALTO_RISCO) > 0;
        double score = valorElevado ? 0.75 : 0.25;
        RiskLevel level = valorElevado ? RiskLevel.HIGH : RiskLevel.LOW;

        return new RiskAssessment(
                RiskType.CREDIT,
                level,
                score,
                "Analise de credito baseada no valor solicitado (%s) frente ao limite de exposicao"
                        .formatted(context.amount()));
    }
}
