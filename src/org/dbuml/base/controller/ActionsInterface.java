/**********************************************
*   Copyright (C) 2007 CINCOM SYSTEMS, INC.
*   All Rights Reserved
**********************************************/
package org.dbuml.base.controller;


import java.io.File; //sbalda dec 1 2005

/**
 * Interface for DBUML model actions.
 * @author  jgunderson
 */
public interface ActionsInterface {
    

    /**
     * Connects to JDBC database.
     * @param selectedItem An object model for the database. 
     */
    public void connect(Object selectedItem); 
    
    /**
     * Connect to registry defined by Registry element. Generic JDBC 
     * does not support registries.
     * @param selectedItem An object model for the registry.
     */
    public void connectRegistry(Object selectedItem); 
    
    //
    //  generateCreateSQL is called by GeneratorSQL (and by GUI actions?).
    //  GeneratorSQL may be called in non-interactive context like by
    // TabSrc or by
    //  ClassGeneratorDialog so it does not respond with GUI errors but 
    // returns error
    //  strings,
    //
    /**
     * Generates SQL statement for the given object model.
     * @param handle The object model. 
     * @param sDialect A value that tells what type of SQL statement to
     * create.
     * </p>Example:
     * </p><code>createAlterSQL</code> to generate alter statements.
     * </p><code>createTopSQL</code> to generate create statements.
     * </p><code>dropSQLTables(/code> to generate drop table drop statements.
     * </p><code>dropSQLViews</code> to generate drop table statements.
     * </p>createAlterConstraintsSQL to generate alter statement when for
     * constraints.
     * @return An SQL statement. 
     */
    public String generateSQL(Object handle, String sDialect); 

    //
    //  Generate SQL for element recursively
    //
    /**
     * Generates SQL for element recursively.
     * @param handle The object model. 
     * @param sDialect A value that tells what type of SQL statement to
     * create.
     * </p>Example:
     * </p><code>createAlterSQL</code> to generate alter statements.
     * </p><code>createTopSQL</code> to generate create statements.
     * </p><code>dropSQLTables(/code> to generate drop table drop statements.
     * </p><code>dropSQLViews</code> to generate drop table statements.
     * </p>createAlterConstraintsSQL to generate alter statement when for
     * constraints.
     * @return An SQL statement.
     */
    String generateSQLDeep(Object handle, String sDialect); 
    
    /**
     * Browse instances of selected table or view model.
     * @param selectedItem A table or view model.
     */
    public void browseInstances(Object selectedItem);  
    
    /**
     * Imports database schemas into the model.
     * @param selectedItem The database model. 
     * @param choices An array of schema names.
     * @param batch A boolean flag for whether or not this method is being
     * invoked in a batch mode.
     */
    void importSchemas(Object selectedItem, String[] choices, boolean batch);  
    
    /**
     * Imports database tables into the model.
     * @param schemaModel  The schema model object.
     * @param choices An array of table names.
     * @param batch A boolean flag for whether or not this method
     * is being invoked in a batch mode.
     */
    void importTablesFromCatalog(Object schemaModel, String[] choices,
            boolean batch);
    
    /**
     * Imports database views into the model.
     * @param schemaModel  The schema model object.
     * @param choices An array of view names.
     * @param batch A boolean flag for whether or not this method
     * is being invoked in batch mode.
     */
    void importViews(Object schemaModel, String[] choices, boolean batch);
       
    /**
     * Imports database attributes types into the model.
     * @param selectedItem The database model.
     */
    public void importAttributeTypes(Object selectedItem);
    
    /**
     * Updates the database with SQL DDL derived from the model.
     * @param selectedItem The model object. 
     * @param update A flag for whether or not to update the entity. 
     */
    void updateCatalog(Object selectedItem, boolean update);
    
    /**
     * Updates the database with SQL DDL derived from the model.
     * @param selectedItem The model to update.
     * @throws Exception When there is an error.
     */
    void updateCatalogUpdate(Object selectedItem) throws Exception; 
    
    /**
     * Replaces the database with SQL DDL derived from the model.
     * @param selectedItem The model to replace.
     * @throws java.sql.SQLException When there is an error in the database. 
     */
    void updateCatalogReplace(Object selectedItem) 
        throws java.sql.SQLException;
    
    /**
     * Generates SQL statements for the specified object model.
     * @param selectedItem The object model.
     * @param fileName The file name to write the SQL statement into.
     */
    public void generateSource(Object selectedItem, File fileName);
    
    /**
     * Generates complete SQL statements for the specified object model.
     * @param selectedItem The object model.
     * @param includeDrop boolean flag for whether or not to include drop
     * statements the generated SQL.
     * @param includeAlter boolean flag for whether or not to include alter 
     * statements the generated SQL.
     * @return The SQL string.
     */
    public String generateSource(Object selectedItem,
                                 boolean includeDrop,
                                 boolean includeAlters);
    
    /**
     * Imports databases into the model.
     * @param selectedItem The parent model.
     * @param choices The names of the database. 
     * @param batch A boolean flag for whether or not this method is being
     * invoked in batch mode.
     */
    void importDatabases(Object selectedItem, String[] choices, boolean batch); 

}
