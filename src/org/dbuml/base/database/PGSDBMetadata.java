/**********************************************
 *   Copyright (C) 2007 CINCOM SYSTEMS, INC.
 *   All Rights Reserved
 **********************************************/

package org.dbuml.base.database;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import org.dbuml.base.model.DBElement;
import org.dbuml.base.model.View;
import org.apache.log4j.Logger;


/**
 * Class for Postgresql metadata support.
 * @author  rgupta
 */
public class PGSDBMetadata extends GenericDBMetadata {
    private static final Logger LOG = Logger.getLogger(PGSDBMetadata.class);
    
    /**
     * Creates a new instance of <code>PGSDBMetadata</code>.
     * @param database the <code>Database</code>.
     * @param connection The jdbc connection.
     * @throws java.sql.SQLException When there is a database error.
     * @throws java.lang.ClassNotFoundException When a class is not found.
     * @throws java.lang.IllegalArgumentException When there is an illegal
     * argument.
     */
    public PGSDBMetadata(DBElement database, Object connection)
        throws java.sql.SQLException, java.lang.ClassNotFoundException,
            java.lang.IllegalArgumentException {
        super(database, connection);
        
    }
    
    /**
     * Creates a view and sets the query property.
     * @param name The name of the view.
     * @return An instance of <code>View</code>.
     */
    protected View newView(String name) {
        View view = new View(name);
        try {
            Statement stmt = ((Connection) connection).createStatement();
            ResultSet result;
            String sqlQuery = "";
            result = stmt.executeQuery(
                    "select definition from pg_views where viewname='" 
                    + name + "'");
            for (int i = 0; result.next(); i++) {
                sqlQuery = (String) result.getString(i + 1);
            }
            view.setProperty(view.PROP_SQL, sqlQuery);
            result.close();
        } catch (java.sql.SQLException e) {
            LOG.error("View definition error: " + e.getMessage());
        }
        return view;
    }
    
    
}
