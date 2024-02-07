package menus;

import mmx.ConexaoDB;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MenuClienteFrame extends JFrame {

    private JButton btnCadastrarCliente;
    private JButton btnListarClientes;
    private JFrame telaAtual;

    public MenuClienteFrame() {
        this.telaAtual = this;
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Menu Cliente");

        JPanel panel = new JPanel();

        btnCadastrarCliente = new JButton("Cadastrar Cliente");
        btnCadastrarCliente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                btnCadastrarClienteActionPerformed(evt);
            }
        });

        btnListarClientes = new JButton("Listar Clientes");
        btnListarClientes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                btnListarClientesActionPerformed(evt);
            }
        });

        panel.add(btnCadastrarCliente);
        panel.add(btnListarClientes);

        BotaoVoltar botaoVoltar = new BotaoVoltar(this);
        panel.add(botaoVoltar);

        getContentPane().add(panel);

        pack();
        setSize(800, 600);
        setLocationRelativeTo(null);
    }

    private void btnCadastrarClienteActionPerformed(ActionEvent evt) {
        new FormularioCadastroCliente().setVisible(true);
    }

    private void btnListarClientesActionPerformed(ActionEvent evt) {
        List<String> clientes = obterNomesClientes();
        JComboBox<String> cbClientes = new JComboBox<>(clientes.toArray(new String[0]));

        JPanel panel = new JPanel();
        panel.add(new JLabel("Selecione um cliente:"));
        panel.add(cbClientes);

        int result = JOptionPane.showConfirmDialog(null, panel, "Listar Clientes",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String selectedCliente = (String) cbClientes.getSelectedItem();
            if (selectedCliente != null) {
                ClienteFrame clienteFrame = obterClienteFrame(selectedCliente);
                clienteFrame.setVisible(true);
            }
        }
    }

    private ClienteFrame obterClienteFrame(String nomeCliente) {
        ClienteFrame clienteFrame = new ClienteFrame(nomeCliente);
        return clienteFrame;
    }

    private List<String> obterNomesClientes() {
        List<String> nomesClientes = new ArrayList<>();
        try (Connection conexao = ConexaoDB.obterConexao();
             PreparedStatement ps = conexao.prepareStatement("SELECT nome FROM clientes");
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                nomesClientes.add(rs.getString("nome"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao obter nomes de clientes.");
        }
        return nomesClientes;
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MenuClienteFrame().setVisible(true);
            }
        });
    }
}
