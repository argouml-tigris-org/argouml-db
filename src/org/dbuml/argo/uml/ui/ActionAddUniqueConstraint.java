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
 * Class for adding Unique constraint action to figures.
 * @author  jgrengbondai
 */
public class ActionAddUniqueConstraint extends UndoableAction {
    

    ////////////////////////////////////////////////////////////////
    // static variables

    /**
     * The instance of this class.
     */
    private static final ActionAddUniqueConstraint SINGLETON =
	new ActionAddUniqueConstraint(); 
    
    /** Creates a new instance of ActionAddUniqueConstraint */
    private ActionAddUniqueConstraint() {
         super(Translator.getInstance().localize("ADD_UNIQUE_CONSTRAINT"));
    }
    
     /**
     * Get the instance of the singleton for ActionAddUniqueConstraint.
     *
     * @return the singleton.
     */
    public static ActionAddUniqueConstraint getInstance() {
         return SINGLETON;
    }
    
    /**
     * Performs the action.
     * @param event ActionEvent instance 
     */
    public void actionPerformed(ActionEvent event) {
        //
        // ActionAddUniqueConstraint is a generic action (does not depend on Database)
        // so we use GenericFigureActions.  We don't use CreateActions as this uses the GUI
        //
        Factory fac = Factory.getFactory(""); // get the gerneic Factory
        FigureActionsInterface action = fac.getFigureActionsInterface(); // get generic figureactions
        action.createConstraintUnique(
                   TargetManager.getInstance().getModelTarget());        
    }
    
}
