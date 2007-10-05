/**********************************************
 *   Copyright (C) 2007 CINCOM SYSTEMS, INC.
 *   All Rights Reserved
 **********************************************/
package org.dbuml.base.model;

import java.util.Comparator;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

/**
 * Internal class to handle property management and common getter, 
 * setter methods.
 */
public abstract class DBElement {
    /** A map of properties.
     */
    protected Map _props;
    
    /** The element name.
     */
    protected String _sName;
    
    /** The uml model for this element.
     */
    protected Object _objModel;
    
    /** The Database element for this element.
     */
    private Database owningDB = null;
    
    /**
     * default constructor
     */
    public DBElement() {
        this("", null);
    }
    
    /**
     * Creates a new instance of DBElement with the given name.
     * It is used to add a new element to the model.
     * @param sName The element name.
     */
    public DBElement(final String sName) {
        this(sName, null);
    }
    
    /**
     * Creates a new instance of DBElement with the given name.
     * Used to add a new element to the model.
     * @param sName The element name.
     * @param properties A <code>Map</code> of the properties.
     */
    public DBElement(final String sName, final Map properties) {
        if (properties != null) {
            this._props = properties;
        } else {
            this._props = new TreeMap();
        }
        _sName = sName;
    }
    
    /**
     * Creates a new instance of DBElement with a given name, model element,
     * and properties.  Used when retrieving elements.
     * @param sName The element name.
     * @param objModel The the UML model.
     * @param properties A <code>Properties</code>.
     */
    public DBElement(final String sName,
            final Object objModel, 
            final Properties properties) {
        this(sName);
        _objModel = objModel;
        this.setProperties(properties);
    }
    
    /**
     * Sets the properties.
     * @param properties A <code>Properties</code> object.
     */
    protected void setProperties(final Properties properties) {
        if (this._props != null) {
            Enumeration e = properties.propertyNames();
            while (e.hasMoreElements()) {
                String key = (String) e.nextElement();
                this._props.put(key, properties.getProperty(key));
            }
        }
    }
      
    /**
     * Sets a property value. If the property doesn't exist it is created, if it
     * does exist it is overwritten.
     *
     * @param name The Property name
     * @param value The new Property value
     */
    public void setProperty(final String name, final String value) {
        String val = value;
        if (val == null) {
            val = "";
        }
        if (this._props != null) {
            this._props.put(name, val);
        }
    }
    
    
    /**
     * Gets the Property associated with name
     *
     * @param name The name of the property to retrieve.
     * @return The Property value
     */
    public String getProperty(final String name) {
        String value = null;
        if (this._props != null) {
            value = (String) this._props.get(name);
        }
        return value;
    }
    
    /**
     * Gets all properties.
     *
     * @return all properties.
     */
    public Map getProperties() {
        return this._props;
    }
    
    /**
     * Gets the name.
     * @return The name.
     */
    public String getName() {
        return _sName;
    }
    
    
    /**
     * Gets the model element.
     * @return The model element.
     */
    public Object getModelElement() {
        return _objModel;
    }
    
    /**
     * Sets the model element.
     * @param objModel The UML model element.
     */
    public void setModelElement(final Object objModel) {
        _objModel = objModel;
    }
    
    /**
     * Sets the name.
     *
     * @param name The name of element.
     */
    public void setName(final String name) {
        _sName = name;
    }
    
    /**
     * Gets the Stereotype string.
     * @return The Stereotype string.
     */
    public abstract String getStereostring();
    
    
    /**
     * Test equality.  This means our properties are the same and our name is 
     * the same. But we don't test reference to the model element
     * @param dbe A DBElement to compare with this DBElement.
     * @return true when the name and propties of the given dbelement and this
     * element are identical, false otherwise.
     */
    public boolean equals(final DBElement dbe) {
        boolean value = false;
        if (dbe != null 
                && _sName.equals(dbe._sName) 
                &&  _props.size() == dbe._props.size() ) {
            Iterator it = this._props.keySet().iterator();
            value = true;
            while (it.hasNext()) {
                if (!dbe._props.containsKey(it.next())) {
                    value = false;
                    break;
                }
            }
        }
        return value;
        
//        if (dbe != null) {
//            if (_sName.equals(dbe._sName)) {
//                if (_props.size() == dbe._props.size()) {
//                    Iterator it = this._props.keySet().iterator();
//                    while (it.hasNext()) {
//                        if (!dbe._props.containsKey(it.next())) {
//                            return false;
//                        }
//                    }
//                    return true;
//                }
//            }
//        }
//        return false;
    }
    /**
     * Sets the Database instance that owns this DBElement.
     * @param db The <code>Database</code> Element that owns this element.
     */
    public void setOwningDatabase(final Database db) {
        this.owningDB = db;
    }
    
    /**
     * Is the owning <code>Database</code> element set.
     * @return True when the owning <code>Database</code> is set.
     */
    protected boolean hasOwningDatabase() {
        return this.owningDB != null;
    }
    
    /**
     * Gets the owning <code>Database</code> element.
     * @return The <code>Database</code> object.
     */
    protected Database getOwningDatabase() {
        return this.owningDB;
    }
    
    // This is used by methods that need double quoted names.
    /** Get names with possible double quotes.
     *@return The name with double quotes when it is necessary.
     */
    public String getFixedName() {
        String value = this.getName();
        if (this.owningDB != null) {
            value = this.owningDB.fixName(value);
        }
        return value;
    }
    
    /** Adds the preferred quote string around the given identifier when needed.
     *@param identifier An identifier.
     *@return The identifier surrounded by the preferred quote string when it 
     * is necessary.
     */
    public String fixName(final String identifier) {
        String value = identifier;
        if (this.owningDB != null) {
            value = this.owningDB.fixName(value);
        }
        return value;
    }
    
    /** this class provides a comparator that can be used by a sorted Map of 
     * properties.
     */
    protected static class KeyComparator implements Comparator {
        private List orderedPropNames;
        
        /**
         * Creates the Comparator from a List of property names.
         * @param orderedPropertyNames A list of property names.
         */
        public KeyComparator(final List orderedPropertyNames) {
            orderedPropNames = orderedPropertyNames;
        }
        
        /**
         * Compare the two elements.
         * @param o1 A model to compare.
         * @param o2 A model to compare.
         * @return an int value of the result.
         */
        public int compare(Object o1, Object o2) {
            int retVal = 0;
            if (orderedPropNames != null) {
                int index1 = orderedPropNames.indexOf(o1.toString());
                int index2 = orderedPropNames.indexOf(o2.toString());
                if (index1 >= 0 && index2 >= 0) {
                    retVal = index1 - index2;
                } else if (index1 < 0 && index2 >= 0) { //index1 not in the list
                    retVal = index2 - index1; // index 2 comes first
                } else if (index2 < 0 && index1 >= 0) { //index2 not in list
                    retVal = index2 - index1;   // index1 comes first
                }
                retVal = o1.toString().compareTo(o2.toString()); 
            } else {
                retVal = o1.toString().compareTo(o2.toString());
            }
            return retVal;
        }
        
    }
}

