/**********************************************
 *   Copyright (C) 2007 CINCOM SYSTEMS, INC.
 *   All Rights Reserved
 **********************************************/

package org.dbuml.base.controller;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.dbuml.base.model.DBDependency;
import org.dbuml.base.model.DBModelFacade;
import org.dbuml.base.model.Registry;
import org.dbuml.base.model.Database;
import org.dbuml.base.model.Schema;
import org.dbuml.base.model.Table;
import org.dbuml.base.model.View;
import org.dbuml.base.model.Column;
import org.dbuml.base.model.PKey;
import org.dbuml.base.model.FKey;
import org.dbuml.base.model.DBDerive;

/**
 * Internal actions handler for Create actions.  These actions do not have
 * a dependency on the Database used as they are not in the context of
 * any Database when first created.
 * All Create actions are generic for all databases.
 */
public class CreateActions {
    
    private static final String VARCHAR = "VARCHAR";
    
    /**
     * Creates a new <code>Registry</code>.
     * @param node The registry object model. 
     * @return An instance of <code>Registry</code>.
     */
    public static Object createRegistry(Object node) {
        Registry registry = new Registry("new_reg");
        registry.setModelElement(node);
        DBModelFacade.getInstance().updateRegistry(registry);
        return node;
    }
    
    /**
     * Creates a new <code>Database</code>.
     * @param node The database object model. 
     * @return An instance of <code>Database</code>.
     */
    public static Object createDatabase(Object node) {
        Database database = new Database("new_db");
        database.setModelElement(node);
        DBModelFacade.getInstance().updateDatabase(database);
        return node;
    }
    
    /**
     * Creates a new <code>Schema</code>.
     * @param node The Schema object model. 
     * @return An instance of <code>Schema</code>.
     */
    public static Object createSchema(Object node) {
        Schema schema = new Schema("new_sch");
        schema.setModelElement(node);
        DBModelFacade.getInstance().updateSchema(schema);
        return node;
    }
    
    /**
     * Creates a new <code>Table</code>.
     * @param node The Table object model. 
     * @return An instance of <code>Table</code>.
     */
    public static Object createTable(Object node) {
        Table table = new Table("new_tbl");
        table.setModelElement(node);
        DBModelFacade.getInstance().updateTable(table);
        return node;
    }
    
    /**
     * Creates a new <code>View</code>.
     * @param node The View object model. 
     * @return An instance of <code>View</code>.
     */
    public static Object createView(Object node) {
        View view = new View("new_view");
        view.setModelElement(node);
        DBModelFacade.getInstance().updateView(view);
        return node;
    }
    
    /**
     * Creates a new <code>Column</code>.
     * @param node The Column object model. 
     * @return An instance of <code>Column</code>.
     */
    public static Object createColumn(Object node) { 
        Column column = new Column("new_col");
        column.setTypeNameJdbc(VARCHAR);
        column.setModelElement(node);
        DBModelFacade.getInstance().updateColumn(column);
        return node;
    }
    
    /**
     * Creates a new <code>PKey</code>.
     * @param node The PKey object model. 
     * @return An instance of <code>PKey</code>.
     */
    public static Object createPKEY(Object node) {
        Column column = new Column("new_col");
        column.setTypeNameJdbc(VARCHAR);
        column.setModelElement(node);
        PKey key = new PKey("", "", "new_col", (short) 1,
                "new_pk_constraint");
        column.setKey(key);
        DBModelFacade.getInstance().updateColumn(column);
        return node;
    }
    
    
    /**
     * Creates a new <code>PKey</code>.
     * @param source The source model. 
     * @param target The target model.
     * @param assoc The association model.
     * @return An instance of <code>PKey</code>.
     */
    public static Object createFKEY(Object source, Object target,
            Object assoc) {
        //  Get the source and target Table and Schema
        
        Table sourceTable = DBModelFacade.getInstance().getTable(source);
        Schema sourceSchema = DBModelFacade.getInstance().getSchema(
                DBModelFacade.getInstance().getParent(sourceTable));
        String sourceSchemaName = null;
        if (sourceSchema != null) {
            sourceSchemaName = sourceSchema.getName();
        }
        Table targetTable = DBModelFacade.getInstance().getTable(target);
        Schema targetSchema = DBModelFacade.getInstance().getSchema(
                DBModelFacade.getInstance().getParent(targetTable));
        String targetSchemaName = null;
        if (targetSchema != null) {
            targetSchemaName = targetSchema.getName();
        }
        
        //  Make the new primary key and add it to the target Table
        
        Column pkColumn = new Column("new_pk");
        pkColumn.setTypeNameJdbc(VARCHAR);
        PKey pkey = new PKey("", "", "new_pk", (short) 1, "");
        pkColumn.setKey(pkey);
        targetTable.addColumn(pkColumn);
        DBModelFacade.getInstance().updateTable(targetTable);
        
        
        //  Make the Foreign Key Column and add it to the source Table
        
        Column fkColumn = new Column("new_fk");
        fkColumn.setTypeNameJdbc(VARCHAR);
        FKey fkey = new FKey(sourceSchemaName, sourceTable.getName(),
                fkColumn.getName(), (short) 1, "new_fk_constraint");
        fkey.setNativeData(targetSchemaName, targetTable.getName(),
                pkColumn.getName(), "new_pk_constratin");
        fkColumn.setKey(fkey);
        DBModelFacade.getInstance().addColumn(fkColumn, source);
        
        //  Update the association given to make it a foreign key assocation
        
        fkey.setModelElement(assoc, source, target);
        fkey.setSourceEndStereoString("PK");
        DBModelFacade.getInstance().updateFKey(fkey);
        
        return assoc;
    }
    
