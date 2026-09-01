package com.financeagent.strategy.impl;

import com.financeagent.domain.risk.RiskAnalysisContext;
import com.financeagent.domain.risk.RiskAssessment;
import com.financeagent.domain.risk.RiskLevel;
import com.financeagent.domain.risk.RiskType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class CreditRiskStrategyTest {

    private final CreditRiskStrategy strategy = new CreditRiskStrategy();

    @Test
    void classificaComoAltoRiscoQuandoValorUltrapassaLimite() {
        RiskAnalysisContext context = new RiskAnalysisContext(
                "CLI-001", RiskType.CREDIT, new BigDecimal("75000"), Map.of());

        RiskAssessment result = strategy.analyze(context);

        assertThat(result.level()).isEqualTo(RiskLevel.HIGH);
        assertThat(result.type()).isEqualTo(RiskType.CREDIT);
    }

    @Test
    void classificaComoBaixoRiscoQuandoValorDentroDoLimite() {
        RiskAnalysisContext context = new RiskAnalysisContext(
                "CLI-001", RiskType.CREDIT, new BigDecimal("1000"), Map.of());

        RiskAssessment result = strategy.analyze(context);

        assertThat(result.level()).isEqualTo(RiskLevel.LOW);
    }
}
