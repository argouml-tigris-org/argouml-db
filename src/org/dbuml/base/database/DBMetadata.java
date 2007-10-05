/**********************************************
 *   Copyright (C) 2007 CINCOM SYSTEMS, INC.
 *   All Rights Reserved
 **********************************************/

package org.dbuml.base.database;

import java.lang.reflect.Field;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;
import org.dbuml.base.model.DBElement;
import org.dbuml.base.model.Table;
import org.dbuml.base.model.View;
import org.dbuml.base.model.Database;
import org.dbuml.base.model.Column;

import org.apache.log4j.Logger;

/**
 * Class for wrapping calls to database metadata.
 * @author jgunderson
 */
public abstract class DBMetadata {
    
    private static final Logger LOG = Logger.getLogger(DBMetadata.class);
    /**
     * Model element for this metadata.
     */
    protected DBElement repository;     
    /**
     * The database connection.
     */
    protected Object connection; 
    /**
     * The jdbc DatabaseMatadata object.
     */
    protected Object dbmd;   
    /**
     * Collection of type names.
     */
    protected static Hashtable htTypesToNames;  // types
    
    static
    {
        //  Construct table of java.sql.types from the Class fields.  We assume
        //  it is JDBC 3.  Only use by JDBC repositories
        
        Field aFields[] = java.sql.Types.class.getDeclaredFields();
        htTypesToNames = new Hashtable();
        for (int i = 0; i < aFields.length; i++) {
            Integer id = new Integer(0);
            try {
                id = (Integer) aFields[i].get(null);
            } catch (Exception e) {
                LOG.info(e.getMessage());
            }
            htTypesToNames.put(id, aFields[i].getName());
        }
        
        //Add codes for JDBC 2 (that changed) 
        //in case a database still uses them.
        htTypesToNames.put(new Integer(9), "DATE");
        htTypesToNames.put(new Integer(10), "TIME");
        htTypesToNames.put(new Integer(11), "TIMESTAMP");
    }
    
    /**
     * Creates a new instance of DBMetadata.
     * @param rep The repository such as <code>Database</code>.
     * @param conn The jdbc connection.
     * @throws java.sql.SQLException When there is a database error.
     * @throws java.lang.ClassNotFoundException When a class is not found.
     * @throws java.lang.IllegalArgumentException When there is 
     * an illegal argument.
     */
    public DBMetadata(DBElement rep, Object conn)
        throws java.sql.SQLException, java.lang.ClassNotFoundException,
            java.lang.IllegalArgumentException {
        this.repository = rep;
        this.connection = conn;
    }
    
    /**
     * Gets table names from the specified schema.
     * @param sSchema The schema name.
     * @return An array of table names.
     * @throws java.sql.SQLException When there is an error in the database.
     */
    public abstract Object[] getTablesInSchema(String sSchema)
        throws java.sql.SQLException;
    
    /**
     * Gets view names from the specified schema.
     * @param sSchema The schema name.
     * @return An array of view names.
     * @throws java.sql.SQLException When there is an error in the database.
     */
    public abstract Object[] getViewsInSchema(String sSchema)
        throws java.sql.SQLException;
    
    /**
     * Gets schema names from the database.
     *
     * @return An array of table names.
     * @throws java.sql.SQLException When there is an error in the database.
     */
    public abstract Object[] getSchemas() throws java.sql.SQLException;
    
    /**
     * Gets the databases.
     * @return An array of table names.
     * @throws java.lang.Exception When there is an error.
     */
    public abstract Object[] getDatabases()
        throws java.lang.Exception;
    
    /**
     * Gets the specified database.
     * @param name The database name.
     * @return The <code>Database</code> instance.
     * @throws java.lang.Exception When there is an error.
     */
    public abstract Database getDatabase(String name)
        throws java.lang.Exception;
    
    /**
     * Gets a table.
     * @param name The table name.
     * @param schema The table's schema name.
     * @throws java.sql.SQLException When there is an error in the database.
     * @return An instance of <code>Table</code>.
     */
    public abstract Table getTable(String name, String schema)
        throws java.sql.SQLException;
    
    /**
     * Gets a view.
     * @return An instance of <code>View</code>.
     * @param viewName The view name.
     * @param schema The view's schema name.
     * @throws java.sql.SQLException When there is an error in the database.
     */
    public abstract View getView(String viewName, String schema)
        throws java.sql.SQLException;
    
    /** Creates a <code>Table</code> instance.
     * @param name The table name.
     * @return An instance of <code>Table</code>.
     */
    protected abstract Table newTable(String name);
    
    /** Creates a <code>View</code> instance.
     * @param name The view name.
     * @return An instance of <code>View</code>.
     */
    protected abstract Table newView(String name);
    
    /** Creates a <code>Column</code> instance.
     * @param name The column name.
     * @return An instance of <code>Column</code>.
     */
    protected abstract Column newColumn(String name);
    
    /**
     * Identifies and caches the primary keys from a resultset.
     * @param tableName The table from which the primary keys are defined.
     * @param schema The schema name in which the table belongs.
     * @param cols An array of <code>Column</code>.
     * @param rs The <code>ResultSet</code> that produces the columns.
     * @throws java.sql.SQLException When there is an error in the database.
     */
    protected abstract void setPrimaryKeys(String tableName, String schema,
            final Column[] cols, final ResultSet rs) throws SQLException;
        
