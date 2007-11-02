/**********************************************
 *   Copyright (C) 2007 CINCOM SYSTEMS, INC.
 *   All Rights Reserved
 **********************************************/

package org.dbuml.base.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;
import org.dbuml.base.model.DBElement;
import org.dbuml.base.model.Database;
import org.dbuml.base.model.Table;
import org.dbuml.base.model.View;
import org.dbuml.base.model.Column;
import org.dbuml.base.i18n.Translator;
import org.dbuml.base.model.PKey;
import org.dbuml.base.model.PKeyInterface;
import org.dbuml.base.model.FKey;
import org.apache.log4j.Logger;

/**
 * Class for generic metadata support.
 * @author  jgunderson
 */
public class GenericDBMetadata extends DBMetadata {
    private static final Logger LOG = Logger.getLogger(GenericDBMetadata.class);
    
    /**
     * Creates a new instance of GenericDBMetadata.
     * @param database the <code>Database</code>.
     * @param connection The jdbc connection.
     * @throws java.sql.SQLException When there is a database error.
     * @throws java.lang.ClassNotFoundException When a class is not found.
     * @throws java.lang.IllegalArgumentException When there is
     * an illagl argument.
     */
    public GenericDBMetadata(DBElement database, Object connection)
        throws java.sql.SQLException, java.lang.ClassNotFoundException,
            java.lang.IllegalArgumentException {
        super(database, connection);
        dbmd = ((Connection) connection).getMetaData();
        
        if (database instanceof Database) {
            initDatabase((Database) database);
        }
        
        if (supportsSchemaInTableDefinitions()) {
            if (!((database.getProperty(
                    Database.SUPPORTS_SCHEMA)).equals("true"))) {
                throw new java.lang.IllegalArgumentException(
                        Translator.getInstance().localize("SCHEMA_MATCH"));
            }
        } else {
            if (!((database.getProperty(
                    Database.SUPPORTS_SCHEMA)).equals("false"))) {
                throw new java.lang.IllegalArgumentException(
                        Translator.getInstance().localize("SCHEMA_MATCH"));
            }
        }
    }
    
    /**
     * Gets table names from the specified schema.
     * @param sSchema The schema name.
     * @return An array of table names.
     * @throws java.sql.SQLException When there is an error in the database.
     */
    public Object[] getTablesInSchema(String sSchema)
        throws java.sql.SQLException {
        return this.getTablesInSchema(sSchema, new String[] {"TABLE"});
    }
    
    /**
     * Gets view names from the specified schema.
     * @param sSchema The schema name.
     * @return An array of view names.
     * @throws java.sql.SQLException When there is an error in the database.
     */
    public Object[] getViewsInSchema(String sSchema)
        throws java.sql.SQLException {
        return this.getTablesInSchema(sSchema, new String[] {"VIEW"});
    }
    
    private Object[] getTablesInSchema(String sSchema, String[] type)
        throws java.sql.SQLException {
        ArrayList alNames = new ArrayList();
        ResultSet rs = null;
        
        if (((Database) repository).isDefaultSchema(sSchema)) {
            rs = ((DatabaseMetaData) dbmd).getTables(null, null, "%", type);
        } else if (((DatabaseMetaData) 
            dbmd).supportsSchemasInTableDefinitions()) {
            rs = ((DatabaseMetaData) dbmd).getTables(null, sSchema, null, type);
        }
        
        String[] sNames = null;
        if (rs == null) {
            sNames = new String[] {};    
        } else {
            while (rs.next()) {
                alNames.add(((Database) repository).fixName(
                        rs.getString("TABLE_NAME"), true));
            }
            rs.close();
            sNames = (String[]) alNames.toArray(new String[0]);
            if (!((DatabaseMetaData) dbmd).supportsSchemasInTableDefinitions() 
                && !((Database) repository).isDefaultSchema(sSchema)) {
                for (int i = 0; i < sNames.length; i++) {
                    sNames[i] = sNames[i].substring(sSchema.length());
                }
            }
        }
        return sNames;
    }
    
