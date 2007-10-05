/**********************************************
 *   Copyright (C) 2007 CINCOM SYSTEMS, INC.
 *   All Rights Reserved
 **********************************************/

package org.dbuml.base.database;


import java.sql.Statement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import org.apache.log4j.Logger;

/**
 * Abstract class for query transactions.
 * @author rgupta
 */
public abstract class QueryTransaction {
    
    private static final Logger LOG = Logger.getLogger(QueryTransaction.class);
    
    /**
     * The jdbc connection.
     */
    protected Connection connection;
    /**
     * The statement object.
     */
    protected Statement statement;
    /**
     * ResultSet object.
     */
    protected ResultSet resultSet;
    /**
     * The maiximum number of row to fetch. Default value is 20.
     */
    protected int maxRowCount = 20;
    /**
     * Boolean flag for wheter or not there are more rows to fetch.
     */
    protected boolean bHasRecords = false;
    /**
     * An array of column names.
     */
    protected String[] colNames;
    
    
    /**
     * Creates a new QueryTransaction.
     */
    public  QueryTransaction() { }
    
    /**
     * Fetches rows from the database..
     * @param conn The jdbc connection.
     * @param query The SQL query statement.
     * @return A Vector of database records.
     * @throws java.sql.SQLException When there is an error in the database.
     */
    public abstract Vector getRows(Connection conn, String query)
        throws java.sql.SQLException;
    
    /**
     * Fetches next, up to <code>maxRowCount</code>, rows.
     * @throws java.sql.SQLException When there is an error in the database.
     * @return A <code>Vector</code> of up to <code>maxRowCount</code> rows.
     */
    public abstract Vector getNextRows()throws java.sql.SQLException;
    
    
    /**
     * Are there any more rows to fetch?
     * @return <code>true</code> or <code>false</code>.
     */
    public abstract boolean hasRecords();
    
    
    /**
     * Sets the maximum number of rows to fetch when 
     * <code>getNextRows</code> method is invoked.
     * @param maxRow The maximum row count.
     */
    public void setMaxRowCount(int maxRow) {
        this.maxRowCount = maxRow;
    }
    
    /**
     * This method is called by getRows(query) and it initializes column
     * information such as the number of columns and the name of columns.
     * @throws java.sql.SQLException When there is an error in the database.
     */
    protected abstract void initializeColumnNames() throws SQLException;
    
    /** Gets the name of the columns for the query given in getRows(query).
     *@return an array of String.
     */
    public String[] getColumnNames() {
//        return this.colNames;
        String[] names = new String[this.colNames.length];
        System.arraycopy(this.colNames, 0, names, 0, this.colNames.length);
        return names;
    }
    
    /**
     * Gets the number of columns for the query given in getRows(query).
     * @return The number of columns.
     */
    public int getColumnCount() {
        return (this.colNames == null ? 0 : colNames.length);
    }
    
    /**
     * It releases database resources used by this QueryTransaction;
     */
    public void close() { //try-catch added by rashmi
        try {
            try {
                if (this.resultSet != null) {
                    this.resultSet.close();
                }
            } catch (SQLException se) {
                LOG.info(se.getMessage());
            }
            if (this.statement != null) {
                this.statement.close();
            }
        } catch (SQLException e) {
            LOG.info(e.getMessage());
        }
    }
}