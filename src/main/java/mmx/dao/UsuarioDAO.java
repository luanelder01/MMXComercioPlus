package mmx.dao;

import mmx.ConexaoDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

    public static String obterProfissaoUsuario(String nomeUsuario, String senha) {
        String profissao = null;

        try (Connection conexao = ConexaoDB.obterConexao()) {
            String sql = "SELECT profissao FROM Usuarios WHERE nome = ? AND senha = ?";
            try (PreparedStatement pstmt = conexao.prepareStatement(sql)) {
                pstmt.setString(1, nomeUsuario);
                pstmt.setString(2, senha);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        profissao = rs.getString("profissao");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return profissao;
    }
}
