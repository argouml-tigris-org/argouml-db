/**********************************************
 *   Copyright (C) 2007 CINCOM SYSTEMS, INC.
 *   All Rights Reserved
 **********************************************/
package org.dbuml.base.database;

import java.sql.Connection;

/**
 * Abstract class for transactions update.
 * @author rgupta
 */
public abstract class UpdateTransaction {
    /**
     * The jdbc connection.
     */
    private Connection connection = null;
    
    /**
     * Execute the given update statement.
     * @param sSQL The update statement.
     * @throws java.sql.SQLException When there is an error in the database.
     */
    public abstract void executeUpdate(String sSQL) 
        throws java.sql.SQLException;
    
    /**
     * Commit the update.
     * @throws java.sql.SQLException When there is an error in the database.
     */
    public abstract void commit() throws java.sql.SQLException;
    
    /**
     * Rollack the update transaction.
     * @throws java.sql.SQLException When there is an error in the database.
     */
    public abstract void rollback() throws java.sql.SQLException;
    
    /** Gets the connection.
     * @return A JDBC <code>Connection</code> object.
     */
    protected Connection getConnection() {
        return this.connection;
    }
    
    /** Sets the connection.
     * @param  conn The JDBC <code>Connection</code> object.
     */
    protected void setConnection(Connection conn) {
        this.connection = conn;
    }
    
}
