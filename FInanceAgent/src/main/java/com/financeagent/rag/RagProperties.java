package com.financeagent.rag;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuracao do profile "rag" (base de conhecimento de compliance via PGVector).
 * Namespace proprio (finance.rag.*), separado de spring.datasource.*, para nao
 * disparar a auto-configuracao padrao do Spring Boot fora deste profile.
 */
@ConfigurationProperties(prefix = "finance.rag")
public record RagProperties(
        DataSource datasource,
        Vector vector,
        boolean autoIngest,
        String documentsLocation
) {

    public record DataSource(String url, String username, String password) {
    }

    public record Vector(String tableName, int dimensions) {
    }
}
