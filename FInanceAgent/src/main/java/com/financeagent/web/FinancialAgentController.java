package com.financeagent.web;

import com.financeagent.agent.FinancialAgentContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/agent")
public class FinancialAgentController {

    private final FinancialAgentContext agentContext;

    public FinancialAgentController(FinancialAgentContext agentContext) {
        this.agentContext = agentContext;
    }

    @PostMapping("/chat")
    public ResponseEntity<String> chat(@RequestBody ChatRequest request) {
        return ResponseEntity.ok(agentContext.handle(request.message()));
    }
}
