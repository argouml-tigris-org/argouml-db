/**********************************************
*   Copyright (C) 2007 CINCOM SYSTEMS, INC.
*   All Rights Reserved
**********************************************/

package org.dbuml.base.model;

/**
 *
 * @author  jgrengbondai
 */
public class PKey implements PKeyInterface {
    /**
     * The schema schema name
     */
    protected String tableSchema;
    /**
     * The table name
     */
    protected String tableName;
    /**
     * The column name
     */
    protected String columnName;
    
    /**
     * Key sequence number.
     */
    protected short key_seq;
    /**
     * The key name
     */
    protected String keyName;
    
    private FKey fkey; // a foreign key may also be a primary key on the
                       // same table
    
//    public PKey() {
//    }
    /**
     * Creates a new instance of PKey
     * @param schema The schema name.
     * @param table The table name
     * @param col The column name
     * @param seq The sequence number of the key.
     * @param kName The key name.
     */
    public PKey(final String schema, final String table, final String col, 
            short seq, final String kName) {
        this.tableSchema = schema;
        this.tableName = table;
        this.columnName = col;
        this.key_seq = seq;
        this.keyName = kName;
    }
    
    /**
     * Gets the table schema name, which may be null.
     *@return The schema name in which the table belongs.
     */
    public String getTableSchema() {
        return this.tableSchema;
    }
    
    
    public void setTableSchema(final String schema) {
        this.tableSchema = schema;
    }
    
    /** Gets the table name.
     *@return The table name.
     */
    public String getTableName() {
        return this.tableName;
    }
    
    public void setTableName(final String tName) {
        this.tableName = tName;
    }
    
    /** Gets the name of the column being described.
     *@return The column name. 
     */
    public String getColumnName() {
        return this.columnName;
    }
    
    public void setColumnName(final String colName) {
        this.columnName = colName;
    }
    
    /** Sequence number within a primary key( useful if the primary key consists
     * of more than one column)
     *@return A number representing the key sequence.
     */
    public short getKeySequence() {
        return this.key_seq;
    }
    
    /**
     * Gets the name of this key which may be null.
     *@return A key name.
     */
    public String getKeyName() {
        return this.keyName;
    }
    
    public void setKeySequence(short seq) {
        this.key_seq = seq;
    }
    
    public String getType() {
        return this.fkey == null ? PK : PFK;
    }
    
    /** set the foreign key information. This primary key is also a foreign on 
     * the same table.
     *@param key <code>FKey</code> object.
     */
    public void setForeignInfo(final FKey key) {
        this.fkey = key;
    }
    
    public boolean isForeignKey() {
        return (this.fkey != null);
    }
    
    public FKey getForeignKey () {
        return this.fkey;
    }  
    
    public void setKeyName(final String kName) {
        this.keyName = kName;
    }
    
}
