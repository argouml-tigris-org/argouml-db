/**********************************************
 *   Copyright (C) 2007 CINCOM SYSTEMS, INC.
 *   All Rights Reserved
 **********************************************/
package org.dbuml.base.model;

import java.sql.Types;
import java.util.Collection;
import java.util.Properties;

/**
 * Class for modeling a database column.
 */
public class Column extends DBElement {
    /**
     * JDBC name for data_type. This must be one of the values 
     * in java.sql.Types.
     */
    private String typeNameJdbc = null;
    
    /**
     * The jdbc type of the column.
     */
    protected short jdbcType = Types.NULL;
    
    private static final String PROP_INDEX_NAME = "INDEX_NAME";
    private static final String PROP_PK_NAME = "PK_NAME";
    private static final String PROP_KEY_SEQ = "KEY_SEQ";
    private static final String PROP_LENGTH = "LENGTH"; // for char and string
    private static final String PROP_PRECISION = "PRECISION"; //numeric & dec.
    private static final String PROP_SCALE = "SCALE";
    
    private static final String PROP_DEFAULT = "DEFAULT"; 
    
    
    /**
     * Length of the column. Used only for character and binary data.
     */
    private int length = 0;
    
    private int scale = 0;
    
    private String defValue = "";
    
    /**
     * Database specific type name
     */
    private String typeName = null;
    
    /**
     * True if column was defined with the 'unique' constraint.
     */
    private boolean unique = false;
    private boolean partOfUniqueConstraint = false;
    /**
     * The owning table.
     */
    protected Table table; 
    
    // Does the column allow nulls in the database?
    private boolean allowNulls = true;
    
    
    // the combination of isHomogeneousArray of true, with a name of null
    // means that the status of homogeneous is unknown.
    private boolean homogeneousArray = true;
    private String homogeneousTypeName = null;
    
    private PKeyInterface key;
    
    // ********************************************************************
    // Constructors
    // ********************************************************************
    
    /**
     * Creates a new Column.
     */
    public Column() {
    }
    
    /**
     * Creates a new Column with the given name.
     * @param name The name of the column.
     */
    public Column(final String name) {
        setName(name);
    }
    
    /**
     * Creates a new instance of with a given name, model element,
     * properties.  Used when retrieving elements.
     * @param sName The column name.
     * @param objModel The model.
     * @param properties The properties.
     */
    public Column(final String sName, final Object objModel, 
            final Properties properties) {
        super(sName, objModel, properties);
    }
    
    /**
     * Determines whether or not this column allows null values. Primary keys
     * are excluded in teh check.
     * @return true when this column allows true, false otherwise.
     */
    public boolean allowsNulls() {
        return this.allowNulls;
    }
    
    /**
     * Checks whether this column allows nulls.
     * @param includeKey true to also check for not nulls on primary keys.
     */
    public boolean allowsNulls(boolean includeKey) {
        if (includeKey && isPrimaryKey()) { 
            return false;
            // Primary keys are implicitly not null, but some databases
            // requires it in the the create statement. This method is called
            // by velocity template for attributes for showing NOT NULL
            //on primary keys.
        }
        return allowNulls;
    }
    
    /**
     * Sets the flag of for allowing null values.
     * @param flag true or lase to set the flag.
     */
    public void setAllowsNulls(boolean flag) {
        this.allowNulls = flag;
    }
    
    /**
     * Determines whether or not this column is part of unique constraint.
     * @return true or false value.
     */
    public boolean isPartOfUniqueConstraint() {
        return this.partOfUniqueConstraint;
    }
    
    /**
     * Sets is part of unique constarint flag.
     * @param flag True or false.
     */
    public void isPartOfUniqueConstraint(boolean flag) {
        this.partOfUniqueConstraint = flag;
    }
    
    /**
     * Determines whether or not this column is an array of homogeneuos values.
     * @return true or false.
     */
    public boolean isHomogeneousArray() {
        return this.homogeneousArray;
    }
    
    /**
     * Sets the homogeneous array flag.
     * @param flag true or false value.
     */
    public void isHomogeneousArray(boolean flag) {
        this.homogeneousArray = flag;
    }
    
