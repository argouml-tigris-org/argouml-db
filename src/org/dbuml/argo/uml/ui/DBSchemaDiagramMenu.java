/**********************************************
 *   Copyright (C) 2007 CINCOM SYSTEMS, INC.
 *   All Rights Reserved
 **********************************************/
package org.dbuml.argo.uml.ui;

import org.argouml.uml.ui.ActionAddDiagram;
import org.argouml.uml.diagram.ui.UMLDiagram;

import org.dbuml.argo.uml.diagram.ui.DBSchemaDiagram;

public class DBSchemaDiagramMenu extends ActionAddDiagram {
    
    
    /**
     * 
     * @param sDiagName 
     */
    public DBSchemaDiagramMenu(String sDiagName) { super(sDiagName); }
    
    public DBSchemaDiagramMenu() { super("DBSchemaDiagramMenu"); }
    
    //
    //  We extend ActionAddDiagram who handles the action method and calls our
    //  creatDiagram method
    //
    /**
     * 
     * @param ns 
     * @return 
     */
    public UMLDiagram createDiagram(Object ns) {
        return new DBSchemaDiagram(ns);
    }
    
    /**
     * Test if the given namespace is a valid namespace to add the diagram to.
     *
     * @param ns the namespace to check
     * @return Returns <code>true</code> if valid.
     * todo:  do we need to do valid checking?
     */
    public boolean isValidNamespace(Object ns) {
        return true;
    }
    
    
    
}
