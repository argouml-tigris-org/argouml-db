/**********************************************
 *   Copyright (C) 2007 CINCOM SYSTEMS, INC.
 *   All Rights Reserved
 **********************************************/

package org.dbuml.base.database;

import java.sql.Connection;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;


/**
 * Generic query transactions support class.
 * @author  rgupta
 */
public class GenericQueryTransaction extends QueryTransaction {
    int colCount;
    int rowCounter;
    
    /**
     * Creates a new GenericQueryTransaction.
     */
    public GenericQueryTransaction() { }
    
    /**
     * This method is called by getRows(query) and it initializes column
     * information such as the number of columns and the name of columns.
     * @throws java.sql.SQLException When there is an error in the database.
     */
    protected void initializeColumnNames() throws SQLException {
        if (this.resultSet != null) {
            final ResultSetMetaData meta = this.resultSet.getMetaData();
            this.colCount = meta.getColumnCount();
            this.colNames = new String[this.colCount];
            for (int i = 1; i <= colCount; i++) {
                this.colNames[i - 1] = meta.getColumnName(i);
            }
        }
    }   
    
    /**
     * Fetches rows from the database..
     * @param conn The jdbc connection.
     * @param query The SQL query statement.
     * @return A Vector of database records.
     * @throws java.sql.SQLException When there is an error in the database.
     */
    public Vector getRows(Connection conn, String query)
        throws java.sql.SQLException {
        
        //to make sure that there is a connection
        
        this.connection = conn;
        Vector v = null;
        if (this.connection != null ) {
            this.statement = this.connection.createStatement();
            this.resultSet = this.statement.executeQuery(query);
            // to initialize the columns names
            this.initializeColumnNames();
            rowCounter = 0;
            v = this.getRows();
        }
        return v;
    }
    
    private Vector getRows() throws java.sql.SQLException {
        this.bHasRecords = resultSet.next();
        Vector rows = new Vector();
        while (bHasRecords)  {
            
            rowCounter++;
            String[] record = new String[colCount];
            for (int i = 0; i < colCount; i++) {
                record[i] = (String) resultSet.getString(i + 1);
            }
            rows.addElement(record);
            
            if (rowCounter % this.maxRowCount == 0) {
                break;
            }
            this.bHasRecords = resultSet.next();
        }
        return rows;
        
    }
    
    /**
     * Fetches next, up to <code>maxRowCount</code>, rows.
     * @throws java.sql.SQLException When there is an error in the database.
     * @return A <code>Vector</code> of up to <code>maxRowCount</code> rows.
     */
    public Vector getNextRows() throws java.sql.SQLException  {        
        return (bHasRecords ? this.getRows() : null); 
    }
    
    /**
     * Are there any more rows to fetch?
     * @return <code>true</code> or <code>false</code>.
     */
    public boolean hasRecords() {
        return bHasRecords;
    }
    
    
}

