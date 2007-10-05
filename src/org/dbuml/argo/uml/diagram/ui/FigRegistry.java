/**********************************************
 *   Copyright (C) 2007 CINCOM SYSTEMS, INC.
 *   All Rights Reserved
 **********************************************/
package org.dbuml.argo.uml.diagram.ui;

import org.tigris.gef.graph.GraphModel;
import org.dbuml.argo.uml.ui.ActionConnectRegistry;
import org.dbuml.argo.uml.ui.ActionImportDatabases;
import org.dbuml.argo.uml.ui.ActionUpdateCatalog;
import java.awt.event.MouseEvent;
import java.util.Vector;
import org.dbuml.base.model.DBModelFacade;
import org.dbuml.base.model.Registry;
import org.dbuml.base.i18n.Translator;

/**
 *
 * @author  jgunderson
 */
public class FigRegistry extends org.argouml.uml.diagram.deployment.ui.FigComponent implements DBFigure {
    private static java.awt.Color filler = new java.awt.Color(120, 120, 255);
    /**
     * Creates new FigRegistry instance.
     * @param gm Graph model.
     * @param node Model element.
     */
    public FigRegistry(GraphModel gm, Object node) {
        super(gm, node);
        setFillColor(filler);
    }
    
    /**
     * Creates new FigRegistry.
     */
    public FigRegistry() {
        super();
        setFillColor(filler);
    }
    
    /**
     * Gets dbuml added actions.
     * @return A vector of actions.
     */
    public Vector getAdditionalPopUpActions() {
        Vector popUpActions = new Vector();
        Registry registry = DBModelFacade.getInstance().getRegistry(getOwner());
        ActionConnectRegistry actionConnect=ActionConnectRegistry.SINGLETON;
        String connectLabel= Translator.getInstance().localize("CONNECT_REGISTRY");
        boolean connected= (registry !=null && registry.isConnected());
        if (connected) {
            connectLabel= Translator.getInstance().localize("DISCONNECT_REGISTRY");
        }
        actionConnect.putValue(javax.swing.Action.NAME, connectLabel);
        actionConnect.putValue(javax.swing.Action.SHORT_DESCRIPTION, connectLabel);
        
        popUpActions.addElement(actionConnect);
        popUpActions.addElement(ActionImportDatabases.SINGLETON);
        popUpActions.addElement(ActionUpdateCatalog.SINGLETON);
        return popUpActions;
    }
    
    /**
     * Build a collection of menu items relevant for a right-click
     * popup menu on a Schema
     *
     * @param     me     a mouse event
     * @return          a collection of menu items
     */
    public Vector getPopUpActions(MouseEvent me) {
        Vector popUpActions = super.getPopUpActions(me);
        popUpActions.addAll(getAdditionalPopUpActions());
        return popUpActions;
    }
    
    /**
     * Updates the line color of this figure.
     */
    public void updateLineColor() {
        if (isConnected()) {
            setLineColor(java.awt.Color.GREEN);
        } else {
            setLineColor(java.awt.Color.BLACK);
        }
    }
    
    private boolean isConnected() {
        Registry registry = DBModelFacade.getInstance().getOwningRegistry(getOwner());
        return (registry !=null && registry.isConnected());
    }
    
    /**
     * See the super class.
     */
    public void postLoad() {
        super.postLoad();
        this.updateLineColor();
    }
    
    /**
     * Default Reply text to be shown while placing node in diagram.
     *
     * @return the text to be shown while placing node in diagram
     */
    public String placeString() {
        return "new " + getDBFigName();
    }
    
    /**
     * See the super class.
     * @param me The mouse event.
     * @return See the super class.
     */
    public String getTipString(MouseEvent me) {
        return getDBFigName() + ": " + getName();
    }
    
    /**
     * Gets DBUML figure type name.
     * @return The figure type name.
     */
    public String getDBFigName() {
        return "Registry";
    }
    
}
