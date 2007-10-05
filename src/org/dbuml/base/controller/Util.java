/**********************************************
*   Copyright (C) 2007 CINCOM SYSTEMS, INC.
*   All Rights Reserved
**********************************************/

package org.dbuml.base.controller;

import javax.swing.JOptionPane;
import java.awt.Component;
import org.apache.log4j.Logger;

/**
 * Utilities class.
 * @author  jgrengbondai
 */
public class Util {
    
    /**
     * A boolean flag that tells the tool whether or not we are running
     * in the batch mode.
     */
    private static boolean batchMode = false;
    
    private static final Logger LOG = Logger.getLogger(
            org.dbuml.base.controller.Util.class);
    
    /**
     * Creates a new instance of Util.
     */
    public Util() {
    }
    
    /**
     * Shows the error message into a error dialog when the tool is running
     * in the GUI mode, it outputs the messages into a log when the tool is
     * running in a batch mode.
     * @param parent The parent frame.
     * @param message The message to show or log.
     * @param title The title of the message.
     * @param messageType The message type.
     */
    public static void showMessageDialog(Component parent, 
                                   Object message,
                                   String title,
                                   int messageType) {
        if (!batchMode) {
            if (message instanceof String 
                    && ((String) message).length() > 250) {
                JOptionPane.showMessageDialog(parent,
                        ((String) message).substring(0, 250) + "...",
                        title, messageType);     
            } else {
                JOptionPane.showMessageDialog(parent, message, title,
                        messageType);
            }
        } else {
            LOG.error("Error(" + title + "):" + message);
        }
    }
    
    /** Sets the batch mode flag.
     * @param flag The batch mode flag.
     */
    public static synchronized  void setBatchMode(boolean flag) {
        batchMode = flag;
    }
    
    public static boolean isBatchMode() {
        return batchMode;
    }
    
}
