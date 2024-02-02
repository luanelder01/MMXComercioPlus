package menus;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class MenuEstoqueFrame extends JFrame {
    private JButton btnAdicionar, btnListar, btnEditar, btnRemover;
    private JTable tabelaEstoque;
    private JScrollPane scrollPane;

    private static final String URL = "jdbc:mysql://localhost:3306/mmxdb";
    private static final String USUARIO = "root";
    private static final String SENHA = "CocaCola123-";

    private static final String[] COLUNAS = {"ID", "Código", "Nome", "Preço", "ID Fornecedor", "Quantidade"};

    public MenuEstoqueFrame() {
        setTitle("Menu de Estoque");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        btnAdicionar = new JButton("Adicionar Item");
        btnListar = new JButton("Listar Itens");
        btnEditar = new JButton("Editar Item");
        btnRemover = new JButton("Remover Item");

        tabelaEstoque = new JTable();
        scrollPane = new JScrollPane(tabelaEstoque);

        JPanel painelBotoes = new JPanel(new FlowLayout());
        painelBotoes.add(btnAdicionar);
        painelBotoes.add(btnListar);
        painelBotoes.add(btnEditar);
        painelBotoes.add(btnRemover);

        add(painelBotoes, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        tabelaEstoque.setModel(new DefaultTableModel(COLUNAS, 0));

        btnAdicionar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                adicionarItem();
            }
        });

        btnListar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listarItens();
                // Oculta a coluna "ID"
                tabelaEstoque.getColumnModel().getColumn(0).setMinWidth(0);
                tabelaEstoque.getColumnModel().getColumn(0).setMaxWidth(0);
                tabelaEstoque.getColumnModel().getColumn(0).setWidth(0);
            }
        });

        btnEditar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editarItem();
            }
        });

        btnRemover.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removerItem();
            }
        });
    }

    private void adicionarItem() {
        try {
            Connection conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
            String sqlFornecedores = "SELECT id, nome FROM fornecedores";
            try (PreparedStatement stmtFornecedores = conexao.prepareStatement(sqlFornecedores);
                 ResultSet rsFornecedores = stmtFornecedores.executeQuery()) {

                JComboBox<String> comboFornecedores = new JComboBox<>();
                while (rsFornecedores.next()) {
                    int idFornecedor = rsFornecedores.getInt("id");
                    String nomeFornecedor = rsFornecedores.getString("nome");
                    comboFornecedores.addItem(idFornecedor + " - " + nomeFornecedor);
                }

                int escolha = JOptionPane.showOptionDialog(
                        null,
                        comboFornecedores,
                        "Selecione um fornecedor",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        null,
                        null);

                if (escolha == JOptionPane.OK_OPTION) {
                    String fornecedorSelecionado = comboFornecedores.getSelectedItem().toString();
                    int idFornecedor = Integer.parseInt(fornecedorSelecionado.split("-")[0].trim());

                    // Lógica para adicionar um item ao estoque
                    String codigo = JOptionPane.showInputDialog("Digite o código do item:");
                    String nome = JOptionPane.showInputDialog("Digite o nome do item:");
                    double preco = Double.parseDouble(JOptionPane.showInputDialog("Digite o preço do item:"));
                    int quantidade = Integer.parseInt(JOptionPane.showInputDialog("Digite a quantidade do item:"));

                    String sql = "INSERT INTO estoque (codigo, nome, preco, id_fornecedor, quantidade) VALUES (?, ?, ?, ?, ?)";
                    try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
                        stmt.setString(1, codigo);
                        stmt.setString(2, nome);
                        stmt.setDouble(3, preco);
                        stmt.setInt(4, idFornecedor);
                        stmt.setInt(5, quantidade);

                        stmt.executeUpdate();
                        JOptionPane.showMessageDialog(null, "Item adicionado com sucesso!");
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void listarItens() {
        DefaultTableModel modeloTabela = new DefaultTableModel(COLUNAS, 0);
        try (Connection conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
             Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM estoque")) {

            while (rs.next()) {
                Object[] linha = {
                        rs.getInt("id"),
                        rs.getString("codigo"),
                        rs.getString("nome"),
                        rs.getDouble("preco"),
                        rs.getInt("id_fornecedor"),
                        rs.getInt("quantidade")
                };
                modeloTabela.addRow(linha);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        tabelaEstoque.setModel(modeloTabela);
    }

    private void editarItem() {
        try {
            Connection conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
            String sqlItens = "SELECT id, nome FROM estoque";
            try (PreparedStatement stmtItens = conexao.prepareStatement(sqlItens);
                 ResultSet rsItens = stmtItens.executeQuery()) {

                JComboBox<String> comboItens = new JComboBox<>();
                while (rsItens.next()) {
                    int idItem = rsItens.getInt("id");
                    String nomeItem = rsItens.getString("nome");
                    comboItens.addItem(idItem + " - " + nomeItem);
                }

                int escolha = JOptionPane.showOptionDialog(
                        null,
                        comboItens,
                        "Selecione um item",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        null,
                        null);

                if (escolha == JOptionPane.OK_OPTION) {
                    String itemSelecionado = comboItens.getSelectedItem().toString();
                    int idItem = Integer.parseInt(itemSelecionado.split("-")[0].trim());
                    exibirFormularioEdicao(idItem);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void exibirFormularioEdicao(int idItem) {
        try {
            Connection conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
            String sql = "SELECT * FROM estoque WHERE id = ?";
            try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
                stmt.setInt(1, idItem);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    // Criar um formulário para editar os campos
                    JTextField txtCodigo = new JTextField(rs.getString("codigo"));
                    JTextField txtNome = new JTextField(rs.getString("nome"));
                    JTextField txtPreco = new JTextField(String.valueOf(rs.getDouble("preco")));
                    JTextField txtQuantidade = new JTextField(String.valueOf(rs.getInt("quantidade")));

                    JPanel panel = new JPanel(new GridLayout(4, 2));
                    panel.add(new JLabel("Código:"));
                    panel.add(txtCodigo);
                    panel.add(new JLabel("Nome:"));
                    panel.add(txtNome);
                    panel.add(new JLabel("Preço:"));
                    panel.add(txtPreco);
                    panel.add(new JLabel("Quantidade:"));
                    panel.add(txtQuantidade);

                    int result = JOptionPane.showConfirmDialog(null, panel, "Editar Item",
                            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                    if (result == JOptionPane.OK_OPTION) {
                        // Atualizar o item com os novos valores
                        atualizarItem(idItem, txtCodigo.getText(), txtNome.getText(),
                                Double.parseDouble(txtPreco.getText()), Integer.parseInt(txtQuantidade.getText()));
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void atualizarItem(int idItem, String novoCodigo, String novoNome, double novoPreco, int novaQuantidade) {
        try {
            Connection conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
            String sql = "UPDATE estoque SET codigo = ?, nome = ?, preco = ?, quantidade = ? WHERE id = ?";
            try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
                stmt.setString(1, novoCodigo);
                stmt.setString(2, novoNome);
                stmt.setDouble(3, novoPreco);
                stmt.setInt(4, novaQuantidade);
                stmt.setInt(5, idItem);

                int linhasAfetadas = stmt.executeUpdate();
                if (linhasAfetadas > 0) {
                    JOptionPane.showMessageDialog(null, "Item editado com sucesso!");
                } else {
                    JOptionPane.showMessageDialog(null, "Nenhum item encontrado com o ID fornecido.");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void removerItem() {
        try {
            Connection conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
            String sqlItens = "SELECT id, nome FROM estoque";
            try (PreparedStatement stmtItens = conexao.prepareStatement(sqlItens);
                 ResultSet rsItens = stmtItens.executeQuery()) {

                JComboBox<String> comboItens = new JComboBox<>();
                while (rsItens.next()) {
                    int idItem = rsItens.getInt("id");
                    String nomeItem = rsItens.getString("nome");
                    comboItens.addItem(idItem + " - " + nomeItem);
                }

                int escolha = JOptionPane.showOptionDialog(
                        null,
                        comboItens,
                        "Selecione um item",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        null,
                        null);

                if (escolha == JOptionPane.OK_OPTION) {
                    String itemSelecionado = comboItens.getSelectedItem().toString();
                    int idItem = Integer.parseInt(itemSelecionado.split("-")[0].trim());

                    String sql = "DELETE FROM estoque WHERE id = ?";
                    try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
                        stmt.setInt(1, idItem);

                        int linhasAfetadas = stmt.executeUpdate();
                        if (linhasAfetadas > 0) {
                            JOptionPane.showMessageDialog(null, "Item removido com sucesso!");
                        } else {
                            JOptionPane.showMessageDialog(null, "Nenhum item encontrado com o ID fornecido.");
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MenuEstoqueFrame().setVisible(true);
            }
        });
    }
}
