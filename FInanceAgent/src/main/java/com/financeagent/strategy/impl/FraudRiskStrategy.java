package com.financeagent.strategy.impl;

import com.financeagent.domain.risk.RiskAnalysisContext;
import com.financeagent.domain.risk.RiskAssessment;
import com.financeagent.domain.risk.RiskLevel;
import com.financeagent.domain.risk.RiskType;
import com.financeagent.strategy.RiskAnalysisStrategy;
import org.springframework.stereotype.Component;

@Component
public class FraudRiskStrategy implements RiskAnalysisStrategy {

    @Override
    public RiskType getType() {
        return RiskType.FRAUD;
    }

    @Override
    public RiskAssessment analyze(RiskAnalysisContext context) {
        int velocidadeTransacoes = (int) context.metadata().getOrDefault("velocidadeTransacoes24h", 0);
        boolean padraoSuspeito = velocidadeTransacoes > 5;

        double score = padraoSuspeito ? 0.9 : 0.1;
        RiskLevel level = padraoSuspeito ? RiskLevel.CRITICAL : RiskLevel.LOW;

        return new RiskAssessment(
                RiskType.FRAUD,
                level,
                score,
                padraoSuspeito
                        ? "Padrao de velocidade de transacoes incomum detectado (%d em 24h)".formatted(velocidadeTransacoes)
                        : "Nenhum padrao suspeito identificado");
    }
}
