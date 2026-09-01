package com.financeagent.command.impl;

import com.financeagent.command.Command;
import com.financeagent.domain.balance.AccountBalanceRepository;

import java.math.BigDecimal;

public class CheckBalanceCommand implements Command<BigDecimal> {

    private final String accountId;
    private final AccountBalanceRepository repository;

    public CheckBalanceCommand(String accountId, AccountBalanceRepository repository) {
        this.accountId = accountId;
        this.repository = repository;
    }

    @Override
    public BigDecimal execute() {
        return repository.findBalance(accountId);
    }

    @Override
    public String getName() {
        return "CheckBalanceCommand:" + accountId;
    }
}
