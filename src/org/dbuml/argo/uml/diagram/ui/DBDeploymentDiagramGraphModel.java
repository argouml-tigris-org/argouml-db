/**********************************************
 *   Copyright (C) 2007 CINCOM SYSTEMS, INC.
 *   All Rights Reserved
 **********************************************/

package org.dbuml.argo.uml.diagram.ui;

import java.beans.PropertyChangeEvent;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import org.argouml.model.Model;
import org.argouml.uml.diagram.deployment.DeploymentDiagramGraphModel;

import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Mode;
import org.tigris.gef.base.ModeManager;

import org.dbuml.base.controller.CreateActions;
import org.dbuml.base.model.DBModelFacade;

/**
 * DBUML deployment graph model.
 */
public class DBDeploymentDiagramGraphModel extends DeploymentDiagramGraphModel {
    
    /**
     * Return all ports on node or edge.
     * 
     * @see org.argouml.uml.diagram.deployment.DeploymentDiagramGraphModel#getPorts(java.lang.Object)
     * @param nodeOrEdge The nodeor Edge object. 
     * @return A list of ports.
     */
    public List getPorts(Object nodeOrEdge) {
        List res = super.getPorts(nodeOrEdge);
        if (Model.getFacade().isAPackage(nodeOrEdge)) {
            res.add(nodeOrEdge);
        }
        return res;
    }
    
    /**
     * Return all edges going to given port.
     * @param port The prot model.
     * @return A list of edges.
     */
    public List getInEdges(Object port) {
        
        List res = super.getInEdges(port);
        
        // This came from similar code in ClassDiagramGrapgModel which should apply
        // to Packages.  What in hell is this stuff doing?
        
        if (Model.getFacade().isAPackage(port)) {
            Iterator it = Model.getFacade().getSupplierDependencies(port).iterator();
            while (it.hasNext()) {
                res.add(it.next());
            }
            it = Model.getFacade().getSpecializations(port).iterator();
            while (it.hasNext()) {
                res.add(it.next());
            }
        }
        
        return res;
    }
    
//    /**
//     * Return all edges going from given port.
//     * @param port The port model. 
//     * @return A list of outgoing edges.
//     */
//    public List getOutEdges(Object port) {
//        
//        //
//        //  The DeploymentDiagramGraphModel does nothing so we don't add new code for
//        //  Packages.  But ClassDiagramGrapgModel does stuff in its method.
//        //  Maybe we should?
//        //
//        
//        return super.getOutEdges(port);
//    }
    
    /**
     * Returns true if the given object is a valid node in this graph.
     * @param node The node model to add.
     * @return <code>true</code> or <code>false</code>.
     */
    public boolean canAddNode(Object node) {
        if (node == null) {
            return false;
        }
        if (containsNode(node)) {
            return false;
        }
        return (Model.getFacade().isANode(node)) ||
                (Model.getFacade().isAComponent(node)) ||
                (Model.getFacade().isAClass(node)) ||
                (Model.getFacade().isAInterface(node)) ||
                (Model.getFacade().isAObject(node)) ||
                (Model.getFacade().isANodeInstance(node)) ||
                (Model.getFacade().isAComponentInstance(node)) ||
                (Model.getFacade().isAPackage(node));
    }
    
    /**
     * Copied from supper class.
     * @param pce The PropertyChangeEvent
     */
    public void vetoableChange(PropertyChangeEvent pce) {
        if ("ownedElement".equals(pce.getPropertyName())) {
            Vector oldOwned = (Vector) pce.getOldValue();
            Object eo = /*(MElementImport)*/ pce.getNewValue();
            Object me = Model.getFacade().getModelElement(eo);
            if (oldOwned.contains(eo)) {
                if (Model.getFacade().isANode(me)) { removeNode(me); }
                if (Model.getFacade().isANodeInstance(me)) { removeNode(me); }
                if (Model.getFacade().isAComponent(me)) { removeNode(me); }
                if (Model.getFacade().isAComponentInstance(me)) { removeNode(me); }
                if (Model.getFacade().isAClass(me)) { removeNode(me); }
                if (Model.getFacade().isAInterface(me)) { removeNode(me); }
                if (Model.getFacade().isAObject(me)) { removeNode(me); }
                if (Model.getFacade().isAAssociation(me)) { removeEdge(me); }
                if (Model.getFacade().isADependency(me)) { removeEdge(me); }
                if (Model.getFacade().isALink(me)) { removeEdge(me); }
                if (Model.getFacade().isAPackage(me)) { removeEdge(me); }
            }
        }
    }
    
    /**
     * Construct and add a new edge of the given kind and connect
     * the given ports.
     *
     * @param fromPort   The originating port to connect
     *
     * @param toPort     The destination port to connect
     *
     * @param edgeType  The type of edge to create. This is one of the types
     *                  returned by the methods of
     *                  <code>org.argouml.model.MetaTypes</code>
     *
     * @return           The type of edge created (the same as
     *                   <code>edgeClass</code> if we succeeded,
     *                   <code>null</code> otherwise)
     */
    public Object connect(Object fromPort, Object toPort, Object edgeType){
        Editor curEditor = Globals.curEditor();
        ModeManager modeManager = curEditor.getModeManager();
        Mode mode = (Mode) modeManager.top();
        Hashtable args = mode.getArgs();
        String type = (String) args.get("dbtype");
        if (type != null && type.equals("DBDepend")) {
            if (DBModelFacade.getInstance().representsASchema(fromPort) &&
                    DBModelFacade.getInstance().representsADatabase(toPort)) {
                Object connection = super.connect(fromPort, toPort, edgeType);
                CreateActions.createDBDependency(fromPort, toPort, connection);
                return connection;
            } else {
                return null;
            }
        }
        return super.connect(fromPort, toPort, edgeType);
        
    }
    
    static final long serialVersionUID = 1003748292917485298L;
    
} /* end class DeploymentDiagramGraphModel */