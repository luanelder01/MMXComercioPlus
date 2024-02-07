package menus;

import mmx.ConexaoDB;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

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

        JPanel panel = new JPanel(new BorderLayout());

        btnCadastrarFornecedor = new JButton("Cadastrar Fornecedor");
        btnListarFornecedores = new JButton("Listar Fornecedores");

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

        BotaoVoltar btnVoltar = new BotaoVoltar(this);

        JPanel painelBotoes = new JPanel(new FlowLayout());
        painelBotoes.add(btnVoltar);
        painelBotoes.add(btnCadastrarFornecedor);
        painelBotoes.add(btnListarFornecedores);

        panel.add(painelBotoes, BorderLayout.NORTH);

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
