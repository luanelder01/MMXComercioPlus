package menus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class MenuVendaFrame extends JFrame {

    public MenuVendaFrame() {
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Menu de Venda");

        JButton adicionarProdutoButton = new JButton("Adicionar Produto ao Carrinho");
        adicionarProdutoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                adicionarProdutoButtonActionPerformed(evt);
            }
        });

        JButton removerProdutoButton = new JButton("Remover Produto do Carrinho");
        removerProdutoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                removerProdutoButtonActionPerformed(evt);
            }
        });

        JButton finalizarVendaButton = new JButton("Finalizar Venda");
        finalizarVendaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                finalizarVendaButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(adicionarProdutoButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(removerProdutoButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(finalizarVendaButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(50, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(adicionarProdutoButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(removerProdutoButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(finalizarVendaButton)
                .addContainerGap(30, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }

    private void adicionarProdutoButtonActionPerformed(ActionEvent evt) {
        // Substitua isso pelo código para adicionar um produto ao carrinho
        JOptionPane.showMessageDialog(this, "Adicionando produto ao carrinho...");
    }

    private void removerProdutoButtonActionPerformed(ActionEvent evt) {
        // Substitua isso pelo código para remover um produto do carrinho
        JOptionPane.showMessageDialog(this, "Removendo produto do carrinho...");
    }

    private void finalizarVendaButtonActionPerformed(ActionEvent evt) {
        // Substitua isso pelo código para finalizar a venda
        JOptionPane.showMessageDialog(this, "Finalizando venda...");
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MenuVendaFrame().setVisible(true);
            }
        });
    }
}
