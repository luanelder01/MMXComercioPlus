package menus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MenuVendaFrame extends JFrame {
    private JButton btnAdicionarAoCarrinho, btnFinalizar;
    private JComboBox<String> comboClientes, comboUsuarios, comboProdutos;
    private JTextField txtQuantidade;
    private JTextArea txtCarrinho;
    private JLabel lblValorTotal;
    private double valorTotal;
    private List<ItemVenda> carrinho;

    private static final String URL = "jdbc:mysql://localhost:3306/mmxdb";
    private static final String USUARIO = "root";
    private static final String SENHA = "CocaCola123-";

    public MenuVendaFrame() {
        setTitle("Menu de Venda");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        btnAdicionarAoCarrinho = new JButton("Adicionar ao Carrinho");
        btnFinalizar = new JButton("Finalizar");

        comboClientes = new JComboBox<>();
        comboUsuarios = new JComboBox<>();
        comboProdutos = new JComboBox<>();
        txtQuantidade = new JTextField(5);
        txtCarrinho = new JTextArea();
        lblValorTotal = new JLabel("Valor Total: R$ 0.0");

        JPanel painelBotoes = new JPanel(new FlowLayout());
        painelBotoes.add(btnAdicionarAoCarrinho);
        painelBotoes.add(btnFinalizar);

        JPanel painelClientes = new JPanel(new FlowLayout());
        painelClientes.add(new JLabel("Cliente:"));
        painelClientes.add(comboClientes);

        JPanel painelUsuarios = new JPanel(new FlowLayout());
        painelUsuarios.add(new JLabel("Usuário:"));
        painelUsuarios.add(comboUsuarios);

        JPanel painelProdutos = new JPanel(new FlowLayout());
        painelProdutos.add(new JLabel("Produto:"));
        painelProdutos.add(comboProdutos);
        painelProdutos.add(new JLabel("Quantidade:"));
        painelProdutos.add(txtQuantidade);

        JPanel painelCarrinho = new JPanel(new BorderLayout());
        painelCarrinho.add(lblValorTotal, BorderLayout.NORTH);
        painelCarrinho.add(new JScrollPane(txtCarrinho), BorderLayout.CENTER);

        add(painelBotoes, BorderLayout.NORTH);
        add(painelClientes, BorderLayout.WEST);
        add(painelUsuarios, BorderLayout.CENTER);
        add(painelProdutos, BorderLayout.EAST);
        add(painelCarrinho, BorderLayout.SOUTH);

        carrinho = new ArrayList<>();

        carregarClientes();
        carregarUsuarios();
        carregarProdutos();

        btnAdicionarAoCarrinho.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                adicionarAoCarrinho();
            }
        });

        btnFinalizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                finalizarVenda();
            }
        });
    }

    private void carregarClientes() {
        try (Connection conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
             Statement statement = conexao.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT nome FROM clientes")) {

            while (resultSet.next()) {
                String nomeCliente = resultSet.getString("nome");
                comboClientes.addItem(nomeCliente);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao carregar clientes do banco de dados.");
        }
    }

    private void carregarUsuarios() {
        try (Connection conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
             Statement statement = conexao.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT nome FROM usuarios")) {

            while (resultSet.next()) {
                String nomeUsuario = resultSet.getString("nome");
                comboUsuarios.addItem(nomeUsuario);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao carregar usuários do banco de dados.");
        }
    }

    private void adicionarAoCarrinho() {
        String produtoSelecionado = comboProdutos.getSelectedItem().toString();
        int quantidade = Integer.parseInt(txtQuantidade.getText());

        // Lógica para obter o preço do produto do banco de dados
        double preco = obterPrecoProduto(produtoSelecionado);

        carrinho.add(new ItemVenda(produtoSelecionado, quantidade, preco));

        exibirCarrinho();

        // Atualizar valor total
        calcularValorTotal();

        // Limpar campos após adicionar ao carrinho
        comboProdutos.setSelectedIndex(0);
        txtQuantidade.setText("");
    }

    private void carregarProdutos() {
        try (Connection conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
             Statement statement = conexao.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT nome FROM estoque")) {

            while (resultSet.next()) {
                String nomeProduto = resultSet.getString("nome");
                comboProdutos.addItem(nomeProduto);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao carregar produtos do banco de dados.");
        }
    }

    private double obterPrecoProduto(String nomeProduto) {
        try (Connection conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
             PreparedStatement ps = conexao.prepareStatement("SELECT preco FROM estoque WHERE nome = ?")) {

            ps.setString(1, nomeProduto);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("preco");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao obter preço do produto do banco de dados.");
        }

        return 0.0;
    }

    private void exibirCarrinho() {
        StringBuilder carrinhoTexto = new StringBuilder("Carrinho de Compras:\n");

        for (ItemVenda item : carrinho) {
            carrinhoTexto.append(item.getProduto()).append(" - Quantidade: ").append(item.getQuantidade()).append("\n");
        }

        txtCarrinho.setText(carrinhoTexto.toString());
    }

    private void calcularValorTotal() {
        valorTotal = 0;

        for (ItemVenda item : carrinho) {
            valorTotal += (item.getPreco() * item.getQuantidade());
        }

        lblValorTotal.setText("Valor Total: R$ " + String.format("%.2f", valorTotal));
    }

    private void finalizarVenda() {
        // Exemplo de obtenção de data atual
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dataAtual = sdf.format(new Date());

        // Exemplo de como você pode obter os IDs do cliente e do operador selecionados nos JComboBox
        int idCliente = obterIdCliente(comboClientes.getSelectedItem().toString());
        int idOperador = obterIdOperador(comboUsuarios.getSelectedItem().toString());

        // Lógica para armazenar no banco de dados os detalhes da venda
        try (Connection conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
             PreparedStatement ps = conexao.prepareStatement("INSERT INTO vendas (id_cliente, id_operador, valor_total, data_venda) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, idCliente);
            ps.setInt(2, idOperador);
            ps.setDouble(3, valorTotal);
            ps.setString(4, dataAtual);

            ps.executeUpdate();

            // Obter o ID gerado automaticamente
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                int idVenda = generatedKeys.getInt(1);

                // Agora, você pode usar o idVenda para associar os detalhes da venda na tabela itensvenda
                for (ItemVenda item : carrinho) {
                    String produto = item.getProduto();
                    int quantidade = item.getQuantidade();
                    double preco = item.getPreco();

                    // Restante da lógica para associar os detalhes da venda na tabela itensvenda
                    cadastrarItensVenda(idVenda, produto, quantidade, preco);

                    // Restante da lógica para diminuir do estoque os produtos vendidos
                    diminuirEstoque(produto, quantidade);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao armazenar detalhes da venda no banco de dados.");
        }

        // Limpar carrinho após finalizar venda
        carrinho.clear();
        txtCarrinho.setText(""); // Limpar área de texto do carrinho
        lblValorTotal.setText("Valor Total: R$ 0.0"); // Resetar valor total
    }

    private int obterIdCliente(String nomeCliente) {
        // Lógica para obter o ID do cliente
        try (Connection conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
             PreparedStatement ps = conexao.prepareStatement("SELECT id FROM clientes WHERE nome = ?")) {

            ps.setString(1, nomeCliente);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao obter ID do cliente do banco de dados.");
        }

        return 0; // Substitua isso pela lógica real
    }

    private int obterIdOperador(String nomeOperador) {
        // Lógica para obter o ID do operador
        try (Connection conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
             PreparedStatement ps = conexao.prepareStatement("SELECT id FROM usuarios WHERE nome = ?")) {

            ps.setString(1, nomeOperador);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao obter ID do operador do banco de dados.");
        }

        return 0; // Substitua isso pela lógica real
    }

    private void cadastrarItensVenda(int idVenda, String produto, int quantidade, double preco) {
        // Lógica para cadastrar detalhes da venda na tabela itensvenda
        try (Connection conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
             PreparedStatement ps = conexao.prepareStatement("INSERT INTO itensvenda (id_venda, id_produto, quantidade, valor_unitario) VALUES (?, ?, ?, ?)")) {

            ps.setInt(1, idVenda);

            // Obter o ID do produto
            int idProduto = obterIdProduto(produto);
            ps.setInt(2, idProduto);

            ps.setInt(3, quantidade);
            ps.setDouble(4, preco);

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar detalhes da venda na tabela itensvenda.");
        }
    }

    private int obterIdProduto(String nomeProduto) {
        // Lógica para obter o ID do produto
        try (Connection conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
             PreparedStatement ps = conexao.prepareStatement("SELECT id FROM estoque WHERE nome = ?")) {

            ps.setString(1, nomeProduto);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao obter ID do produto do banco de dados.");
        }

        return 0; // Substitua isso pela lógica real
    }

    private void diminuirEstoque(String produto, int quantidade) {
        // Lógica para diminuir o estoque no banco de dados
        try (Connection conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
             PreparedStatement ps = conexao.prepareStatement("UPDATE estoque SET quantidade = quantidade - ? WHERE nome = ?")) {

            ps.setInt(1, quantidade);
            ps.setString(2, produto);

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao diminuir estoque no banco de dados.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MenuVendaFrame().setVisible(true);
        });
    }
}

class ItemVenda {
    private String produto;
    private int quantidade;
    private double preco;

    public ItemVenda(String produto, int quantidade, double preco) {
        this.produto = produto;
        this.quantidade = quantidade;
        this.preco = preco;
    }

    public String getProduto() {
        return produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public double getPreco() {
        return preco;
    }
}
