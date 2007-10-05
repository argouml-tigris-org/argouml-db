/**********************************************
 *   Copyright (C) 2007 CINCOM SYSTEMS, INC.
 *   All Rights Reserved
 **********************************************/

package org.dbuml.argo.uml.ui;

import org.tigris.gef.undo.UndoableAction;
import java.awt.event.ActionEvent;
import org.argouml.ui.targetmanager.TargetManager;
import org.dbuml.base.controller.FigureActionsInterface;
import org.dbuml.base.i18n.Translator;
import org.dbuml.base.factory.Factory;

/**
 *
 * @author  jgrengbondai
 */
public class ActionCreateColumnForView extends UndoableAction {
    
    ////////////////////////////////////////////////////////////////
    // static variables
    
    /**
     * The instance of this class.
     */
    public static final ActionCreateColumnForView SINGLETON =
            new ActionCreateColumnForView();
    
    /** Creates a new instance of ActionCreateColumnForView */
    protected ActionCreateColumnForView() {
        super(Translator.getInstance().localize("ADD_COLUMN_FORVIEW"));
    }
    
    /**
     * 
     * @param event 
     */
    public void actionPerformed(ActionEvent event) {
        //
        // ActionCreateColumnForView is a generic action (does not depend on Database)
        // so we use GenericFigureActions.  We don't use CreateActions as this uses the GUI
        //
        Factory fac = Factory.getFactory(""); // get the gerneic Factory
        FigureActionsInterface action = fac.getFigureActionsInterface(); // get generic figureactions
        action.createColumnForView(
                TargetManager.getInstance().getModelTarget());
    }
    
}
