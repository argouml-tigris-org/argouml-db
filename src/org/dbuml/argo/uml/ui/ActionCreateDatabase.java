/**********************************************
 *   Copyright (C) 2007 CINCOM SYSTEMS, INC.
 *   All Rights Reserved
 **********************************************/

package org.dbuml.argo.uml.ui;

import org.dbuml.base.controller.CreateActions;
import org.argouml.ui.CmdCreateNode;
import org.argouml.model.Model;
import javax.swing.Action;
import javax.swing.ImageIcon;

/**
 *
 * @author  jgunderson
 */

public class ActionCreateDatabase extends CmdCreateNode {
    
    /** Creates a new instance of ActionCreateDatabase */
    public ActionCreateDatabase() {
        super(Model.getMetaTypes().getComponent(), "Component");
        //dbuml:removed
//        super(Model.getMetaTypes().getComponent(), "Database");
        putValue(Action.SHORT_DESCRIPTION, "New Database");
        // todo:  problems with icons.  Swing errors when loading diagrams.
        //Couldn't experience the problem (Jules 5/25/04)
        // Problem happens when the created icon is greater than 16x16. (Jules 12/16/04)
        putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/org/dbuml/argo/Images/Database.gif")));
        
    }
    
    /**
     * 
     * @return 
     */
    public Object makeNode() {
        Object node = super.makeNode();
        return CreateActions.createDatabase(node);
    }
    
}
