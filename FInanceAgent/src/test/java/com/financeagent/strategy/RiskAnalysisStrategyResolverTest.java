package com.financeagent.strategy;

import com.financeagent.domain.risk.RiskType;
import com.financeagent.strategy.impl.CreditRiskStrategy;
import com.financeagent.strategy.impl.FraudRiskStrategy;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RiskAnalysisStrategyResolverTest {

    private final RiskAnalysisStrategyResolver resolver = new RiskAnalysisStrategyResolver(
            List.of(new CreditRiskStrategy(), new FraudRiskStrategy()));

    @Test
    void resolveRetornaEstrategiaDeCredito() {
        assertThat(resolver.resolve(RiskType.CREDIT)).isInstanceOf(CreditRiskStrategy.class);
    }

    @Test
    void resolveRetornaEstrategiaDeFraude() {
        assertThat(resolver.resolve(RiskType.FRAUD)).isInstanceOf(FraudRiskStrategy.class);
    }

    @Test
    void resolveLancaExcecaoParaTipoNaoRegistrado() {
        RiskAnalysisStrategyResolver resolverVazio = new RiskAnalysisStrategyResolver(List.of());

        assertThatThrownBy(() -> resolverVazio.resolve(RiskType.CREDIT))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
