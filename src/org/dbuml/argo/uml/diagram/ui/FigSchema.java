/**********************************************
 *   Copyright (C) 2007 CINCOM SYSTEMS, INC.
 *   All Rights Reserved
 **********************************************/

package org.dbuml.argo.uml.diagram.ui;

import org.dbuml.argo.uml.ui.ActionImportTablesFromCatalog;
import org.dbuml.argo.uml.ui.ActionImportViews;
import org.dbuml.argo.uml.ui.ActionUpdateCatalog;
import java.awt.event.MouseEvent;
import java.util.Vector;
import org.dbuml.base.i18n.Translator;
import org.dbuml.base.model.Database;
import org.dbuml.base.model.DBModelFacade;
import org.dbuml.argo.uml.ui.ActionGenerateSource;
import org.argouml.ui.targetmanager.TargetManager;
import org.tigris.gef.graph.GraphModel;


/**
 *
 * @author  jgunderson
 */
public class FigSchema extends org.argouml.uml.diagram.static_structure.ui.FigPackage
        implements DBFigure {
    private static java.awt.Color filler = java.awt.Color.YELLOW;
    
    /**
     * Creates a new FigSchema.
     *
     * @param node the UML model.
     * @param x the x coordinate of the location
     * @param y the y coordinate of the location
     */
    public FigSchema(Object node, int x, int y) {
        super(node, x, y);
        setFillColor(filler);
    }
    
    /**
     * Contructor that hooks the fig into the UML element.
     *
     * @param node the UML element
     */
    public FigSchema(Object node) {
        super(node, 0, 0);
        setFillColor(filler);
    }
    
     /**
     * Creates new FigSchema instance.
     * @param gm The graph model
     * @param node the UML element 
     */
    public FigSchema(GraphModel gm, Object node) {
        super(gm, node);
        setFillColor(filler);
    }
    
    /**
     * Gets dbuml added actions.
     * @return A vector of actions.
     */
    public Vector getAdditionalPopUpActions() {
        Vector popUpActions = new Vector();
        Object model = TargetManager.getInstance().getModelTarget();
        Database database = DBModelFacade.getInstance().getOwningDatabase(getOwner());
        
        if ((database != null) &&
                (DBModelFacade.getInstance().getOwningSchema(model) != null)) {
            if (database.isConnected()) {
                org.argouml.ui.ArgoJMenu importMenu = new org.argouml.ui.ArgoJMenu(
                        Translator.getInstance().localize("IMPORT_FROM_CATALOG"));
                importMenu.add(ActionImportTablesFromCatalog.SINGLETON);
                importMenu.add(ActionImportViews.SINGLETON);
                popUpActions.addElement(importMenu);
                popUpActions.addElement(ActionUpdateCatalog.SINGLETON);
            }
            popUpActions.addElement(ActionGenerateSource.SINGLETON);
            
        }
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
        return "Schema";
    }
    
}
