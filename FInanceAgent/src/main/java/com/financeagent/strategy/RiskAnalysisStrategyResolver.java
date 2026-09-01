package com.financeagent.strategy;

import com.financeagent.domain.risk.RiskType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class RiskAnalysisStrategyResolver {

    private static final Logger log = LoggerFactory.getLogger(RiskAnalysisStrategyResolver.class);

    private final Map<RiskType, RiskAnalysisStrategy> strategies;

    public RiskAnalysisStrategyResolver(List<RiskAnalysisStrategy> strategyBeans) {
        this.strategies = strategyBeans.stream()
                .collect(Collectors.toMap(RiskAnalysisStrategy::getType, Function.identity()));
    }

    public RiskAnalysisStrategy resolve(RiskType type) {
        RiskAnalysisStrategy strategy = strategies.get(type);
        if (strategy == null) {
            throw new IllegalArgumentException("Nenhuma estrategia de risco registrada para: " + type);
        }
        log.info("[STRATEGY] Tipo '{}' resolvido para {}", type, strategy.getClass().getSimpleName());
        return strategy;
    }
}
