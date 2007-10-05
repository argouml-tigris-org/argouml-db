/**********************************************
 *   Copyright (C) 2007 CINCOM SYSTEMS, INC.
 *   All Rights Reserved
 **********************************************/
package org.dbuml.base.controller;

/** Interface for DBUML figure actions.
 *
 * @author  jgrengbondai
 */
public interface FigureActionsInterface extends ActionsInterface {
    
    /**
     * Imports database tables into the model.
     * @param schemaModel The schema model.
     */
    public void importTablesFromCatalog(Object schemaModel);
    
    /**
     * Updates the database with SQL DDL derived from the model.
     * @param selectedItem The model object.
     */
    public void updateCatalog(Object selectedItem);
    
    /**
     * Generates an SQL statement for the selected object model
     * @param selectedItem The model object.
     */
    public void generateSource(Object selectedItem);  //sbalda, Dec 2 2005
    
    /**
     * Imports database schema into the model.
     * @param selectedItem The parent model of the schema.
     */
    public void importSchemas(Object selectedItem);
    
    /**
     * Adds not null constraint to a table.
     * @param tableModel The table model.
     */
    public void createConstraintNotNull(Object tableModel);
    
    /**
     * Adds unique constraint to a table.
     * @param tableModel The table model.
     */
    public void createConstraintUnique(Object tableModel);
    
    /**
     * Import database views into the model.
     * @param schemaModel The schema model.
     */
    public void importViews(Object schemaModel);
    
    /**
     * Adds a column to a view.
     * @param viewModel The view model object.
     */
    public void createColumnForView(Object viewModel);
    
    /**
     * Imports databases into the model.
     * @param selectedItem The parent model object.
     */
    public void importDatabases(Object selectedItem);
    
}
