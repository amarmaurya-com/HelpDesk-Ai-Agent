package com.codes.Help_desk_backend;


import com.codes.Help_desk_backend.service.vector.DocumentIngestionService;
import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class HelpDeskBackendApplicationTests {

    @Autowired
    private DocumentIngestionService documentIngestionService;
    @Autowired
    private VectorStore vectorStore;

    @Test
    void main() {
        System.out.println("Inside the main method");
        documentIngestionService.loadDocuments();
        System.out.println("Data saved in PDVDB");
    }

    @Test
    public void testSearch() {

        List<Document> docs = vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query("Relocation \n" +
                                "Transfer Leave")
                        .topK(5)
                        .build()
        );

        docs.forEach(d -> {
            System.out.println("----------------");
            System.out.println(d.getText());
        });
    }
}
