/**********************************************
 *   Copyright (C) 2007 CINCOM SYSTEMS, INC.
 *   All Rights Reserved
 **********************************************/

package org.dbuml.argo.uml.diagram.ui;

import org.tigris.gef.graph.GraphModel;
import java.awt.event.MouseEvent;
import java.util.Vector;
import java.util.Iterator;

import org.dbuml.argo.uml.ui.ActionCreateColumnForView;

import org.argouml.ui.ArgoJMenu;
import org.argouml.uml.diagram.ui.ActionAddNote;
import org.argouml.i18n.Translator;

/**
 * View figure class.
 * @author  jgrengbondai
 */
public class FigView extends FigTable {
    private static java.awt.Color filler = new java.awt.Color(204, 204, 255);
    
    /**
     * 
     * @param modelElement 
     * @param x 
     * @param y 
     * @param w 
     * @param h 
     */
    public FigView(Object modelElement, int x, int y, int w, int h) {
        super(modelElement, x, y, w, h);
        setFillColor( getFiller());
    }
    
    /**
     * Contructor that hooks the fig into the UML element.
     *
     * @param gm ignored
     * @param node the UML element
     */
    public FigView(GraphModel gm, Object node) {
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
                    addMenu.add(ActionCreateColumnForView.SINGLETON);
                    addMenu.add(new ActionAddNote());
                    popUpActions.insertElementAt(addMenu, i);
                    break;
                }
            }
            
        }
        return popUpActions;
    }
    
    
    /**
     * Gets DBUML figure type name.
     * @return The figure type name.
     */
    public String getDBFigName() {
        return "View";
    }
    
}
