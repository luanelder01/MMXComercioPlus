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
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import mmx.ConexaoDB;

public class MenuLoginFrame extends JFrame {

    private JButton btnLogin;
    private JTextField txtNome;
    private JPasswordField txtSenha;
    private JLabel lblNome;
    private JLabel lblSenha;

    public MenuLoginFrame() {
        initComponents();
    }

    private void initComponents() {
        btnLogin = new JButton();
        txtNome = new JTextField();
        txtSenha = new JPasswordField();
        lblNome = new JLabel();
        lblSenha = new JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        btnLogin.setText("Login");
        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnLoginActionPerformed(evt);
            }
        });

        lblNome.setText("Nome:");

        lblSenha.setText("Senha:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(53, 53, 53)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(lblSenha)
                                                        .addComponent(lblNome))
                                                .addGap(18, 18, 18)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(txtNome)
                                                        .addComponent(txtSenha, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(127, 127, 127)
                                                .addComponent(btnLogin)))
                                .addContainerGap(52, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(42, 42, 42)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lblNome)
                                        .addComponent(txtNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lblSenha)
                                        .addComponent(txtSenha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(btnLogin)
                                .addContainerGap(43, Short.MAX_VALUE))
        );

        pack();
    }

    private void btnLoginActionPerformed(ActionEvent evt) {
        String nome = txtNome.getText();
        String senha = new String(txtSenha.getPassword());

        if (validarLogin(nome, senha)) {
            JOptionPane.showMessageDialog(this, "Login bem-sucedido!");
            MenuPrincipalFrame menuPrincipal = new MenuPrincipalFrame(nome, senha);
            menuPrincipal.setVisible(true);
            this.dispose(); // Fecha a janela de login após abrir o MenuPrincipalFrame
        } else {
            JOptionPane.showMessageDialog(this, "Nome de usuário ou senha incorretos.");
        }
    }

    private boolean validarLogin(String nome, String senha) {
        try (Connection conexao = ConexaoDB.obterConexao();
             PreparedStatement ps = conexao.prepareStatement("SELECT * FROM usuarios WHERE nome = ? AND senha = ?")) {

            ps.setString(1, nome);
            ps.setString(2, senha);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); // Se houver resultados, significa que o login é válido
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao validar login.");
            return false;
        }
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MenuLoginFrame().setVisible(true);
            }
        });
    }
}
