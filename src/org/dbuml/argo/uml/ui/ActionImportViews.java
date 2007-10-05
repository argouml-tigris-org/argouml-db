/**********************************************
 *   Copyright (C) 2007 CINCOM SYSTEMS, INC.
 *   All Rights Reserved
 **********************************************/

package org.dbuml.argo.uml.ui;

import org.dbuml.base.controller.FigureActionsInterface;
import org.dbuml.base.controller.Util;
import org.dbuml.base.model.Database;
import org.dbuml.base.model.DBModelFacade;
import org.dbuml.base.model.DBElement;
import org.dbuml.base.factory.Factory;
import org.dbuml.base.i18n.Translator;

import org.argouml.ui.targetmanager.TargetManager;

import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;

import org.tigris.gef.undo.UndoableAction;

/**
 *
 * @author  jgrengbondai
 */
public class ActionImportViews extends UndoableAction {
    
    /**
     * The instance of this class.
     */
    public static final ActionImportViews SINGLETON = new ActionImportViews();
    
    /** Creates a new instance of ActionImportViews */
    protected ActionImportViews() {
        super(Translator.getInstance().localize("IMPORT_VIEWS"));
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
            actionsinterface.importViews(selectedItem);
        } else {
            DBElement dbe = DBModelFacade.getInstance().getDBElement(selectedItem);
            Util.showMessageDialog(null,
                    Translator.getInstance().localize("DB_FACTORY_NOTFOUND",
                    dbe.getProperty(Database.FACTORY)),
                    Translator.getInstance().localize("IMPORT_VIEWS"),
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
}
