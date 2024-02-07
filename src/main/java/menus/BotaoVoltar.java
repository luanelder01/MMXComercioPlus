package menus;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BotaoVoltar extends JButton {
    private JFrame telaAtual;

    public BotaoVoltar(JFrame telaAtual) {
        super("Voltar");
        this.telaAtual = telaAtual;
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                voltarParaMenuPrincipal();
            }
        });
    }

    private void voltarParaMenuPrincipal() {
        MenuPrincipalFrame menuPrincipal = new MenuPrincipalFrame("NomeUsuario", "SenhaUsuario");
        menuPrincipal.setVisible(true);
        telaAtual.dispose();
    }
}

