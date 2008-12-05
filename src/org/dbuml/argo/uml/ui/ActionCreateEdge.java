/**********************************************
 *   Copyright (C) 2007 CINCOM SYSTEMS, INC.
 *   All Rights Reserved
 **********************************************/

package org.dbuml.argo.uml.ui;


import javax.swing.Action;
import org.argouml.uml.diagram.ui.ActionSetMode;

/**
 *
 * @author  jgunderson
 */
public class ActionCreateEdge extends org.argouml.uml.diagram.ui.RadioAction {
    /**
     * Creates a new instance of ActionCreateFKEY.
     * @param cmd 
     * @param hint 
     */
    public ActionCreateEdge(ActionSetMode cmd, String hint) {
        super(cmd);
        putValue(Action.SHORT_DESCRIPTION, hint);
        //putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/org/dbuml/argo/uml/ui/Table1.gif")));
    }
    
    //
    //  The following is called on selection of the action button, not after the dependency
    //  is added to the diagram.  Same is tru of actionPerformed( ) of the CmdSetMode.
    //
    
//    /**
//     * 
//     * @param event 
//     */
//    public void actionPerformed(java.awt.event.ActionEvent event) {
//        super.actionPerformed(event);
//    }
}
