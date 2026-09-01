package com.financeagent.tool;

import com.financeagent.command.CommandInvoker;
import com.financeagent.command.impl.CheckBalanceCommand;
import com.financeagent.domain.balance.AccountBalanceRepository;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CheckBalanceTool {

    private final AccountBalanceRepository repository;
    private final CommandInvoker invoker;

    public CheckBalanceTool(AccountBalanceRepository repository, CommandInvoker invoker) {
        this.repository = repository;
        this.invoker = invoker;
    }

    @Tool(description = "Consulta o saldo atual de uma conta pelo ID do cliente")
    public String consultarSaldo(@ToolParam(description = "ID do cliente/conta") String accountId) {
        CheckBalanceCommand command = new CheckBalanceCommand(accountId, repository);
        BigDecimal balance = invoker.execute(command);
        return "Saldo da conta %s: R$ %.2f".formatted(accountId, balance);
    }
}