    /**
     * Gets the type name of the homogeneous array.
     * @return The type name.
     */
    public String getHomogeneousArrayTypeName() {
        return this.homogeneousTypeName;
    }
    
    /**
     * Sets homogeneous array type name.
     * @param name The type name.
     */
    public void setHomogeneousArrayTypeName(final String name) {
        this.homogeneousTypeName = name;
    }
    
    /**
     * Gets the stereotype string.
     * @return The stereotype string.
     */
    public String getStereostring() {
        String val = null;
        if (key != null) {
            val = key.getType();
        } else if (this.isUnique()) {
            val = "Unique";
        }
        return val;
    }
    
    /**
     * Determines whether or not this column was defined with the 'unique' 
     * constraint.
     * @return true or false.
     */
    public boolean isUnique() {
        return this.unique;
    }
    
    public void isUnique(boolean flag) {
        this.unique = flag;
    }
    
    /** Set the index information of this column.
     *@param iname String The index name.
     */
    public void setIndexInfo(final String iname) {
        if (this.getIndexNames().length == 0) {
            this.setProperty(PROP_INDEX_NAME,  iname);
        } else {
            this.setProperty(PROP_INDEX_NAME,
                    this.getProperty(PROP_INDEX_NAME) + ":" + iname);
        }
        if (this.table != null) {
            this.table.recordIndex(this);
        }
    }
    
    /** Get the name of the indexes for this column. An empty array is returned
     * when there is no index.
     *@return An array of index names.
     */
    public String[] getIndexNames() {
        String indexStr = this.getProperty(PROP_INDEX_NAME);
        String[] val = null;
        if (indexStr != null && !indexStr.equals("")) {
            val = indexStr.split(":");
        } else {
            val = new String[] {};
        }
        return val;
    }
    
    /** Determine whether or not this column has a stereotype.
     *@return A boolean for whether or not the stereotype string is set.
     */
    public boolean hasStereotype() {
        return (this.getStereostring() != null);
    }
    
    /** Gets the length or precision of this column.
     *@return an integer length of the column.
     */
    public int getLength() {
        int retLength = this.length;
        boolean typeUnknown = true;
        
        // if it's a string type or it has a length tag but its jdbc type is 
        //never set
        if (this.isStringType() || this.jdbcType == Types.NULL) {
            String val = this.getProperty(PROP_LENGTH);
            if (val != null) {
                try {
                    retLength = Integer.valueOf(val).intValue();
                } catch (NumberFormatException nf) {
                    System.err.println(nf.getMessage());
                }
                typeUnknown = false;
            }
        }
        
        // if it's a decimal type or it has a precision tag but its jdbc type is
        //never set
        if (typeUnknown && (this.isDecimalType() 
                || this.jdbcType == Types.NULL)) {
            String prec = this.getProperty(PROP_PRECISION);
            if (prec != null) {
                try {
                    retLength = Integer.valueOf(prec).intValue();
                } catch (NumberFormatException nf) {
                    System.err.println(nf.getMessage());
                }
            }
        }
        return retLength;
    }
    
    /**
     * Set the length of this column. This is called a precision when the column
     * is numeric. It's recommended to set the jdbc type of this column before
     * calling is method. Otherwise neither the LENGTH nor the PRECISION tag 
     * will be set.
     *@param len The column length.
     *
     */
    public void setLength(int len) {
        this.length = len;
        if (this.isStringType()) {
            this.setProperty(PROP_LENGTH, String.valueOf(len));
        } else if (this.isDecimalType()) {
            this.setProperty(PROP_PRECISION, String.valueOf(len));
        }
    }
    
    /** Gets the scale of this column if it exists or zero if it does not exist.
     *@return An integer scale of the column.
     */
    public int getScale() {
        String val = this.getProperty(PROP_SCALE);
        int retVal = this.scale;
        if (val != null) {
            try {
                retVal = Integer.valueOf(val).intValue();
            } catch (NumberFormatException nf) {
                System.err.println(nf.getMessage());
            }
        }
        return retVal;
    }
    
