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

public class DetalheFornecedorFrame extends JFrame {

    private JTextField txtNome;
    private JTextField txtCnpj;
    private JTextField txtEndereco;
    private JTextField txtTelefone;
    private JButton btnSalvar;
    private JButton btnExcluir;

    private String fornecedorSelecionado;

    public DetalheFornecedorFrame(String fornecedorSelecionado) {
        this.fornecedorSelecionado = fornecedorSelecionado;
        initComponents();
        carregarDetalhes();
    }

    private void initComponents() {
        setTitle("Detalhes do Fornecedor");
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
        btnSalvar = new JButton("Salvar");
        btnExcluir = new JButton("Excluir");

        lblNome.setBounds(10, 10, 100, 25);
        txtNome.setBounds(120, 10, 250, 25);
        lblCnpj.setBounds(10, 50, 100, 25);
        txtCnpj.setBounds(120, 50, 250, 25);
        lblEndereco.setBounds(10, 90, 100, 25);
        txtEndereco.setBounds(120, 90, 250, 25);
        lblTelefone.setBounds(10, 130, 100, 25);
        txtTelefone.setBounds(120, 130, 250, 25);
        btnSalvar.setBounds(10, 170, 150, 25);
        btnExcluir.setBounds(170, 170, 150, 25);

        btnSalvar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                salvarAlteracoes();
            }
        });

        btnExcluir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                excluirFornecedor();
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
        panel.add(btnSalvar);
        panel.add(btnExcluir);

        getContentPane().add(panel);

        setLocationRelativeTo(null);
    }

    private void carregarDetalhes() {
        try (Connection conexao = ConexaoDB.obterConexao(); PreparedStatement ps = conexao.prepareStatement("SELECT * FROM fornecedores WHERE nome = ?")) {

            ps.setString(1, fornecedorSelecionado);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    txtNome.setText(rs.getString("nome"));
                    txtCnpj.setText(rs.getString("cnpj"));
                    txtEndereco.setText(rs.getString("endereco"));
                    txtTelefone.setText(rs.getString("telefone"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao carregar detalhes do fornecedor.");
        }
    }

    private void salvarAlteracoes() {
        String nome = txtNome.getText();
        String cnpj = txtCnpj.getText();
        String endereco = txtEndereco.getText();
        String telefone = txtTelefone.getText();

        try (Connection conexao = ConexaoDB.obterConexao(); PreparedStatement ps = conexao.prepareStatement("UPDATE fornecedores SET nome = ?, cnpj = ?, endereco = ?, telefone = ? WHERE nome = ?")) {

            ps.setString(1, nome);
            ps.setString(2, cnpj);
            ps.setString(3, endereco);
            ps.setString(4, telefone);
            ps.setString(5, fornecedorSelecionado);

            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Alterações salvas com sucesso!");

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao salvar alterações do fornecedor.");
        }
    }

    private void excluirFornecedor() {
        int confirmacao = JOptionPane.showConfirmDialog(this, "Deseja realmente excluir o fornecedor?", "Confirmação", JOptionPane.YES_NO_OPTION);

        if (confirmacao == JOptionPane.YES_OPTION) {
            try (Connection conexao = ConexaoDB.obterConexao(); PreparedStatement ps = conexao.prepareStatement("DELETE FROM fornecedores WHERE nome = ?")) {

                ps.setString(1, fornecedorSelecionado);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Fornecedor excluído com sucesso!");
                dispose(); // Fecha o formulário após excluir

            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro ao excluir fornecedor.");
            }
        }
    }
}
