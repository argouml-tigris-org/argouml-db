/**********************************************
*   Copyright (C) 2007 CINCOM SYSTEMS, INC.
*   All Rights Reserved
**********************************************/

package org.dbuml.base.controller.ui;

/**
 * Singleton dialog for updating the catalog.
 * @author jgrengbondai
 */
public class CatalogUpdateDlg extends javax.swing.JDialog {
    private boolean ok = false;
    
    private static CatalogUpdateDlg dlg;
    
    /** Creates new form CatalogUpdateDlg */
    private CatalogUpdateDlg(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }
    
    /**
     * Gets the instance of the dialog.
     * @return The update catolg instance.
     */
    public static CatalogUpdateDlg newInstance() {
        if (dlg == null) {
            dlg = new CatalogUpdateDlg(new javax.swing.JFrame(), true);
        }
        return dlg;
    }
    
    /**
     * Shows the dialog
     * @param title The title of the dialog
     * @return <code>tue</code> or <code>false</code> for wheteher 
     * or not the dialog was closed with an OK button.
     */
    public boolean showDialog(String title) {
        this.initScreen(title);
        this.setVisible(true);
        return ok;
    }
    
    /**
     * Boolen value for whether or not to pudate the catalog or replace it.
     * @return <code>true</code> or <code>false</code>.
     */
    public boolean isUpdate() {
        return this.updateCheckBox.isSelected();
    }
    

    private void initScreen(String title) {
        this.setTitle(title);
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        jPanel1 = new javax.swing.JPanel();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        updateCheckBox = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        jPanel1.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(1, 1, 20, 1)));
        jPanel1.setMinimumSize(new java.awt.Dimension(139, 56));
        jPanel1.setPreferredSize(new java.awt.Dimension(139, 56));
        okButton.setText(java.util.ResourceBundle.getBundle("org/dbuml/base/i18n/DBUML").getString("OK_BTN"));
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        jPanel1.add(okButton);

        cancelButton.setText(java.util.ResourceBundle.getBundle("org/dbuml/base/i18n/DBUML").getString("CANCEL_BTN"));
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        jPanel1.add(cancelButton);

        getContentPane().add(jPanel1, java.awt.BorderLayout.SOUTH);

        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel2.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(20, 20, 10, 20)));
        jPanel2.setMinimumSize(new java.awt.Dimension(243, 88));
        jPanel2.setPreferredSize(new java.awt.Dimension(243, 88));
        jPanel3.setLayout(new java.awt.GridLayout(2, 0));

        updateCheckBox.setText(java.util.ResourceBundle.getBundle("org/dbuml/base/i18n/DBUML").getString("UPDATE"));
        jPanel3.add(updateCheckBox);

        jPanel4.add(jPanel3);

        jPanel2.add(jPanel4, java.awt.BorderLayout.CENTER);

        jLabel1.setText("Default is to replace entities using drop\n and create commands.");
        jPanel2.add(jLabel1, java.awt.BorderLayout.SOUTH);

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        pack();
    }//GEN-END:initComponents

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        // Add your handling code here:
        ok = false;
        this.shutDown();
        evt.getID(); // to keep PMD quiet!
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        // Add your handling code here:
        ok = true;
        this.shutDown();
        evt.getID(); // to keep PMD quiet!
    }//GEN-LAST:event_okButtonActionPerformed
    
    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
//        setVisible(false);
//        dispose();
        ok = false;
        this.shutDown();
        evt.getID(); // to keep PMD quiet!
    }//GEN-LAST:event_closeDialog
    
    private void shutDown() {
        setVisible(false);
//        dispose();
    }
    
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JButton okButton;
    private javax.swing.JCheckBox updateCheckBox;
    // End of variables declaration//GEN-END:variables
    
}