    /**
     * Gets schema names from the database.
     *
     * @return An array of table names.
     * @throws java.sql.SQLException When there is an error in the database.
     */
    public Object[] getSchemas()
        throws java.sql.SQLException {
        ArrayList alNames = new ArrayList();
        ResultSet rs = ((DatabaseMetaData) dbmd).getSchemas();
        
        while (rs.next()) {
            alNames.add(rs.getString("TABLE_SCHEM"));
        }
        rs.close();
        String[] sNames = (String[]) alNames.toArray(new String[0]);
        return sNames;
    }
    
    /**
     * Gets the databases. This method is not implemented for generic JDBC.
     * @return An array of table names.
     * @throws java.lang.Exception When there is an error.
     */
    public Object[] getDatabases()
        throws java.lang.Exception {
        throw new java.lang.Exception(
                "Get databases not implemented for JDBC metadata");
    }
    
    /**
     * Gets the specified database. This method is not implemented 
     * for generic JDBC.
     * @param name The database name.
     * @return The <code>Database</code> instance.
     * @throws java.lang.Exception When there is an error.
     */
    public Database getDatabase(String name)
        throws java.lang.Exception {
        throw new java.lang.Exception(
                "Get database not implemented for JDBC metadata");
    }
    
    /**
     * Gets a table.
     * @param tableName The table name.
     * @param schema The table's schema name.
     * @return An instance of <code>Table</code>.
     * @throws java.sql.SQLException When there is an error in the database.
     */
    public Table getTable(String tableName, String schema)
        throws java.sql.SQLException {
        // get name returned by metadata. It usually does not have Quotes.
        String mdTableName = ((Database) repository).removeQuote(tableName);
        Column[] cols = getColumns(tableName, schema);
        Table table = newTable(tableName);
        table.setColumns(cols);
        
        // set primary keys
        if ((((Database) repository).isDefaultSchema(schema))) {
            schema = null;
        }
        ResultSet rs = null;
        try {
            try {
                rs = ((DatabaseMetaData) dbmd).getPrimaryKeys(null,
                        schema, mdTableName);
            } catch (java.sql.SQLException ignore) {
                // try again without the quote
                rs = ((DatabaseMetaData) dbmd).getPrimaryKeys(null,
                        schema, tableName);
            }
            this.setPrimaryKeys(tableName, schema, cols, rs);
        } catch (java.sql.SQLException e) {
            LOG.error("Table = " + tableName + ": Metadata.getPrimaryKeys() " 
                    + e.getMessage());
        }
        
        // set Foreign keys
        try {
            try {
                rs = ((DatabaseMetaData) dbmd).getImportedKeys(null, 
                        schema, mdTableName);
            } catch (java.sql.SQLException ignore) {
                // try again without the quote
                rs = ((DatabaseMetaData) dbmd).getImportedKeys(null, 
                        schema, tableName);
            }
            this.setForeignKeys(table, schema, cols, rs);
        } catch (java.sql.SQLException e) {
            LOG.error("Table = " + tableName + ": Metadata.getImportedKeys() " 
                    + e.getMessage());
            // should display this message or log it?
        }
        
        // set index info
        try {
            try {
                rs = ((DatabaseMetaData) dbmd).getIndexInfo(null, schema, 
                        mdTableName, true, false); //true for unique index
            } catch (java.sql.SQLException ignore) {
                rs = ((DatabaseMetaData) dbmd).getIndexInfo(null, schema,
                        tableName, true, false); //true for unique index
            }
            this.setIndexInfo(table, cols, rs);
        } catch (java.sql.SQLException e) {
            LOG.error("Table = " + tableName + ": Metadata.getIndexInfo "
                    + e.getMessage());
        }
        
        return table;
    }
    
