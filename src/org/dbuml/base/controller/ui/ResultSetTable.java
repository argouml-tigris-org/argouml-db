/**********************************************
 *   Copyright (C) 2007 CINCOM SYSTEMS, INC.
 *   All Rights Reserved
 **********************************************/
package org.dbuml.base.controller.ui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import org.dbuml.base.i18n.Translator;
import org.dbuml.base.controller.Util;
import org.dbuml.base.model.Database; //Added by RGupta
import org.dbuml.base.factory.Factory; //Added by RGupta
import org.dbuml.base.database.QueryTransaction; //Added by RGupta
import javax.swing.table.AbstractTableModel; //Added by rgupta

/**
 * Result set table class.
 */
public class ResultSetTable extends JDialog {
    
    private JButton selectButton;
    private JButton closeButton;
    private JButton clearButton;
    private JButton nextButton;
//    private JPanel  connectionPanel;
    private JFrame frame = new JFrame();
    private JTextArea queryTextArea;
    private JComponent queryAggregate;
    private JPanel  mainPanel;
    private JTable table;
    private JPanel textAreaPanel = new JPanel();
    private JPanel buttonSelectPanel = new JPanel();
    private JPanel buttonCancelPanel = new JPanel();
    private RSTableModel rsTable; // Added by RGupta
    private boolean nextFlag = false; // for Next Button problem.
    
    private Connection connection;
    
    
    /**
     * Creates new ResultSetTable instance.
     * @param conn The jdbc connection.
     * @param db The <code>Database</code> instance.
     * @param query The query statement.
     */
    public ResultSetTable(Connection conn, Database db, String query)   {
        
        connection = conn;
        rsTable = new RSTableModel(db); //Added by RGupta
        Counter.incCounter();
        mainPanel = new JPanel();
        queryTextArea = new JTextArea(query, 1, 49);
        selectButton = new JButton("Select");
        selectButton.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent e) {
                
                rsTable.fetchRecords();
                nextButton.setEnabled(nextFlag);
            }
        });
        
        clearButton = new JButton("Clear");
        clearButton.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent e) {
                
                queryTextArea.setText("");
            }
        });
        
        nextButton = new JButton("Next");
        nextButton.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent e) {
                rsTable.nextRecords();
                nextButton.setEnabled(nextFlag);
            }
        });
        
        // To disable it initially
        nextButton.setEnabled(false);
        
        closeButton = new JButton("Close");
        closeButton.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent e) {
                rsTable.close();
                frame.setVisible(false);
                frame.dispose();
                frame = null;
            }
        });
        
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        buttonSelectPanel.add(selectButton);
        buttonSelectPanel.add(clearButton);
        buttonCancelPanel.add(nextButton);
        buttonCancelPanel.add(closeButton);
        queryTextArea.setLineWrap(true);
        queryAggregate = new JScrollPane(queryTextArea);
        textAreaPanel.add(queryAggregate);
        textAreaPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        textAreaPanel.setBorder(
                BorderFactory.createEmptyBorder(15, 10, -4, 10));
        mainPanel.add(textAreaPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        mainPanel.setAlignmentX(selectButton.CENTER_ALIGNMENT);
        mainPanel.add(buttonSelectPanel);
        buttonSelectPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        buttonSelectPanel.setBorder(
                BorderFactory.createEmptyBorder(5, 10, 5, 10));
        table = new JTable(this.rsTable);    //Added by RGupta
        table.setColumnSelectionAllowed(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        JScrollPane scrollpane = new JScrollPane(table);
        mainPanel.add(scrollpane);
        mainPanel.add(buttonCancelPanel);
        JFrame.setDefaultLookAndFeelDecorated(true);
        frame.getContentPane().add(mainPanel);
        Toolkit tkit = Toolkit.getDefaultToolkit();
        Dimension dim = tkit.getScreenSize();
        int wd = (int) dim.getWidth();
        int ht = (int) dim.getHeight();
        //frame.setSize(wd-550, ht-180);
        frame.setSize(wd - 550, ht - 250);
        frame.setLocation(150, 60);
        java.net.URL imgURL = ResultSetTable.class.getResource(
                "Images/Explorer.gif");
        
        if (imgURL != null) {
            frame.setIconImage(new javax.swing.ImageIcon(imgURL).getImage());
        }
        frame.setTitle("Browse Instances");
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                rsTable.close();
                //Connection is closing by Window close.
            }
        });
        
        frame.setVisible(true);
        frame.setResizable(false);
    } 
    
    // RSTableModel class replaces DBConnection
    // The new class is an inner class of ResultSetTable class
    // It extends AbstractTableModel similar to DBConnection
    // Added by RGupta
    private class RSTableModel extends AbstractTableModel {
        private String[] columnNames; // Holds the column names.
        private Database dbase;
        private Vector data; // Holds the table data
        private Factory factory;
        private QueryTransaction qTrans;
        
        
        public RSTableModel() {
        }
        
        public RSTableModel( Database db) {
            dbase = db;
        }
        
        public void fetchRecords() {
            String sFactName = dbase.getProperty(Database.FACTORY);
            factory = Factory.getFactory(sFactName);
            // To close the query transaction if it's open.
            if (qTrans != null) { this.close(); }
            qTrans = factory.getQueryTransaction();
            
            if (queryTextArea.getText().equals("")) {
                Util.showMessageDialog(null,
                        Translator.getInstance().localize(
                            "UNABLE_TO_GET_DATABASE_STRING"),
                        Translator.getInstance().localize("NO_SQL_STRING"),
                        JOptionPane.ERROR_MESSAGE);
            }
            
            if (!queryTextArea.getText().equals("")) {
                try {
                    
                    if (factory == null) {
                        Util.showMessageDialog(null,
                                Translator.getInstance().localize(
                                    "UNABLE_TO_LOAD_FACTORY", "Factory" ),
                                Translator.getInstance().localize(
                                    "IMPORT_FAILURE_TITLE"),
                                JOptionPane.ERROR_MESSAGE);
                    } else {
                        data = qTrans.getRows(connection,
                                queryTextArea.getText());
                        columnNames = qTrans.getColumnNames();
                        fireTableChanged(null);
                    }
                } catch (java.sql.SQLException sqle) {
                    Util.showMessageDialog(null,
                            sqle.getMessage(),
                            Translator.getInstance().localize(
                                "BROWSE_INSTANCES"),
                            JOptionPane.ERROR_MESSAGE);
                }
                nextFlag = qTrans.hasRecords();
            }
            
        }
                
        public void nextRecords() {
            if ((qTrans != null) && qTrans.hasRecords()) {
                try {
                    data = qTrans.getNextRows();
                    fireTableChanged(null);
                } catch (java.sql.SQLException sqle) {
                    Util.showMessageDialog(null,
                            sqle.getMessage(),
                            Translator.getInstance().localize(
                                "BROWSE_INSTANCES"),
                            JOptionPane.ERROR_MESSAGE);
                }
                nextFlag = qTrans.hasRecords();
            }
        }
        
        public void close() {
            if (qTrans != null) {
                qTrans.close();
                qTrans = null;
            }
        }
        
        // Returns the columns headings in a table
        public String getColumnName(int i) {
            return columnNames[i];
        }
        
        //Returns the number of columns in a table
        public int getColumnCount() {
            return (columnNames == null ? 0 : columnNames.length);
        }
        
        // Return the number of rows in the table.
        public int getRowCount() {
            return (data == null ? 0 : data.size());
        }
        
        public Object getValueAt(int rowIndex, int columnIndex) {
            return ((String[]) data.elementAt(rowIndex))[columnIndex];
        }
        
        
    }
    
}






















