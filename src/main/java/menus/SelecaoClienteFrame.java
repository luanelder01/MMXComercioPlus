package menus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import mmx.ConexaoDB;

public class SelecaoClienteFrame extends JFrame {

    private JComboBox<String> cbClientes;
    private JTextField txtNome;
    private JTextField txtCpf;
    private JTextField txtEndereco;
    private JTextField txtDataNascimento;
    private JTextField txtTelefone;
    private JButton btnSelecionar;
    private JButton btnEditar;
    private JButton btnExcluir;
    private JButton btnSalvarAlteracoes;
    private boolean modoEdicao = false;

    public SelecaoClienteFrame() {
        initComponents();
        popularClientes();
    }

    private void initComponents() {
        cbClientes = new JComboBox<>();
        txtNome = new JTextField();
        txtCpf = new JTextField();
        txtEndereco = new JTextField();
        txtDataNascimento = new JTextField();
        txtTelefone = new JTextField();
        btnSelecionar = new JButton("Selecionar Cliente");
        btnEditar = new JButton("Editar Cliente");
        btnExcluir = new JButton("Excluir Cliente");
        btnSalvarAlteracoes = new JButton("Salvar Alterações");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Seleção de Cliente");

        JPanel panel = new JPanel();

        JLabel lblClientes = new JLabel("Clientes:");

        cbClientes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                selecionarCliente();
            }
        });

        panel.add(lblClientes);
        panel.add(cbClientes);
        panel.add(btnSelecionar);
        panel.add(btnEditar);
        panel.add(btnExcluir);
        panel.add(txtNome);
        panel.add(txtCpf);
        panel.add(txtEndereco);
        panel.add(txtDataNascimento);
        panel.add(txtTelefone);
        panel.add(btnSalvarAlteracoes);

        getContentPane().add(panel);

        pack();
        setLocationRelativeTo(null);

        btnSelecionar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                selecionarCliente();
            }
        });

        btnEditar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                entrarModoEdicao();
            }
        });

        btnExcluir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                excluirCliente();
            }
        });

        btnSalvarAlteracoes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                salvarAlteracoes();
            }
        });
    }

    private void popularClientes() {
        List<String> clientes = obterNomesClientes();
        for (String cliente : clientes) {
            cbClientes.addItem(cliente);
        }
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

    private void selecionarCliente() {
        String nomeCliente = (String) cbClientes.getSelectedItem();
        if (nomeCliente != null) {
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
                        desabilitarCampos();
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro ao selecionar cliente.");
            }
        }
    }

    private void entrarModoEdicao() {
        modoEdicao = true;
        habilitarCampos();
    }

    private void habilitarCampos() {
        txtNome.setEnabled(true);
        txtCpf.setEnabled(true);
        txtEndereco.setEnabled(true);
        txtDataNascimento.setEnabled(true);
        txtTelefone.setEnabled(true);
        btnSalvarAlteracoes.setEnabled(true);
    }

    private void desabilitarCampos() {
        txtNome.setEnabled(false);
        txtCpf.setEnabled(false);
        txtEndereco.setEnabled(false);
        txtDataNascimento.setEnabled(false);
        txtTelefone.setEnabled(false);
        btnSalvarAlteracoes.setEnabled(false);
    }

    private void salvarAlteracoes() {
        if (modoEdicao) {
            String nomeCliente = (String) cbClientes.getSelectedItem();
            if (nomeCliente != null) {
                try (Connection conexao = ConexaoDB.obterConexao();
                     PreparedStatement ps = conexao.prepareStatement("UPDATE clientes SET nome = ?, cpf = ?, endereco = ?, data_nascimento = ?, telefone = ? WHERE nome = ?")) {

                    ps.setString(1, txtNome.getText());
                    ps.setString(2, txtCpf.getText());
                    ps.setString(3, txtEndereco.getText());
                    ps.setString(4, txtDataNascimento.getText());
                    ps.setString(5, txtTelefone.getText());
                    ps.setString(6, nomeCliente);

                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Alterações salvas com sucesso!");
                    modoEdicao = false;
                    desabilitarCampos();
                    popularClientes();

                } catch (SQLException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Erro ao salvar alterações.");
                }
            }
        }
    }

    private void excluirCliente() {
        String nomeCliente = (String) cbClientes.getSelectedItem();
        if (nomeCliente != null) {
            int confirmacao = JOptionPane.showConfirmDialog(this, "Deseja realmente excluir o cliente?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (confirmacao == JOptionPane.YES_OPTION) {
                try (Connection conexao = ConexaoDB.obterConexao();
                     PreparedStatement ps = conexao.prepareStatement("DELETE FROM clientes WHERE nome = ?")) {

                    ps.setString(1, nomeCliente);
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Cliente excluído com sucesso!");
                    modoEdicao = false;
                    desabilitarCampos();
                    cbClientes.removeItem(nomeCliente);

                } catch (SQLException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Erro ao excluir cliente.");
                }
            }
        }
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SelecaoClienteFrame().setVisible(true);
            }
        });
    }
}
