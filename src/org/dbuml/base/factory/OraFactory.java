/**********************************************
 *   Copyright (C) 2007 CINCOM SYSTEMS, INC.
 *   All Rights Reserved
 **********************************************/

package org.dbuml.base.factory;
import org.dbuml.base.database.OraReservedWords;
import org.dbuml.base.transform.DBUMLToSQL;    
import org.dbuml.base.transform.OraDBUMLToSQL; 
import org.dbuml.base.database.OraDBMetadata;
import org.dbuml.base.database.DBMetadata;
import org.dbuml.base.model.DBElement;


/**
 * Factory class for Oracle specific transformations and functions.
 * @author  jgrengbondai
 */
public class OraFactory extends GenericFactory {
    
    /** Creates a new instance of OraFactory */
    public OraFactory() {
    }
    
    /**
     * Initializes the list of oracle reserved words.
     */
    public void initDBReservedWords() {
        
        this.dbReservedWords = new OraReservedWords();
    }
    
    /**
     * Gets Oracle factory for DBUMLToSQL transformation.
     * @return A <code>DBUMLToSQL</code> instance.
     */
    public DBUMLToSQL getDBUMLToSQL() {
        if (dbumltosql == null) {
            dbumltosql = (DBUMLToSQL) new OraDBUMLToSQL();
        }
        return dbumltosql;
        
    }
    
    /**
     * Creates a DBMetadata for Oracle database.
     * @param database An instance of <code>Database</code>.
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
        return new OraDBMetadata(database, connection);
    }
    
}
