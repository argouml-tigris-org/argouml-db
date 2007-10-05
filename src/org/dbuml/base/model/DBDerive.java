/**********************************************
*   Copyright (C) 2007 CINCOM SYSTEMS, INC.
*   All Rights Reserved
**********************************************/

package org.dbuml.base.model;

/**
 * Class for modeling a view derive dependency.
 * @author jgrengbondai
 */
public class DBDerive extends DBAbstractDepend {
     
    
    /** Creates a new instance of DBDerive */
    public DBDerive() {
        super();
    }
    
    /**
     * Create a view derive from a dependency model.
     * @param dependencyModel The dependency model.
     */
    public DBDerive(final Object dependencyModel) {
        super(dependencyModel);
    }
    
    /**
     * Creates a new derive dependency.
     * @param dependencyModel The dependency model.
     * @param srcModel The source model.
     * @param tModel The terget model.
     */
    public DBDerive(final Object dependencyModel, final Object srcModel, 
            final Object tModel) {
        super(dependencyModel, srcModel, tModel);
    }
    
    /**
     * Gets the sterotype.
     * @return The stereotype.
     */
    public static String getStereotype() {
        return "Derive";
    }
    
    /**
     * Gets the stereotype.
     * @return The stereotype string.
     */
    public String getStereostring() {
        return getStereotype();
    }
    
}
