package com.financeagent.domain.risk;

import java.math.BigDecimal;
import java.util.Map;

public record RiskAnalysisContext(
        String customerId,
        RiskType type,
        BigDecimal amount,
        Map<String, Object> metadata
) {
}