    public void setScale(int digit) {
        this.scale = digit;
        if (this.isDecimalType() && digit > 0) {
            this.setProperty(PROP_SCALE, String.valueOf(digit));
        }
    }
    
    public String getTypeName() {
        return this.typeName;
    }
    
    public void setTypeName(String name) {
        this.typeName = name;
    }
    
    public String getTypeNameJdbc() {
        return this.typeNameJdbc;
    }
    
    public void setTypeNameJdbc(final String name) {
        this.typeNameJdbc = name;
    }
    
    public final boolean isPrimaryKey() {
        return (this.key != null && (this.key.getType() == PKeyInterface.PK
                || this.key.getType() == PKeyInterface.PFK));
    }
    
    public final boolean isForeignKey() {
        return (this.key != null && this.key.isForeignKey());
    }
    
    public void setKey(final PKeyInterface k) {
        if (this.key != null && k instanceof FKey) {
            ((FKey) k).setColumn(this);
            this.key.setForeignInfo((FKey) k);
        } else {
            this.key = k;
            if (key instanceof FKey) {
                ((FKey) key).setColumn(this);
            } else {
                // set tag values for this primary key.
                this.setProperty(PROP_PK_NAME,  key.getKeyName());
                this.setProperty(PROP_KEY_SEQ,  
                        (new Short(key.getKeySequence())).toString());
            }
        }
    }
    
    public PKeyInterface getKey() {
        return this.key;
    }
    
    public boolean isRef() {
        return (Types.REF == this.jdbcType);
        // TO DO: CLOB, BLOB may also be references
    }
    
    public  boolean isArray() {
        return (Types.ARRAY == this.jdbcType);
    }
    
    public boolean isRelated() {
        return (isRef() && this.typeName != null) || this.isForeignKey();
    }
    
    public String getRelatedTableName() {
        String tName = null;
        if (this.isForeignKey()) {
            tName = this.getKey().getForeignKey().getNativeTableName();
        } else if (this.isRef()) {
            tName = this.getTypeName();
        }
        return tName != null ? fixName(tName) : tName;
        
    }
    
    public boolean inherited() {
        return false;
    }
    
    public void setJdbcType(short type) {
        this.jdbcType = type;
    }
    
    public short getJdbcType() {
        return this.jdbcType;
    }
    
    /** Is this a primary and also a foreign key 
     *@return A boolean for whether or not this column is both primary and 
     * foreign key.
     */
    public boolean isComplexKey() {
        return (key != null && key.getType() == PKey.PFK);
    }
    
    /** Set the table that owns this column.
     *@param t The table that owns thsi column.
     */
    public void setTable(final Table t) {
        this.table = t;
    }
    
    /** Get the Collection of  set of columns that share the same index with 
     * this column. Primary keys are not included in this set.
     * It returns an empty collection when there is no explicit index on this 
     * column or if the table that owns this column is not set yet.
     * @return A collection of indexes.
     */
    public Collection getIndexSets() {
        return this.table.getIndexSets(this);
    }
    
    private boolean isStringType() {
        return (
                java.sql.Types.CHAR == this.jdbcType 
                || java.sql.Types.VARCHAR == this.jdbcType
                || java.sql.Types.BINARY == this.jdbcType   //Added by RGupta
                || java.sql.Types.LONGVARCHAR == this.jdbcType);
    }
    
    private boolean isDecimalType() {
        return (
                java.sql.Types.NUMERIC == this.jdbcType 
                || java.sql.Types.REAL == this.jdbcType 
                || java.sql.Types.DECIMAL == this.jdbcType);
    }
    
    public String toString() { return getName(); }
    
    //Added by RGupta for the purpose of Getting Default Values of Columns
    public void  setDefault(String defaultValue) { 
        this.setProperty(PROP_DEFAULT, defaultValue);
    }
    
    //Added by RGupta for the purpose of Getting Default Values of Columns
    public String getDefault() {
        String defaultValue = this.getProperty(PROP_DEFAULT);
        String val = this.defValue;
        if (defaultValue != null) {
            val = String.valueOf(defaultValue).toString();
        }
        return val;
    }
    
}

