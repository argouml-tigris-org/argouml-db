/**********************************************
 *   Copyright (C) 2007 CINCOM SYSTEMS, INC.
 *   All Rights Reserved
 **********************************************/

package org.dbuml.argo.uml.ui;

import org.dbuml.base.controller.CreateActions;
import org.dbuml.base.i18n.Translator;

import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.model.Model;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;

import java.awt.event.ActionEvent;

import javax.swing.Action;

import org.tigris.gef.undo.UndoableAction;

/**
 *
 * @author  jgunderson
 *
 * This class is basically a copy of ActionAddAttribute that also creates the
 * Column class.  ActionAddAttribute is now private and created by TargetManager
 * and can't be extended.
 *
 * todo:  This class may need to listen for changes to the TargetManager to
 * determine when to enable its action.
 *
 */

public class ActionCreatePKEY extends UndoableAction {
    ////////////////////////////////////////////////////////////////
    // static variables
    
    /**
     * The instance of this class.
     */
    private static final ActionCreatePKEY SINGLETON = new ActionCreatePKEY();
    
    /** Creates a new instance of ActionCreateColumn */
    private ActionCreatePKEY() {
        super(Translator.getInstance().localize("ADD_PKEY"),
//                ResourceLoaderWrapper.lookupIcon("button.new-attribute"));
                ResourceLoaderWrapper.lookupIcon("PKey"));
        putValue(Action.SHORT_DESCRIPTION, "Primary Key");
        putValue(Action.NAME, Translator.getInstance().localize("ADD_PKEY"));
        
    }
    
    /**
     * Get the instance of the singleton for ActionAddUniqueConstraint.
     *
     * @return the singleton.
     */
    public static ActionCreatePKEY getInstance() {
         return SINGLETON;
    }
    
    /**
     * 
     * @param ae 
     */
    public void actionPerformed(ActionEvent ae) {
        super.actionPerformed(ae);
        
        Object target = TargetManager.getInstance().getModelTarget();
        Object classifier = null;
        
        if (Model.getFacade().isAClassifier(target)
        || Model.getFacade().isAAssociationEnd(target)) {
            classifier = target;
        } else if (Model.getFacade().isAAttribute(target)) {
            classifier = Model.getFacade().getOwner(target);
        } else {
            return;
        }
        
        Project project = ProjectManager.getManager().getCurrentProject();
        
        Object intType = project.findType("int");
        Object model = project.getModel();
        Object attr =
                Model.getCoreFactory().buildAttribute(
                classifier,
                model,
                intType);
        TargetManager.getInstance().setTarget(attr);
        CreateActions.createPKEY(attr);
    }
    
    /**
     * @return true if this tool should be enabled
     */
    public boolean shouldBeEnabled() {
        Object target = TargetManager.getInstance().getSingleModelTarget();
        if (target == null) { return false; }
        return Model.getFacade().isAClassifier(target)
        || Model.getFacade().isAFeature(target)
        || Model.getFacade().isAAssociationEnd(target);
    }
    
}
