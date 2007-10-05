/**********************************************
 *   Copyright (C) 2007 CINCOM SYSTEMS, INC.
 *   All Rights Reserved
 **********************************************/

package org.dbuml.base.transform;

import org.dbuml.base.model.Schema;
import org.dbuml.base.model.Table;
import org.dbuml.base.model.View;
import org.dbuml.base.model.Database;
import org.dbuml.base.database.DBMetadata;

/**
 * Abstract class for transforming database entities into DBUML classes.
 * @author jgunderson
 */
public abstract class DBMetadataToDBUML {
    
    /**
     * Creates a new instance of DBMetadataToDBUML.
     */
    public DBMetadataToDBUML() {
    }
    
    /**
     * Transforms a database table into DBUML <code>Table</code>.
     * @param name The table name.
     * @param schema The schema name.
     * @param dbmd The database metadata.
     * @return The DBUML table.
     * @throws java.sql.SQLException When there is a database error.
     */
    public abstract Table table(String name, Schema schema, DBMetadata dbmd)
        throws java.sql.SQLException;
    
    /**
     * Transforms a database view into DBUML <code>View</code>.
     * @param name The view name.
     * @param schema The schema name.
     * @param dbmd Teh database metadata.
     * @throws java.sql.SQLException When there is a database errror.
     * @return The dbuml View.
     */
    public abstract View view(String name, Schema schema, DBMetadata dbmd)
        throws java.sql.SQLException;
    
    /**
     * Updates a DBUML table from a model.
     * @param objTable The table model.
     * @param schemaName The schema name.
     * @param dbmd The metadata
     * @throws java.sql.SQLException When there is a database error.
     */
    public abstract void updateTable(Object objTable, String schemaName,
            DBMetadata dbmd) throws java.sql.SQLException;
    
    /**
     * Updates a DBUML view from a model.
     * @param objTable The view model.
     * @param schemaName The schema name.
     * @param dbmd The metadata.
     * @throws java.sql.SQLException Whene there is a database error.
     */
    public abstract void updateView(Object objTable, String schemaName,
            DBMetadata dbmd) throws java.sql.SQLException;
    
    /**
     * Transforms a database schema into DBUML <code>Schema</code>.
     * @param owner A database model.
     * @param name The schema name.
     * @param dbmd The metadata.
     * @return the DBUML schema.
     */
    public abstract Schema schema(Object owner, String name, DBMetadata dbmd);
    /**
     * Updates a DBUML schema from a model.
     * @param model schema model.
     */
    public abstract void updateSchema(Object model);
    
    /**
     * Transforms a database into DBUML <code>Database</code>.
     * @param owner The parent model.
     * @param name The database name.
     * @param dbmd The metadata.
     * @throws java.lang.Exception When there is an error.
     * @return The DBUML <code>Database</code>.
     */
    public abstract Database database(Object owner,
            String name,
            DBMetadata dbmd)
        throws java.lang.Exception;
    
    /**
     * Updates a DBUML database from a model.
     * @param model The database model.
     * @param dbmd Teh metadata.
     * @throws java.lang.Exception When there is an error.
     */
    public abstract void updateDatabase(Object model, DBMetadata dbmd)
        throws java.lang.Exception;
    
}
