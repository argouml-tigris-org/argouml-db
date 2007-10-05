/**********************************************
 *   Copyright (C) 2007 CINCOM SYSTEMS, INC.
 *   All Rights Reserved
 **********************************************/

package org.dbuml.base.transform;

import org.dbuml.base.model.DBModelFacade;
import org.dbuml.base.model.Schema;
import org.dbuml.base.model.Table;
import org.dbuml.base.model.View;
import org.dbuml.base.model.Database;
import org.dbuml.base.database.DBMetadata;

/**
 * 
 * Class for transforming database entities into DBUML classes using 
 * standard jdbc.
 * @author jgunderson
 */
public class GenericDBMetadataToDBUML extends DBMetadataToDBUML {
    
    /** Creates a new instance of DBMetadataToDBUML */
    public GenericDBMetadataToDBUML() {
    }
    
    /**
     * Transforms a database table into DBUML <code>Table</code>.
     * @param name The table name.
     * @param schema The schema name.
     * @param dbmd The database metadata.
     * @return The DBUML table.
     * @throws java.sql.SQLException When there is a database error.
     */
    public Table table(String name, Schema schema, DBMetadata dbmd)
        throws java.sql.SQLException {
        Table table = dbmd.getTable(name, schema.getName());
        DBModelFacade.getInstance().addTable(table, schema.getModelElement());
        return table;
    }
    
    /**
     * Transforms a database view into DBUML <code>View</code>.
     * @param name The view name.
     * @param schema The schema name.
     * @param dbmd Teh database metadata.
     * @throws java.sql.SQLException When there is a database errror.
     * @return The dbuml View.
     */
    public View view(String name, Schema schema, DBMetadata dbmd) 
        throws java.sql.SQLException {
        View view = dbmd.getView(name, schema.getName());
        DBModelFacade.getInstance().addView(view, schema.getModelElement());
        return view;
    }
    
    /**
     * Updates a DBUML table from a model.
     * @param objTable The table model.
     * @param schemaName The schema name.
     * @param dbmd The metadata
     * @throws java.sql.SQLException When there is a database error.
     */
    public void updateTable(Object objTable, String schemaName, DBMetadata dbmd)
        throws java.sql.SQLException {
        Table table = dbmd.getTable(
                DBModelFacade.getInstance().getName(objTable), schemaName);
        table.setModelElement(objTable);
        DBModelFacade.getInstance().updateTable(table );
    }
    
    /**
     * Updates a DBUML view from a model.
     * @param objTable The view model.
     * @param schemaName The schema name.
     * @param dbmd The metadata.
     * @throws java.sql.SQLException Whene there is a database error.
     */
    public void updateView(Object objTable, String schemaName, DBMetadata dbmd)
        throws java.sql.SQLException {
        View view = dbmd.getView(
                DBModelFacade.getInstance().getName(objTable), schemaName);
        view.setModelElement(objTable);
        DBModelFacade.getInstance().updateView(view );
    }
    
    /**
     * Transforms a database schema into DBUML <code>Schema</code>.
     * @param owner A database model.
     * @param name The schema name.
     * @param dbmd The metadata.
     * @return the DBUML schema.
     */
    public Schema schema(Object owner, String name, DBMetadata dbmd) {
        //
        //  Currently there is nothing to get from DBMetadata so we don't need 
        // a getSchema( )
        //
        Schema schema = new Schema(name);
        DBModelFacade.getInstance().addSchema(schema, owner);
        return schema;
    }
    
    /**
     * Updates a DBUML schema from a model.
     * @param model schema model.
     */
    public void updateSchema(Object model) {
        // we really have noting to update, but proceed any way
        // just in case we have anything to update in the future.
        Schema schema = new Schema(DBModelFacade.getInstance().getName(model));
        schema.setModelElement(model);
        DBModelFacade.getInstance().updateSchema(schema);
    }
    
    /**
     * Transforms a database into DBUML <code>Database</code>.
     * @param owner The parent model.
     * @param name The database name.
     * @param dbmd The metadata.
     * @throws java.lang.Exception When there is an error.
     * @return The DBUML <code>Database</code>.
     */
    public Database database(Object owner, String name, DBMetadata dbmd)
        throws java.lang.Exception {
        //
        //  Currently there is nothing to get from DBMetadata so we don't need 
        // a getSchema( )
        //
        Database database = dbmd.getDatabase(name);
        DBModelFacade.getInstance().addDatabase(database, owner);
        return database;
    }
    
    /**
     * Updates a DBUML database from a model.
     * @param model The database model.
     * @param dbmd Teh metadata.
     * @throws java.lang.Exception When there is an error.
     */
    public void updateDatabase(Object model, DBMetadata dbmd)
        throws java.lang.Exception {
        // we really have noting to update, but proceed any way
        // just in case we have anything to update in the future.
        Database database = dbmd.getDatabase(
                DBModelFacade.getInstance().getName(model));
        database.setModelElement(model);
        DBModelFacade.getInstance().updateDatabase(database);
    }
    
    
}
