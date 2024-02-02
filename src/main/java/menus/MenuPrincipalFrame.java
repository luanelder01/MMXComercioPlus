package menus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JOptionPane;

import mmx.ConexaoDB;

public class MenuPrincipalFrame extends JFrame {

    private String nomeUsuario;
    private String senhaUsuario;

    public MenuPrincipalFrame(String nomeUsuario, String senhaUsuario) {
        this.nomeUsuario = nomeUsuario;
        this.senhaUsuario = senhaUsuario;
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Menu Principal");

        JPanel panel = new JPanel();

        JButton btnMenuUsuario = new JButton("Menu Usuário");
        btnMenuUsuario.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                btnMenuUsuarioActionPerformed(evt);
            }
        });

        JButton btnMenuCliente = new JButton("Menu Cliente");
        btnMenuCliente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                btnMenuClienteActionPerformed(evt);
            }
        });

        JButton btnMenuFornecedor = new JButton("Menu Fornecedor");
        btnMenuFornecedor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                btnMenuFornecedorActionPerformed(evt);
            }
        });

        JButton btnMenuEstoque = new JButton("Menu Estoque");
        btnMenuEstoque.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                btnMenuEstoqueActionPerformed(evt);
            }
        });

        JButton btnMenuVenda = new JButton("Menu Venda");
        btnMenuVenda.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                btnMenuVendaActionPerformed(evt);
            }
        });

        panel.add(btnMenuUsuario);
        panel.add(btnMenuCliente);
        panel.add(btnMenuFornecedor);
        panel.add(btnMenuEstoque);
        panel.add(btnMenuVenda);

        getContentPane().add(panel);

        pack();
        setLocationRelativeTo(null);
    }

    private void btnMenuUsuarioActionPerformed(ActionEvent evt) {
        if (usuarioLogadoEhAdministrador()) {
            new MenuUsuarioFrame().setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Somente administradores podem acessar o Menu Usuário.");
        }
    }

    private void btnMenuClienteActionPerformed(ActionEvent evt) {
        new MenuClienteFrame().setVisible(true);
        dispose();
    }

    private void btnMenuFornecedorActionPerformed(ActionEvent evt) {
        new MenuFornecedorFrame().setVisible(true);
        dispose();
    }

    private void btnMenuEstoqueActionPerformed(ActionEvent evt) {
        new MenuEstoqueFrame().setVisible(true);
        dispose();
    }

    private void btnMenuVendaActionPerformed(ActionEvent evt) {
        new MenuVendaFrame().setVisible(true);
        dispose();
    }

    private boolean usuarioLogadoEhAdministrador() {
        try (Connection conexao = ConexaoDB.obterConexao();
             PreparedStatement ps = conexao.prepareStatement("SELECT profissao FROM usuarios WHERE nome = ? AND senha = ?")) {

            ps.setString(1, nomeUsuario);
            ps.setString(2, senhaUsuario);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String profissao = rs.getString("profissao");
                    return "administrador".equalsIgnoreCase(profissao);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao verificar se o usuário é administrador.");
        }

        return false;
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MenuPrincipalFrame("NomeUsuario", "SenhaUsuario").setVisible(true);
            }
        });
    }
}
