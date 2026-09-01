package com.financeagent.rag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Le os documentos de politica em src/main/resources/compliance/*.md, quebra em pedacos
 * e indexa no VectorStore na subida da aplicacao — apenas se a tabela ainda estiver vazia,
 * para nao duplicar o conteudo a cada restart.
 */
@Component
@Profile("rag")
public class ComplianceKnowledgeIngestionRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(ComplianceKnowledgeIngestionRunner.class);

    private final VectorStore vectorStore;
    private final JdbcTemplate jdbcTemplate;
    private final RagProperties properties;

    public ComplianceKnowledgeIngestionRunner(VectorStore vectorStore, JdbcTemplate jdbcTemplate, RagProperties properties) {
        this.vectorStore = vectorStore;
        this.jdbcTemplate = jdbcTemplate;
        this.properties = properties;
    }

    @Override
    public void run(String... args) throws Exception {
        if (!properties.autoIngest()) {
            log.info("[RAG] Auto-ingestao desabilitada (finance.rag.auto-ingest=false)");
            return;
        }

        Integer existing = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM " + properties.vector().tableName(), Integer.class);
        if (existing != null && existing > 0) {
            log.info("[RAG] Base de conhecimento ja possui {} chunks indexados, ingestao pulada", existing);
            return;
        }

        Resource[] resources = new PathMatchingResourcePatternResolver()
                .getResources(properties.documentsLocation());

        List<Document> chunks = new ArrayList<>();
        TokenTextSplitter splitter = new TokenTextSplitter();

        for (Resource resource : resources) {
            List<Document> documents = new TextReader(resource).get();
            chunks.addAll(splitter.split(documents));
            log.info("[RAG] Documento '{}' lido e dividido", resource.getFilename());
        }

        if (chunks.isEmpty()) {
            log.warn("[RAG] Nenhum documento encontrado em '{}'", properties.documentsLocation());
            return;
        }

        vectorStore.add(chunks);
        log.info("[RAG] Ingestao concluida: {} chunks indexados em '{}'", chunks.size(), properties.vector().tableName());
    }
}
