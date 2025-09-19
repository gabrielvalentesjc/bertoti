package org.example;

import io.github.ollama4j.OllamaAPI;
import io.github.ollama4j.exceptions.OllamaBaseException;
import io.github.ollama4j.models.response.OllamaResult;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws OllamaBaseException, IOException, InterruptedException {
        String host = "http://localhost:11434";
        String modelName = "qwen3:1.7b";

        OllamaAPI ollamaAPI = new OllamaAPI(host);
        ollamaAPI.setRequestTimeoutSeconds(120);
        OllamaResult result =
                ollamaAPI.generate(modelName, "Quem descobriu o Brasil?", null);

        System.out.println(result.getResponse());
    }
}