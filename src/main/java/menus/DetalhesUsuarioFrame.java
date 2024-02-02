package menus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import mmx.ConexaoDB;

public class DetalhesUsuarioFrame extends JFrame {

    private String nomeUsuario;
    private JTextField txtNome;
    private JTextField txtCpf;
    private JTextField txtEndereco;
    private JTextField txtDataNascimento;
    private JTextField txtProfissao;
    private JTextField txtSalario;
    private JTextField txtTelefone;
    private JTextField txtSenha;

    private JButton btnSalvarAlteracoes;
    private JButton btnExcluir;

    public DetalhesUsuarioFrame(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
        initComponents();
        carregarDetalhesUsuario();
    }

    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Detalhes do Usuário");

        JPanel panel = new JPanel();

        JLabel lblNome = new JLabel("Nome:");
        JLabel lblCpf = new JLabel("CPF:");
        JLabel lblEndereco = new JLabel("Endereço:");
        JLabel lblDataNascimento = new JLabel("Data de Nascimento:");
        JLabel lblProfissao = new JLabel("Profissão:");
        JLabel lblSalario = new JLabel("Salário:");
        JLabel lblTelefone = new JLabel("Telefone:");
        JLabel lblSenha = new JLabel("Senha:");

        txtNome = new JTextField(30);
        txtCpf = new JTextField(15);
        txtEndereco = new JTextField(50);
        txtDataNascimento = new JTextField(10);
        txtProfissao = new JTextField(20);
        txtSalario = new JTextField(10);
        txtTelefone = new JTextField(15);
        txtSenha = new JTextField(15);

        btnSalvarAlteracoes = new JButton("Salvar Alterações");
        btnSalvarAlteracoes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                btnSalvarAlteracoesActionPerformed(evt);
            }
        });

        btnExcluir = new JButton("Excluir Usuário");
        btnExcluir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                btnExcluirActionPerformed(evt);
            }
        });

        panel.add(lblNome);
        panel.add(txtNome);
        panel.add(lblCpf);
        panel.add(txtCpf);
        panel.add(lblEndereco);
        panel.add(txtEndereco);
        panel.add(lblDataNascimento);
        panel.add(txtDataNascimento);
        panel.add(lblProfissao);
        panel.add(txtProfissao);
        panel.add(lblSalario);
        panel.add(txtSalario);
        panel.add(lblTelefone);
        panel.add(txtTelefone);
        panel.add(lblSenha);
        panel.add(txtSenha);
        panel.add(btnSalvarAlteracoes);
        panel.add(btnExcluir);

        getContentPane().add(panel);

        pack();
        setLocationRelativeTo(null);
    }

    private void carregarDetalhesUsuario() {
        try (Connection conexao = ConexaoDB.obterConexao();
             PreparedStatement ps = conexao.prepareStatement("SELECT * FROM usuarios WHERE nome = ?")) {

            ps.setString(1, nomeUsuario);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    txtNome.setText(rs.getString("nome"));
                    txtCpf.setText(rs.getString("cpf"));
                    txtEndereco.setText(rs.getString("endereco"));
                    txtDataNascimento.setText(rs.getString("data_nascimento"));
                    txtProfissao.setText(rs.getString("profissao"));
                    txtSalario.setText(String.valueOf(rs.getDouble("salario")));
                    txtTelefone.setText(rs.getString("telefone"));
                    txtSenha.setText(rs.getString("senha"));
                } else {
                    JOptionPane.showMessageDialog(this, "Usuário não encontrado.");
                    dispose(); // Fecha a janela se o usuário não for encontrado
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao carregar detalhes do usuário.");
        }
    }

    private void btnSalvarAlteracoesActionPerformed(ActionEvent evt) {
        salvarAlteracoesUsuario();
    }

    private void btnExcluirActionPerformed(ActionEvent evt) {
        excluirUsuario();
    }

    private void salvarAlteracoesUsuario() {
        try (Connection conexao = ConexaoDB.obterConexao();
             PreparedStatement ps = conexao.prepareStatement(
                     "UPDATE usuarios SET cpf=?, endereco=?, data_nascimento=?, profissao=?, salario=?, telefone=?, senha=? WHERE nome=?")) {

            ps.setString(1, txtCpf.getText());
            ps.setString(2, txtEndereco.getText());
            ps.setString(3, txtDataNascimento.getText());
            ps.setString(4, txtProfissao.getText());
            ps.setDouble(5, Double.parseDouble(txtSalario.getText()));
            ps.setString(6, txtTelefone.getText());
            ps.setString(7, txtSenha.getText());
            ps.setString(8, nomeUsuario);

            int linhasAfetadas = ps.executeUpdate();

            if (linhasAfetadas > 0) {
                JOptionPane.showMessageDialog(this, "Alterações salvas com sucesso!");
            } else {
                JOptionPane.showMessageDialog(this, "Nenhuma alteração realizada.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao salvar alterações.");
        }
    }

    private void excluirUsuario() {
        int confirmacao = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir o usuário?", "Confirmação", JOptionPane.YES_NO_OPTION);

        if (confirmacao == JOptionPane.YES_OPTION) {
            try (Connection conexao = ConexaoDB.obterConexao();
                 PreparedStatement ps = conexao.prepareStatement("DELETE FROM usuarios WHERE nome=?")) {

                ps.setString(1, nomeUsuario);

                int linhasAfetadas = ps.executeUpdate();

                if (linhasAfetadas > 0) {
                    JOptionPane.showMessageDialog(this, "Usuário excluído com sucesso!");
                    dispose(); // Fecha a janela após excluir o usuário
                } else {
                    JOptionPane.showMessageDialog(this, "Nenhuma exclusão realizada.");
                }

            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro ao excluir usuário.");
            }
        }
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DetalhesUsuarioFrame("ExemploUsuario").setVisible(true);
            }
        });
    }
}
