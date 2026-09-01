package com.financeagent.agent;

import com.financeagent.agent.state.AgentStateHolder;
import com.financeagent.agent.state.ExecutingCommandState;
import com.financeagent.agent.state.ProcessingLLMState;
import com.financeagent.tool.CheckBalanceTool;
import com.financeagent.tool.RiskAnalysisTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;

@Service
public class FinancialAgentContext {

    private static final Logger log = LoggerFactory.getLogger(FinancialAgentContext.class);

    private final ChatClient chatClient;
    private final AgentStateHolder stateHolder;

    public FinancialAgentContext(ChatClient.Builder chatClientBuilder,
                                  AgentStateHolder stateHolder,
                                  RiskAnalysisTool riskAnalysisTool,
                                  CheckBalanceTool checkBalanceTool,
                                  ObjectProvider<VectorStore> vectorStoreProvider) {
        this.stateHolder = stateHolder;

        ChatClient.Builder builder = chatClientBuilder
                .defaultSystem("""
                        Voce e um agente financeiro corporativo. Utilize as ferramentas disponiveis
                        para consultar saldo e executar analises de risco de credito ou fraude.
                        Nunca invente valores: sempre chame a ferramenta apropriada.
                        Quando houver contexto de politica interna recuperado, baseie sua resposta
                        nele e cite que a informacao vem da politica interna.
                        """)
                .defaultTools(riskAnalysisTool, checkBalanceTool);

        // O VectorStore so existe quando o profile "rag" esta ativo (ver RagConfig).
        // Sem ele, o agente funciona normalmente, so sem grounding de compliance.
        VectorStore vectorStore = vectorStoreProvider.getIfAvailable();
        if (vectorStore != null) {
            log.info("[RAG] VectorStore encontrado, ativando QuestionAnswerAdvisor para grounding de compliance");
            builder.defaultAdvisors(new QuestionAnswerAdvisor(vectorStore));
        } else {
            log.info("[RAG] Nenhum VectorStore disponivel (profile 'rag' inativo) — agente roda sem RAG");
        }

        this.chatClient = builder.build();
    }

    public String handle(String userMessage) {
        stateHolder.transitionTo(ProcessingLLMState.INSTANCE);
        try {
            // O tool-calling (e efeitos colaterais como execucao de Commands) so
            // acontece dentro desta chamada; por isso o estado avanca para
            // EXECUTING_COMMAND antes dela. O CommandInvoker rejeita qualquer
            // Command executado fora dessa janela, barrando chamadas indevidas.
            stateHolder.transitionTo(ExecutingCommandState.INSTANCE);
            return chatClient.prompt()
                    .user(userMessage)
                    .call()
                    .content();
        } finally {
            stateHolder.reset();
        }
    }
}
