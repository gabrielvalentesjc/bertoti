package com.financeagent.domain.balance;

import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class AccountBalanceRepository {

    private final Map<String, BigDecimal> balances = new ConcurrentHashMap<>(Map.of(
            "CLI-001", new BigDecimal("15320.50"),
            "CLI-002", new BigDecimal("874.10")
    ));

    public BigDecimal findBalance(String accountId) {
        return balances.getOrDefault(accountId, BigDecimal.ZERO);
    }
}
