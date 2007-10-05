/**********************************************
 *   Copyright (C) 2007 CINCOM SYSTEMS, INC.
 *   All Rights Reserved
 **********************************************/
package org.dbuml.argo.uml.ui;

import org.dbuml.base.controller.CreateActions;
import org.argouml.ui.CmdCreateNode;
import org.argouml.model.Model;
import javax.swing.Action;

/**
 *
 * @author  jgunderson
 */

public class ActionCreateRegistry extends CmdCreateNode {
    
    /** Creates a new instance of ActionCreateRegistry */
    public ActionCreateRegistry() {
        super(Model.getMetaTypes().getComponent(), "Registry");
        putValue(Action.SHORT_DESCRIPTION, "New Registry");
        // todo:  problems with icons.  Swing errors when loading diagrams.
        //Couldn't experinece the problem (Jules 5/25/04)
//        putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/org/dbuml/argo/Images/Registry.gif")));
//        putValue(Action.SMALL_ICON, ResourceLoaderWrapper.lookupIcon("Registry"));
        
    }
    
    /**
     * 
     * @return 
     */
    public Object makeNode() {
        Object node = super.makeNode();
        return CreateActions.createRegistry(node);
    }
    
}
