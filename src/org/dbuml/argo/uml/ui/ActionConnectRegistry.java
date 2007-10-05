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


public class ActionConnectRegistry extends UndoableAction {
    
    ////////////////////////////////////////////////////////////////
    // static variables
    
    /**
     * The instance of this class.
     */
    public static final ActionConnectRegistry SINGLETON = new ActionConnectRegistry();
    
    ////////////////////////////////////////////////////////////////
    // constructors
    
    /**
     * Creates an instance of ActionConnectRegistry.
     */
    protected ActionConnectRegistry() {
        super(Translator.getInstance().localize("CONNECT_REGISTRY"));
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
            actionsinterface.connectRegistry(selectedItem);
            // call the figure to update iteself.
            Object fig = org.argouml.ui.targetmanager.TargetManager.getInstance().getFigTarget();
            if (fig instanceof org.dbuml.argo.uml.diagram.ui.FigRegistry) {
                ((org.dbuml.argo.uml.diagram.ui.FigRegistry)fig).updateLineColor();
            }
        } else {
            DBElement dbe = DBModelFacade.getInstance().getDBElement(selectedItem);
            Util.showMessageDialog(null,
                    Translator.getInstance().localize("DB_FACTORY_NOTFOUND",
                    dbe.getProperty(Database.FACTORY)),
                    Translator.getInstance().localize("CONNECT_FAILURE_TITLE"),
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
}
