package Biblioteca;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Cria tabela caso não exista
        LivroDAO.criarTabela();

        boolean running = true;
        while (running) {
            System.out.println("\n===== Menu Biblioteca =====");
            System.out.println("1 - Inserir livro");
            System.out.println("2 - Buscar livros por título");
            System.out.println("3 - Buscar livro por ISBN");
            System.out.println("4 - Sair");
            System.out.print("Escolha uma opção: ");

            int opcao = scanner.nextInt();
            scanner.nextLine();  // limpar buffer

            switch (opcao) {
                case 1:
                    System.out.print("Título: ");
                    String titulo = scanner.nextLine();

                    System.out.print("Autor: ");
                    String autor = scanner.nextLine();

                    System.out.print("ISBN: ");
                    String isbn = scanner.nextLine();

                    Livro livro = new Livro(titulo, autor, isbn);
                    LivroDAO.inserirLivro(livro);
                    break;

                case 2:
                    System.out.print("Digite o título para buscar: ");
                    String tituloBusca = scanner.nextLine();
                    List<Livro> encontrados = LivroDAO.buscarPorTitulo(tituloBusca);

                    if (encontrados.isEmpty()) {
                        System.out.println("Nenhum livro encontrado com esse título.");
                    } else {
                        System.out.println("Livros encontrados:");
                        for (Livro l : encontrados) {
                            System.out.println("- " + l.getTitulo() + " | " + l.getAutor() + " | ISBN: " + l.getIsbn());
                        }
                    }
                    break;

                case 3:
                    System.out.print("Digite o ISBN para buscar: ");
                    String isbnBusca = scanner.nextLine();
                    Livro livroEncontrado = LivroDAO.buscarPorIsbn(isbnBusca);

                    if (livroEncontrado != null) {
                        System.out.println("Livro encontrado: " + livroEncontrado.getTitulo() + " por " + livroEncontrado.getAutor());
                    } else {
                        System.out.println("Nenhum livro encontrado com esse ISBN.");
                    }
                    break;

                case 4:
                    System.out.println("Saindo...");
                    running = false;
                    break;

                default:
                    System.out.println("Opção inválida! Tente novamente.");
            }
        }

        scanner.close();
    }
}