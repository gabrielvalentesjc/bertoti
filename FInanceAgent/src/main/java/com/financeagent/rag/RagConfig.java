package com.financeagent.rag;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * Toda a infraestrutura de RAG (Postgres + pgvector) vive atras do profile "rag".
 * Fora dele, nenhum bean aqui e criado e a aplicacao roda exatamente como antes,
 * sem exigir Postgres no ar. Ative com:
 *
 *   mvn spring-boot:run -Dspring-boot.run.profiles=rag
 */
@Configuration
@Profile("rag")
@EnableConfigurationProperties(RagProperties.class)
public class RagConfig {

    @Bean
    public DataSource ragDataSource(RagProperties properties) {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(properties.datasource().url());
        dataSource.setUsername(properties.datasource().username());
        dataSource.setPassword(properties.datasource().password());
        return dataSource;
    }

    @Bean
    public JdbcTemplate ragJdbcTemplate(DataSource ragDataSource) {
        return new JdbcTemplate(ragDataSource);
    }

    @Bean
    public VectorStore vectorStore(JdbcTemplate ragJdbcTemplate, EmbeddingModel embeddingModel, RagProperties properties) {
        return PgVectorStore.builder(ragJdbcTemplate, embeddingModel)
                .vectorTableName(properties.vector().tableName())
                .dimensions(properties.vector().dimensions())
                .distanceType(PgVectorStore.PgDistanceType.COSINE_DISTANCE)
                .indexType(PgVectorStore.PgIndexType.HNSW)
                .initializeSchema(true)
                .build();
    }
}
