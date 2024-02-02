package menus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class EdicaoItemEstoqueFrame extends JFrame {

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

    private JButton btnSalvarAlteracoes;
    private JButton btnCancelar;

    private List<ItemEstoque> listaItens;
    private FornecedorComboBoxModel fornecedorComboBoxModel;

    private ItemEstoque itemEditar;

    public EdicaoItemEstoqueFrame(List<ItemEstoque> listaItens, List<Fornecedor> listaFornecedores) {
        this.listaItens = listaItens;
        initComponents(listaFornecedores);
    }

    private void initComponents(List<Fornecedor> listaFornecedores) {
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

        btnSalvarAlteracoes = new JButton("Salvar Alterações");
        btnCancelar = new JButton("Cancelar");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Edição de Produto");

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(5, 5, 5, 5);

        panel.add(lblCodigo, constraints);
        constraints.gridy++;
        panel.add(lblNome, constraints);
        constraints.gridy++;
        panel.add(lblPreco, constraints);
        constraints.gridy++;
        panel.add(lblFornecedor, constraints);
        constraints.gridy++;
        panel.add(lblQuantidade, constraints);

        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        panel.add(txtCodigo, constraints);
        constraints.gridy++;
        panel.add(txtNome, constraints);
        constraints.gridy++;
        panel.add(txtPreco, constraints);
        constraints.gridy++;
        panel.add(cbFornecedores, constraints);
        constraints.gridy++;
        panel.add(txtQuantidade, constraints);

        constraints.gridx = 0;
        constraints.gridy++;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.NONE;
        panel.add(btnSalvarAlteracoes, constraints);
        constraints.gridy++;
        panel.add(btnCancelar, constraints);

        btnSalvarAlteracoes.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnSalvarAlteracoesActionPerformed(evt);
            }
        });

        btnCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        // Preencher a combobox de fornecedores
        fornecedorComboBoxModel = new FornecedorComboBoxModel(listaFornecedores);
        cbFornecedores.setModel(fornecedorComboBoxModel);

        // Preencher os campos com os dados do item a ser editado
        if (!listaItens.isEmpty()) {
            itemEditar = listaItens.get(0); // Pega o primeiro item por padrão
            preencherCampos();
        }

        getContentPane().add(panel);

        pack();
        setLocationRelativeTo(null);
    }

    private void preencherCampos() {
        txtCodigo.setText(itemEditar.getCodigo());
        txtNome.setText(itemEditar.getNome());
        txtPreco.setText(String.valueOf(itemEditar.getPreco()));
        cbFornecedores.setSelectedItem(fornecedorComboBoxModel.obterNomeFornecedorPorId(itemEditar.getIdFornecedor()));
        txtQuantidade.setText(String.valueOf(itemEditar.getQuantidade()));
    }

    private void btnSalvarAlteracoesActionPerformed(ActionEvent evt) {
        // Adicione aqui a lógica para validar os campos e salvar as alterações no banco de dados
        String codigo = txtCodigo.getText();
        String nome = txtNome.getText();
        double preco = Double.parseDouble(txtPreco.getText());
        int idFornecedor = fornecedorComboBoxModel.obterIdFornecedorPorNome((String) cbFornecedores.getSelectedItem());
        int quantidade = Integer.parseInt(txtQuantidade.getText());

        itemEditar = new ItemEstoque(itemEditar.getId(), codigo, nome, preco, idFornecedor, quantidade);
        dispose();
    }

    private void btnCancelarActionPerformed(ActionEvent evt) {
        dispose();
    }

    private class FornecedorComboBoxModel extends DefaultComboBoxModel<String> {
        private List<Fornecedor> fornecedores;

        public FornecedorComboBoxModel(List<Fornecedor> fornecedores) {
            this.fornecedores = fornecedores;
            atualizarComboBox();
        }

        public void atualizarComboBox() {
            removeAllElements();
            for (Fornecedor fornecedor : fornecedores) {
                addElement(fornecedor.getNome());
            }
        }

        public String obterNomeFornecedorPorId(int idFornecedor) {
            for (Fornecedor fornecedor : fornecedores) {
                if (fornecedor.getId() == idFornecedor) {
                    return fornecedor.getNome();
                }
            }
            return "";
        }

        public int obterIdFornecedorPorNome(String nomeFornecedor) {
            for (Fornecedor fornecedor : fornecedores) {
                if (fornecedor.getNome().equals(nomeFornecedor)) {
                    return fornecedor.getId();
                }
            }
            return -1;
        }
    }

    public ItemEstoque obterItemEditado() {
        return itemEditar;
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new EdicaoItemEstoqueFrame(null, null).setVisible(true);
            }
        });
    }
}
