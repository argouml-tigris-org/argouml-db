/**********************************************
*   Copyright (C) 2007 CINCOM SYSTEMS, INC.
*   All Rights Reserved
**********************************************/

package org.dbuml.argo.uml.ui;

import java.awt.event.ActionEvent;
import org.argouml.ui.targetmanager.TargetManager;
import org.dbuml.base.controller.FigureActionsInterface;
import org.dbuml.base.i18n.Translator;
import org.dbuml.base.factory.Factory;
import org.tigris.gef.undo.UndoableAction;

/**
 * Class for adding NotNull constraint action to figures.
 * @author  jgrengbondai
 */
public class ActionAddNotNullConstraint extends UndoableAction {
     
    ////////////////////////////////////////////////////////////////
    // static variables
    

    /**
     * The instance of ActionAddNotNullConstraint
     */
    private static final ActionAddNotNullConstraint fSINGLETON =
	new ActionAddNotNullConstraint();
    
    /** Creates a new instance of ActionAddConstraint */
    protected ActionAddNotNullConstraint() {
         super(Translator.getInstance().localize("ADD_NOTNULL_CONSTRAINT"));
    }
    
    /**
     * Get the instance of the singleton for ActionAddNotNullConstraint.
     *
     * @return the singleton.
     */
    public static ActionAddNotNullConstraint getInstance() {
         return fSINGLETON;
    }
    
    /**
     * Performs the action.
     * @param event ActionEvent instance 
     */
    public void actionPerformed(ActionEvent event) {
        
        //
        // ActionAddNotNullConstraint is a generic action (does not depend on Database)
        // so we use GenericFigureActions.  We don't use CreateActions as this uses the GUI
        //
        Factory fac = Factory.getFactory(""); // get the generic Factory
        FigureActionsInterface action = fac.getFigureActionsInterface(); // get generic figureactions
        action.createConstraintNotNull( 
                   TargetManager.getInstance().getModelTarget());         
    }
    
}
