/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package UI;

import Products.IProduct;
import Products.ProductSystem;
import Users.UserSystem;
import Utils.UiUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author bersl
 */
public class InventoryScreen extends javax.swing.JFrame {

    private Map<Integer, Integer> tableIndexToProductIdMap = new HashMap<>();

    public InventoryScreen() {
        initComponents();
    }

    private void filltheTable() {
        itemTable.removeAll();
        ArrayList<IProduct> allProducts = ProductSystem.getAllProducts();

        DefaultTableModel dtm = (DefaultTableModel) itemTable.getModel();

        for (int i = 0; i < allProducts.size(); i++) {
            IProduct it = allProducts.get(i);
            tableIndexToProductIdMap.put(i, it.getProductId());
            dtm.addRow(UiUtils.convertProductToRowItem(it));
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        storeButton = new javax.swing.JButton();
        searchInput = new javax.swing.JTextField();
        searchButton = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        usernameOutput = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        itemTable = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Inventory");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        storeButton.setText("STORE");
        storeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                storeButtonActionPerformed(evt);
            }
        });

        searchInput.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));

        searchButton.setText("SEARCH 🔎");

        jLabel3.setText("WELCOME");

        usernameOutput.setFont(new java.awt.Font("Trebuchet MS", 1, 12)); // NOI18N
        usernameOutput.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        itemTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"1", "1", "1", "1", "1", null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Product Name", "Genre", "Language", "Publisher", "Release Date", "Price"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        itemTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                itemTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(itemTable);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(storeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 303, Short.MAX_VALUE)
                        .addComponent(searchInput, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(25, 25, 25)
                        .addComponent(searchButton, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(25, 25, 25)
                        .addComponent(jLabel3)
                        .addGap(10, 10, 10)
                        .addComponent(usernameOutput, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(25, 25, 25))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(searchInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchButton)
                    .addComponent(jLabel3)
                    .addComponent(usernameOutput, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(storeButton))
                .addGap(25, 25, 25)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(25, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void storeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_storeButtonActionPerformed
        WindowManager.Store.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_storeButtonActionPerformed

    private void itemTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_itemTableMouseClicked
        if (evt.getClickCount() == 2) {
            int selectedIndex = itemTable.getSelectedRow();

            if (selectedIndex == -1) {
                JOptionPane.showMessageDialog(null, "Nothing is selected.");
                return;
            }

            Integer selectedId = tableIndexToProductIdMap.get(selectedIndex);

            if (selectedId == null) {
                JOptionPane.showMessageDialog(null, "Cannot find the product associated with the selected row.");
                return;
            }

            IProduct selectedProduct = ProductSystem.searchProduct(selectedId);

            if (selectedProduct == null) {
                JOptionPane.showMessageDialog(null, "Product is null.");
                return;
            }

            WindowManager.ProductInfo.setVisible(true);
        }
    }//GEN-LAST:event_itemTableMouseClicked

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        usernameOutput.setText(UserSystem.getLoggedInUser().getName().toUpperCase() + " " + UserSystem.getLoggedInUser().getSurname().toUpperCase());
        filltheTable();
    }//GEN-LAST:event_formWindowActivated

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable itemTable;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton searchButton;
    private javax.swing.JTextField searchInput;
    private javax.swing.JButton storeButton;
    private javax.swing.JLabel usernameOutput;
    // End of variables declaration//GEN-END:variables
}
