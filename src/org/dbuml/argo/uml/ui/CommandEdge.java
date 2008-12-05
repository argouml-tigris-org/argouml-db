/**********************************************
 *   Copyright (C) 2007 CINCOM SYSTEMS, INC.
 *   All Rights Reserved
 **********************************************/

package org.dbuml.argo.uml.ui;

import org.argouml.uml.diagram.ui.ActionSetMode;

public class CommandEdge extends ActionSetMode {
    
    /**
     * 
     * @param eClass 
     * @param eString 
     * @param mClass 
     * @param mString 
     * @param type 
     */
    public CommandEdge(Class eClass, String eString, Class mClass, String mString, String type) {
        super(eClass, eString, mClass, mString);
        modeArgs.put("dbtype", type);
        modeArgs.put("unidirectional", Boolean.TRUE);
    }
}
