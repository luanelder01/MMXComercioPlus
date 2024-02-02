package menus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import mmx.ConexaoDB;

public class MenuEstoqueFrame extends JFrame {

    private JButton btnCadastrar;
    private JButton btnEditar;
    private JButton btnExcluir;
    private JTable tblEstoque;
    private JComboBox<String> comboFornecedores;
    private DefaultTableModel modeloTabela;
    private List<Integer> idFornecedores;
    private boolean modoEdicao;

    public MenuEstoqueFrame() {
        initComponents();
        carregarFornecedores();
        carregarEstoque();
    }

    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Menu Estoque");

        btnCadastrar = new JButton("Cadastrar Item");
        btnCadastrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                btnCadastrarActionPerformed(evt);
            }
        });

        btnEditar = new JButton("Editar Item");
        btnEditar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                btnEditarActionPerformed(evt);
            }
        });

        btnExcluir = new JButton("Excluir Item");
        btnExcluir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                btnExcluirActionPerformed(evt);
            }
        });

        modeloTabela = new DefaultTableModel(
                new Object[][]{},
                new String[]{"ID", "Código", "Nome", "Preço", "Quantidade", "Fornecedor"});

        tblEstoque = new JTable(modeloTabela);

        JScrollPane scrollPane = new JScrollPane(tblEstoque);

        comboFornecedores = new JComboBox<>();

        setLayout(new java.awt.GridLayout(3, 1));
        add(btnCadastrar);
        add(btnEditar);
        add(btnExcluir);
        add(comboFornecedores);
        add(scrollPane);

        pack();
        setLocationRelativeTo(null);
    }

    private void carregarFornecedores() {
        idFornecedores = new ArrayList<>();
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();

        try (Connection conexao = ConexaoDB.obterConexao();
             PreparedStatement ps = conexao.prepareStatement("SELECT id, nome FROM fornecedores");
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                idFornecedores.add(id);
                model.addElement(nome);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao carregar fornecedores.");
        }

        comboFornecedores.setModel(model);
    }

    private void carregarEstoque() {
        modeloTabela.setRowCount(0);

        try (Connection conexao = ConexaoDB.obterConexao();
             PreparedStatement ps = conexao.prepareStatement(
                     "SELECT e.id, e.codigo, e.nome, e.preco, e.quantidade, f.nome AS fornecedor " +
                             "FROM estoque e " +
                             "JOIN fornecedores f ON e.id_fornecedor = f.id");
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String codigo = rs.getString("codigo");
                String nome = rs.getString("nome");
                double preco = rs.getDouble("preco");
                int quantidade = rs.getInt("quantidade");
                String fornecedor = rs.getString("fornecedor");

                modeloTabela.addRow(new Object[]{id, codigo, nome, preco, quantidade, fornecedor});
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao carregar estoque.");
        }
    }

    private void btnCadastrarActionPerformed(ActionEvent evt) {
        // Código para cadastrar um novo item no estoque
    }

    private void btnEditarActionPerformed(ActionEvent evt) {
        // Código para editar o item selecionado no estoque
    }

    private void btnExcluirActionPerformed(ActionEvent evt) {
        // Código para excluir o item selecionado no estoque
    }

    private void limparCampos() {
        // Método para limpar os campos do formulário
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MenuEstoqueFrame().setVisible(true);
            }
        });
    }
}
private void cadastrarItem(String codigo, String nome, double preco, int id_fornecedor, int quantidade) {
    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/seu_banco_de_dados", "usuario", "senha");
         PreparedStatement ps = conn.prepareStatement("INSERT INTO estoque (codigo, nome, preco, id_fornecedor, quantidade) VALUES (?, ?, ?, ?, ?)")) {

        ps.setString(1, codigo);
        ps.setString(2, nome);
        ps.setDouble(3, preco);
        ps.setInt(4, id_fornecedor);
        ps.setInt(5, quantidade);

        ps.executeUpdate();

    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Erro ao cadastrar item.");
    }
}

private void editarItem(int id, String codigo, String nome, double preco, int id_fornecedor, int quantidade) {
    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/seu_banco_de_dados", "usuario", "senha");
         PreparedStatement ps = conn.prepareStatement("UPDATE estoque SET codigo = ?, nome = ?, preco = ?, id_fornecedor = ?, quantidade = ? WHERE id = ?")) {

        ps.setString(1, codigo);
        ps.setString(2, nome);
        ps.setDouble(3, preco);
        ps.setInt(4, id_fornecedor);
        ps.setInt(5, quantidade);
        ps.setInt(6, id);

        ps.executeUpdate();

    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Erro ao editar item.");
    }
}

private void excluirItem(int id) {
    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/seu_banco_de_dados", "usuario", "senha");
         PreparedStatement ps = conn.prepareStatement("DELETE FROM estoque WHERE id = ?")) {

        ps.setInt(1, id);

        ps.executeUpdate();

    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Erro ao excluir item.");
    }
}