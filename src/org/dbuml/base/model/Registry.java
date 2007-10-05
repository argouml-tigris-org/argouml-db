/**********************************************
 *   Copyright (C) 2007 CINCOM SYSTEMS, INC.
 *   All Rights Reserved
 **********************************************/
package org.dbuml.base.model;

import java.util.Properties;
import org.dbuml.base.database.DBMetadataCache;

/**
 * @author  jgunderson
 */


/**
 * Class for modeling a Registry.
 */
public class Registry extends DBElement {
    /**
     * The static stereotype string.
     */
    public static final String STEREOSTRING = "Registry";
    
    /**
     * The URL label.
     */
    public static final String URL            = "Url"; //Added by RGupta
    /**
     * The factory label.
     */
    public static final String FACTORY        = "Factory"; //Added by RGupta
    
    /**
     * Creates a new <code>Registry</code>.
     */
    public Registry() {
        this(null);
    }
    
    /**
     * Creates a new instance of Registry with a given name
     *  Used when adding elements to model.
     * @param sName The name of the registry.
     */
    public Registry(final String sName) {
        super(sName);
        setProperty("Url", 
            "http://localhost:8080/ogsa/services/ogsadai/DAIServiceGroupRegistry");
        setProperty("Factory", "org.dbuml.base.factory.DAISFactory");
    }
    
    /**
     *
     * Creates a new instance of with a given name, model element, owner model
     * element and properties.  Used when retrieving elements.
     * @param sName The name of the registry.
     * @param objModel The object model.
     * @param properties The properties.
     */
    public Registry(final String sName, final Object objModel, 
            final Properties properties) {
        this(sName);
        this._objModel = objModel;
        this.setProperties(properties);
    }
    
    /**
     * Get the Stereotype string
     * @return The Stereotype string
     */
    public String getStereostring() {
        return STEREOSTRING;
    }
    
    /**
     * Are we connected to the database?
     * @return true when we connected to the database, false otherwise.
     */
    public  boolean isConnected() {
        return (DBMetadataCache.getDBMetadata(this) != null);
    }
    
}
