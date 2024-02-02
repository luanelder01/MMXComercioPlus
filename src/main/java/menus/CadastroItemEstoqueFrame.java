package menus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import mmx.ConexaoDB;

public class CadastroItemEstoqueFrame extends JFrame {

    private JLabel lblCodigo;
    private JLabel lblNome;
    private JLabel lblPreco;
    private JLabel lblFornecedor;
    private JLabel lblQuantidade;

    private JTextField txtCodigo;
    private JTextField txtNome;
    private JTextField txtPreco;
    private JComboBox<String> cbFornecedores;
    private JTextField txtQuantidade;

    private JButton btnCadastrar;

    private List<MenuEstoqueFrame.Fornecedor> listaFornecedores;

    private MenuEstoqueFrame.ItemEstoque itemCadastrado;

    public CadastroItemEstoqueFrame(List<MenuEstoqueFrame.Fornecedor> listaFornecedores) {
        this.listaFornecedores = listaFornecedores;
        initComponents();
    }

    private void initComponents() {
        lblCodigo = new JLabel("Código:");
        lblNome = new JLabel("Nome:");
        lblPreco = new JLabel("Preço:");
        lblFornecedor = new JLabel("Fornecedor:");
        lblQuantidade = new JLabel("Quantidade:");

        txtCodigo = new JTextField();
        txtNome = new JTextField();
        txtPreco = new JTextField();
        cbFornecedores = new JComboBox<>();
        txtQuantidade = new JTextField();

        btnCadastrar = new JButton("Cadastrar");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Cadastro de Produto");

        JPanel panel = new JPanel();

        lblCodigo.setBounds(10, 10, 100, 25);
        lblNome.setBounds(10, 50, 100, 25);
        lblPreco.setBounds(10, 90, 100, 25);
        lblFornecedor.setBounds(10, 130, 100, 25);
        lblQuantidade.setBounds(10, 170, 100, 25);

        txtCodigo.setBounds(120, 10, 150, 25);
        txtNome.setBounds(120, 50, 150, 25);
        txtPreco.setBounds(120, 90, 150, 25);
        cbFornecedores.setBounds(120, 130, 150, 25);
        txtQuantidade.setBounds(120, 170, 150, 25);

        btnCadastrar.setBounds(10, 210, 150, 25);

        btnCadastrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                btnCadastrarActionPerformed(evt);
            }
        });

        panel.setLayout(null);
        panel.add(lblCodigo);
        panel.add(lblNome);
        panel.add(lblPreco);
        panel.add(lblFornecedor);
        panel.add(lblQuantidade);
        panel.add(txtCodigo);
        panel.add(txtNome);
        panel.add(txtPreco);
        panel.add(cbFornecedores);
        panel.add(txtQuantidade);
        panel.add(btnCadastrar);

        // Preencher a combobox de fornecedores
        for (MenuEstoqueFrame.Fornecedor fornecedor : listaFornecedores) {
            cbFornecedores.addItem(fornecedor.getNome());
        }

        getContentPane().add(panel);

        pack();
        setLocationRelativeTo(null);
    }

    private void btnCadastrarActionPerformed(ActionEvent evt) {
        // Adicione aqui a lógica para validar os campos e cadastrar o produto no banco de dados
        String codigo = txtCodigo.getText();
        String nome = txtNome.getText();
        double preco = Double.parseDouble(txtPreco.getText());
        int idFornecedor = listaFornecedores.get(cbFornecedores.getSelectedIndex()).getId();
        int quantidade = Integer.parseInt(txtQuantidade.getText());

        // Simulação de cadastro no banco de dados
        try (Connection conexao = ConexaoDB.obterConexao();
             PreparedStatement ps = conexao.prepareStatement("INSERT INTO estoque (codigo, nome, preco, id_fornecedor, quantidade) VALUES (?, ?, ?, ?, ?)")) {

            ps.setString(1, codigo);
            ps.setString(2, nome);
            ps.setDouble(3, preco);
            ps.setInt(4, idFornecedor);
            ps.setInt(5, quantidade);

            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Produto cadastrado com sucesso!");

            // Configura o item cadastrado
            itemCadastrado = new MenuEstoqueFrame.ItemEstoque(0, codigo, nome, preco, idFornecedor, quantidade);

            // Fecha a janela de cadastro
            dispose();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao cadastrar produto.");
        }
    }

    public MenuEstoqueFrame.ItemEstoque obterItemCadastrado() {
        return itemCadastrado;
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CadastroItemEstoqueFrame(null).setVisible(true);
            }
        });
    }
}
