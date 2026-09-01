package com.financeagent.strategy.impl;

import com.financeagent.domain.risk.RiskAnalysisContext;
import com.financeagent.domain.risk.RiskAssessment;
import com.financeagent.domain.risk.RiskLevel;
import com.financeagent.domain.risk.RiskType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class FraudRiskStrategyTest {

    private final FraudRiskStrategy strategy = new FraudRiskStrategy();

    @Test
    void classificaComoCriticoQuandoVelocidadeDeTransacoesElevada() {
        RiskAnalysisContext context = new RiskAnalysisContext(
                "CLI-002", RiskType.FRAUD, new BigDecimal("500"),
                Map.of("velocidadeTransacoes24h", 8));

        RiskAssessment result = strategy.analyze(context);

        assertThat(result.level()).isEqualTo(RiskLevel.CRITICAL);
    }

    @Test
    void classificaComoBaixoRiscoQuandoSemPadraoSuspeito() {
        RiskAnalysisContext context = new RiskAnalysisContext(
                "CLI-002", RiskType.FRAUD, new BigDecimal("500"), Map.of());

        RiskAssessment result = strategy.analyze(context);

        assertThat(result.level()).isEqualTo(RiskLevel.LOW);
    }
}
