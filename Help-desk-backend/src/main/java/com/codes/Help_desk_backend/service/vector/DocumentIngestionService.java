package com.codes.Help_desk_backend.service.vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class DocumentIngestionService {

    private static final Logger logger = LoggerFactory.getLogger(DocumentIngestionService.class);

    private final VectorStore vectorStore;
    private final ResourcePatternResolver resourcePatternResolver;
    private final boolean ingestionEnabled;

    public DocumentIngestionService(
            VectorStore vectorStore,
            ResourcePatternResolver resourcePatternResolver,
            @Value("${helpdesk.rag.ingestion.enabled:false}") boolean ingestionEnabled) {
        this.vectorStore = vectorStore;
        this.resourcePatternResolver = resourcePatternResolver;
        this.ingestionEnabled = ingestionEnabled;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void loadDocumentsOnStartup() {
        if (!ingestionEnabled) {
            logger.info("RAG document ingestion is disabled. Set helpdesk.rag.ingestion.enabled=true to load PDFs.");
            return;
        }

        loadDocuments();
    }

    public void loadDocuments() {
        try {
            Resource[] resources = resourcePatternResolver.getResources("classpath:/docs/*.pdf");

            if (resources.length == 0) {
                logger.warn("No PDF files found in classpath:/docs/");
                return;
            }

            List<Document> rawDocuments = new ArrayList<>();
            for (Resource resource : resources) {
                PagePdfDocumentReader reader = new PagePdfDocumentReader(resource);
                List<Document> documents = reader.read();
                documents.forEach(document -> {
                    document.getMetadata().put("source", resource.getFilename());
                    document.getMetadata().put("knowledge_base", "helpdesk");
                });
                rawDocuments.addAll(documents);
            }

            logger.info("Extracted {} raw page documents. Beginning chunking...", rawDocuments.size());

            // 1. CHUNK: Break pages into smaller 800-token chunks with overlap
            TokenTextSplitter splitter = TokenTextSplitter.builder()
                    .withChunkSize(800)
                    .withMinChunkSizeChars(350)
                    .withMinChunkLengthToEmbed(5)
                    .withMaxNumChunks(1000)
                    .withKeepSeparator(true)
                    .build();

            List<Document> chunkedDocuments = splitter.apply(rawDocuments);

            // 2. INGEST: Push chunked documents to PGVector via Ollama Embeddings
            vectorStore.add(chunkedDocuments);

            logger.info("Successfully ingested {} chunked documents into PGVector.", chunkedDocuments.size());

        } catch (IOException e) {
            logger.error("Failed to read PDF files from classpath", e);
            throw new RuntimeException("Document ingestion failed", e);
        }
    }
}
