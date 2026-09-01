package com.financeagent.domain.risk;

public record RiskAssessment(
        RiskType type,
        RiskLevel level,
        double score,
        String justification
) {
}
