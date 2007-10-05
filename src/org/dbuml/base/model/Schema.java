/**********************************************
 *   Copyright (C) 2007 CINCOM SYSTEMS, INC.
 *   All Rights Reserved
 **********************************************/

package org.dbuml.base.model;

import java.util.Properties;

/**
 *
 * @author  jgunderson
 */

/**
 * Class for modeling the database schema.
 */
public class Schema extends DBElement {
    /**
     * The stereotype string.
     */
    public static final String STEREOSTRING = "Schema";
    /** Creates a new instance of Schema */
    public Schema() {
        super();
    }
    
    /**
     * Creates a new instance of Schema with a given name
     *  Used wehn adding a new element to the model.
     * @param sName The schema name.
     */
    public Schema(final String sName) {
        super(sName);
    }
    
    /**
     *
     * Creates a new instance of with a given name, model element,
     * and properties.  Used when retrieving elements.
     * @param sName The schema name.
     * @param objModel The object model.
     * @param properties The properties.
     */
    public Schema(final String sName, final Object objModel, 
            final Properties properties) {
        super(sName, objModel, properties);
    }
    
    /**
     * Get the Stereotype string
     * @return The Stereotype string
     */
    public String getStereostring() {
        return STEREOSTRING;
    }
    
    
}