    /**
     * Creates a view.
     * @return An instance of <code>View</code>.
     * @param viewName The view name.
     * @param schema The view's schema name.
     * @throws java.sql.SQLException When there is an error in the database.
     */
    public View getView(String viewName, String schema) 
        throws java.sql.SQLException {
        View view = newView(viewName);
        Column[] cols = getColumns(viewName, schema);
        view.setColumns(cols);
        return view;
    }
    
    /** Creates a <code>Table</code> instance.
     * @param name The table name.
     * @return An instance of <code>Table</code>.
     */
    protected Table newTable(String name) {
        return new Table(name);
    }
    
    /** Creates a <code>View</code> instance.
     * @param name The view name.
     * @return An instance of <code>View</code>.
     */
    protected View newView(String name) {
        return new View(name);
    }
    
    /** Creates a <code>Column</code> instance.
     * @param name The column name.
     * @return An instance of <code>Column</code>.
     */
    protected Column newColumn(String name) {
        return new Column(name);
    }
    
    /**
     * Identifies and caches the primary keys from a resultset.
     * @param tableName The table from which the primary keys are defined.
     * @param schema The schema name in which the table belongs.
     * @param cols An array of <code>Column</code>.
     * @param rs The <code>ResultSet</code> that produces the columns.
     * @throws java.sql.SQLException When there is an error in the database.
     */
    protected void setPrimaryKeys(String tableName, String schema,
            final Column[] cols, final ResultSet rs) throws SQLException {
        if (rs != null) {
            while (rs.next()) {
                // get the key data and instanciate PKey
                String colName = rs.getString("COLUMN_NAME");
                short seq = rs.getShort("KEY_SEQ");
                String keyName = rs.getString("PK_NAME");
                PKey key = new PKey(schema, tableName, colName, seq, keyName);
                this.setKey(cols, colName, key);
            }
            rs.close();
        }
    }
    
    private void setKey(Column[] cols, String colName, PKeyInterface key) {
        if (key != null && cols != null) {
            //Locate the column in cols and set its key
            for (int i = 0; i < cols.length; i++) {
                if (cols[i].getName().equalsIgnoreCase(colName)) {
                    cols[i].setKey(key);
                    // set multiplicity end for foreign key.
                    if (cols[i].isForeignKey()) {
                        if (cols[i].isUnique()) { // no more than 1.
                            if (cols[i].allowsNulls()) {// 0-1
                                ((FKey)key).setTargetMultiplicityAtMostOne();
                            } else {// 1
                                ((FKey)key).setTargetMultiplicityExactlyOne();
                            }
                        } else { // n
                            if (cols[i].allowsNulls()) { //0-n
                                ((FKey)key).setTargetMultiplicityZeroOrMore();
                            } else { // 1-n
                                ((FKey)key).setTargetMultiplicityAtLeastOne();
                            }
                        }
                    }
                    break;
                }
            }
        }
    }
    
    /**
     * Identifies and caches the foreign keys from a resultset.
     * @param table The <code>Table</code> from which the foreign keys 
     * are defined.
     * @param schema The schema name in which the table belongs.
     * @param cols An array of <code>Column</code>.
     * @param rs The <code>ResultSet</code> that produces the columns.
     * @throws java.sql.SQLException When there is an error in the database.
     */
    protected void setForeignKeys(Table table, String schema, Column[] cols, 
            ResultSet rs) throws SQLException {
        if (rs != null) {
            while (rs.next()) {
                // get the key data and instantiate PKey
                String colName = rs.getString("FKCOLUMN_NAME");
                short seq = rs.getShort("KEY_SEQ");
                String keyName = rs.getString("FK_NAME");
                FKey key = new FKey(schema, table.getName(), 
                        colName, seq, keyName);
                String nativeSchema = rs.getString("PKTABLE_SCHEM");
                String nativeTable = rs.getString("PKTABLE_NAME");
                String nativeCol = rs.getString("PKCOLUMN_NAME");
                String nativeName = rs.getString("PK_NAME");
                key.setNativeData(nativeSchema, nativeTable, 
                        nativeCol, nativeName);
                key.setUpdateRule(
                        getReferentialAction(rs.getShort("UPDATE_RULE")));
                key.setDeleteRule(
                        getReferentialAction(rs.getShort("DELETE_RULE")));
                this.setKey(cols, colName, key);
            }
        }
    }
    
