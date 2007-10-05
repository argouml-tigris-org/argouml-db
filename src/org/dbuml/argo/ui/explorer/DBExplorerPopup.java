/**********************************************
 *   Copyright (C) 2007 CINCOM SYSTEMS, INC.
 *   All Rights Reserved
 **********************************************/
/*
 * DBExplorerPopup.java
 *
 * Created on September 5, 2007, 9:44 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.dbuml.argo.ui.explorer;

import org.argouml.ui.explorer.ExplorerPopup;
import java.awt.event.MouseEvent;

/** Popup for extra dbuml functionality for the Explorer.
 *
 * @author jgrengbondai
 */
public class DBExplorerPopup extends ExplorerPopup {
    
    /** Creates a new instance of DBExplorerPopup.
     *
     * @param selectedItem
     *            is the item that we are pointing at.
     * @param me
     *            is the event.
     */
    public DBExplorerPopup(Object selectedItem, MouseEvent me) {
        super(selectedItem, me);
        
        //dbuml<<<
        //
        //  If this is one of the DB model elements, get the correcponding figure
        //  and ask the figure for any additional actions.
        //

        org.dbuml.argo.uml.diagram.ui.DBFigure fg = 
            (org.dbuml.argo.uml.diagram.ui.DBFigure) 
            org.dbuml.argo.uml.diagram.ui.FigureFactory.getInstance().getFigure(selectedItem);
        if (fg != null){
            java.util.Vector vActions = fg.getAdditionalPopUpActions();
            for (java.util.Enumeration e = vActions.elements() ; e.hasMoreElements() ;) {
                final Object obj = e.nextElement();
                if (obj instanceof javax.swing.Action) {
                    this.add((javax.swing.Action) obj);
                } else if (obj instanceof javax.swing.JMenuItem) {
                    this.add((javax.swing.JMenuItem) obj);
                }
            }
        }
        //dbuml>>>
    }
    
}
