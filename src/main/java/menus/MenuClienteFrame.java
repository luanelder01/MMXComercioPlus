package menus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import mmx.ConexaoDB;

public class MenuClienteFrame extends JFrame {

    private JButton btnCadastrarCliente;
    private JButton btnClienteCadastrado;

    public MenuClienteFrame() {
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Menu Cliente");

        JPanel panel = new JPanel();

        btnCadastrarCliente = new JButton("Cadastrar Cliente");
        btnCadastrarCliente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                btnCadastrarClienteActionPerformed(evt);
            }
        });

        btnClienteCadastrado = new JButton("Clientes Cadastrados");
        btnClienteCadastrado.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                btnClienteCadastradoActionPerformed(evt);
            }
        });

        panel.add(btnCadastrarCliente);
        panel.add(btnClienteCadastrado);

        getContentPane().add(panel);

        pack();
        setLocationRelativeTo(null);
    }

    private void btnCadastrarClienteActionPerformed(ActionEvent evt) {
        new FormularioCadastroCliente().setVisible(true);
    }

    private void btnClienteCadastradoActionPerformed(ActionEvent evt) {
        new SelecaoClienteFrame().setVisible(true);
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MenuClienteFrame().setVisible(true);
            }
        });
    }
}
