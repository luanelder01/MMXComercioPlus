package menus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import mmx.ConexaoDB;

public class FormularioCadastroCliente extends JFrame {

    private JTextField txtNome;
    private JTextField txtCpf;
    private JTextField txtEndereco;
    private JTextField txtDataNascimento;
    private JTextField txtTelefone;

    private JButton btnCadastrar;

    public FormularioCadastroCliente() {
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Cadastro de Cliente");

        JPanel panel = new JPanel();

        JLabel lblNome = new JLabel("Nome:");
        JLabel lblCpf = new JLabel("CPF:");
        JLabel lblEndereco = new JLabel("Endereço:");
        JLabel lblDataNascimento = new JLabel("Data de Nascimento (AAAA-MM-DD):");
        JLabel lblTelefone = new JLabel("Telefone:");

        txtNome = new JTextField(30);
        txtCpf = new JTextField(15);
        txtEndereco = new JTextField(50);
        txtDataNascimento = new JTextField(10);
        txtTelefone = new JTextField(15);

        btnCadastrar = new JButton("Cadastrar Cliente");
        btnCadastrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                btnCadastrarActionPerformed(evt);
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
        panel.add(btnCadastrar);

        getContentPane().add(panel);

        pack();
        setLocationRelativeTo(null);
    }

    private void btnCadastrarActionPerformed(ActionEvent evt) {
        cadastrarCliente();
    }

    private void cadastrarCliente() {
        try (Connection conexao = ConexaoDB.obterConexao();
             PreparedStatement ps = conexao.prepareStatement(
                     "INSERT INTO clientes (nome, cpf, endereco, data_nascimento, telefone) VALUES (?, ?, ?, ?, ?)")) {

            ps.setString(1, txtNome.getText());
            ps.setString(2, txtCpf.getText());
            ps.setString(3, txtEndereco.getText());
            ps.setString(4, txtDataNascimento.getText());
            ps.setString(5, txtTelefone.getText());

            int linhasAfetadas = ps.executeUpdate();

            if (linhasAfetadas > 0) {
                JOptionPane.showMessageDialog(this, "Cliente cadastrado com sucesso!");
                limparCampos();
            } else {
                JOptionPane.showMessageDialog(this, "Nenhum cadastro realizado.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao cadastrar cliente.");
        }
    }

    private void limparCampos() {
        txtNome.setText("");
        txtCpf.setText("");
        txtEndereco.setText("");
        txtDataNascimento.setText("");
        txtTelefone.setText("");
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FormularioCadastroCliente().setVisible(true);
            }
        });
    }
}
