/**********************************************
 *   Copyright (C) 2007 CINCOM SYSTEMS, INC.
 *   All Rights Reserved
 **********************************************/

package org.dbuml.base.model;

/**
 * A facade class for DBUML models.
 * @author jgunderson
 */

public abstract class DBModelFacade implements DBModelFacadeInterface {
    /**
     * The DBModel Facade instance.
     */
    protected static DBModelFacade instance;
    
    /** Creates a new instance of DBModelFacade */
    public DBModelFacade() {
    }
    
    /**
     * Gets instance.  This is set by the implementing class.
     * @return The the implementation of DBModelFacade.
     */
    public static DBModelFacade getInstance() {
        return instance;
    }
    
    
}
