/**********************************************
 *   Copyright (C) 2007 CINCOM SYSTEMS, INC.
 *   All Rights Reserved
 **********************************************/

package org.dbuml.base.model;

import java.util.Collection;
import org.dbuml.base.factory.Factory;

/**
 * Interface for DBModelFacade.
 * @author jgunderson
 */
public interface DBModelFacadeInterface {
    
    /**
     * Get the "Parent" element that "owns" this element. A Database model does
     * not have parent.
     * @param element The model element.
     * @return a Model element Object or null.
     */
    public Object getParent(Object element);
    
    /**
     * Gets the element name.
     * @param element The model element.
     * @return a Model element name.
     */
    public String getName(Object element);
    
    /**
     * Gets the element type.
     * @param element The model element.
     * @return a Model element type.
     */
    public Object getType(Object element);
    
    /**
     * Returns a named object in the given object by calling it's lookup method.
     *
     * @param handle the object that we search through
     * @param name of the model element
     * @return found object, null otherwise
     */
    Object lookupIn(Object handle, String name);
    
    /**
     * Gets the child elements of this element.
     * @param element The model element.
     * @return a Collection of children.
     */
    public Collection getChildren(Object element);
    
    /**
     * Gets the child elements of this element that should be updated in a
     * catalog update.
     * @param element The model element.
     * @return a Collection of children.
     */
    public Collection getChildrenForUpdate(Object element);
    
    
    /**
     * Gets the Database element that "owns" this element
     * @param element The model element.
     * @return Database Object.
     */
    public Database getOwningDatabase(Object element);
    
    /**
     * Gets the registry model element that "owns" this element and make a
     * Registry.
     * @param element The model element.
     * @return An instance of Registry.
     */
    public Registry getOwningRegistry(Object element);
    
    /**
     * Gets the Factory implementation for this element.  This is the Factory
     * for the owning Database or the Registry (if this is a Registry)
     * @param element The model element.
     * @return the Factory.
     */
    public Factory getMyFactory(Object element);
    
    
    /**
     *  Creates a new model element representing a table from a Table object
     * having a owner model element.
     * @param table An instance of <code>Table</code> to be added.
     * @param objOwner The model owner of this table which is most likely to be
     * a model of a schema.
     */
    public void addTable(Table table, Object objOwner);
    
    /**
     *  Creates a new model element representing a view from a View object
     * having a owner model element.
     * @param view An instance of the <code>View</code> to add.
     * @param objOwner The model owner.
     **/
    public void addView(View view, Object objOwner);
    
    /**
     *  Create a new model element representing a schema from a Schema object.
     * @param schema An instance of the <code>Schema</code> to add. 
     * @param owner The model owner. 
     */
    public void addSchema(Schema schema, Object owner);
    
    /**
     *  Create a new model element representing a database from a Database 
     * object.
     * @param database An instance of the <code>Database</code> to add. 
     * @param owner The model owner.
     **/
    public void addDatabase(Database database, Object owner);
    
    /**
     *  Updates a model element representing a table from a <code>Table</code>
     * object.
     * @param table A <code>Table</code> instance.
     */
    public void updateTable(Table table);
    
    /**
     *  Updates a model element representing a view from a <code>View</code>
     * object.
     * @param view A <code>View</code> instance.
     **/
    public void updateView(View view);
    
    /**
     *  Updates a model element representing a schema from a Schema object.
     * @param schema A <code>Schema</code> instance.
     **/
    public void updateSchema(Schema schema);
    
    /**
     *  Updates a model element representing a registry from a Registry object.
     * @param registry A <code>Registry</code> instance.
     **/
    public void updateRegistry(Registry registry);
    
    /**
     *  Updates a model element representing a database from a Database object.
     * @param database A <code>Database</code> instance.
     **/
    public void updateDatabase(Database database);
    
    /**
     *  Updates a model element representing a column from a Column object.
     * @param column A <code>Column</code> instance.
     **/
    public void updateColumn(Column column);
    
    /**
     *  Updates a model element representing a foreign key association.
     * @param fkey A <code>FKey</code> instance.
     **/
    public void updateFKey(FKey fkey);
    
    /**
     *  Updates a model element representing a Derive dependency.
     * @param derive A <code>DBDerive</code> instance.
     **/
    public void updateDBDerive(DBDerive derive);
    
    /**
     *  Updates a model element representing a schema dependency.
     * @param depend A <code>DBDependency</code> instance.
     **/
    public void updateDBDependency(DBDependency depend);
    
    /**
     *  Updates a model element representing an association.
     * @param dbAsso A <code>DBAssociation</code> instance.
     **/
    public void updateDBAssociation(DBAssociation dbAsso);
    
    /**
     * Makes <code>Schema</code> from model object.
     * @param handle The object model.
     * @return An <code>Schema</code> object.
     */
    public Schema getSchema(Object handle);
    
    /** Makes Database from model object.
     *
     * @param handle object model.
     * @return <code>Database</code>
     */
    public Database getDatabase(Object handle);
    
    /** Makes Registry from model object.
     *
     * @param handle object model.
     * @return Registry
     */
    public Registry getRegistry(Object handle);
    
    /** Makes Table from model object without associations.
     *
     * Used when getting tables that are related to a table by foreign keys,
     * etc. This prevents loops when there are circular associations
     *
     * @param handle object model.
     * @return Table A Table object.
     */
    public Table getTableNoAssoc(Object handle);
    
