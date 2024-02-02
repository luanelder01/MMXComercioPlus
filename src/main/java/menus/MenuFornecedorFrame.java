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

public class MenuFornecedorFrame extends JFrame {

    private JComboBox<String> cbFornecedores;
    private JTextField txtNome;
    private JTextField txtCnpj;
    private JTextField txtEndereco;
    private JTextField txtTelefone;
    private JButton btnSelecionar;
    private JButton btnEditar;
    private JButton btnExcluir;
    private JButton btnSalvarAlteracoes;
    private JButton btnCadastrarFornecedor;
    private boolean modoEdicao = false;

    public MenuFornecedorFrame() {
        initComponents();
        popularFornecedores();
    }

    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Menu Fornecedor");

        JPanel panel = new JPanel();

        JLabel lblFornecedores = new JLabel("Fornecedores:");
        cbFornecedores = new JComboBox<>();
        txtNome = new JTextField();
        txtCnpj = new JTextField();
        txtEndereco = new JTextField();
        txtTelefone = new JTextField();
        btnSelecionar = new JButton("Selecionar");
        btnEditar = new JButton("Editar");
        btnExcluir = new JButton("Excluir");
        btnSalvarAlteracoes = new JButton("Salvar Alterações");
        btnCadastrarFornecedor = new JButton("Cadastrar Fornecedor");

        lblFornecedores.setBounds(10, 10, 100, 25);
        cbFornecedores.setBounds(120, 10, 150, 25);
        btnSelecionar.setBounds(280, 10, 100, 25);

        JLabel lblNome = new JLabel("Nome:");
        lblNome.setBounds(10, 50, 100, 25);
        txtNome.setBounds(120, 50, 150, 25);

        JLabel lblCnpj = new JLabel("CNPJ:");
        lblCnpj.setBounds(10, 90, 100, 25);
        txtCnpj.setBounds(120, 90, 150, 25);

        JLabel lblEndereco = new JLabel("Endereço:");
        lblEndereco.setBounds(10, 130, 100, 25);
        txtEndereco.setBounds(120, 130, 150, 25);

        JLabel lblTelefone = new JLabel("Telefone:");
        lblTelefone.setBounds(10, 170, 100, 25);
        txtTelefone.setBounds(120, 170, 150, 25);

        btnCadastrarFornecedor.setBounds(10, 210, 150, 25);
        btnEditar.setBounds(170, 210, 100, 25);
        btnExcluir.setBounds(280, 210, 100, 25);
        btnSalvarAlteracoes.setBounds(10, 250, 150, 25);

        btnSelecionar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                selecionarFornecedor();
            }
        });

        btnCadastrarFornecedor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                cadastrarFornecedor();
            }
        });

        btnEditar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                editarFornecedor();
            }
        });

        btnSalvarAlteracoes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                salvarAlteracoesFornecedor();
            }
        });

        btnExcluir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                excluirFornecedor();
            }
        });

        panel.setLayout(null);
        panel.add(lblFornecedores);
        panel.add(cbFornecedores);
        panel.add(btnSelecionar);
        panel.add(lblNome);
        panel.add(txtNome);
        panel.add(lblCnpj);
        panel.add(txtCnpj);
        panel.add(lblEndereco);
        panel.add(txtEndereco);
        panel.add(lblTelefone);
        panel.add(txtTelefone);
        panel.add(btnCadastrarFornecedor);
        panel.add(btnEditar);
        panel.add(btnExcluir);
        panel.add(btnSalvarAlteracoes);

        getContentPane().add(panel);

        pack();
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
            popularFornecedores();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao cadastrar fornecedor.");
        }
    }

    private void popularFornecedores() {
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

    private void selecionarFornecedor() {
        String nomeSelecionado = (String) cbFornecedores.getSelectedItem();

        try (Connection conexao = ConexaoDB.obterConexao(); PreparedStatement ps = conexao.prepareStatement("SELECT * FROM fornecedores WHERE nome = ?")) {

            ps.setString(1, nomeSelecionado);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    txtNome.setText(rs.getString("nome"));
                    txtCnpj.setText(rs.getString("cnpj"));
                    txtEndereco.setText(rs.getString("endereco"));
                    txtTelefone.setText(rs.getString("telefone"));

                    modoEdicao = true;
                    habilitarEdicao();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao selecionar fornecedor.");
        }
    }

    private void editarFornecedor() {
        modoEdicao = true;
        habilitarEdicao();
    }

    private void habilitarEdicao() {
        txtNome.setEnabled(modoEdicao);
        txtCnpj.setEnabled(modoEdicao);
        txtEndereco.setEnabled(modoEdicao);
        txtTelefone.setEnabled(modoEdicao);
    }

    private void salvarAlteracoesFornecedor() {
        if (!modoEdicao) {
            JOptionPane.showMessageDialog(this, "Selecione um fornecedor para editar.");
            return;
        }

        String nome = txtNome.getText();
        String cnpj = txtCnpj.getText();
        String endereco = txtEndereco.getText();
        String telefone = txtTelefone.getText();

        try (Connection conexao = ConexaoDB.obterConexao(); PreparedStatement ps = conexao.prepareStatement("UPDATE fornecedores SET nome = ?, cnpj = ?, endereco = ?, telefone = ? WHERE nome = ?")) {

            ps.setString(1, nome);
            ps.setString(2, cnpj);
            ps.setString(3, endereco);
            ps.setString(4, telefone);
            ps.setString(5, (String) cbFornecedores.getSelectedItem());

            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Alterações salvas com sucesso!");
            popularFornecedores();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao salvar alterações do fornecedor.");
        }

        modoEdicao = false;
        habilitarEdicao();
    }

    private void excluirFornecedor() {
        String nomeSelecionado = (String) cbFornecedores.getSelectedItem();

        int confirmacao = JOptionPane.showConfirmDialog(this, "Deseja realmente excluir o fornecedor?", "Confirmação", JOptionPane.YES_NO_OPTION);

        if (confirmacao == JOptionPane.YES_OPTION) {
            try (Connection conexao = ConexaoDB.obterConexao(); PreparedStatement ps = conexao.prepareStatement("DELETE FROM fornecedores WHERE nome = ?")) {

                ps.setString(1, nomeSelecionado);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Fornecedor excluído com sucesso!");
                popularFornecedores();

            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro ao excluir fornecedor.");
            }

            limparCampos();
        }
    }

    private void limparCampos() {
        txtNome.setText("");
        txtCnpj.setText("");
        txtEndereco.setText("");
        txtTelefone.setText("");
        modoEdicao = false;
        habilitarEdicao();
    }
    // Adicione este método à classe MenuFornecedorFrame

    private String obterNomeFornecedorPorId(int idFornecedor) {
        try (Connection conexao = ConexaoDB.obterConexao(); PreparedStatement ps = conexao.prepareStatement("SELECT nome FROM fornecedores WHERE id = ?")) {

            ps.setInt(1, idFornecedor);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("nome");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao obter nome do fornecedor.");
        }

        return "";
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MenuFornecedorFrame().setVisible(true);
            }
        });
    }
}
