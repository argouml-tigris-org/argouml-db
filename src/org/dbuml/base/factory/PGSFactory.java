/**********************************************
 *   Copyright (C) 2007 CINCOM SYSTEMS, INC.
 *   All Rights Reserved
 **********************************************/

package org.dbuml.base.factory;
import org.dbuml.base.database.PGSDBMetadata;
import org.dbuml.base.database.DBMetadata;
import org.dbuml.base.model.DBElement;

/**
 * Factory class for PostGreSQL specific transformations and functions.
 * @author  rgupta
 */
public class PGSFactory  extends GenericFactory {
    
    /** Creates a new instance of PGSFactory. */
    public PGSFactory() {
    }
    
    /**
     * Creates a DBMetadata for PostgreSQL database.
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
        return new PGSDBMetadata(database, connection);
    }
   
}
