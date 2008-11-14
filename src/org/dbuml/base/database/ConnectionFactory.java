/**********************************************
 *   Copyright (C) 2007 CINCOM SYSTEMS, INC.
 *   All Rights Reserved
 **********************************************/
package org.dbuml.base.database;

import java.sql.Driver;

import org.dbuml.base.model.DBElement;

/**
 * Abstract class for connecting to the database.
 */
public abstract class ConnectionFactory {
    private Driver driver = null;
    
    /**
     * Loads the jdbc driver.
     * @param sDriver The driver string.
     * @throws java.lang.ClassNotFoundException When the driver class
     * is not found.
     * @throws java.lang.NoSuchMethodException When a method is not found.
     * @throws java.lang.InstantiationException When there is a 
     * reflection error.
     * @throws java.lang.IllegalAccessException When there is an illegal access.
     * @throws java.lang.reflect.InvocationTargetException When there is an 
     * invalid relection invocation.
     */
    protected void loadDriver(String sDriver)
        throws java.lang.ClassNotFoundException, 
            java.lang.NoSuchMethodException,
            java.lang.InstantiationException, java.lang.IllegalAccessException,
            java.lang.reflect.InvocationTargetException {
        // TODO: This should use the module's classloader so that
        // it doesn't pollute the higher classloaders for other modules
        Class.forName(sDriver, true, ClassLoader.getSystemClassLoader());
    }
    
    /**
     * Gets the jdbc driver.
     * @return <code>Driver</code>
     */
    protected Driver getDriver() {
        return driver;
    }
    
    /**
     * Connects to the database.
     * @param dbelement The DBElement element such as <code>Database</code>
     * to connect to.
     * @return A connection object.
     * @throws java.sql.SQLException When there is database error.
     * @throws java.lang.ClassNotFoundException When a class is not found.
     * @throws java.lang.NoSuchMethodException When a method is not found.
     * @throws java.lang.InstantiationException When there is a 
     * reflection error.
     * @throws java.lang.IllegalAccessException When there is an illegal access.
     * @throws java.lang.reflect.InvocationTargetException When there is an 
     * invalid relection invocation.
     * @throws java.lang.Exception For any other error.
     */
    public abstract Object getConnection(DBElement dbelement)
        throws java.sql.SQLException, java.lang.ClassNotFoundException,
            java.lang.NoSuchMethodException, java.lang.InstantiationException,
            java.lang.IllegalAccessException,
            java.lang.reflect.InvocationTargetException,
            java.lang.Exception;
    
    /**
     * Closes the database connection.
     * @param conn The connection to close.
     * @throws java.sql.SQLException Whene there is an error in the database.
     */
    public abstract void closeConnection(Object conn)
        throws java.sql.SQLException;
    
    /**
     * Connects to the registry.
     * @param dbelement The Registry DBElement.
     * @throws java.lang.Exception When there is an error.
     * @return A Connection.
     */
    public abstract Object getConnectionRegistry(DBElement dbelement)
        throws java.lang.Exception;
    
    /**
     * Closes a connection to the registry.
     * @param conn The connection to close.
     */
    public abstract void closeConnectionRegistry(Object conn);
    
}
