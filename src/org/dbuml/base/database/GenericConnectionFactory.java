/**********************************************
 *   Copyright (C) 2007 CINCOM SYSTEMS, INC.
 *   All Rights Reserved
 **********************************************/
package org.dbuml.base.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import org.dbuml.base.model.Database;
import org.dbuml.base.model.DBElement;
/**
 * Class for connecting to database using the standard or generic jdbc.
 */
public class GenericConnectionFactory extends ConnectionFactory {
    
    /**
     * Connects to the database.
     * @param database The <code>Database</code> to connect to.
     * @return A connection object.
     * @throws java.sql.SQLException When there is database error.
     * @throws java.lang.ClassNotFoundException When a class is not found.
     * @throws java.lang.NoSuchMethodException When a method is not found.
     * @throws java.lang.InstantiationException When there is a 
     * reflection error.
     * @throws java.lang.IllegalAccessException When there is an illegal access.
     * @throws java.lang.reflect.InvocationTargetException When there is 
     * an invalid relection invocation.
     * @throws java.lang.Exception For any other error.
     */
    public Object getConnection(DBElement database)
        throws java.sql.SQLException, java.lang.ClassNotFoundException,
            java.lang.NoSuchMethodException, java.lang.InstantiationException,
            java.lang.IllegalAccessException, 
            java.lang.reflect.InvocationTargetException,
            java.lang.Exception {
        // load java.sql.Driver implementation
        loadDriver(database.getProperty(Database.DRIVER));
        
        //  get a Connection
        StringBuffer url = new StringBuffer(database.getProperty(Database.URL));
        Properties connectInfo = new Properties();
        connectInfo.put("user", database.getProperty(Database.USER));
        connectInfo.put("password", database.getProperty(Database.PASSWORD));
        Connection conn = DriverManager.getConnection(url.toString(),
                connectInfo);
        conn.setAutoCommit(false);
        
        return conn;
    }
    
    /**
     * Closes the database connection.
     * @param conn The connection to close.
     * @throws java.sql.SQLException Whene there is an error in the database.
     */
    public void closeConnection(Object conn)
        throws java.sql.SQLException {
        //
        // Some drivers get upset if you read tables and then close 
        // without a commit.
        // (get Invalid Transaction)  So we always commit.
        //
        ((Connection) conn).commit();
        ((Connection) conn).close();
    }
    
    /**
     * Connects to the registry. Generic JDBC does not support registries.
     * 
     * @return A Connection.
     * @param dbelement The Registry DBElement.
     */
    public Object getConnectionRegistry(DBElement dbelement) {
        return null;
    }
    
    /**
     * Closes a connection to the registry. Generic JDBC does not 
     * support registries.
     * @param conn The connection to close.
     */
    public void closeConnectionRegistry(Object conn) {
    }
    
}
