package menus;

import java.awt.GridLayout;
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

public class FormularioCadastroUsuario extends JFrame {

    private JTextField txtNome, txtCpf, txtEndereco, txtDataNascimento, txtProfissao, txtSalario, txtTelefone, txtSenha;
    private JButton btnCadastrar;

    public FormularioCadastroUsuario() {
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Cadastro de Usuário");

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(9, 2, 5, 5));

        JLabel lblNome = new JLabel("Nome:");
        txtNome = new JTextField(30);

        JLabel lblCpf = new JLabel("CPF:");
        txtCpf = new JTextField(15);

        JLabel lblEndereco = new JLabel("Endereço:");
        txtEndereco = new JTextField(50);

        JLabel lblDataNascimento = new JLabel("Data de Nascimento (AAAA-MM-DD):");
        txtDataNascimento = new JTextField(10);

        JLabel lblProfissao = new JLabel("Profissão:");
        txtProfissao = new JTextField(20);

        JLabel lblSalario = new JLabel("Salário:");
        txtSalario = new JTextField(10);

        JLabel lblTelefone = new JLabel("Telefone:");
        txtTelefone = new JTextField(15);

        JLabel lblSenha = new JLabel("Senha:");
        txtSenha = new JTextField(15);

        btnCadastrar = new JButton("Cadastrar Usuário");
        btnCadastrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                btnCadastrarActionPerformed(evt);
            }
        });

        panel.add(lblNome); panel.add(txtNome);
        panel.add(lblCpf); panel.add(txtCpf);
        panel.add(lblEndereco); panel.add(txtEndereco);
        panel.add(lblDataNascimento); panel.add(txtDataNascimento);
        panel.add(lblProfissao); panel.add(txtProfissao);
        panel.add(lblSalario); panel.add(txtSalario);
        panel.add(lblTelefone); panel.add(txtTelefone);
        panel.add(lblSenha); panel.add(txtSenha);
        panel.add(btnCadastrar);

        getContentPane().add(panel);

        setSize(400, 300);
        setLocationRelativeTo(null);
    }

    private void btnCadastrarActionPerformed(ActionEvent evt) {
        cadastrarUsuario();
    }

    private void cadastrarUsuario() {
        try (Connection conexao = ConexaoDB.obterConexao();
             PreparedStatement ps = conexao.prepareStatement(
                     "INSERT INTO usuarios (nome, cpf, endereco, data_nascimento, profissao, salario, telefone, senha) VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {

            ps.setString(1, txtNome.getText());
            ps.setString(2, txtCpf.getText());
            ps.setString(3, txtEndereco.getText());
            ps.setString(4, txtDataNascimento.getText());
            ps.setString(5, txtProfissao.getText());
            ps.setDouble(6, Double.parseDouble(txtSalario.getText()));
            ps.setString(7, txtTelefone.getText());
            ps.setString(8, txtSenha.getText());

            int linhasAfetadas = ps.executeUpdate();

            if (linhasAfetadas > 0) {
                JOptionPane.showMessageDialog(this, "Usuário cadastrado com sucesso!");
                limparCampos();
            } else {
                JOptionPane.showMessageDialog(this, "Nenhum cadastro realizado.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao cadastrar usuário.");
        }
    }

    private void limparCampos() {
        txtNome.setText("");
        txtCpf.setText("");
        txtEndereco.setText("");
        txtDataNascimento.setText("");
        txtProfissao.setText("");
        txtSalario.setText("");
        txtTelefone.setText("");
        txtSenha.setText("");
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FormularioCadastroUsuario().setVisible(true);
            }
        });
    }
}
