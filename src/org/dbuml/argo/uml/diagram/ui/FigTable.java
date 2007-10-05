/**********************************************
 *   Copyright (C) 2007 CINCOM SYSTEMS, INC.
 *   All Rights Reserved
 **********************************************/

package org.dbuml.argo.uml.diagram.ui;

import org.tigris.gef.graph.GraphModel;
import java.awt.event.MouseEvent;
import java.util.Vector;
import java.util.Iterator;

import org.dbuml.base.model.Database;
import org.dbuml.base.model.DBModelFacade;
import org.dbuml.argo.uml.ui.ActionBrowseInstances;
import org.dbuml.argo.uml.ui.ActionUpdateCatalog;
import org.dbuml.argo.uml.ui.ActionGenerateSource;
import org.dbuml.argo.uml.ui.ActionCreateColumn;
import org.dbuml.argo.uml.ui.ActionCreatePKEY;
import org.dbuml.argo.uml.ui.ActionAddNotNullConstraint;
import org.dbuml.argo.uml.ui.ActionAddUniqueConstraint;

import org.argouml.ui.ArgoJMenu;
import org.argouml.uml.diagram.ui.ActionAddNote;
import org.argouml.i18n.Translator;

/**
 * Table figure class.
 * @author  jgunderson
 */
public class FigTable extends org.argouml.uml.diagram.static_structure.ui.FigClass implements DBFigure {
    private static java.awt.Color filler = new java.awt.Color(255, 255, 200);
    
    /**
     *  Creates an instance of FigTable.
     * @param modelElement 
     * @param x 
     * @param y 
     * @param w 
     * @param h 
     */
    public FigTable(Object modelElement, int x, int y, int w, int h) {
        super(modelElement, x, y, w, h);
        setFillColor( getFiller());
    }
    
    /**
     * Contructor that hooks the fig into the UML element.
     *
     * @param gm ignored
     * @param node the UML element
     */
    public FigTable(GraphModel gm, Object node) {
        super(gm, node);
        setFillColor( getFiller());
    }
    
    /**
     * Gets the color for filling the inside of the figure.
     * @return A Color instance.
     */
    protected java.awt.Color getFiller() {
        return filler;
    }
    
    /**
     * Gets dbuml added actions.
     * @return A vector of actions.
     */
    public Vector getAdditionalPopUpActions() {
        Vector popUpActions = new Vector();
        
        Database database = DBModelFacade.getInstance().getOwningDatabase(getOwner());
        if (database != null) {
            if (database.isConnected()) {
                popUpActions.addElement(ActionUpdateCatalog.SINGLETON);
                popUpActions.addElement(ActionBrowseInstances.SINGLETON);
            }
            popUpActions.addElement(ActionGenerateSource.SINGLETON);
        }
        return popUpActions;
    }
    
    /**
     * Build a collection of menu items relevant for a right-click
     * popup menu on a Table
     *
     * @param     me     a mouse event
     * @return          a collection of menu items
     */
    public Vector getPopUpActions(MouseEvent me) {
        //
        //  Get FigClass's Actions, replace the "add" menue with ours and add the new actions
        //
        
        
        Vector popUpActions = super.getPopUpActions(me);
        
        Iterator it = popUpActions.iterator();
        for (int i = 0; it.hasNext(); i++) {
            Object obj = it.next();
            if (obj instanceof ArgoJMenu) {
                ArgoJMenu menue = (ArgoJMenu) obj;
                if ((menue.getText()).equals(Translator.localize("menu.popup.add"))) {
                    popUpActions.removeElement(obj);
                    ArgoJMenu addMenu = new ArgoJMenu("menu.popup.add");
                    addMenu.add(ActionCreateColumn.getInstance());
                    addMenu.add(ActionCreatePKEY.getInstance());
                    addMenu.add(ActionAddNotNullConstraint.getInstance());
                    addMenu.add(ActionAddUniqueConstraint.getInstance());
                    addMenu.add(new ActionAddNote());
                    popUpActions.insertElementAt(addMenu, i);
                    break;
                }
            }
            
        }
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
        return "Table";
    }
    
}
