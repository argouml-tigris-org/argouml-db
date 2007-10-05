/**********************************************
 *   Copyright (C) 2007 CINCOM SYSTEMS, INC.
 *   All Rights Reserved
 **********************************************/

package org.dbuml.base.model;

/**
 *
 * @author  jgunderson
 */

import java.util.Properties;
import org.apache.log4j.Logger;

/**
 * Class for modeling views.
 */
public class View extends Table {
    //Added by RGupta
    /**
     * Property label for view SQL.
     */
    public static final String PROP_SQL = "SQL_QUERY";
    
    private static final Logger LOG = Logger.getLogger(View.class);
    
    /** Creates a new instance of View. */
    public View() {
        super();
        setProperty(PROP_SQL, "");
    }
    
    /**
     * Creates a new instance of View with a given name
     *  Use when adding a new element to the model.
     * @param sName The name of the view.
     */
    public View(final String sName) {
        super(sName);
        setProperty(PROP_SQL, "");
    }
    
    /**
     * Creates a new instance of with a given name, model element,
     * properties.  Used when retrieving elements.
     * @param sName The view name.
     * @param objModel The model object.
     * @param properties The view properties.
     */
    public View(final String sName, final Object objModel, 
            final Properties properties) {
        super(sName, objModel, properties);
    }
    
    /**
     * Get the Stereotype string.
     * @return The Stereotype string
     */
    public String getStereostring() {
        return View.getStereotype();
    }
    
    /**
     * The stereotype string.
     * @return The stereotype string.
     */
    public static String getStereotype() {
        return "View";
    }
    
    /**
     * This this the query statement after the "AS" clause in create view 
     * statement
     * @return The query statement when it is available, or null when it is not.
     */
    public String getQueryString() {
        return this.getProperty(PROP_SQL);
    }
    
    /**
     * Sets the query statement.
     * @param sqlQuery The query statement.
     */
    public void setQueryString(final String sqlQuery) {
        this.setProperty(PROP_SQL, sqlQuery);
    }
    
    /**
     * Gets the base tables specified in the query statement.
     * @return An array of base table names.
     */
    public String[] getBaseTablesFromQuery() {
        
        // Parse the following:
        // select:
        //SELECT [ qualifier ] select_list
        //FROM table_list
        //[ WHERE clause ]
        //[ GROUP BY ... HAVING clause ]
        
        
        
        String query = getQueryString();
        String sqlUp = query.toUpperCase();
        int iSelect = sqlUp.indexOf("SELECT");
        int iFrom = sqlUp.indexOf("FROM");
        int iWhere = sqlUp.indexOf("WHERE");
        
        String[] tables = null;
        
        if (iSelect >= 0 && iFrom > 0) {
            String tableList = null;
            if (iWhere > 0) {
                tableList = query.substring(iFrom + 4, iWhere).trim();
            } else {
                tableList = query.substring(iFrom + 4).trim();
            }
            
            if (tableList != null) {
                tables = tableList.split(",");
                for (int i = 0; i < tables.length; i++) {
                    tables[i] = tables[i].trim();
                    int indexOfBlank = tables[i].indexOf(' ');
                    if (indexOfBlank > 0 ) {
                        //product prod
                        // the table name is on the left of the blank.
                        tables[i] = tables[i].substring(0, indexOfBlank);
                    }
                }
            }
            
        } else {
            LOG.info("Invalid view SQL statement:)" + query);
        }
        
        return ((tables != null) ? tables : new String[] {});
    }
    
    
}
