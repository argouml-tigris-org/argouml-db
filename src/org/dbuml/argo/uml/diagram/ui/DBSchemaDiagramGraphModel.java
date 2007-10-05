/**********************************************
 *   Copyright (C) 2007 CINCOM SYSTEMS, INC.
 *   All Rights Reserved
 **********************************************/
package org.dbuml.argo.uml.diagram.ui;


import java.util.Hashtable;

import org.dbuml.base.controller.CreateActions;
import org.dbuml.base.model.DBModelFacade;

import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Mode;
import org.tigris.gef.base.ModeManager;

/** Graph model for DBUML schema diagram.
 */
public class DBSchemaDiagramGraphModel
        extends org.argouml.uml.diagram.static_structure.ClassDiagramGraphModel {
    
    /** Contruct and add a new edge of the given kind and connect
     * the given ports.
     *
     * @param fromPort   The originating port to connect
     *
     * @param toPort     The destination port to connect
     *
     * @param edgeClass  The NSUML type of edge to create.
     *
     * @return           The type of edge created (the same as
     *                   <code>edgeClass</code> if we succeeded,
     *                   <code>null</code> otherwise)
     */
    public Object connect(Object fromPort, Object toPort,
            java.lang.Class edgeClass) {
        //Object connection = super.connect(fromPort, toPort, edgeClass);
        Editor curEditor = Globals.curEditor();
        ModeManager modeManager = curEditor.getModeManager();
        Mode mode = (Mode) modeManager.top();
        Hashtable args = mode.getArgs();
        String type = (String) args.get("dbtype");
        if (type != null && type.equals("FK")) {
            if (DBModelFacade.getInstance().representsATable(fromPort) &&
                    DBModelFacade.getInstance().representsATable(toPort)) {
                Object connection = super.connect(fromPort, toPort, edgeClass);
                return CreateActions.createFKEY(fromPort, toPort, connection);
            } else {
                return null;
            }
        }
        
        if (type != null && type.equals("DERIVE")) {
            if (DBModelFacade.getInstance().representsAView(fromPort) &&
                    (DBModelFacade.getInstance().representsATable(toPort) ||
                    DBModelFacade.getInstance().representsAView(toPort))) {
                return createDBDerive(fromPort, toPort, edgeClass);
            } else {
                return null;
            }
        }
        return super.connect(fromPort, toPort, edgeClass);
    }
    
    /**
     * Creates DBDerive connection from a view to the source table/view.
     * @param fromPort a model for the view. 
     * @param toPort a model for the source table/view.
     * @param edgeClass see super class.
     * @return A DBDerive object.
     */
    protected Object createDBDerive(Object fromPort, Object toPort,
            java.lang.Class edgeClass) {
        final Object connection = super.connect(fromPort, toPort, edgeClass);
        CreateActions.createDBDerive(fromPort, toPort, connection);
        return connection;
    }
    
} /* end class DeploymentDiagramGraphModel */