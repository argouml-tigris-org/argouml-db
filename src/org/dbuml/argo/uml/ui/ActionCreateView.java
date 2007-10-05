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
 * @author  jgrengbondai
 */
public class ActionCreateView extends org.argouml.ui.CmdCreateNode {
    
    
    /** Creates a new instance of ActionCreateView */
    public ActionCreateView() {
        super(Model.getMetaTypes().getUMLClass(), "Class");
        //dbuml:removed
//        super(Model.getMetaTypes().getUMLClass(), "View");
        putValue(Action.SHORT_DESCRIPTION, "New View");
//        // todo:  problems with icons.  Swing errors when loading diagrams.
//        // Problem happens when the created icon is greater than 16x16.
        putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/org/dbuml/argo/Images/View.gif")));
        
    }
    
    /**
     * 
     * @return 
     */
    public Object makeNode() {
        Object node = super.makeNode();
        return CreateActions.createView(node);
    }
    
}
