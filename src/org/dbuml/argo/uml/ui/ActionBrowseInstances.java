/**********************************************
 *   Copyright (C) 2007 CINCOM SYSTEMS, INC.
 *   All Rights Reserved
 **********************************************/
package org.dbuml.argo.uml.ui;

import org.tigris.gef.undo.UndoableAction;
import org.argouml.ui.targetmanager.TargetManager;

import org.dbuml.base.controller.FigureActionsInterface;
import org.dbuml.base.controller.Util;
import org.dbuml.base.model.Database;
import org.dbuml.base.model.DBModelFacade;
import org.dbuml.base.model.DBElement;
import org.dbuml.base.factory.Factory;

import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import org.dbuml.base.i18n.Translator;

/** Action to trigger importing from sources.
 * @stereotype singleton
 */
public class ActionBrowseInstances extends UndoableAction {
    
    ////////////////////////////////////////////////////////////////
    // static variables
    
    /**
     * The instance of this class.
     */
    public static final ActionBrowseInstances SINGLETON = new ActionBrowseInstances();
    
    ////////////////////////////////////////////////////////////////
    // constructors
    
    /**
     * Create new ActionBrowseInstances.
     */
    protected ActionBrowseInstances() {
        super(Translator.getInstance().localize("BROWSE_INSTANCES"));
    }
    
    /**
     * 
     * @param event 
     */
    public void actionPerformed(ActionEvent event) {
        Object selectedItem = TargetManager.getInstance().getModelTarget();
        Factory fac = DBModelFacade.getInstance().getMyFactory(selectedItem);
        if (fac != null) {
            FigureActionsInterface actionsinterface = fac.getFigureActionsInterface();
            actionsinterface.browseInstances(selectedItem);
        } else {
            DBElement dbe = DBModelFacade.getInstance().getDBElement(selectedItem);
            Util.showMessageDialog(null,
                    Translator.getInstance().localize("DB_FACTORY_NOTFOUND",
                    dbe.getProperty(Database.FACTORY)),
                    Translator.getInstance().localize("BROWSE_INSTANCES"),
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
}

