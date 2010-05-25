/**********************************************
 *   Copyright (C) 2007 CINCOM SYSTEMS, INC.
 *   All Rights Reserved
 **********************************************/

package org.dbuml.argo.uml.diagram.ui;

import org.tigris.gef.graph.GraphModel;
import org.argouml.uml.diagram.DiagramSettings;
import org.dbuml.argo.uml.ui.ActionConnect;
import org.dbuml.argo.uml.ui.ActionImportSchemas;
import org.dbuml.argo.uml.ui.ActionImportAttributeTypes;
import org.dbuml.argo.uml.ui.ActionUpdateCatalog;
import org.dbuml.argo.uml.ui.ActionGenerateSource;

import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.Vector;
import org.dbuml.base.model.Database;
import org.dbuml.base.model.DBModelFacade;
import org.dbuml.base.database.DBMetadata;
import org.dbuml.base.database.DBMetadataCache;
import org.dbuml.base.i18n.Translator;

/**
 * Database figure class.
 * @author  jgunderson
 */
public class FigDatabase extends org.argouml.uml.diagram.deployment.ui.FigComponent
        implements DBFigure {
    private static java.awt.Color filler = new java.awt.Color(204, 204, 255);
    /**
     * Creates new FigDatabase instance.
     * @param gm The graph model
     * @param node the UML element 
     */
    public FigDatabase(GraphModel gm, Object node) {
        
        super(node, new Rectangle(0, 0, 0, 0), new DiagramSettings());
        setFillColor(filler);
    }
    
    /**
     * Creates new FigDatabase instance.
     */
    public FigDatabase(Object owner, Rectangle bounds, DiagramSettings settings) {
        super(owner, bounds, settings);
        setFillColor(filler);
    }

    /**
     * Gets dbuml added actions.
     * @return A vector of actions.
     */
    public Vector getAdditionalPopUpActions() {
        
        Vector popUpActions = new Vector();
        Database database = DBModelFacade.getInstance().getOwningDatabase(getOwner());
        ActionConnect actionConnect=ActionConnect.SINGLETON;
        String connectLabel= Translator.getInstance().localize("CONNECT_CATALOG");
        boolean connected= (database !=null && database.isConnected());
        if (connected) {
            connectLabel= Translator.getInstance().localize("DISCONNECT_CATALOG");
        }
        actionConnect.putValue(javax.swing.Action.NAME, connectLabel);
        actionConnect.putValue(javax.swing.Action.SHORT_DESCRIPTION, connectLabel);
        
        popUpActions.addElement(actionConnect);
        if (connected) {
            org.argouml.ui.ArgoJMenu importMenu = new org.argouml.ui.ArgoJMenu(Translator.getInstance().localize("IMPORT_FROM_CATALOG"));
            try {
                DBMetadata dbmd = DBMetadataCache.getDBMetadata(database);
                importMenu.add(ActionImportSchemas.SINGLETON);
                ActionImportSchemas.SINGLETON.setEnabled((dbmd!= null) && dbmd.supportsSchemaInTableDefinitions());
            } catch (java.sql.SQLException e) {
                System.err.println(e.getMessage());
            }
            importMenu.add(ActionImportAttributeTypes.SINGLETON);
            importMenu.setEnabled(connected);
            popUpActions.addElement(importMenu);
            popUpActions.addElement(ActionUpdateCatalog.SINGLETON);
        }
        
        popUpActions.addElement(ActionGenerateSource.SINGLETON);
        
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
     * Default Reply text to be shown while placing node in diagram.
     *
     * @return the text to be shown while placing node in diagram
     */
    public String placeString() {
        // TODO: Localize user visible strings using Translator
        return "new " + getDBFigName();
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
        Database database = DBModelFacade.getInstance().getOwningDatabase(getOwner());
        return (database !=null && database.isConnected());
    }
    
    /**
     * See the super class.
     */
    public void postLoad() {
        super.postLoad();
        this.updateLineColor();
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
        return "Database";
    }
    
}
