package org.example;

import java.util.Scanner;
import io.github.ollama4j.OllamaAPI;
import io.github.ollama4j.exceptions.OllamaBaseException;
import io.github.ollama4j.models.response.OllamaResult;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws OllamaBaseException, IOException, InterruptedException {
        String host = "http://localhost:11434";
        String modelName = "qwen3:1.7b";
        int opcao = 1;

        OllamaAPI ollamaAPI = new OllamaAPI(host);
        ollamaAPI.setRequestTimeoutSeconds(120);

        System.out.println("Seja bem-vindo(a)! Sou uma IA, e estou animada para responder suas perguntas! :)");

        while (opcao != 0) {
            Scanner entrada = new Scanner(System.in);

            System.out.println("Qual a sua pergunta:");
            String pergunta = entrada.nextLine();

            System.out.println("Pensando...");

            OllamaResult result = ollamaAPI.generate(modelName, pergunta, null);
            System.out.println(result.getResponse());

            System.out.println("Você deseja continuar? (Sim = 1/Não = 0)");
            Scanner ContOuPara = new Scanner(System.in);
            opcao = ContOuPara.nextInt();
        }

        System.out.println("Até a próxima! :)");
    }
}