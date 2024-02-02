package menus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import mmx.ConexaoDB;

public class FormularioCadastroFornecedor extends JDialog {

    private JTextField txtNome;
    private JTextField txtCnpj;
    private JTextField txtEndereco;
    private JTextField txtTelefone;

    public FormularioCadastroFornecedor(JFrame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    private void initComponents() {
        setTitle("Cadastro de Fornecedor");
        setSize(400, 300);

        JPanel panel = new JPanel();

        JLabel lblNome = new JLabel("Nome:");
        txtNome = new JTextField();
        JLabel lblCnpj = new JLabel("CNPJ:");
        txtCnpj = new JTextField();
        JLabel lblEndereco = new JLabel("Endereço:");
        txtEndereco = new JTextField();
        JLabel lblTelefone = new JLabel("Telefone:");
        txtTelefone = new JTextField();
        JButton btnCadastrar = new JButton("Cadastrar");

        lblNome.setBounds(10, 10, 100, 25);
        txtNome.setBounds(120, 10, 250, 25);
        lblCnpj.setBounds(10, 50, 100, 25);
        txtCnpj.setBounds(120, 50, 250, 25);
        lblEndereco.setBounds(10, 90, 100, 25);
        txtEndereco.setBounds(120, 90, 250, 25);
        lblTelefone.setBounds(10, 130, 100, 25);
        txtTelefone.setBounds(120, 130, 250, 25);
        btnCadastrar.setBounds(10, 170, 150, 25);

        btnCadastrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                cadastrarFornecedor();
            }
        });

        panel.setLayout(null);
        panel.add(lblNome);
        panel.add(txtNome);
        panel.add(lblCnpj);
        panel.add(txtCnpj);
        panel.add(lblEndereco);
        panel.add(txtEndereco);
        panel.add(lblTelefone);
        panel.add(txtTelefone);
        panel.add(btnCadastrar);

        getContentPane().add(panel);

        setLocationRelativeTo(null);
    }

    private void cadastrarFornecedor() {
        String nome = txtNome.getText();
        String cnpj = txtCnpj.getText();
        String endereco = txtEndereco.getText();
        String telefone = txtTelefone.getText();

        try (Connection conexao = ConexaoDB.obterConexao(); PreparedStatement ps = conexao.prepareStatement("INSERT INTO fornecedores (nome, cnpj, endereco, telefone) VALUES (?, ?, ?, ?)")) {

            ps.setString(1, nome);
            ps.setString(2, cnpj);
            ps.setString(3, endereco);
            ps.setString(4, telefone);

            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Fornecedor cadastrado com sucesso!");

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao cadastrar fornecedor.");
        }

        dispose(); // Fecha o formulário após cadastrar
    }
}
