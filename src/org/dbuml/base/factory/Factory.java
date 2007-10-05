/**********************************************
 *   Copyright (C) 2007 CINCOM SYSTEMS, INC.
 *   All Rights Reserved
 **********************************************/

package org.dbuml.base.factory;

import org.dbuml.base.model.DBElement;
import org.dbuml.base.transform.DBUMLToSQL;
import org.dbuml.base.transform.DBMetadataToDBUML;
import org.dbuml.base.database.DBMetadata;
import org.dbuml.base.database.QueryTransaction;
import org.dbuml.base.database.DBReservedWords;
import org.dbuml.base.database.ConnectionFactory;
import org.dbuml.base.controller.FigureActionsInterface;
import org.dbuml.base.controller.ActionsInterface;
import java.util.Hashtable;

import java.lang.reflect.Constructor;

/**
 * Abstract factory class for creating all transformation factories.
 * @author jgunderson
 */
public abstract class Factory {
    
    private static Hashtable factories;
    
    /**
     * <code>DBUMLToSQL</code> instance.
     */
    protected DBUMLToSQL dbumltosql = null;
    
    /**
     * <code>DBMetadataToDBUML</code> instance.
     */
    protected DBMetadataToDBUML dbmetadatatodbuml = null;
    
    /**
     * <code>ConnectionFactory</code> instance.
     */
    protected ConnectionFactory connectionfactory = null;
    
    /**
     * Instance of <code>ActionsInterface</code>.
     */
    protected ActionsInterface actionsinterface = null;
    
    /**
     * <code>FigureActionsInterface</code> instance.
     */
    protected FigureActionsInterface figureactionsinterface = null;
    
    /**
     * <code>DBReservedWords</code> instance.
     */
    protected DBReservedWords dbReservedWords = null;
    
    
    // initialize packaged factories
    static {
        factories = new Hashtable();
        try {
            Class factClass = Class.forName(
                    "org.dbuml.base.factory.GenericFactory");
            Constructor c = factClass.getConstructor(new Class[] {});
            Factory fact = (Factory) c.newInstance(new Object[] {});
            factories.put("org.dbuml.base.factory.GenericFactory", fact);
            // Add new packaged factories here
        } catch (Exception e) {
            System.err.println("Error initializing DB-UML factories");
        }
    }
    
    /**
     * Gets an instance of the specified factory.
     * @param factoryName The factory name. For example 
     * "org.dbuml.base.factory.GenericFactory".
     * @return An instance of the specified factory.
     */
    public static final Factory getFactory(String factoryName) {
        Factory factory;
        
        if (factoryName == null || factoryName == "") {
            factory = (Factory) factories.get(
                    "org.dbuml.base.factory.GenericFactory");
        } else {
            factory = (Factory) factories.get(factoryName);
            if (factory == null) {
                try {
                    Class factClass = Class.forName(factoryName);
                    Constructor c = factClass.getConstructor(new Class[] {});
                    factory = (Factory) c.newInstance(new Object[] {});
                    factories.put(factoryName, factory);
                } catch (Exception e) {
                    factory = null;
                }
            }
        }
        return factory;
    }
    
    /** Creates a new instance of FactoryLoader */
    public Factory() {
    }
    
    /**
     * Creates a DBMetadata.
     * @param database An instance of <code>Database</code>..
     * @param connection The database connection object.
     * @return An instance of <code>DBMetadata</code>.
     * @throws java.sql.SQLException When there is a database error.
     * @throws java.lang.ClassNotFoundException When the factory 
     * class is not found.
     * @throws java.lang.Exception For any other error.
     */
    public abstract DBMetadata newDBMetadata(DBElement database,
            Object connection) throws java.sql.SQLException,
            java.lang.ClassNotFoundException, java.lang.Exception;
    
    /**
     * Gets the factory for DBUMLToSQL transformation.
     * @return A <code>DBUMLToSQL</code> instance.
     */
    public abstract DBUMLToSQL getDBUMLToSQL();
    
    /**
     * Gets the factory for DBMetadataToDBUML.
     * @return A <code>DBMetadaToDBUML</code>
     */
    public abstract DBMetadataToDBUML getDBMetadataToDBUML();
    
    /**
     * Gets the implementation of the ConncetionFactory.
     * @return A <code>ConnectionFactory</code> isnatnace.
     */
    public abstract ConnectionFactory getConnectionFactory();
    
    /**
     * Gets FigureActionsInterface.
     * @return An implimentation of FigureActionsInterface.
     */
    public abstract FigureActionsInterface getFigureActionsInterface();
    
    /**
     * Gets ActionsInterface.
     * @return An implimentation of <code>ActionsInterface</code>.
     */
    public abstract ActionsInterface getActionsInterface();
    
    /**
     * Gets QueryTransaction.
     * @return <code>QueryTransaction</code> instance..
     */
    public abstract QueryTransaction getQueryTransaction(); 
    
    /**
     * Gets DBReservedWords.
     * @return <code>DBReservedWords</code> instance.
     */
    public abstract DBReservedWords getDBReservedWords(); 
    
    /**
     * Initializes the list of database reserved words.
     */
    protected abstract void initDBReservedWords();  
    
}
