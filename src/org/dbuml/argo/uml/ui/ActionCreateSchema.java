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
public class ActionCreateSchema extends CmdCreateNode {
    
    /** Creates a new instance of ActionCreateTable */
    public ActionCreateSchema() {
        super(Model.getMetaTypes().getPackage(), "Package");
        
        // This code works when Package.gif in org\argouml\Images\plaf\javax\swing\plaf\metal\MetalLookAndFeel\toolbarButtonGraphics\argouml\elements
        // does not exist or is renamed. Otherwise, we get a null pointer exception in swing when loading the diagrams.
        //dbuml:removed
//        super(Model.getMetaTypes().getPackage(), "Schema");
        
        putValue(Action.SHORT_DESCRIPTION, "New Schema");
//        // todo:  problems with icons.  Swing errors when loading diagrams.
//        putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/org/dbuml/argo/Images/Schema.gif")));
        
    }
    
    /**
     * 
     * @return 
     */
    public Object makeNode() {
        Object node = super.makeNode();
        return CreateActions.createSchema(node);
    }
    
}
