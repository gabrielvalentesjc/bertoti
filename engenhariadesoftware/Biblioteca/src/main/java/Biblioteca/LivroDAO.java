package Biblioteca;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class LivroDAO {
    private static final String URL = "jdbc:sqlite:C:/Users/Noturno/sqlite/db/biblioteca.db";

    // Cria a tabela caso ainda não exista
    public static void criarTabela() {
        String sql = """
            CREATE TABLE IF NOT EXISTS livros (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                titulo TEXT NOT NULL,
                autor TEXT NOT NULL,
                isbn TEXT NOT NULL UNIQUE
            );
        """;

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql);
            System.out.println("Tabela 'livros' verificada/criada com sucesso.");

        } catch (SQLException e) {
            System.out.println("Erro ao criar/verificar tabela: " + e.getMessage());
        }
    }

    // Insere um livro
    public static void inserirLivro(Livro livro) {
        String sql = "INSERT INTO livros (titulo, autor, isbn) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, livro.getTitulo());
            pstmt.setString(2, livro.getAutor());
            pstmt.setString(3, livro.getIsbn());

            int rows = pstmt.executeUpdate();
            System.out.println(rows + " livro(s) inserido(s) com sucesso.");

        } catch (SQLException e) {
            System.out.println("Erro ao inserir livro: " + e.getMessage());
        }
    }

    public static List<Livro> buscarPorTitulo(String titulo) {
        List<Livro> encontrados = new ArrayList<>();
        String sql = "SELECT titulo, autor, isbn FROM livros WHERE titulo LIKE ?";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + titulo + "%"); // LIKE com coringa para busca parcial

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String t = rs.getString("titulo");
                String a = rs.getString("autor");
                String i = rs.getString("isbn");

                encontrados.add(new Livro(t, a, i));
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar livros por título: " + e.getMessage());
        }

        return encontrados;
    }
    // Buscar livro por ISBN (único, retorna um só)
    public static Livro buscarPorIsbn(String isbn) {
        String sql = "SELECT titulo, autor, isbn FROM livros WHERE isbn = ?";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, isbn);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Livro(rs.getString("titulo"), rs.getString("autor"), rs.getString("isbn"));
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar livro por ISBN: " + e.getMessage());
        }

        return null;
    }
}

