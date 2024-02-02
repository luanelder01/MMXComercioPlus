package menus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import mmx.ConexaoDB;

public class MenuFornecedorFrame extends JFrame {

    private JButton btnCadastrarFornecedor;
    private JButton btnListarFornecedores;

    public MenuFornecedorFrame() {
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Menu Fornecedor");
        setSize(800, 600);

        JPanel panel = new JPanel();

        btnCadastrarFornecedor = new JButton("Cadastrar Fornecedor");
        btnListarFornecedores = new JButton("Listar Fornecedores");

        btnCadastrarFornecedor.setBounds(10, 10, 200, 25);
        btnListarFornecedores.setBounds(220, 10, 200, 25);

        btnCadastrarFornecedor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                abrirFormularioCadastro();
            }
        });

        btnListarFornecedores.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                listarFornecedores();
            }
        });

        panel.setLayout(null);
        panel.add(btnCadastrarFornecedor);
        panel.add(btnListarFornecedores);

        getContentPane().add(panel);

        setLocationRelativeTo(null);
    }

    private void abrirFormularioCadastro() {
        FormularioCadastroFornecedor formularioCadastro = new FormularioCadastroFornecedor(this, true);
        formularioCadastro.setVisible(true);
    }

    private void listarFornecedores() {
        JComboBox<String> cbFornecedores = new JComboBox<>();
        popularFornecedores(cbFornecedores);

        JOptionPane.showMessageDialog(this, cbFornecedores, "Listar Fornecedores", JOptionPane.PLAIN_MESSAGE);

        String fornecedorSelecionado = (String) cbFornecedores.getSelectedItem();

        if (fornecedorSelecionado != null) {
            DetalheFornecedorFrame detalheFrame = new DetalheFornecedorFrame(fornecedorSelecionado);
            detalheFrame.setVisible(true);
        }
    }

    private void popularFornecedores(JComboBox<String> cbFornecedores) {
        cbFornecedores.removeAllItems();

        try (Connection conexao = ConexaoDB.obterConexao(); PreparedStatement ps = conexao.prepareStatement("SELECT * FROM fornecedores"); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String nome = rs.getString("nome");
                cbFornecedores.addItem(nome);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao obter fornecedores.");
        }
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MenuFornecedorFrame().setVisible(true);
            }
        });
    }
}
