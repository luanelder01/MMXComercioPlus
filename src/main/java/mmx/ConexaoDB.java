package mmx;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoDB {
    private static final String URL = "jdbc:mysql://localhost:3306/MMXDB";
    private static final String USUARIO = "root";
    private static final String SENHA = "CocaCola123-";
    private static Connection conexao = null;

    // Método para obter a conexão com o banco de dados
    public static Connection obterConexao() throws SQLException {
        try {
            if (conexao == null || conexao.isClosed()) {
                conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Erro ao obter a conexão com o banco de dados.", e);
        }
        return conexao;
    }

    // Método para fechar a conexão com o banco de dados
    public static void fecharConexao() {
        try {
            if (conexao != null && !conexao.isClosed()) {
                conexao.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
