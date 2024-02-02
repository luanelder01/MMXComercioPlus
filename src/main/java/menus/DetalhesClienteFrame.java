package menus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import mmx.ConexaoDB;

public class DetalhesClienteFrame extends JFrame {

    private String nomeCliente;
    private JTextField txtNome;
    private JTextField txtCpf;
    private JTextField txtEndereco;
    private JTextField txtDataNascimento;
    private JTextField txtTelefone;
    private JButton btnSalvarAlteracoes;
    private JButton btnCancelar;

    public DetalhesClienteFrame(String nomeCliente) {
        this.nomeCliente = nomeCliente;
        initComponents();
        carregarDetalhesCliente();
    }

    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Detalhes do Cliente");

        JPanel panel = new JPanel();

        JLabel lblNome = new JLabel("Nome:");
        JLabel lblCpf = new JLabel("CPF:");
        JLabel lblEndereco = new JLabel("Endereço:");
        JLabel lblDataNascimento = new JLabel("Data de Nascimento:");
        JLabel lblTelefone = new JLabel("Telefone:");

        txtNome = new JTextField(30);
        txtCpf = new JTextField(15);
        txtEndereco = new JTextField(50);
        txtDataNascimento = new JTextField(10);
        txtTelefone = new JTextField(15);

        btnSalvarAlteracoes = new JButton("Salvar Alterações");
        btnSalvarAlteracoes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                btnSalvarAlteracoesActionPerformed(evt);
            }
        });

        btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                btnCancelarActionPerformed(evt);
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
        panel.add(lblTelefone);
        panel.add(txtTelefone);
        panel.add(btnSalvarAlteracoes);
        panel.add(btnCancelar);

        getContentPane().add(panel);

        setSize(400, 300);
        setLocationRelativeTo(null);
    }

    private void carregarDetalhesCliente() {
        try (Connection conexao = ConexaoDB.obterConexao();
             PreparedStatement ps = conexao.prepareStatement("SELECT * FROM clientes WHERE nome = ?")) {

            ps.setString(1, nomeCliente);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    txtNome.setText(rs.getString("nome"));
                    txtCpf.setText(rs.getString("cpf"));
                    txtEndereco.setText(rs.getString("endereco"));
                    txtDataNascimento.setText(rs.getString("data_nascimento"));
                    txtTelefone.setText(rs.getString("telefone"));
                } else {
                    JOptionPane.showMessageDialog(this, "Cliente não encontrado.");
                    dispose();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao carregar detalhes do cliente.");
        }
    }

    private void btnSalvarAlteracoesActionPerformed(ActionEvent evt) {
        salvarAlteracoesCliente();
    }

    private void btnCancelarActionPerformed(ActionEvent evt) {
        dispose();
    }

    private void salvarAlteracoesCliente() {
        try (Connection conexao = ConexaoDB.obterConexao();
             PreparedStatement ps = conexao.prepareStatement(
                     "UPDATE clientes SET cpf=?, endereco=?, data_nascimento=?, telefone=? WHERE nome=?")) {

            ps.setString(1, txtCpf.getText());
            ps.setString(2, txtEndereco.getText());
            ps.setString(3, txtDataNascimento.getText());
            ps.setString(4, txtTelefone.getText());
            ps.setString(5, nomeCliente);

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

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DetalhesClienteFrame("ExemploCliente").setVisible(true);
            }
        });
    }
}
