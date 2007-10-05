/**********************************************
*   Copyright (C) 2007 CINCOM SYSTEMS, INC.
*   All Rights Reserved
**********************************************/

package org.dbuml.base.model;

/**
 * Abstract class for modeling a dependency.
 * @author jgrengbondai
 */
public abstract class DBAbstractDepend extends DBElement {
    /**
    * Model element for the source dependency.
    */
    protected Object sourceModel;
    
    /**
     * Model element for the target dependency.
     */
    protected Object targetModel;
    
    /** Creates a new instance of DBDepend */
    public DBAbstractDepend() {
        super();
    }
    
    /**
     * Creates a new dependency from a model.
     * @param dependencyModel The dependency model.
     */
    public DBAbstractDepend(final Object dependencyModel) {
        super();
        setModelElement(dependencyModel);
    }
    
    /**
     * Creates a new dependency.
     * @param dependencyModel The dependency model.
     * @param sModel The source model.
     * @param tModel The terget model.
     */
    public DBAbstractDepend(final Object dependencyModel,
            final Object sModel,
            final Object tModel) {
        super();
        setModelElement(dependencyModel, sModel, tModel);
    }
    
    /**
     * Sets the model elements.
     * @param dependencyModel The dependency model.
     * @param srcModel The source model.
     * @param trgModel The target model.
     */
    public void setModelElement(final Object dependencyModel,
            final Object srcModel,
            final Object trgModel) {
        setModelElement(dependencyModel);
        this.sourceModel = srcModel; 
        this.targetModel = trgModel; 
    }
    
    /**
     * Gets the source model.
     * @return The source model.
     */
    public Object getSourceModel() {
        return this.sourceModel;
    }
    
    /**
     * Gets the target model.
     * @return The target model.
     */
    public Object getTargetModel() {
        return this.targetModel;
    }
    
}