    /**
     * Creates an instance of <code>DBDerive</code>. 
     * @param source The source model. 
     * @param target The target model.
     * @param dep The dependency model.
     * @return An instance of <code>DBDerive</code>.
     */
    public static DBDerive createDBDerive(
            Object source, Object target, Object dep) {
        DBDerive derive = new DBDerive(dep, source, target);
//        DBDerive derive = new DBDerive(dep);
        derive.setName(DBModelFacade.getInstance().getName(
                source) + "->" + DBModelFacade.getInstance().getName(target));
        DBModelFacade.getInstance().updateDBDerive(derive);
        return derive;
    }
    
    /**
     * Creates an instance of <code>DBDependency</code>. 
     * @param source The source model. 
     * @param target The target model.
     * @param dep The dependency model.
     * @return An instance of <code>DBDependency</code>.
     */
    public static DBDependency createDBDependency(Object source,
            Object target, Object dep) {
        DBDependency depend = new DBDependency(dep, source, target);
        DBModelFacade.getInstance().updateDBDependency(depend);
        return depend;
    }
    
    /**
     * Adds a not null contraint. 
     * @param tableModel The table model. 
     * @param choices An array of column names.
     */
    protected static void createConstraintNotNull(Object tableModel,
            String[] choices) {
        if (choices != null) {
            // TO DO: Make sure that context item is a Table.
            for (int i = 0; i < choices.length; i++) {
                DBModelFacade.getInstance().addConstraintNotNull(
                        choices[i], tableModel);
            }
        }
        
    }
    
    /**
     * Adds a unique constraint.
     * @param table The table model.
     * @param choices An array of column names.
     * @param indexName The index name.
     */
    protected static void createConstraintUnique(Table table,
            String[] choices,
            String indexName) {
        //
        if (choices != null && choices.length >= 1) {
        
            Set setOfColumnNames = new HashSet();
            for (int i = 0; i < choices.length; i++) {
                setOfColumnNames.add(choices[i]);
            }
            DBModelFacade.getInstance().addConstraintUnique(
                    table.getModelElement(),
                    setOfColumnNames,
                    indexName);
            
            // update the index
            for (int i = 0; i < choices.length; i++) {
                Column col = table.getColumn(choices[i]);
                col.setIndexInfo(indexName);
                DBModelFacade.getInstance().updateColumn(col);
            }
        }
        
    }
    
    /** It creates and adds Column to into the specified view.
     *@param viewModel Object representing the view model.
     *@param allBasesColumnsMap A map of all the columns in all base tables
     * or views. The key is the base name followed by a '.' and the
     * column name, that is, baseName.columnName and the value
     * is an instance of Column object defined in the base table.
     *@param choices An array of String that represents a key for the column
     * name to be added into the view. The string must follow the pattern 
     * specified in the Map key mentioned above.
     */
    public static void createColumnForView(Object viewModel,
            Map allBasesColumnsMap, String[] choices) {
        // Create view attributes in the order that the user selected.
        for (int i = 0; i < choices.length; i++) {
            final Column tCol = (Column) allBasesColumnsMap.get(choices[i]);
            final Column viewCol = new Column(tCol.getName());
            viewCol.setTypeName(choices[i]);
            viewCol.setJdbcType(tCol.getJdbcType());
            DBModelFacade.getInstance().addColumn(viewCol, viewModel, true);
        }
    }

    
}
