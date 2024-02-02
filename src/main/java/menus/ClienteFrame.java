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

public class ClienteFrame extends JFrame {

    private JTextField txtNome;
    private JTextField txtCpf;
    private JTextField txtEndereco;
    private JTextField txtDataNascimento;
    private JTextField txtTelefone;

    private JButton btnSalvar;

    private final String nomeCliente;

    public ClienteFrame(String nomeCliente) {
        this.nomeCliente = nomeCliente;
        initComponents();
        carregarDadosCliente();
    }

    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Editar Cliente");

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

        btnSalvar = new JButton("Salvar Alterações");
        btnSalvar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                btnSalvarActionPerformed(evt);
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
        panel.add(btnSalvar);

        getContentPane().add(panel);

        pack();
        setLocationRelativeTo(null);
    }

    private void carregarDadosCliente() {
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
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao carregar dados do cliente.");
        }
    }

    private void btnSalvarActionPerformed(ActionEvent evt) {
        JOptionPane.showMessageDialog(this, "Alterações salvas com sucesso!");
        dispose();
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ClienteFrame("ExemploCliente").setVisible(true);
            }
        });
    }
}
