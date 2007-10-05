/**********************************************
 *   Copyright (C) 2007 CINCOM SYSTEMS, INC.
 *   All Rights Reserved
 **********************************************/
package org.dbuml.base.database;

import java.sql.Connection;
import java.sql.Statement;
import java.util.StringTokenizer;


/**
 * Generic class for transactions update.
 * @author rgupta
 */
public class GenericUpdateTransaction extends UpdateTransaction {
    
    /**
     * Creates an instance of <code>GenericUpdateTransaction</code>.
     * @param conn The jdbc connection.
     * @throws java.lang.IllegalArgumentException When an argument is illegal.
     * @throws java.sql.SQLException When there is an error in the database.
     */
    public GenericUpdateTransaction(Connection conn)
        throws java.lang.IllegalArgumentException, java.sql.SQLException {
        if (conn.isClosed()) {
            throw new java.lang.IllegalArgumentException();
        }
        this.setConnection(conn);
    }
    
    /**
     * Execute the given update statement.
     * @param sSQL The update statement.
     * @throws java.sql.SQLException When there is an error in the database.
     */
    public void executeUpdate(String sSQL) throws java.sql.SQLException {
        if (sSQL == null) {
            throw new java.lang.IllegalArgumentException();
        }
        
        Statement stmt = this.getConnection().createStatement();
        StringTokenizer strtok = new StringTokenizer(sSQL, ";"); 
        try {
            while (strtok.hasMoreTokens()) {
                String sql = strtok.nextToken();
                if ( sql != null && sql.length() > 2) {
                    stmt.executeUpdate(sql);
                }
            }
        } catch (java.sql.SQLException e) {
            rollback();
            throw e;
        }
        stmt.close();
    }
    
    /**
     * Commit the update.
     * @throws java.sql.SQLException When there is an error in the database.
     */
    public void commit()
        throws java.sql.SQLException {
        this.getConnection().commit();
    }
    
    /**
     * Rollback the update transaction.
     * @throws java.sql.SQLException When there is an error in the database.
     */
    public void rollback() throws java.sql.SQLException {
        this.getConnection().rollback();
    }
    
}
