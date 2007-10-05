/**********************************************
 *   Copyright (C) 2007 CINCOM SYSTEMS, INC.
 *   All Rights Reserved
 **********************************************/

package org.dbuml.argo.uml.ui;


import org.dbuml.base.controller.CreateActions;
import org.argouml.model.Model;
import javax.swing.Action;
import javax.swing.ImageIcon;

/**
 *
 * @author  jgunderson
 */
public class ActionCreateTable extends org.argouml.ui.CmdCreateNode {
    
    
    /** Creates a new instance of ActionCreateTable */
    public ActionCreateTable() {
        super(Model.getMetaTypes().getUMLClass(), "Class");
        
        //dbuml:removed
//        super(Model.getMetaTypes().getUMLClass(), "Table");
        putValue(Action.SHORT_DESCRIPTION, "New Table");
        // todo:  problems with icons.  Swing errors when loading diagrams.
        putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/org/dbuml/argo/Images/Table.gif")));
        
    }
    
    /**
     * 
     * @return 
     */
    public Object makeNode() {
        Object node = super.makeNode();
        return CreateActions.createTable(node);
    }
    
}