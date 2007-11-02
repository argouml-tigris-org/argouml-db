/**********************************************
 *   Copyright (C) 2007 CINCOM SYSTEMS, INC.
 *   All Rights Reserved
 **********************************************/

package org.dbuml.base.model;

/**
 * Class for modeling foreign key relationship.
 * @author jgrengbondai
 */
public class FKey extends DBAssociation implements PKeyInterface {
    private String nativeSchema;
    private String nativeTableName;
    private String nativeColumnName;
    private String nativeName;
    
    private String tableSchema;
    private String tableName;
    private String columnName;
    private Column column;
    
    /**
     * Creates a new FKey.
     * @param name The foreign key relationship name.
     */
    public FKey(final String name) {
        super(name);
    }
    
    /**
     * Creates a new instance of FKey.
     * @param schema The schema name.
     * @param table The table name.
     * @param col The column name.
     * @param seq The foreign key sequence.
     * @param keyName The foreign key name.
     */
    public FKey(final String schema, final String table, final String col,
            short seq, final String keyName) {
        super(col);
        this.tableSchema = schema;
        this.tableName = table;
        this.columnName = col;
        this.setProperty("KEY_SEQ", (new Short(seq)).toString());
        this.setProperty("FK_NAME",  keyName);
    }
    /**
     * Sets the native or primary data information of this foreign key.
     * @param schema The schema name.
     * @param table The table name.
     * @param col The column name
     * @param name The name of the foreign key.
     */
    public void setNativeData(final String schema, final String table, 
            final String col, final String name) {
        this.nativeSchema = schema;
        this.nativeTableName = table;
        this.setNativeColumnName(col);
        this.nativeName = name;
    }
    
    /**
     * Gets the native schema name.
     * @return The natice schema name.
     */
    public String getNativeSchema() {
        return this.nativeSchema;
    }
    
    /**
     * The the native table name.
     * @return The native table name.
     */
    public String getNativeTableName() {
        return this.nativeTableName;
    }
    
    /**
     * Gets the naive column name.
     * @return The native column name.
     */
    public String getNativeColumnName() {
        return this.nativeColumnName;
    }
    
    /**
     * Sets the native column name.
     * @param name The native column name.
     */
    public void setNativeColumnName(final String name) {
        this.nativeColumnName = name;
        this.setSourceEndName(name);
    }
    
    /**
     * Gets the native name of this foreign key.
     * @return The native name.
     */
    public String getNativeName() {
        return this.nativeName;
    }
    
    /**
     * The type of this PKInterface.
     * @return The FK type.
     */
    public String getType() {
        return FK;
    }
    
    /**
     * Is this key a foreign key?
     * @return true.
     */
    public boolean isForeignKey() {
        return true;
    }
    
    /**
     * return this object.
     * @return this object.
     */
    public FKey getForeignKey() {
        return this;
    }
    
    /**
     * Gets the coulmn name.
     * @return The column name.
     */
    public String getColumnName() {
        return this.columnName;
    }
    
    /**
     * Sets the column name.
     * @param colName The column name.
     */
    public void setColumnName(final String colName) {
        this.columnName = colName;
    }
    
    /**
     * Gets the table name.
     * @return The table name.
     */
    public String getTableName() {
        return this.tableName;
    }
    
    /**
     * Sets the table name.
     * @param tName The table name.
     */
    public void setTableName(final String tName) {
        this.tableName = tName;
    }
    
    /**
     * Gets the keys sequence.
     * @return The key sequence.
     */
    public short getKeySequence() {
        String s = this.getProperty("KEY_SEQ");
//        return s == null ? 0 : (new Short(s)).shortValue();
        return s == null ? 1 : (new Short(s)).shortValue();
    }
    
    /**
     * Sets the key sequence.
     * @param seq The key sequence value.
     */
    public void setKeySequence(short seq) {
        this.setProperty("KEY_SEQ", (new Short(seq)).toString());
    }
    
    /**
     * Gets the key name.
     * @return The key name.
     */
    public String getKeyName() {
        return this.getProperty("FK_NAME");
    }
    
    /**
     * Sets the key name.
     * @param keyName The keys name.
     */
    public void setKeyName(final String keyName) {
        this.setProperty("FK_NAME", keyName);
    }
    
    /**
     * Sets the foreign key information.
     * @param key The foreign key.
     */
    public void setForeignInfo(FKey key) {
        //DO BE DETERMINED: do we need this?
    }
    
    /**
     * Gets the stereotype string.
     * @return The steeotype string.
     */
    public String getStereostring() {
        return (column != null) ? column.getStereostring() : "FK";
    }
    
    /**
     * Gets the table schema name.
     * @return The table schema name.
     */
    public String getTableSchema() {
        return this.tableSchema;
    }
    
    /**
     * Sets the table schema name.
     * @param schema The table schema name.
     */
    public void setTableSchema(final String schema) {
        this.tableSchema = schema;
    }
    
    /**
     * Sets the column.
     * @param col The <code>Column</code>.
     */
    public void setColumn(final Column col) {
        this.column = col;
    }
    
    /**
     * Gets the sterotype of the source end.
     * @return The end source stereotype.
     */
    public String getSourceEndStereoString() {
        return "PK"; // It can also be "PFK".
        // the best way is to find the right column in the target table and get
        // it stereotype string which is mostly "PK" or sometimes "PFK".
        
        //Should we simply remove this?
    }
    
    /**Sets UPDATE_RULE value.
     * @param value <code>String</code> indicating what happens to the foreign
     * key when the primary key is updated. The possible values are:
     * CASCADE, SET NULL, SET DEFAULT, RESTRICT, and NO ACTION.
     */
    public void setUpdateRule(String value) {
        //TO DO: validate value
        this.setProperty("UPDATE_RULE", value);
    }
    /** Gets the update rule string.
     * @return The update rule string.
     */
    public String getUpdateRule() {
        return this.getProperty("UPDATE_RULE");
    }
    
    /**Determines whether there is update rule defined for this foreign key.
     * @return <code>true</code> or <code>false</code>.
     */
    public boolean hasUpdateRule() {
        String rule = getUpdateRule();
        return (rule != null && rule != "");
    }
    
    /**Sets DELETE_RULE value.
     * @param value <code>String</code> indicating what happens to the foreign
     * key when the primary key is deleted. The possible values are:
     * CASCADE, SET NULL, SET DEFAULT, RESTRICT, and NO ACTION.
     */
    public void setDeleteRule(String value) {
        //TO DO: validate value
        this.setProperty("DELETE_RULE", value);
    }
    
    /**Determines whether there is a delete rule defined for this foreign key.
     * @return <code>true</code> or <code>false</code>.
     */
    public boolean hasDeleteRule() {
        String rule = getDeleteRule();
        return (rule != null && rule != "");
    }
    
    /** Gets the delete rule string.
     * @return The delete rule string.
     */
    public String getDeleteRule() {
        return this.getProperty("DELETE_RULE");
    }
    
}
