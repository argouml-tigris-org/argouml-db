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
public class ActionImportAttributeTypes extends UndoableAction {
   
    /**
     * The instance of this class.
     */
    public static final ActionImportAttributeTypes SINGLETON = new ActionImportAttributeTypes();
    
    /** Creates a new instance of ActionImportAttributeTypes */
    protected ActionImportAttributeTypes() {
        super(Translator.getInstance().localize("IMPORT_ATTR_TYPES"));
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
            actionsinterface.importAttributeTypes(selectedItem);
        } else {
            DBElement dbe = DBModelFacade.getInstance().getDBElement(selectedItem);
            Util.showMessageDialog(null,
                    Translator.getInstance().localize("DB_FACTORY_NOTFOUND",
                    dbe.getProperty(Database.FACTORY)),
                    Translator.getInstance().localize("IMPORT_ATTR_TYPES"),
                    JOptionPane.ERROR_MESSAGE);
        }
        
    }
    
}
