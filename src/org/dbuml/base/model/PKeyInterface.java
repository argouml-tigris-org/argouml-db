/**********************************************
*   Copyright (C) 2007 CINCOM SYSTEMS, INC.
*   All Rights Reserved
**********************************************/

package org.dbuml.base.model;

/**
 * Interface of modeling primary and foreign keys.
 * @author jgrengbondai
 */
public interface PKeyInterface {
    /**
     * The Primamary key type.
     */
    public static final String PK = "PK";
    /**
     * The foreign key type.
     */
    public static final String FK = "FK";
    /**
     * The primary and forign key type for modeling column that are both 
     * primary and foreign key on the same table.
     */
    public static final String PFK = "PFK";
    /**
     * Gets the table schema name, which may be null.
     * @return The table schema name or null.
     */
    public String getTableSchema();
    
    /**
     * Sets the table schema name.
     * @param schema The schema name.
     */
    public void setTableSchema(String schema);
    
    /**
     * Gets the table name.
     * @return The table name.
     */
    public String getTableName();
    
    /**
     * Sets the table name.
     * @param tableName The table name.
     */
    public void setTableName(String tableName);
    
    /**
     * Gets the name of the column being described.
     * @return The column name.
     */
    public String getColumnName();
    
    /**
     * Sets the column name.
     * @param colName The column name.
     */
    public void setColumnName(String colName);
    
    /**
     * Sequence number within a primary key( useful if the primary key consists
     * of more than one column)
     * @return The key sequence.
     */
    public short getKeySequence();
    
    /**
     * Sets the key sequence.
     * @param seq The sequence.
     */
    public void setKeySequence(short seq);
    
    /**
     * Gets the name of this key which may be null.
     * @return The key name.
     */
    public String getKeyName();
    
    /**
     * Sets the key name.
     * @param keyName The key name.
     */
    public void setKeyName(String keyName);
    
    /**
     * Gets the interface type.
     * @return The type.
     */
    public String getType();
    
    /**
     * Sets the foreign key information. This primary key is also a foreign key
     * on the same table.
     * @param key The foreign object.
     */
    public void setForeignInfo(FKey key);
    
    /**
     * Is this a foreign key?
     * @return true or false.
     */
    public boolean isForeignKey();
    
    /**
     * Gets foreign key.
     * @return A FKey instance.
     */
    public FKey getForeignKey ();
    
}
