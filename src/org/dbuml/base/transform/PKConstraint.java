/**********************************************
 *   Copyright (C) 2007 CINCOM SYSTEMS, INC.
 *   All Rights Reserved
 **********************************************/

package org.dbuml.base.transform;

import java.util.Iterator;
import java.util.Vector;

/**
 * Helper class for generating primary key statement in velocity.
 * @author  jgunderson
 */

public class PKConstraint {
    private String pkName;
    private Vector pkColumnNames = new Vector(256);
    
    /**
     * Creates a new PKConstraint.
     */
    public PKConstraint() {
    }
    
    //
    //  Get methods needed for Velocity
    
    /**
     * Gets the primary key name.
     * @return The primary key name.
     */
    public String getPkName() { return pkName; }
    
    /**
     * Sets the primary key name.
     * @param name The primary key name.
     */
    public void setPkName(String name) { 
        pkName = name;
    }
    /**
     * Gets the names of the columns that make up the primary key.
     * @return A Vector of names.
     */
    public Vector getPkColumnNames() { return pkColumnNames; }
    
    /**
     * Gets concatenated and ordered list of column names.
     * @param dQuoted boolean for whether or not to wrap the names with the 
     * preferred quote string.
     * @return The concatenated names.
     */
    public String getColumnNames(boolean dQuoted) {
        Iterator it = this.pkColumnNames.iterator();
        StringBuffer buf = new StringBuffer("");
        if (it.hasNext()) {
            if (dQuoted) { buf.append('"'); }
            buf.append((String) it.next());
            if (dQuoted) { buf.append('"'); }
        }
        while (it.hasNext()) {
            buf.append(',');
            if (dQuoted) { buf.append('"'); }
            buf.append((String) it.next());
            if (dQuoted) { buf.append('"'); }
        }
        return buf.toString();
    }
    
}


