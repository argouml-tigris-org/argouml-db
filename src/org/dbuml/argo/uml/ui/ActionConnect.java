/**********************************************
 *   Copyright (C) 2007 CINCOM SYSTEMS, INC.
 *   All Rights Reserved
 **********************************************/
package org.dbuml.argo.uml.ui;

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
import org.tigris.gef.undo.UndoableAction;

/** Action to trigger importing from sources.
 * @stereotype singleton
 */
public class ActionConnect extends UndoableAction {
    
    ////////////////////////////////////////////////////////////////
    // static variables
    
    /**
     * The instance of this class.
     */
    public static final ActionConnect SINGLETON = new ActionConnect();
    
    ////////////////////////////////////////////////////////////////
    // constructors
    
    /**
     * Creates an instance of ActionConnect.
     */
    protected ActionConnect() {
        super(Translator.getInstance().localize("CONNECT_CATALOG"));
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
            actionsinterface.connect(selectedItem);
            // call the figure to update iteself.
            Object fig = org.argouml.ui.targetmanager.TargetManager.getInstance().getFigTarget();
            if (fig instanceof org.dbuml.argo.uml.diagram.ui.FigDatabase) {
                ((org.dbuml.argo.uml.diagram.ui.FigDatabase)fig).updateLineColor();
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
