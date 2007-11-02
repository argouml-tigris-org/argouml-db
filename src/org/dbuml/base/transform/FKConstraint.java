/**********************************************
 *   Copyright (C) 2007 CINCOM SYSTEMS, INC.
 *   All Rights Reserved
 **********************************************/

package org.dbuml.base.transform;

import java.util.Vector;
import org.dbuml.base.model.FKey;

/**
 * Helper class for generating foreign key statement in velocity.
 * @author jgunderson
 */

public class FKConstraint {
    private String fkName;
    
    // TO DO: Remove the following and use fkey
    private String pkTableSchema;
    private String pkTableName;
    private Vector pkColumnNames = new Vector(256);
    private Vector fkColumnNames = new Vector(256);
   
    
    private FKey fkey;
    private String updateRule;
    private String deleteRule;
    
    /**
     * Creates a new FKConstraint.
     * @param name The foreign key name.
     */
    public FKConstraint(String name) {
        fkName = name;
    }
    
    //
    //  Get methods needed for Velocity
    
    /**
     * Gets the foreign key name.
     * @return The foreign key name.
     */
    public String getFkName() { return fkName; }
    
    /**
     * Sets the foreign key name.
     * @param name The foreign key name.
     */
    public void setFkName(String name) {
        this.fkName = name;
    }
    
    /**
     * Gets the schema name of the table in which the primary key is defined.
     * @return A schema name.
     */
    public String getPkTableSchema() { return pkTableSchema; }
    
    /**
     * Sets the schema name of the table in which the primary key is defined.
     * @param schemaName The schema name.
     */
    public void setPkTableSchema(String schemaName) {
        this.pkTableSchema = schemaName;
    }
    
    /**
     * Gets the table name in which the pimary key is defined.
     * @return The table name.
     */
    public String getPkTableName() { return pkTableName; }
    
    
    /**
     * Sets the table name in which the pimary key is defined.
     * @param tableName The table name.
     */
    public void setPkTableName(String tableName) {
        this.pkTableName = tableName; 
    }
    /**
     * Gets the names of the primary key columns.
     * @return A Vector of names.
     */
    public Vector getPkColumnNames() { return pkColumnNames; }
    
    /**
     * Gets the names of the foreign key columns.
     * @return The foreign key names.
     */
    public Vector getFkColumnNames() { return fkColumnNames; }
    
    /** Sets foreign key information.
     * @param key An instance of <code>FKey</code>.
     */
    public void setInfo(FKey key) {
        setPkTableSchema(key.getNativeSchema());
        setPkTableName (key.getNativeTableName());

        this.fkColumnNames.add(key.getKeySequence() - 1, key.getColumnName());
        this.pkColumnNames.add(key.getKeySequence() - 1,
                key.getNativeColumnName());
        
        // TO DO: Remove the above lines and try using the information
        // in fkey within the velocity templates.
        
        this.fkey = key;
    }
    
    /** Gets the FKey for t
     */
    public FKey getFKey() {
        return this.fkey;
    }
    /**
     * Gets the update rule.
     * @return The update rule string.
     */
    public String getUpdateRule() { return updateRule; }
    /**
     * Gets the delete rule.
     * @return The delete rule string.
     */
    public String getDeleteRule() { return deleteRule; }
}