    private String getReferentialAction(short action) {
        String sAction = null;
        switch (action) {
            case DatabaseMetaData.importedKeyCascade:
                sAction = "CASCADE"; 
                break;  
            case DatabaseMetaData.importedKeySetNull:
                sAction = "SET NULL"; 
                break;
            case DatabaseMetaData.importedKeySetDefault:
                sAction = "SET DEFAULT"; 
                break;
            case DatabaseMetaData.importedKeyRestrict:
                sAction = "RESTRICT"; 
                break;
            case DatabaseMetaData.importedKeyNoAction:
                sAction = "NO ACTION"; 
                break;
            default:
        }
        return sAction;
    }
    
    /**
     * Sets index information for the given table.
     * @param table The <code>Table</code> instance.
     * @param cols An arrys of <code>Column</code>.
     * @param rs The <code>ResultSet</code> that produces the columns.
     * @throws java.sql.SQLException When there is an error in the database.
     */
    protected void setIndexInfo(Table table, Column[] cols, ResultSet rs)
        throws SQLException {
        if (rs != null) {
            while ( rs.next() ) {
                String colName = rs.getString("COLUMN_NAME");
                // locate the column in cols and set its index info
                for (int i = 0; i < cols.length; i++) {
                    if (cols[i].getName().equalsIgnoreCase(colName)) {
                        if (!cols[i].isPrimaryKey()) { // skip primary keys
                            cols[i].setIndexInfo(rs.getString("INDEX_NAME"));
                        }
                        break;
                    }
                }
            }
            table.markUniqueColumns();
        }
    }
    
    /**
     * Gets columns of a table or view.
     * @param name The name of table or view.
     * @param schema The schema name in which the table belongs.
     * @return An array of <code>Column</code>.
     * @throws java.sql.SQLException When there is an error in the database.
     */
    protected Column[] getColumns(String name, String schema)
        throws java.sql.SQLException {
        ResultSet rs = null;
        if (((Database) repository).isDefaultSchema(schema) 
                || !((DatabaseMetaData)
                 dbmd).supportsSchemasInTableDefinitions()) {
            schema = null;
        }
        rs = ((DatabaseMetaData) dbmd).getColumns( null, schema,
                ((Database) repository).removeQuote(name), null);
        return makeColumns(rs);
    }
    
    //
    //  Make Columns from column metadata ResultSet.  
    // This was split from the getColumns method so databases that have
    // custom getColumns logic can reuse this method.
    //
    
