/**********************************************
 *   Copyright (C) 2007 CINCOM SYSTEMS, INC.
 *   All Rights Reserved
 **********************************************/

package org.dbuml.base.controller;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
/**
 * A frame for SQL source generation.
 * @author  sbalda
 */
public class FileOutput extends JFrame {
    // ---------- FIELDS ---------
    /**
     * A text area.
     */
    private JTextArea textArea;
    /**
     * The file name.
     */
    private File fileName;
    /**
     * A file writer.
     */
    private PrintWriter fileOutput;
    // ---------- CONSTRUCTORS ----------
    /**
     * Creates a new FileOutput.
     * @param s The frame title.
     * @param file The File object. 
     */
    public FileOutput(String s, File file) {
        super(s);
        
        this.fileName = file;
        // Content pane
        Container container = getContentPane();
        container.setBackground(Color.pink);
        container.setLayout(new BorderLayout(5, 5)); // 5 pixel gaps
        
        // Text area
        textArea = new JTextArea(40, 15);
        textArea.setEditable(false);
        container.add(new JScrollPane(textArea), BorderLayout.CENTER);
        
        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2));
        
        container.add(buttonPanel, BorderLayout.NORTH);
    }
    
    /** Outputs a specified text.
     *  @param text The text string.
     */
    public void addText(String text) {
        this.textArea.append(text);   
    }
    /**
     * Displays the query strings.
     * @param query The query string.
     * @return <code>true</code> when there is no error, and <code>false</code>
     * when there is an error.
     */
    public boolean saveData(String query) {
        boolean status = false;
        try {
            fileOutput = new PrintWriter(new FileWriter(fileName), true);
            //printing output
            fileOutput.print(query);
            fileOutput.flush();
            fileOutput.close();
            status = true;
        } catch (IOException ioException) {
            JOptionPane.showMessageDialog(this, "Error saving File",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
        return status;
    } //end of saveData()
    
    
}