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
    private JComboBox<String> comboClientes, comboUsuarios, comboProdutos;
    private JButton btnIniciarVenda, btnAdicionarAoCarrinho, btnFinalizar;
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

        JPanel panelClientes = new JPanel(new FlowLayout());
        panelClientes.add(new JLabel("Cliente:"));
        comboClientes = new JComboBox<>();
        panelClientes.add(comboClientes);

        JPanel panelUsuarios = new JPanel(new FlowLayout());
        panelUsuarios.add(new JLabel("Usuário:"));
        comboUsuarios = new JComboBox<>();
        panelUsuarios.add(comboUsuarios);

        btnIniciarVenda = new JButton("Iniciar Venda");

        JPanel panelSuperior = new JPanel();
        panelSuperior.add(panelClientes);
        panelSuperior.add(panelUsuarios);
        panelSuperior.add(btnIniciarVenda);

        add(panelSuperior, BorderLayout.NORTH);

        carrinho = new ArrayList<>();

        btnIniciarVenda.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                iniciarVenda();
            }
        });

        carregarClientes();
        carregarUsuarios();
    }

    private void iniciarVenda() {
        JFrame frameVenda = new JFrame("Venda");
        frameVenda.setSize(600, 400);
        frameVenda.setLayout(new BorderLayout());

        JPanel panelProdutos = new JPanel(new FlowLayout());
        panelProdutos.add(new JLabel("Produto:"));
        comboProdutos = new JComboBox<>();
        panelProdutos.add(comboProdutos);

        JPanel panelQuantidade = new JPanel(new FlowLayout());
        panelQuantidade.add(new JLabel("Quantidade:"));
        txtQuantidade = new JTextField(5);
        panelQuantidade.add(txtQuantidade);

        btnAdicionarAoCarrinho = new JButton("Adicionar ao Carrinho");
        btnFinalizar = new JButton("Finalizar Venda");

        JPanel panelBotoes = new JPanel(new FlowLayout());
        panelBotoes.add(btnAdicionarAoCarrinho);
        panelBotoes.add(btnFinalizar);

        txtCarrinho = new JTextArea();
        lblValorTotal = new JLabel("Valor Total: R$ 0.0");

        JPanel panelCarrinho = new JPanel(new BorderLayout());
        panelCarrinho.add(lblValorTotal, BorderLayout.NORTH);
        panelCarrinho.add(new JScrollPane(txtCarrinho), BorderLayout.CENTER);

        frameVenda.add(panelProdutos, BorderLayout.WEST);
        frameVenda.add(panelQuantidade, BorderLayout.CENTER);
        frameVenda.add(panelBotoes, BorderLayout.SOUTH);
        frameVenda.add(panelCarrinho, BorderLayout.EAST);

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

        frameVenda.setVisible(true);
    }

    private void adicionarAoCarrinho() {
        String produtoSelecionado = comboProdutos.getSelectedItem().toString();
        int quantidade = Integer.parseInt(txtQuantidade.getText());
        double preco = obterPrecoProduto(produtoSelecionado);

        carrinho.add(new ItemVenda(produtoSelecionado, quantidade, preco));
        exibirCarrinho();
        calcularValorTotal();

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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dataAtual = sdf.format(new Date());

        int idCliente = obterIdCliente(comboClientes.getSelectedItem().toString());
        int idOperador = obterIdOperador(comboUsuarios.getSelectedItem().toString());

        try (Connection conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
             PreparedStatement ps = conexao.prepareStatement("INSERT INTO vendas (id_cliente, id_operador, valor_total, data_venda) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, idCliente);
            ps.setInt(2, idOperador);
            ps.setDouble(3, valorTotal);
            ps.setString(4, dataAtual);

            ps.executeUpdate();

            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                int idVenda = generatedKeys.getInt(1);

                for (ItemVenda item : carrinho) {
                    String produto = item.getProduto();
                    int quantidade = item.getQuantidade();
                    double preco = item.getPreco();

                    cadastrarItensVenda(idVenda, produto, quantidade, preco);
                    diminuirEstoque(produto, quantidade);
                }
            }

            JOptionPane.showMessageDialog(null, "Venda Realizada");

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao armazenar detalhes da venda no banco de dados.");
        }

        carrinho.clear();
        txtCarrinho.setText("");
        lblValorTotal.setText("Valor Total: R$ 0.0");
    }

    private int obterIdCliente(String nomeCliente) {
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

        return 0;
    }

    private int obterIdOperador(String nomeOperador) {
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

        return 0;
    }

    private void cadastrarItensVenda(int idVenda, String produto, int quantidade, double preco) {
        try (Connection conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
             PreparedStatement ps = conexao.prepareStatement("INSERT INTO itensvenda (id_venda, id_produto, quantidade, valor_unitario) VALUES (?, ?, ?, ?)")) {

            ps.setInt(1, idVenda);

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

        return 0;
    }

    private void diminuirEstoque(String produto, int quantidade) {
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

    private void carregarClientes() {
        comboClientes.removeAllItems();
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
        comboUsuarios.removeAllItems();
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
