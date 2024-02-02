package menus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import mmx.ConexaoDB;

public class MenuUsuarioFrame extends JFrame {

    private JButton btnCadastrarUsuario;
    private JButton btnSelecionarUsuario;
    private JComboBox<String> cbUsuarios;

    public MenuUsuarioFrame() {
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Menu Usuário");

        JPanel panel = new JPanel();

        btnCadastrarUsuario = new JButton("Cadastrar Usuário");
        btnCadastrarUsuario.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                btnCadastrarUsuarioActionPerformed(evt);
            }
        });

        btnSelecionarUsuario = new JButton("Selecionar Usuário");
        btnSelecionarUsuario.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                btnSelecionarUsuarioActionPerformed(evt);
            }
        });

        cbUsuarios = new JComboBox<>();
        carregarUsuarios();

        panel.add(btnCadastrarUsuario);
        panel.add(btnSelecionarUsuario);
        panel.add(cbUsuarios);

        getContentPane().add(panel);

        pack();
        setLocationRelativeTo(null);
    }

    private void btnCadastrarUsuarioActionPerformed(ActionEvent evt) {
        new FormularioCadastroUsuario().setVisible(true);
    }

    private void btnSelecionarUsuarioActionPerformed(ActionEvent evt) {
        String usuarioSelecionado = (String) cbUsuarios.getSelectedItem();
        if (usuarioSelecionado != null) {
            new DetalhesUsuarioFrame(usuarioSelecionado).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um usuário antes de prosseguir.");
        }
    }

    private void carregarUsuarios() {
        cbUsuarios.removeAllItems();

        try (Connection conexao = ConexaoDB.obterConexao();
             PreparedStatement ps = conexao.prepareStatement("SELECT nome FROM usuarios");
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                cbUsuarios.addItem(rs.getString("nome"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao carregar usuários.");
        }
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MenuUsuarioFrame().setVisible(true);
            }
        });
    }
}
