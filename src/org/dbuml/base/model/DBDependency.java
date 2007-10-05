/**********************************************
*   Copyright (C) 2007 CINCOM SYSTEMS, INC.
*   All Rights Reserved
**********************************************/

package org.dbuml.base.model;

/**
 * Class for modeling a dependency.
 * @author jgrengbondai
 */
public class DBDependency extends DBAbstractDepend {
    
    /** Creates a new instance of DBDependency */
    public DBDependency() {
        super();
    }
    
    /**
     * Creates a dependency from a model.
     * @param dependencyModel A dependency model.
     */
    public DBDependency(final Object dependencyModel) {
        super();
        setModelElement(dependencyModel);
    }
    
    /**
     * Creates a new Dependency.
     * @param dependencyModel The dependency model.
     * @param sourceModel The source model.
     * @param targetModel The terget model.
     */
    public DBDependency(final Object dependencyModel, final Object sourceModel,
            final Object targetModel) {
        super();
        setModelElement(dependencyModel, sourceModel, targetModel);
    }
    
    /**
     * Gets the sterotype.
     * @return The stereotype.
     */
    public static String getStereotype() {
        return "Dependency";
    }
    
    /**
     * Gets the stereotype.
     * @return The stereotype string.
     */
    public String getStereostring() {
        return getStereotype();
    }
    
}