    /** Make Table from model object
     *
     * @param handle object model.
     * @return Table A table object.
     */
    public Table getTable(Object handle);
    
    /** Make View from model object
     *
     * @param handle object model.
     * @return View The view object.
     */
    public View getView(Object handle);
    
    /**
     * Makes Column from the model.  An option is provided to not
     *  get association columns (like foreign keys) to prevent
     *  circular reference loops.
     * @return Column
     * @param bAssoc A boolean value.
     * @param handle object model.
     */
    public Column getColumnPartially(Object handle, boolean bAssoc);
    
    /** Make DBElement from model object
     *
     * @param handle object model.
     * @return DBElement The dbuml element.
     */
    public DBElement getDBElement(Object handle);
    
    /** Recognizer for model elements representing Schemas
     *
     * @param handle object model.
     * @return true if model element represents Schemas
     */
    public boolean representsASchema(Object handle);
    
    /** Recognizer for model elements representing Databases
     *
     * @param handle object model.
     * @return true if model element represents Databases
     */
    public boolean representsADatabase(Object handle);
    
    /** Recognizer for model elements representing Registries
     *
     * @param handle object model.
     * @return true if model element represents Registries
     */
    public boolean representsARegistry(Object handle);
    
    /** Recognizer for model elements representing Tables
     *
     * @param handle candidate
     * @return true if model element represents a Table
     */
    public boolean representsATable(Object handle);
    
    /** Recognizer for model elements representing Views
     *
     * @param handle candidate
     * @return true if model element represents a View
     */
    public boolean representsAView(Object handle);
    
    /** Recognizer for model elements representing Columns
     *
     * @param handle candidate
     * @return true if model element represents a Column
     */
    public boolean representsAColumn(Object handle);
    
    
    /** Recognizer for any DBUML profile model element
     *
     * @param handle candidate
     * @return true if model element represents an element in the DBUML profile
     */
    public boolean representsADbumlElement(Object handle);
    
    
    /** The name of a model element
     *
     * @param handle that points out the object.
     * @return the name
     */
    public String getModelElementName(Object handle);
    
//    /**
//     *  Add attibutes to model element representing a table Columens in Table.
//     **/
//    public void addAttributes(Table table);
    
    /**
     *  Adds types to the package given.
     * @param types String array of types to add.
     * @param pkgName The name of the package into which the types are added.
     */
    public void addTypes(String[] types, String pkgName);
    
    /**
     *  Checks base Java types are in the model.
     * @param sTypes A string array of type names.
     * @return True when types are in the model, and false otherwise.
     */
    public boolean checkTypes(String[] sTypes);
    
    /** Recognizer for model elements representing a default Schemas
     *
     * @param handle candidate
     * @return true if model element represents Schemas
     */
    public boolean isDefaultSchema(Object handle);
    
    /**
     * Gets the database namespace.  This is usually (always?) the owning 
     * Schema name. But only if the database supports schemas as defined in
     * the owning database or
     * owning local database model element and only if it is not the default 
     * Schema. The default Schema means the name is not qualified.
     * @param element The model object.
     * @return The database namespace.
     */
    public String getDBNamespace(Object element);
    
    /**
     * Gets the owning Schema. But only if the database supports schemas as
     * defined in the owning database or if it is the default Schema.
     *
     * @param element The model object.
     * @return Schema
     */
    
    public Schema getOwningSchema(Object element);
    
    /**
     * Finds a model element with this name and make Database (if it
     * represents a Database)
     * @param sName the name of the database.
     * @return Database
     */
    public Database findDatabase(String sName);
    
    
    /**
     * Finds a model element with this name with the given parent and make
     * Database (if it represents a Database)
     * @param parentName The node name or null if the the database is not
     * within a node.
     * @param dbName the databse name.
     *
     * @return Database
     */
    public Database findDatabase( String parentName, String dbName);
    
    
    /**
     * Adds Column to a model elemen t.
     * @param column The <code>Column</code> object.
     * @param tableModel The model object of the table.
     */
    public void addColumn(Column column, Object tableModel);
    
    /**
     * Adds column to the table or view model.
     * @param column The <code>Column</code> to add.
     * @param tableModel The table or view model object.
     * @param forView Boolean flag. True for adding the column into a view,
     * and false for adding it into a table.
     */
    public void addColumn(Column column, Object tableModel, boolean forView);
    
    /**
     * Finds the identified child model within the parent model.
     * @return The child's model element or null if the child is not found.
     * @param name The name of the model.
     * @param parentModel The parent model object.
     */
    public Object findChild(String name, Object parentModel);
    
    /**
     * Adds Not Null for the column constraint on the specified table model.
     * @param columnName The column name.
     * @param tableModel The table model.
     */
    public void addConstraintNotNull(String columnName, Object tableModel);
    
    /** Add unique constraint for attributes on the given table model.
     *@param tableModel The table model
     *@param setOfColumnNames A set of attribute names
     *@param indexName Name of the index on the given columns.
     */
    public void addConstraintUnique(final Object tableModel, 
            final java.util.Set setOfColumnNames, String indexName);
    
    /**
     * Gets the collection of base tables or base views of the given view.
     * @param viewModel The view model object.
     * @return A collection object.
     */
    public Collection getViewBaseModels(Object viewModel);
    
    /**
     * Gets the DBUML class name such as Database, Schema, Table etc... of the
     * given model.
     * @param model The model object.
     * @return A DBUML class name such as Database, Schema, Table, Column, etc..
     */
    public String getDBUMLClassName(Object model);
    
}
