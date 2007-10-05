/**********************************************
 *   Copyright (C) 2007 CINCOM SYSTEMS, INC.
 *   All Rights Reserved
 **********************************************/

package org.dbuml.argo.uml.ui;

public class CommandEdge extends org.argouml.ui.CmdSetMode {
    
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
        _modeArgs.put("dbtype", type);
        _modeArgs.put("unidirectional", Boolean.TRUE);
    }
}
