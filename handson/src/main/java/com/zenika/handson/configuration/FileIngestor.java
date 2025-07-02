package com.zenika.handson.configuration;

import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@Component
public class FileIngestor {

    public FileIngestor(VectorStore store) throws IOException {
        ClassPathResource resource = new ClassPathResource("term-and-co.txt");
        String content = Files.readString(resource.getFile().toPath());
        Document doc = new Document(content);
        List<Document> docs = new TokenTextSplitter().split(doc);
        store.add(docs);
    }
}