    /**
     * Creates <code>Column</code>s from a <code>ResultSet</code>.
     * @return An array of <code>Column</code>.
     * @param resultset A <code>ResultSet</code> that producces the columns.
     * @throws java.sql.SQLException When there is an error in the database.
     */
    protected Column[] makeColumns(Object resultset)
        throws java.sql.SQLException {
        Column[] columns = null;
        
        if (resultset != null) {
            ResultSet rs = (ResultSet) resultset;
            ArrayList  alColumns = new ArrayList();
            Column     column;

            while ( rs.next()) {
                // Make a new Column
                column = newColumn(rs.getString("COLUMN_NAME"));
                
                //Added by SBalda in order to handle quotes for default values
                String colDef = rs.getString("COLUMN_DEF");

                if (colDef != null) {
                    if ((colDef.indexOf('\'') < 0) && colDef.length() >= 0) {
                        column.setDefault(colDef);
                    }
                    // for oracle quotes are present & len >=0
                    if (colDef.length() >= 0 &&  colDef.indexOf('\'') >= 0) {
                        int beginIndex = colDef.indexOf('\'');
                        int lastIndex = colDef.lastIndexOf('\'');
                        colDef = colDef.substring(beginIndex + 1, lastIndex);
                        column.setDefault(colDef);

                    }

                }

                this.setJDBCType(rs, column); //Dependency: it needs column_name
                column.setLength(rs.getInt("COLUMN_SIZE"));
                column.setScale(rs.getInt("DECIMAL_DIGITS"));

                //Commented by RGupta

    //           try {
    //                String defValue = rs.getString("COLUMN_DEF");
    //                if (defValue!=null && !defValue.trim().equals(""))
    //                column.setDefault(defValue);
    //           } catch(java.sql.SQLException e){
    //            }


                //
                // todo:  For views and proxies with ordbMetadata add 
                // the following code:
                //
                // if (table.isVirtualClass()) {
                //      column.isUnique = 
                //        rs.getString("IS_UNIQUE").equals("YES");
                //      if (column.isUnique)
                //          table.hasUniqueConstraints(true);
                //  }
                //  else
                //    column.isUnique = false;
                alColumns.add(column);
            }
            // Close the result set.
            rs.close();

            // form the array of Columns
            columns = (Column[]) alColumns.toArray(new Column[0]);
        }
        return columns;
    }
    
    /**
     * Sets the jdbc type of a <code>Column</code> instance.
     * @param resultset The <code>ResultSet</code> that produces the column.
     * @param column An instance of <code>Column</code>.
     * @throws java.sql.SQLException When there is an error in the database.
     */
    protected void setJDBCType(final Object resultset, final Column column)
        throws java.sql.SQLException {
        
        ResultSet rs = (ResultSet) resultset;
        // resolve some dependencies.
        column.allowsNulls(!"NO".equals(rs.getString("IS_NULLABLE"))); // dep
        column.setTypeName(rs.getString("TYPE_NAME")); // dep
        
        short jdbcType = rs.getShort("DATA_TYPE");
        column.setJdbcType(jdbcType);
        column.setTypeNameJdbc(getPrimitiveTypeName(jdbcType));
    }
    
    
    /**
     * JDBC also defines a method for supportsSchemaInIndexDefintions.  
     * But we don't use this as we assume an index belongs to a table.
     * And when generating create
     * index statements most databases don't recognize a schema qualified name.
     * @return true or false.
     * @throws java.sql.SQLException When there is an error in the database.
     */
    public boolean supportsSchemaInTableDefinitions()
        throws java.sql.SQLException {
        return ((DatabaseMetaData) dbmd).supportsSchemasInTableDefinitions();
    }
    
    /**
     * Gets the type names from a given type information <code>ResultSet</code>.
     * @return An array of type names
     * @param typeInfoRs A <code>ResultSet</code> that produces the
     * type information.
     * @throws java.sql.SQLException When tere is an error in the database.
     */
    protected String[] getNewTypes(ResultSet typeInfoRs)
        throws java.sql.SQLException {
        //  todo:  Cam probably write a generic method for all databases
        // to get new types.
        //  Compare getPrimitiveTypes() to the DatabaseMetaData getTypeInfo()
        //
        String[] retTypes = null;
        
        if (typeInfoRs != null) {
            Vector sqlTypes = new Vector();
            String aSQLTypes[] = getPrimitiveTypes();
            for (int i = 0; i < aSQLTypes.length; i++) {
                sqlTypes.add(aSQLTypes[i]);
            }

            ArrayList  newTypes = new ArrayList();

            while (typeInfoRs.next()) {
                String name = ((String) typeInfoRs.getString(
                        "TYPE_NAME")).toUpperCase();
                if (!sqlTypes.contains(name)) {
                    newTypes.add(name);
                }
            }
            
            typeInfoRs.close();
            typeInfoRs = null;
            retTypes = (String[]) newTypes.toArray(new String[newTypes.size()]);
        }
        return retTypes;
    }
    
}
