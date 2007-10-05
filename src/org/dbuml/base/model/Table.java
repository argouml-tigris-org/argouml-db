/**********************************************
 *   Copyright (C) 2007 CINCOM SYSTEMS, INC.
 *   All Rights Reserved
 **********************************************/

package org.dbuml.base.model;

/**
 * Class for modeling a database table.
 * @author  jgunderson
 */

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;

//import org.apache.log4j.Logger;

/**
 * Class for modeling a database table.
 */
public class Table extends DBElement {
//    private static final Logger LOG = Logger.getLogger(Table.class);
    
    /**
     * A list of Columns.
     */
    protected List columns = new Vector();
    
    private Map indexMap = new Hashtable(); // key= indexName, 
                                            // Values = set of columns
    
    /** Creates a new instance of Table. */
    public Table() {
        super();
    }
    
    /**
     * Creates a new instance of Table with a given name
     *  Use when adding a new element to throws model.
     * @param sName The table name.
     */
    public Table(final String sName) {
        super(sName);
    }
    
    /**
     * Creates a new instance of with a given name, model element,
     * properties.  Used when retrieving elements.
     * @param sName The table name.
     * @param objModel The object model.
     * @param properties The properties.
     */
    public Table(final String sName, final Object objModel, 
            final Properties properties) {
        super(sName, objModel, properties);
    }
    
    /**
     * Get the Stereotype string
     * @return The Stereotype string
     */
    public String getStereostring() {
        return Table.getStereotype();
    }
    
    /**
     * Get columns.
     * @return _columns
     */
    public Column[] getColumns() {
        return (Column[]) this.columns.toArray(new Column[]{});
    }
    
    /**
     * Get columns as a Vector
     * @return _columns as a Vector
     */
    public Vector getColumnsVector() {
        return new Vector(this.columns);
    }
    
    /**
     * Adds a column.
     * @param column The <code>Column</code> to add.
     */
    public void addColumn(final Column column) {
        if (this.hasOwningDatabase()) {
            column.setOwningDatabase(this.getOwningDatabase());
        }
        this.columns.add(column);
    }
    
    
    /**
     * Set the columns.
     * @param cols Array of <code>Column</code>
     */
    public void setColumns(final Column[] cols) {
//        LOG.debug("Setting columns for table " + getName());
        this.columns.clear();
        this.columns.addAll(Arrays.asList(cols));
        // set a ref to this table into the columns
        Iterator it = this.columns.iterator();
        while (it.hasNext()) {
            Column col = (Column) it.next();
            col.setTable(this);
        }
    }
    
    /** Gets a Column on this table.
     *@param name The column name.
     *@return A Column object or null if the given name is not found.
     */
    public Column getColumn(final String name) {
        Column col = null;
        boolean found = false;
        if (name != null) {
            Iterator it = this.columns.iterator();
            while (it.hasNext()) {
                col = (Column) it.next();
                if (name.equals(col.getName())) {
                    found = true;
                    break;
                }
            }
        }
        return (found ? col : null);
    }
    
    /**
     * Puts this column into the appropropriate index set.
     * @param col The column to put into the the idex set.
     */
    public void recordIndex(final Column col) {
        // add this column into the Map of set of columns
        // Note that the map key is the index name, the value is a set of 
        // columns
        // that is, the columns in each set have the same index name.
        
        String[] indexes = col.getIndexNames();
        
        for (int i = 0; i < indexes.length; i++) {
            if (indexes[i] != null) {
                Set set = (Set) this.indexMap.get(indexes[i]);
                if (set == null) {
                    set = new HashSet();
                    set.add(col);
                    this.indexMap.put(indexes[i], set);
                } else {
                    set.add(col);
                }
            }
        }
    }
    
    /** Mark all columns that are explicitly either unique or part of unique 
     * constraints.
     */
    public void markUniqueColumns() {
        // go through each index set and mark those that are part of unique 
        // constraint.
        // Problem solve: some db do not return valid cardinality on
        //DatabaseMetaData.getIndexInfo()
        Iterator it = this.indexMap.values().iterator();
        Set set;
        Column col;
        while (it.hasNext()) {
            set = (Set) it.next();
            Iterator it2 = set.iterator();
            while (it2.hasNext()) {
                col = (Column) it2.next();
                if (set.size() > 1) {
                    col.isPartOfUniqueConstraint(true);
                } else {
                    col.isUnique(true);
                }
            }
        }
    }
    
    /**
     * Gets the collection of set of indexes that goes with the given column
     * @param col The <code>Column</code> instance.
     * @return Collection of set of indexes.
     */
    public Collection getIndexSets(final Column col) {
        Collection isets = new ArrayList();
        String[] indeces = col.getIndexNames();
        for (int i = 0; i < indeces.length; i++) {
            isets.add((Set) this.indexMap.get(indeces[i]));
        }
        return isets;
    }
    
    /**
     * Gets the collection of set of column within this table
     * @return A Collection instance.
     */
    public Collection getIndexSetCollection() {
        return this.indexMap.values();
    }
    
    /**
     * Gets indexMap.
     * @return A Map isntance.
     */
    public Map getIndexMap() {
        return this.indexMap;
    }
    
    /**
     * The stereotype.
     * @return The stereotype string.
     */
    public static String getStereotype() {
        return "Table";
    }
    
    /**
     * Sets the owning database.
     * @param db The database object.
     */
    public void setOwningDatabase(final Database db) {
        super.setOwningDatabase(db);
        // update children
        Iterator it = this.columns.iterator();
        while (it.hasNext()) {
            Column col = (Column) it.next();
            col.setOwningDatabase(db);
        }
    }
    
}
