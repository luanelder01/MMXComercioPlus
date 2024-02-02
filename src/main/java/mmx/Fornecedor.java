package mmx;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Fornecedor {
    private int id;
    private String nome;
    private String cnpj;
    private String endereco;
    private String telefone;

    public Fornecedor(int id, String nome, String cnpj, String endereco, String telefone) {
        this.id = id;
        this.nome = nome;
        this.cnpj = cnpj;
        this.endereco = endereco;
        this.telefone = telefone;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCnpj() {
        return cnpj;
    }

    public String getEndereco() {
        return endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    // Métodos para interagir com o banco de dados

    public static List<Fornecedor> listarFornecedores() {
        List<Fornecedor> fornecedores = new ArrayList<>();

        try (Connection conexao = ConexaoDB.obterConexao();
             PreparedStatement ps = conexao.prepareStatement("SELECT * FROM fornecedores");
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String cnpj = rs.getString("cnpj");
                String endereco = rs.getString("endereco");
                String telefone = rs.getString("telefone");

                Fornecedor fornecedor = new Fornecedor(id, nome, cnpj, endereco, telefone);
                fornecedores.add(fornecedor);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Tratar a exceção conforme necessário
        }

        return fornecedores;
    }

    public static Fornecedor obterFornecedorPorId(int idFornecedor) {
        try (Connection conexao = ConexaoDB.obterConexao();
             PreparedStatement ps = conexao.prepareStatement("SELECT * FROM fornecedores WHERE id = ?")) {

            ps.setInt(1, idFornecedor);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String nome = rs.getString("nome");
                    String cnpj = rs.getString("cnpj");
                    String endereco = rs.getString("endereco");
                    String telefone = rs.getString("telefone");

                    return new Fornecedor(id, nome, cnpj, endereco, telefone);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Tratar a exceção conforme necessário
        }

        return null;
    }

    // Novo método para obter o nome do fornecedor por ID
    public static String obterNomeFornecedorPorId(int idFornecedor) {
        Fornecedor fornecedor = obterFornecedorPorId(idFornecedor);
        return (fornecedor != null) ? fornecedor.getNome() : "";
    }
}
