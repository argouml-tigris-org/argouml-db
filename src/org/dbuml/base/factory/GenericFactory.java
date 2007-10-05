/**********************************************
 *   Copyright (C) 2007 CINCOM SYSTEMS, INC.
 *   All Rights Reserved
 **********************************************/

package org.dbuml.base.factory;

import org.dbuml.base.transform.DBUMLToSQL;
import org.dbuml.base.transform.DBMetadataToDBUML;
import org.dbuml.base.transform.GenericDBMetadataToDBUML;
import org.dbuml.base.transform.GenericDBUMLToSQL;
import org.dbuml.base.model.DBElement;
import org.dbuml.base.database.DBMetadata;
import org.dbuml.base.database.GenericDBMetadata;
import org.dbuml.base.database.ConnectionFactory;
import org.dbuml.base.database.GenericConnectionFactory;
import org.dbuml.base.controller.FigureActionsInterface;
import org.dbuml.base.controller.ActionsInterface;
import org.dbuml.base.database.QueryTransaction; 
import org.dbuml.base.database.GenericQueryTransaction; 
import org.dbuml.base.controller.GenericActions;
import org.dbuml.base.controller.GenericFigureActions;
import org.dbuml.base.database.DBReservedWords; 
import org.dbuml.base.database.GenericDBReservedWords; 

/**
 * Generic factory class for creating all transformation factories.
 * @author  jgunderson
 */
public class GenericFactory extends Factory {
    
    /** Creates a new instance of GenericFactory. */
    public GenericFactory() {
    }
    
    /**
     * Creates a DBMetadata.
     * @param database An instance of <code>Database</code>..
     * @param connection The database connection object.
     * @return An instance of <code>DBMetadata</code>.
     * @throws java.sql.SQLException When there is a database error.
     * @throws java.lang.ClassNotFoundException When the factory class
     * is not found.
     * @throws java.lang.Exception For any other error.
     */
    public DBMetadata newDBMetadata(DBElement database, Object connection)
        throws java.sql.SQLException, java.lang.ClassNotFoundException,
            java.lang.Exception {
        return new GenericDBMetadata(database, connection);
    }
    
    /**
     * Gets the factory for DBUMLToSQL transformation.
     * @return A <code>DBUMLToSQL</code> instance.
     */
    public DBUMLToSQL getDBUMLToSQL() {
        if (dbumltosql == null) {
            dbumltosql = (DBUMLToSQL) new GenericDBUMLToSQL();
        }
        return dbumltosql;
    }
    
    /**
     * Gets the factory for DBMetadataToDBUML.
     * @return A <code>DBMetadaToDBUML</code>
     */
    public DBMetadataToDBUML getDBMetadataToDBUML() {
        if (dbmetadatatodbuml == null) {
            dbmetadatatodbuml = 
                    (DBMetadataToDBUML) new GenericDBMetadataToDBUML();
        }
        return dbmetadatatodbuml;
    }
    
    /**
     * Gets the implementation of the ConncetionFactory.
     * @return A <code>ConnectionFactory</code> isnatnace.
     */
    public ConnectionFactory getConnectionFactory() {
        if (connectionfactory == null) {
            connectionfactory = 
                    (ConnectionFactory) new GenericConnectionFactory();
        }
        return connectionfactory;
    }
    
    /**
     * Gets FigureActionsInterface.
     * @return An implimentation of FigureActionsInterface.
     */
    public FigureActionsInterface getFigureActionsInterface() {
        if (figureactionsinterface == null) {
            figureactionsinterface = 
                    (FigureActionsInterface) new GenericFigureActions();
        }
        return figureactionsinterface;
    }
    
    /**
     * Gets QueryTransaction.
     * @return <code>QueryTransaction</code> instance..
     */
    public QueryTransaction getQueryTransaction( ) { 
        return new GenericQueryTransaction();
    }
    
    /**
     * Gets ActionsInterface.
     * @return An implimentation of <code>ActionsInterface</code>.
     */
    public ActionsInterface getActionsInterface() {
        if (actionsinterface == null) {
            actionsinterface = (ActionsInterface) new GenericActions();
        }
        return actionsinterface;
    }
    
    /**
     * Gets DBReservedWords.
     * @return <code>DBReservedWords</code> instance.
     */
    public DBReservedWords getDBReservedWords() {
        if (this.dbReservedWords == null) {
            this.initDBReservedWords();
        }
        return (dbReservedWords );
    }
    
    /**
     * Initializes the list of database reserved words.
     */
    protected void initDBReservedWords() {
        this.dbReservedWords =  new GenericDBReservedWords();
    }
    
}

