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

/**
 * Class for Oracle metadata support.
 * @author  rgupta
 */
public class OraDBMetadata extends GenericDBMetadata {
    
    /**
     * Creates a new instance of OraDBMetadata.
     * @param database the <code>Database</code>.
     * @param connection The jdbc connection.
     * @throws java.sql.SQLException When there is a database error.
     * @throws java.lang.ClassNotFoundException When a class is not found.
     * @throws java.lang.IllegalArgumentException When there is
     * an illagl argument.
     */
    public OraDBMetadata(DBElement database, Object connection)
        throws java.sql.SQLException, java.lang.ClassNotFoundException,
            java.lang.IllegalArgumentException {
        super(database, connection);
    }
    
    /**
     * Creates a view and sets the query property.
     * @return An instance of <code>View</code>.
     * @param viewName The view name.
     * @param schema The view's schema name.
     * @throws java.sql.SQLException When there is an error in the database.
     */
    public View getView(String viewName, String schema) 
        throws java.sql.SQLException {
        View view = super.getView(viewName, schema);
        Statement stmt = ((Connection) connection).createStatement();
        ResultSet result;
        result = stmt.executeQuery(
                "select text  from user_views where view_name='" 
                + viewName + "'");
        String sqlQuery = "";
        for (int i = 0; result.next(); i++) {
            sqlQuery = (String) result.getString(i + 1) + ";";
        }
        view.setProperty(view.PROP_SQL, sqlQuery);
        result.close();
        stmt.close();
        return view;
    }
    
    
    
}