    /**
     * Identifies and caches the foreign keys from a resultset.
     * @param table The <code>Table</code> from which the foreign keys
     * are defined.
     * @param schema The schema name in which the table belongs.
     * @param cols An array of <code>Column</code>.
     * @param rs The <code>ResultSet</code> that produces the columns.
     * @throws java.sql.SQLException When there is an error in the database.
     */
    protected abstract void setForeignKeys(Table table, String schema,
            Column[] cols, ResultSet rs) throws SQLException;
    
    /**
     * Sets index information for the given table.
     * @param table The <code>Table</code> instance.
     * @param cols An arrys of <code>Column</code>.
     * @param rs The <code>ResultSet</code> that produces the columns.
     * @throws java.sql.SQLException When there is an error in the database.
     */
    protected abstract void setIndexInfo(Table table, Column[] cols,
            ResultSet rs) throws SQLException;
    
    /**
     * Gets columns of a table or view.
     * @param name The name of table or view.
     * @param schema The schema name in which the table belongs.
     * @return An array of <code>Column</code>.
     * @throws java.sql.SQLException When there is an error in the database.
     */
    protected abstract  Column[] getColumns(String name, String schema)
        throws java.sql.SQLException;
    
    /**
     * Creates <code>Column</code>s from a <code>ResultSet</code>.
     * @return An array of <code>Column</code>.
     * @param resultset A <code>ResultSet</code> that producces the columns.
     * @throws java.sql.SQLException When there is an error in the database.
     */
    protected abstract Column[] makeColumns(Object resultset) 
        throws java.sql.SQLException;
    
    /**
     * Sets the jdbc type of a <code>Column</code> instance.
     * @param resultset The <code>ResultSet</code> that produces the column.
     * @param column An instance of <code>Column</code>.
     * @throws java.sql.SQLException When there is an error in the database.
     */
    protected abstract void setJDBCType(final Object resultset,
            final Column column) throws java.sql.SQLException;
    
    /**
     * JDBC also defines a method for supportsSchemaInIndexDefintions.  
     * But we don't use this as we assume an index belongs to a table.  
     * And when generating create index statements most databases don't
     * recognize a schema qualified name.
     * @return true or false.
     * @throws java.sql.SQLException When there is an error in the database.
     */
    public abstract boolean supportsSchemaInTableDefinitions()
        throws java.sql.SQLException;
    
    /**
     * Gets generic database type names.
     * @return An array of database type names.
     */
    public static String[] getPrimitiveTypes() {
        return (String[]) htTypesToNames.values().toArray(new String[0]);
    }
    
    /**
     * Gets a generic database type name.
     * @param id The index of the type in the list of types.
     * @return The type name.
     */
    public static String getPrimitiveTypeName(int id) {
        return (String) htTypesToNames.get(new Integer(id));
    }
    
    /**
     * Gets a database connection.
     * @return A jdbc connection.
     */
    public Object getConnection() {
        return connection;
    }
    
    /**
     * Gets the repository.
     * @return The repository DBElement.
     */
    public DBElement getRepository() {
        return repository;
    }
    
    /**
     * Gets the type names for this database.
     * @return An array of type names
     * @throws java.sql.SQLException When there is an error in the database.
     */
    public String[] getNewTypes() throws java.sql.SQLException {
        return getNewTypes(((DatabaseMetaData) dbmd).getTypeInfo());
    }
    
    /**
     * Gets the type names from a given type information <code>ResultSet</code>.
     * @return An array of type names
     * @param typeInfoRs A <code>ResultSet</code> that produces the
     * type information.
     * @throws java.sql.SQLException When tere is an error in the database.
     */
    protected abstract String[] getNewTypes(
            ResultSet typeInfoRs) throws java.sql.SQLException;
    
    /**
     * Sets needed metadata info for the given Database
     * @param db An instance of <code>Database</code>.
     */
    public void initDatabase(Database db) {
        // set the quote str
        try {
            db.setIdentifierQuoteString(
                    ((DatabaseMetaData) dbmd).getIdentifierQuoteString());
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }
        
        try {
            db.likesLowerCaseIdentifier(
                    (((DatabaseMetaData) dbmd).storesLowerCaseIdentifiers()));
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }
        
        try {
            db.likesUpperCaseIdentifier(
                    (((DatabaseMetaData) dbmd).storesUpperCaseIdentifiers()));
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }
        
        try {
            db.likesMixedCaseIdentifier(
                    (((DatabaseMetaData) dbmd).storesMixedCaseIdentifiers()));
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }
        
        try {
            db.likesLowerCaseQuotedIdentifier(
                    (((DatabaseMetaData)
                    dbmd).storesLowerCaseQuotedIdentifiers()));
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }
        
        try {
            db.likesUpperCaseQuotedIdentifier(
                    (((DatabaseMetaData)
                     dbmd).storesUpperCaseQuotedIdentifiers()));
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }
        
        try {
            db.likesMixedCaseQuotedIdentifier(
                    (((DatabaseMetaData)
                     dbmd).storesMixedCaseQuotedIdentifiers()));
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }
        
    }
    
}
