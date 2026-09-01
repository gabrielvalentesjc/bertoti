package com.financeagent.tool;

import com.financeagent.command.CommandInvoker;
import com.financeagent.command.impl.AnalyzeRiskCommand;
import com.financeagent.domain.risk.RiskAnalysisContext;
import com.financeagent.domain.risk.RiskAssessment;
import com.financeagent.domain.risk.RiskType;
import com.financeagent.strategy.RiskAnalysisStrategy;
import com.financeagent.strategy.RiskAnalysisStrategyResolver;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

@Component
public class RiskAnalysisTool {

    private final RiskAnalysisStrategyResolver resolver;
    private final CommandInvoker invoker;

    public RiskAnalysisTool(RiskAnalysisStrategyResolver resolver, CommandInvoker invoker) {
        this.resolver = resolver;
        this.invoker = invoker;
    }

    @Tool(description = "Executa analise de risco (CREDIT ou FRAUD) para uma operacao financeira de um cliente")
    public String analisarRisco(
            @ToolParam(description = "ID do cliente") String customerId,
            @ToolParam(description = "Tipo de risco: CREDIT ou FRAUD") String tipoRisco,
            @ToolParam(description = "Valor da operacao financeira") BigDecimal valor,
            @ToolParam(description = "Numero de transacoes do cliente nas ultimas 24 horas (usado apenas na analise de FRAUD)", required = false)
            Integer velocidadeTransacoes24h) {

        RiskType type = RiskType.valueOf(tipoRisco.trim().toUpperCase());
        RiskAnalysisStrategy strategy = resolver.resolve(type);

        Map<String, Object> metadata = velocidadeTransacoes24h != null
                ? Map.of("velocidadeTransacoes24h", velocidadeTransacoes24h)
                : Map.of();

        RiskAnalysisContext context = new RiskAnalysisContext(customerId, type, valor, metadata);
        AnalyzeRiskCommand command = new AnalyzeRiskCommand(strategy, context);

        RiskAssessment assessment = invoker.execute(command);

        return "Nivel de risco: %s | Score: %.2f | Justificativa: %s"
                .formatted(assessment.level(), assessment.score(), assessment.justification());
    }
}
