/**********************************************
 *   Copyright (C) 2007 CINCOM SYSTEMS, INC.
 *   All Rights Reserved
 **********************************************/
package org.dbuml.argo.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;
import org.argouml.model.Model;

import org.argouml.kernel.ProjectManager;
import org.argouml.kernel.Project;
import org.dbuml.base.model.DBDependency;

import org.dbuml.base.model.DBModelFacade;
import org.dbuml.base.model.Table;
import org.dbuml.base.model.View;
import org.dbuml.base.model.Schema;
import org.dbuml.base.model.Database;
import org.dbuml.base.model.Registry;
import org.dbuml.base.model.DBElement;
import org.dbuml.base.model.Column;
import org.dbuml.base.model.FKey;
import org.dbuml.base.model.PKey;
import org.dbuml.base.model.PKeyInterface;
import org.dbuml.base.model.DBAssociation;
import org.dbuml.base.model.DBDerive;
import org.dbuml.base.factory.Factory;

import org.apache.log4j.Logger;

/**
 * A facade class for Argo models.
 * @author  jgunderson
 */
public class ArgoDBModelFacade extends DBModelFacade {
    
    private static final Logger LOG = Logger.getLogger(ArgoDBModelFacade.class);
    
    /** Creates a new instance of DBModelFacade */
    public ArgoDBModelFacade() {
        instance = this;
        ProjectManager.getManager().makeEmptyProject();
    }
    
    /**
     * Get instance.  This is set by the implementing class.
     * @return The DBModelFacade.
     */
    public static DBModelFacade getInstance() {
        if (instance == null) {  
            new ArgoDBModelFacade();
        }
        return instance;
    }
    
    /** Recognizer for model elements representing Schemas
     * @param handle candidate
     * @return true if model element represents Schemas
     */
    public boolean representsASchema(Object handle) {
        boolean status = false;
        if (Model.getFacade().isAPackage(handle)) {
            Iterator its = Model.getFacade().getStereotypes(handle).iterator();
            if (its.hasNext()) {
                Object o = its.next();
                status = Schema.STEREOSTRING.equals(Model.getFacade()
                    .getName(o));
            }
        }
        return status;
    }
    
    /** Recognizer for model elements representing Databases
     * @param handle candidate
     * @return true if model element represents Databases
     */
    public boolean representsADatabase(Object handle) {
        boolean status = false;
        if (Model.getFacade().isAComponent(handle)) {
            Iterator its = Model.getFacade().getStereotypes(handle).iterator();
            if (its.hasNext()) {
                Object o = its.next();
                status = Database.STEREOSTRING.equals(Model.getFacade()
                    .getName(o));
            }
        }
        return status;
    }
    
    /** Recognizer for model elements representing Registries
     * @param handle candidate
     * @return true if model element represents Registries
     */
    public boolean representsARegistry(Object handle) {
        boolean status = false;
        if (Model.getFacade().isAComponent(handle)) {
            Iterator its = Model.getFacade().getStereotypes(handle).iterator();
            while (its.hasNext()) {
                Object o = its.next();
                status = Registry.STEREOSTRING.equals(Model.getFacade()
                    .getName(o));
            }
        }
        return status;
    }
    
    /** Recognizer for model elements representing Tables.
     * @param handle candidate
     * @return true if model element represents a Table
     */
    public boolean representsATable(Object handle) {
        boolean status = false;
        if (Model.getFacade().isAClass(handle)) {
            Iterator its = Model.getFacade().getStereotypes(handle).iterator();
            if (its.hasNext()) {
                Object o = its.next();
                status = Table.getStereotype().equals(Model.getFacade()
                    .getName(o));
            }
        }
        return status;
    }
    
    /** Recognizer for model elements representing Views
     * @param handle candidate
     * @return true if model element represents a View
     */
    public boolean representsAView(Object handle) {
        boolean status = false;
        if (Model.getFacade().isAClass(handle)) {
            Iterator its = Model.getFacade().getStereotypes(handle).iterator();
            while (its.hasNext()) {
                Object o = its.next();
                status = View.getStereotype().equals(Model.getFacade()
                    .getName(o));
            }
        }
        return status;
    }
    
    /** Recognizer for model elements representing Columns.
     * @param handle candidate
     * @return true if model element represents a Column
     */
    public boolean representsAColumn(Object handle) {
        boolean status = false;
        if (Model.getFacade().isAAttribute(handle)) {
            Object owner = Model.getFacade().getOwner(handle);
            status = representsADbumlElement(owner);
        }
        return status;
    }
    
    /**
     * Recognizer for any DBUML profile model element
     * @return true if model element represents an element in the DBUML profile
     * @param model The model object.
     */
    public boolean representsADbumlElement(Object model) {
        return representsADatabase(model) 
                || representsASchema(model) 
                || representsATable(model)
                || representsAView(model)
                || representsAColumn(model)
                || representsAFKey(model)
                || representsADBDerive(model)
                || representsADBDependency(model);
    }
    
    /**
     * Gets the DBUML class name such as Database, Schema, Table etc... of the
     * given model.
     * @param handle The model object.
     * @return A DBUML class name such as Database, Schema, Table, Column, etc..
     */
    public String getDBUMLClassName(Object handle) {
        String status = null;
        if (representsADatabase(handle))  { status = Database.STEREOSTRING; }
        else if (representsASchema(handle)) { status = Schema.STEREOSTRING; }
        else if (representsATable(handle)) { status = Table.getStereotype(); }
        else if (representsAView(handle)) { status = View.getStereotype(); }
        else if (representsAColumn(handle))  { status = "Column"; }
        else if (representsAFKey(handle)) { status = "FKey"; }
        else if (representsADBDerive(handle)) { status = "DBDerive"; }
        else if (representsADBDependency(handle)) { status = "DBDependency"; }
        return status;  
    }
    
    /** Recognizer for model elements representing FKey link.
     * @param handle candidate
     * @return true if model element represents a Column
     */
    public boolean representsAFKey(Object handle) {
        boolean status = false;
        if (Model.getFacade().isAAssociation(handle)) {
            Iterator its = Model.getFacade().getStereotypes(handle).iterator();
            if (its.hasNext()) {
                Object o = its.next();
                String stereoName = Model.getFacade().getName(o);
                status = ("FK".equals(stereoName) || "PFK".equals(stereoName));
            }
        }
        return status;
    }
    
    /** Recognizer for model elements representing View dependency
     * @param handle candidate
     * @return true if model element represents a View dependency.
     */
    public boolean representsADBDerive(Object handle) {
        boolean status = false;
        if (Model.getFacade().isADependency(handle)) {
            Iterator its = Model.getFacade().getStereotypes(handle).iterator();
            if (its.hasNext()) {
                Object o = its.next();
                status = DBDerive.getStereotype().equals(
                        Model.getFacade().getName(o));
            }
        }
        return status;
    }
    
    /** Recognizer for model elements representing View dependency
     *
     * @param handle candidate
     * @return true if model element represents a View dependency.
     */
    public boolean representsADBDependency(Object handle) {
        boolean status = false;
        if (Model.getFacade().isADependency(handle)) {
            Iterator its = Model.getFacade().getStereotypes(handle).iterator();
            if (its.hasNext()) {
                Object o = its.next();
                status = DBDependency.getStereotype()
                    .equals(Model.getFacade().getName(o));
            }
        }
        return status;
    }
    
    /**
     * Finds a model element with this name and make Database (if it
     * represents a Database)
     * @param sName the name of the database.
     * @return Database
     */
    public Database findDatabase(String sName) {
        return this.findDatabase(null, sName);
    }
    
    /**
     * Finds a model element with this name with the given parent and make
     * Database (if it
     * represents a Database)
     * @param parentName The node name or null if the the database is 
     * not within a node.
     * @param dbName the databse name.
     * @return Database
     */
    public Database findDatabase(String parentName, String dbName) {
        Object parentModel;
        Object projectModel = ProjectManager.getManager()
            .getCurrentProject().getModel();
        if (parentName != null) {
            parentModel = Model.getFacade().lookupIn(projectModel,
                    parentName);
        } else { 
            parentModel = projectModel;
        }
        Object database = Model.getFacade().lookupIn(parentModel, dbName);
        return ((database != null) ? getDatabase(database) : null);
    }
    
    /**
     * Gets the Database element that "owns" this element
     * @param element The model element.
     * @return Database Object.
     */
    public Database getOwningDatabase(Object element) {
        Database db = null;
        if (element != null) { 
            db = representsADatabase(element)
                ? getDatabase(element)
                : getOwningDatabase(getParent(element));
        }
        return db;
    }
    
    /**
     * Gets the registry model element that "owns" this element and make
     * a Registry
     * @param element The model element.
     * @return An instance of Registry.
     */
    public Registry getOwningRegistry(Object element) {
        Registry r = null;
        if (element != null) {
            r = representsARegistry(element)
                ? getRegistry(element)
                : getOwningRegistry(getParent(element));
        }
        return r;
    }
    
    /**
     * Gets the Factory implementation for this element.  This is the Factory
     * for the
     * owning Database or the Registry (if this is a Registry)
     * @param element The model element.
     * @return the Factory.
     */
    public Factory getMyFactory(Object element) {
        Factory fact = null;
        if (element != null) {
            DBElement dbelement;
            if (representsARegistry(element)) {
                dbelement = (DBElement) DBModelFacade.getInstance()
                    .getRegistry(element);
            } else {
                dbelement = (DBElement) DBModelFacade.getInstance()
                    .getOwningDatabase(element);
            }
            if (dbelement != null) {
                fact = Factory.getFactory(dbelement.getProperty(
                        Database.FACTORY));
            }
        }
        return fact;
    }
    
    /**
     * Get the "Parent" element that "owns" this element. A Database 
     * model does not have parent.
     * @param element The model element.
     * @return a Model element Object or null.
     */
    public Object getParent(Object element) {
        Object parent = null;
        if (element != null) {
            if (representsATable(element) || representsAView(element)) {
                parent = getParentForTableLike(element);
            } else if (representsASchema(element)) {
                parent = getParentForSchema(element);
            } else if (representsAColumn(element)) {
                parent = Model.getFacade().getOwner(element);
            } else if (representsADatabase(element)) {
                parent = getParentForDatabase(element);
            }
            // Registry does not have parent. Does it?
        }
        return parent;
    }
    
    /**
     * Gtes the parent for a Database model.
     * @param element A database or registry model.
     * @return The parent model or null.
     */
    protected final Object getParentForDatabase(final Object element) {
        Iterator iter = Model.getFacade().getClientDependencies(
                element).iterator();
        Object parent = null;
        while ( iter.hasNext() ) {
            Object dep =  iter.next();
            Collection suppliers = Model.getFacade().getSuppliers(dep);
            Iterator iter1 = suppliers.iterator();
            // scan all suppliers and make sure that we do not have
            //prevented nodes.
            int dbcount = 0;
            while (iter1.hasNext()) {
                Object node = iter1.next();
                if (this.representsARegistry(node)) {
                    dbcount++;
                } else {
                    dbcount = 0;
                    break;
                }
            }
            // more than one registry is confusing.
            if (dbcount == 1) {
                //We have at most one registry supplier here
                Iterator iter2 = suppliers.iterator();
                if (iter2.hasNext()) { // only one database here
                    parent =  iter2.next();
                }
            }
        }
        return parent;
    }
    
    /**
     * Gets the schema for table like models. Table Like are table, view, etc ..
     * @param element A model for a table or view.
     * @return The schema model or null.
     */
    protected final Object getParentForTableLike(final Object element) {
        Object ns = Model.getFacade().getNamespace(element);
        Object parent = null;
        if (representsASchema(ns)) {
            parent = isValidSchema(ns) ? ns : null;
        }
        return parent;
    }
    
    /**
     * Gets the child elements of this element.
     * @param element The model element.
     * @return a Collection of children.
     */
    public Collection getChildren(Object element) {
        Vector vChildren = new Vector();
        //
        //  We need to know the owning Database to see if schemas are supported.
        //  And a valid structure must have an owning database.
        //
        Database db = getOwningDatabase(element);
        if (db != null) {
            if (representsADatabase(element)) {
                Iterator iter = Model.getFacade()
                    .getSupplierDependencies(element).iterator();
                while ( iter.hasNext() ) {
                    Object dep =  iter.next();
                    Collection clients = Model.getFacade().getClients(dep);
                    Iterator iter1 = clients.iterator();
                    while (iter1.hasNext()) {
                        Object node = iter1.next();
                        if (representsASchema(node)) {
                            if (db.getProperty(
                                Database.SUPPORTS_SCHEMA)
                                .equalsIgnoreCase("true")
                                || db.isDefaultSchema(
                                    Model.getFacade().getName(node))) {
                                vChildren.add(node);
                            }
                        }
                    }
                }
            } else if (representsASchema(element)) {
                Iterator iter 
                    = Model.getFacade().getOwnedElements(element).iterator();
                while ( iter.hasNext() ) {
                    Object node = iter.next();
                    if (representsATable(node) || representsAView(node)) {
                        vChildren.add(node);
                    }
                }
            }
        }
        return vChildren;
    }
    
    /**
     * Finds the identified child model within the parent model.
     * @return The child's model element or null if the child is not found.
     * @param name The name of the model.
     * @param parentModel The parent model object.
     */
    public Object findChild(String name, Object parentModel) {
        Collection col = this.getChildren(parentModel);
        Iterator it = col.iterator();
        Object child = null;
        boolean found = false;
        while (it.hasNext() && !found) {
            child = it.next();
            if (name.equals(Model.getFacade().getName(child))) {
                found = true;
            }
        }
        return (found ? child : null);
    }
    
    /**
     * Gets the child elements of this element that should be updated in a
     * catalog update.
     * @param element The model element.
     * @return a Collection of children.
     */
    public Collection getChildrenForUpdate(Object element) {
        return getChildren(element);
    }
    
    private Object getParentForSchema(Object element) {    
        Object parent = null;    
        if (representsASchema(element) && !representsASchema(
                Model.getFacade().getNamespace(element))) {
        
            Iterator iter = Model.getFacade().getClientDependencies(
                    element).iterator();
            while ( iter.hasNext() ) {
                Object dep =  iter.next();
                Collection suppliers = Model.getFacade().getSuppliers(dep);
                Iterator iter1 = suppliers.iterator();
                // scan all suppliers and make sure that we do not have
                // prevented nodes.
                int dbcount = 0;
                while (iter1.hasNext()) {
                    Object node = iter1.next();
                    if (validSchemaDependencySupplier(node)) {
                        dbcount++;
                    } else {
                        dbcount = 0;
                        break;
                    }
                }

                // more than one database (supplier) is confusing.
                if (dbcount == 1) {
                    //We have at most one database supplier here.
                    Iterator iter2 = suppliers.iterator();
                    if (iter2.hasNext()) {
                        Object endObj = iter2.next();
                        if (representsASchema(endObj)) {
                            parent = null;
                        } else {
                            parent = endObj;
                        }
                        break;
                    }
                }
            }
        }
        return parent;
    }
    
    /**
     * Is the given model a valid schema model?
     * @param model Teh model object
     * @return <code>true</code> when the model is a valid schema model,
     * or <code>false</code> when it is not.
     */
    protected boolean isValidSchema(Object model) {
        //we've already checked that the model is a schema
        boolean status = false;
        // it must be a default schema or its database must support schema
        if (isDefaultSchema(model)) {
            status = true;
        } else {
            Database db = getOwningDatabase(model);
            if ((db != null)
                    && db.getProperty(
                        Database.SUPPORTS_SCHEMA).equalsIgnoreCase("true")) {
                status = true;
            }
        }
        return status;
    }
    
    /**
     * Gets the database namespace.  This is usually (always?) the owning
     * Schema name.
     * But only if the database supports schemas as defined in the 
     * owning database or
     * owning local database model element and only if it is not 
     * the default Schema.
     * The default Schema means the name is not qualified.
     * @param element The model object.
     * @return The database namespace.
     */
    public String getDBNamespace(Object element) {
        Schema schema = getOwningSchema(element);
        String val = "";
        if (schema != null) {
            if (!isDefaultSchema(schema.getModelElement())) {
                val = schema.getName();
            }
        }
        return val;
    }
    
    /**
     * Gets the element name.
     * @param element The model element.
     * @return a Model element name.
     */
    public String getName(Object element) {
        return Model.getFacade().getName(element);
    }
    
    /**
     * Gets the element type.
     * @param element The model element.
     * @return a Model element type.
     */
    public Object getType(Object element) {
        return Model.getFacade().getType(element);
    }
    
    /**
     * Returns a named object in the given object by calling it's
     * lookup method.
     *
     * @param handle the object that we search through
     * @param name of the model element
     * @return found object, null otherwise
     */
    public Object lookupIn(Object handle, String name) {
        return Model.getFacade().lookupIn(handle, name);
    }
    
    /**
     * Gets the owning Schema. But only if the database supports schemas
     * as defined
     * in the owning database or if it is the default Schema.
     *
     * @param element The model object.
     * @return Schema
     */
    public Schema getOwningSchema(Object element) {
        Schema schema = null;
        if (element != null) {
            if (representsASchema(element)) {
                schema = isValidSchema(element) ? getSchema(element) : null;
            } else {
                schema = getOwningSchema(getParent(element));
            }
        }
        return schema;
    }
    
    /**
     * Verify whether the specified model represents an accepted supplier
     * of a schema's dependency. Only a database model is accepted here.
     * @param element The model to verify.
     * @return <code>true</code> when it a valid dependency supplier,
     * <code>false</code> otherwise.
     */
    protected boolean validSchemaDependencySupplier(Object element) {
        return representsADatabase(element);
    }
    
    /**
     * Makes Database from model object.
     * @return Database A Database object or null when the handle does
     * not represent a model for a database.
     * @param handle the model object. 
     */
    public Database getDatabase(Object handle) {
        if (!(representsADatabase(handle))) { return null; } 
        return new Database(Model.getFacade().getName(handle),
                handle, getTags(handle));
    }
    
    /**
     * Makes Registry from model object.
     * 
     * @return Registry A Registry object or null when the handle
     * does not represent a model for a registry.
     * @param handle The model object.
     */
    public Registry getRegistry(Object handle) {
        if (!(representsARegistry(handle))) { return null; }   
        return new Registry(Model.getFacade().getName(handle),
                handle, getTags(handle));
    }
    
    /** It clears this table before updating. It removes attributes
     * and associations.
     * @param objTable The table model.
     */
    protected void clearTable(Object objTable) {
        // remove existing attributes
        Iterator it = Model.getFacade().getAttributes(objTable).iterator();
        while (it.hasNext()) {
            Model.getUmlFactory().delete(it.next());
        }
        // remove existing associations
        it = Model.getFacade().getAssociationEnds(objTable).iterator();
        while (it.hasNext()) {
            Object assoEnd = it.next();
            
            if (!Model.getFacade().isNavigable(assoEnd)) {
                Model.getUmlFactory().delete(
                        Model.getFacade().getAssociation(assoEnd));
            }
        }
        // remove constraints
        it = Model.getFacade().getConstraints(objTable).iterator();
        while (it.hasNext()) {
            Model.getUmlFactory().delete(it.next());
        }
    }
    
    /**
     * Build this table by updating its properties and adding attributes.
     * 
     * @param table The table instance to build.
     * @param objOwner  The schema model.
     */
    protected void buildTable(Table table, Object objOwner) {
        LOG.debug("Building table " + table.getName());
        // Now use the updateDBElement method to set Stereotype and tags
        updateDBElement(table);
        // Add the attributes from column definitions
        addAttributes(table, objOwner);
    }
    
    /**
     *  Creates a new model element representing a table from a 
     * Table object having
     *  a owner model element.
     * @param table An instance of <code>Table</code> to be added.
     * @param objOwner The model owner of this table which is most likely 
     * to be a model of a schema.
     */
    public void addTable(Table table, Object objOwner) {
        Object objTable = Model.getCoreFactory().buildClass(
                table.getName(), objOwner);
        table.setModelElement(objTable);
        this.buildTable(table, objOwner);
    }
    
    /**
     *  Creates a new model element representing a view from a 
     * View object having
     *  a owner model element.
     * @param view An instance of the <code>View</code> to add.
     * @param objOwner The model owner.
     **/
    public void addView(View view, Object objOwner) {
        this.addTable(view, objOwner);
        //Add view bases (create Derive links).
        addViewBases(view, objOwner);
    }
    
    /**
     *  Updates a model element representing a table from a
     * <code>Table</code> object.
     * @param table A <code>Table</code> instance.
     */
    public void updateTable(Table table) {
        LOG.debug("Updating table " + table.getName());
        this.clearTable(table.getModelElement());
        this.buildTable(table, this.getParent(table.getModelElement()));
    }
    
    /**
     *  Updates a model element representing a view from
     * a <code>View</code> object.
     * @param view A <code>View</code> instance.
     **/
    public void updateView(View view) {
        this.updateTable(view);
        addViewBases(view, this.getParent(view.getModelElement()));
    }
    
    /** Adds the derive link between a view and its base source tables when the
     * view query is available.
     * 
     * @param view <code>View</code> instance.
     * @param objOwner The schema model object.
     */
    protected void addViewBases(View view, Object objOwner) {
        // Create Derive links
        String[] baseNames = view.getBaseTablesFromQuery();
        for (int i = 0; i < baseNames.length; i++) {
            Object viewModel = view.getModelElement();
            Object baseModel = Model.getFacade().lookupIn(
                    objOwner, baseNames[i]);
            if (baseModel == null) {
                // The dummy base is a table by default. 
                // It's automatically updated
                // once the user explicitly imports it from the catalog.
                String tSchema = null;
                String tName = baseNames[i];
                int indexOfPeriod = tName.indexOf('.');
                if (indexOfPeriod > 0) {
                    tSchema = tName.substring(0, indexOfPeriod);
                    tName = tName.substring(indexOfPeriod + 1);
                }
                Table baseTable = new Table(tName);
                
                // TO DO: owner is really the schema model identified by tSchema
                // when tSchema is not null.
                
                Object baseTableOwner = objOwner;
                if (tSchema != null) {
                    // get the identified scheama or create it. 
                    // Its parent should be the contained database.
                    baseTableOwner = this.lookupIn(
                        this.getParent(objOwner), tSchema);
                    if (baseTableOwner == null) {
                        Schema schema = new Schema(tSchema);
                        this.addSchema(new Schema(tSchema),
                            getOwningDatabase(objOwner));
                        baseTableOwner = schema.getModelElement();
                    }
                }
                this.addTable(baseTable, baseTableOwner);
                baseModel = baseTable.getModelElement();
            }
            Object dep = Model.getCoreFactory().buildDependency(
                viewModel, baseModel);
            Model.getCoreHelper().setNamespace(dep, objOwner);
            DBDerive derive = new DBDerive(dep);
            derive.setName(
                Model.getFacade().getName(
                    viewModel) + "->" + Model.getFacade().getName(baseModel));
            this.updateDBDerive(derive);
        }
    }
    
    /** Builds and adds attributes to a <code>Table</code>.
     * 
     * @param table The <code>Table</code> object.
     * @param objOwner The model object of the table schema.
     */
    protected void addAttributes(Table table, Object objOwner) {
        LOG.debug("Adding attributes to model of table " + table.getName());
        Column columns[] = table.getColumns();
        for (int i = 0; i < columns.length; i++) {
            processColumn(columns[i], table.getModelElement());
        }
        this.addConstraintsToModel(table);
    }
    
    /** Adds constraints to a table.
     * 
     * @param table The <code>Table</code> instance.
     */
    protected void addConstraintsToModel(Table table) {
        // add unique constraints for each of the index set in the table.
        // The primary keys were not added into the sets.
        Map indexMap = table.getIndexMap();
        Iterator it = indexMap.keySet().iterator();
        Set setOfColumns = null;
        while (it.hasNext()) {
            String indexName = (String) it.next();
            setOfColumns = (Set) (indexMap.get(indexName));
            // 1. convert the set of columns into a set of column names
            // 2. extract the index name
            Set colNames = new HashSet();
            Iterator colIter = setOfColumns.iterator();
            Column column = (Column) colIter.next();
            colNames.add(column.getName());
            while (colIter.hasNext()) {
                colNames.add(((Column) colIter.next()).getName());
            }
            // 3. invoke addConstraintUnique
            this.addConstraintUnique(table.getModelElement(),
                colNames, indexName);
        }
    }
    
    /** Add unique constraint for attributes on the given table model.
     *@param tableModel The table model
     *@param setOfColumnNames A set of attribute names
     *@param indexName Name of the index on the given columns.
     */
    public void addConstraintUnique(final Object tableModel,
            final Set setOfColumnNames, String indexName) {
        Object mc = this.buildConstraintUnique(
            Model.getFacade().getName(tableModel), setOfColumnNames, indexName);
        addConstraintToModel(mc, tableModel);
    }
    
    /**It adds the column to the table, adds foreign key relation when the 
     * column is a foreign key, and adds not null constraints when necessary.
     * 
     * @param column The column to process.
     * @param tableModel The model of the table in which the column belongs.
     */
    protected void processColumn(Column column, Object tableModel) {
        LOG.debug("Processing column " + column.getName());
        addColumn(column, tableModel);
        // Also, show an association for a foreign.(unusual UML modeling!)
        if (column.isForeignKey()) {
            this.addForeignKeyRelation(tableModel, column);
        }
        // add explicit contraints such as not null, unique etc..
        // note that primary keys, foreign keys are implicitly not null
        if (!column.isPrimaryKey() && !column.isForeignKey() ) {
            if (!column.allowsNulls()) {
                this.addConstraintNotNull(column.getName(), tableModel);
            }
        }
    }
    
    /**
     * Adds Not Null for the column constraint on the specified table model.
     * @param columnName The column name.
     * @param tableModel The table model.
     */
    public void addConstraintNotNull(String columnName, Object tableModel) {
        Object mc = buildConstraintNotnull( 
            Model.getFacade().getName(tableModel),
            columnName);
        addConstraintToModel(mc, tableModel);
    }
    
    private void addConstraintToModel(Object mc, Object model) {
        if (mc != null) {
            Model.getCoreHelper().addConstraint(model, mc  );
            // add namespace
            Model.getCoreHelper().addOwnedElement(
                Model.getFacade().getNamespace(model), mc);
        }
    }
    
    private Object buildConstraintNotnull(String context, String attrName) {
        StringBuffer buf = new StringBuffer(50);
        buf.append("context ");
        buf.append(context);
        buf.append(" inv ");
        // it seems like the constraint name must be lowercase,
        String constraintName = attrName.toLowerCase() + "_notnull";
        buf.append(constraintName);
        buf.append(": self.");
        //the attribute name must be lowercase, 
        //the parser gives an exception when it's not.
        buf.append(attrName);
        buf.append("->notEmpty");
        return this.createOCLConstraint(constraintName, buf.toString());
    }
    
    private Object buildConstraintUnique(
            String context, final Set setOfColNames, String indexName) {
        if (setOfColNames.size() == 0) {
            return null;
        }
        StringBuffer buf = new StringBuffer("context ");
        buf.append(context);
        buf.append(" inv ");
        Iterator it = setOfColNames.iterator();
        String colName = (String) it.next();
        String constraintName = indexName.toLowerCase(); // argo's OCL
        buf.append(constraintName);
        buf.append(": ");
        buf.append(context);
        if (setOfColNames.size() == 1) { // simpler form
            buf.append(".allInstances->isUnique(");
            buf.append(colName);
            buf.append(')');
        } else if (setOfColNames.size() > 1) {
            buf.append(".allInstances->forAll (x,y | x<>y implies (x.");
            buf.append(colName);
            buf.append("<>y.");
            buf.append(colName);
            while (it.hasNext()) {
                colName = (String) it.next();
                buf.append(" and x.");
                buf.append(colName);
                buf.append("<>y.");
                buf.append(colName);
            }
            buf.append(" ))");
        }
        return this.createOCLConstraint(constraintName, buf.toString());
    }
    
    private Object createOCLConstraint(String constraintName, String expr) {
        Object bexpr = Model.getDataTypesFactory().createBooleanExpression(
            "OCL", expr);
        Object mc = Model.getCoreFactory().buildConstraint(
            constraintName, bexpr);
        return mc;
    }
    
    /**
     * Adds column to the table or view model.
     * @param column The <code>Column</code> to add.
     * @param tableModel The table or view model object.
     * @param forView Boolean flag. True for adding the column into a view,
     * and false for adding it into a table.
     */
    public void addColumn(Column column, Object tableModel, boolean forView) {
        Project p = ProjectManager.getManager().getCurrentProject();
        // If the database specific name (type_name) is defined as a type
        // in the model then use it.
        // Else, if data_type maps to a name in java.sql.types then use.
        // Else, die trying to use a null
        // 
        Object mType = null;
        String typeName = column.getTypeName();
        if (typeName != null) {
            if (forView) {
                mType = p.findType(typeName, true);
            } else {
                if (java.sql.Types.OTHER == column.getJdbcType() ) {
                    // add user defined data type. 
                    //for example oracle's TIMESTAMP(n)
                    mType = p.findType(typeName.toUpperCase(), false);
                    if (mType == null) {
                        // Create a datatype
                        mType = Model.getCoreFactory().buildDataType(
                                typeName.toUpperCase(),
                               getPackage(getOwningDatabase(
                                tableModel).getDefaultTypesPkgName()));
                    }
                } else {
                   mType = p.findType(typeName.toUpperCase(), false);
                }
            }
        }
        if (mType == null) {
            if (column.getTypeNameJdbc() != null) {
                mType = p.findType(
                    column.getTypeNameJdbc().toUpperCase(), false);
            }
        }
        Object model =
                ProjectManager.getManager().getCurrentProject().getModel();
        Object mAttr =
                Model.getCoreFactory().buildAttribute(tableModel, mType);
        Model.getCoreHelper().addFeature(tableModel, mAttr);
        setVisibilityDefault(mAttr);
        column.setModelElement(mAttr);
        this.updateDBElement(column);
    }
    
    /**
     * Sets the visibility of this model to default or package.
     *@param The object model.
     */
    protected void setVisibilityDefault(Object model) {
        Model.getCoreHelper().setVisibility(
                    model,
                    Model.getVisibilityKind().getPackage());         
    }
    
    /**
     * Adds Column to a model elemen t.
     * @param column The <code>Column</code> object.
     * @param tableModel The model object of the table.
     */
    public void addColumn(Column column, Object tableModel) {
        addColumn(column, tableModel, false);
    }
    
    private void addForeignKeyRelation(Object fromTable, Column col) {
        if (!col.isRelated()) { return; }
        Object objOwner =  getParent(fromTable);
        if (objOwner == null) { return; }
        Object toTable = Model.getFacade().lookupIn(objOwner,
            col.getRelatedTableName());
        if (toTable == null) {
            LOG.debug("****Unable to find the model for table " 
                + col.getRelatedTableName() + ". So, create it!!!!!");
            Table t = new Table(col.getRelatedTableName());
            this.addTable(t, objOwner);
            toTable = t.getModelElement();
        }
        String assoName = col.getName();
        if (!this.hasAssociation(fromTable, assoName)) {
            Object asso = this.buildAssociation(fromTable, toTable,  assoName);
            if (col.isForeignKey()) {
                FKey fkey = col.getKey().getForeignKey();
                fkey.setModelElement(asso, fromTable, toTable);
                this.updateDBAssociation(fkey);
            }
        }
    }
    
    /**
     *  Updates a model element representing a Derive dependency.
     * @param derive A <code>DBDerive</code> instance.
     **/
    public void updateDBDerive(DBDerive derive) {
        // Move the source model into the namespace of the target.
        Object source = derive.getSourceModel();
        Object target = derive.getTargetModel();
        if (source != null && target != null) {
            Object schema = this.getParent(source);
            if (schema == null) {
                schema = this.getParent(target);
                if (schema != null) {
                    Model.getCoreHelper().setNamespace(source, schema);
                }
            }
        }
        this.updateDBElement(derive);
    }
    
    /**
     *  Updates a model element representing a schema dependency.
     * @param depend A <code>DBDependency</code> instance.
     **/
    public void updateDBDependency(DBDependency depend) {
        // Move the source model into the namespace of the target.
        Object source = depend.getSourceModel();
        Object target = depend.getTargetModel();
        if (source != null && target != null) {
            depend.setName(getName(source) + "->" + getName(target));
            if (this.representsASchema(source) 
                && this.representsADatabase(target)) {
                Model.getCoreHelper().setNamespace(source, target);
                Model.getCoreHelper().setNamespace(
                    depend.getModelElement(), source);
            }
        }
        this.updateDBElement(depend, false);
    }
    
    /**
     *  Updates a model element representing an association.
     * @param dbAsso A <code>DBAssociation</code> instance.
     **/
    public void updateDBAssociation(DBAssociation dbAsso) {
        if (dbAsso != null) {
            Object asso = dbAsso.getModelElement();
            if (asso == null) { return; } // display error?
            
            // decorate the source end
            Object sourceModel = dbAsso.getSourceModel();
            if (sourceModel != null) {
                Object fromEnd = Model.getFacade().getAssociationEnd(
                    sourceModel,  asso);
                String sourceEndName = dbAsso.getSourceEndName();
                if (sourceEndName != null) {
                    Model.getCoreHelper().setName(fromEnd, sourceEndName);
                }
                String sourceEndStereoStr = dbAsso.getSourceEndStereoString();
                if (sourceEndStereoStr != null) {
                    this.addStereotype(sourceEndStereoStr, fromEnd);
                }
            }
            
            // decorate the source end
            Object targetModel = dbAsso.getTargetModel();
            if (targetModel != null) {
                Object fromEnd = Model.getFacade().getAssociationEnd(
                    sourceModel,  asso);
                String sourceEndName = dbAsso.getSourceEndName();
                if (sourceEndName != null) {
                    Model.getCoreHelper().setName(fromEnd, sourceEndName);
                }
                String sourceEndStereoStr = dbAsso.getSourceEndStereoString();
                if (sourceEndStereoStr != null) {
                    this.addStereotype(sourceEndStereoStr, fromEnd);
                }
                
                // add multiplicity
                if (dbAsso.isSourceMultiplicityAtMostOne()) {
                    Model.getCoreHelper().setMultiplicity(fromEnd, "0_1");
                } else if (dbAsso.isSourceMultiplicityAtLeastOne()) {
                    Model.getCoreHelper().setMultiplicity(fromEnd, "1_N");
                } else if (dbAsso.isSourceMultiplicityZeroOrMore()) {
                    Model.getCoreHelper().setMultiplicity(fromEnd, "0_N");
                }
                // default is 1_1
                
                if (dbAsso.isSourceOrdered()) {
                    // add Ordering to fromEnd
                    Model.getCoreHelper().setOrdering(fromEnd,
                        Model.getOrderingKind().getOrdered());
                }
            }
//            if (targetModel != null) {
//                Object toEnd = Model.getFacade().getAssociationEnd(
//                    targetModel,  asso);
//                String targetEndName = dbAsso.getTargetEndName();
//                if (targetEndName != null) {
//                    Model.getCoreHelper().setName(toEnd, targetEndName);
//                }
//                String targetEndStereoStr = dbAsso.getTargetEndStereoString();
//                if (targetEndStereoStr != null) {
//                    this.addStereotype(targetEndStereoStr, toEnd);
//                }
//                
//                // add multiplicity
//                if (dbAsso.isTargetMultiplicityAtMostOne()) {
//                    Model.getCoreHelper().setMultiplicity(toEnd, "0_1");
//                } else if (dbAsso.isTargetMultiplicityAtLeastOne()) {
//                    Model.getCoreHelper().setMultiplicity(toEnd, "1_N");
//                } else if (dbAsso.isTargetMultiplicityZeroOrMore()) {
//                    Model.getCoreHelper().setMultiplicity(toEnd, "0_N");
//                }
//                // default is 1_1
//                
//                if (dbAsso.isTargetOrdered()) {
//                    // add Ordering to toEnd
//                    Model.getCoreHelper().setOrdering(toEnd,
//                        Model.getOrderingKind().getOrdered());
//                }
//            }
            
            this.updateDBElement(dbAsso);
        }
    }
    
    /** Build an association link between the given modles.
     * 
     * @param from The starting model.
     * @param to The ending model.
     * @param name The name of the association.
     * @return An assocation model or null when there is an error.
     */
    protected Object buildAssociation(Object from, Object to, String name) {
        try {
            Object asso = Model.getUmlFactory().buildConnection(
                    Model.getMetaTypes().getAssociation(),
                    from, null, to, null, Boolean.TRUE, null);
            Model.getCoreHelper().setName(asso,  name);
            return asso;
        } catch (Exception e) {
            LOG.debug (e.getMessage());
        }
        return null;
    }
    
    /**Determines whether or not there is a named association at the
     * given model.
     * 
     * @param from The model from which to check the named association.
     * @param assoName The association name.
     * @return <code>true</code> when the named association is found,
     * <code>false</code> otherwise.
     */
    protected boolean hasAssociation(Object from, String assoName) {
        Iterator it = Model.getFacade().getAssociationEnds(from).iterator();
        while (it.hasNext()) {
            Object assoEnd = (Object) it.next();
            Object asso = Model.getFacade().getAssociation(assoEnd);
            if (assoName.equalsIgnoreCase(Model.getFacade().getName(asso))) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Gets an object model of a named association end.
     * 
     * @param from The model from which to get the named association end.
     * @param assoName The association name.
     * @return The assocation end model or null.
     */
    protected Object getAssociationEnd(Object from, String assoName) {
        Iterator it = Model.getFacade().getAssociationEnds(from).iterator();
        while (it.hasNext()) {
            Object assoEnd = (Object) it.next();
            Object asso = Model.getFacade().getAssociation(assoEnd);
            if (assoName.equalsIgnoreCase(Model.getFacade().getName(asso))) {
                return assoEnd;
            }
        }
        return null;
    }
    
    /**
     *  Create a new model element representing a schema from a Schema
     * object.
     * @param schema An instance of the <code>Schema</code> to add. 
     * @param owner The model owner. 
     */
    public void addSchema(Schema schema, Object owner) {
        // Make a package and
        Object objSchema = Model.getModelManagementFactory().buildPackage(
            schema.getName());
        Model.getCoreHelper().setNamespace(objSchema, owner); 
        schema.setModelElement(objSchema);
        
        // now use the updateDBElement method to set Stereotype and tags
        updateDBElement(schema);
        
        // create a dependency from the schema element to the database element.
        Object dep = Model.getCoreFactory().buildDependency(objSchema, owner);
        
        DBDependency dbdep = new DBDependency(dep, objSchema, owner);
        updateDBDependency(dbdep);
    }
    
    
    /**
     *  Create a new model element representing a database from a Database
     * object.
     * @param database An instance of the <code>Database</code> to add. 
     * @param owner The model owner.
     **/
    public void addDatabase(Database database, Object owner) {
        Object component = Model.getCoreFactory().createComponent();
        Model.getCoreHelper().setName(component, database.getName());
        Model.getCoreHelper().setNamespace(component, owner); 
        database.setModelElement(component);
        
        // now use the updateDBElement method to set Stereotype and tags
        updateDatabase(database);
        
        if (owner != null) {
            Object dep = Model.getCoreFactory().buildDependency(component,
                owner);
            String depName = Model.getFacade().getName(component) + "->" 
                + Model.getFacade().getName(owner);
            Model.getCoreHelper().setName(dep, depName);
            Model.getCoreHelper().setNamespace(dep, owner);
        }
    }
    
    /**
     *  Update a model element representing a DBElement from a DBElement object
     * 
     *  There's a problem with Schema stereotypes.  Although they are added to 
     *  the model with addStereotype we can't find them with getStereotypes.  
     *  We find Table Stereotypes.
     * 
     * @param dbelem The DBElement object. 
     */
    //Whatthehell?
    protected void updateDBElement(DBElement dbelem) {
        this.updateDBElement(dbelem, true);
    }
    
    private void updateDBElement(DBElement dbelem, boolean addStereotypes) {
        Object objElement = dbelem.getModelElement();
        dbelem.setOwningDatabase(this.getOwningDatabase(objElement));
        // update Name
        final String name = dbelem.getName();
        if (name != null) {
            Model.getCoreHelper().setName(objElement, name);
        }
        
        // First set tags from properties
        setTags(dbelem.getProperties(), objElement);
        
        if (addStereotypes) {
            
            Vector vStereos = new Vector(Model.getFacade().getStereotypes(
                objElement));
            for (Enumeration e = vStereos.elements(); e.hasMoreElements();) {
                Object objStereo = e.nextElement();
                if (objStereo != null) {
                    if (Model.getFacade().getName(objStereo) 
                        == dbelem.getStereostring()) {
                        return;
                    }
                }
            }
            
            // Now see if the proper stereoype is already defined in the model
            // If so we need to set it for the model element
            
            // todo:  needs testing as this was changed for new model package
            // and not sure how to get the namespace
            
            Object model = ProjectManager.getManager().getCurrentProject()
                .getModel();
            //Object ns = Model.getFacade().getNamespace(model);
            vStereos = new Vector(
                Model.getExtensionMechanismsHelper().getStereotypes(model));
            for (Enumeration e = vStereos.elements(); e.hasMoreElements();) {
                Object objStereo = e.nextElement();
                if (objStereo != null) {
                    if (Model.getFacade().getName(objStereo) 
                        == dbelem.getStereostring()) {
                        Model.getCoreHelper().addStereotype(
                            objElement, objStereo);
                        return;
                    }
                }
            }
            
            // Otherwise we must create a new Stereotype and set it in the model
            // element
            Object objS = addStereotype(dbelem.getStereostring(), objElement);
            if (objS != null) {
                Model.getCoreHelper().addStereotype(objElement, objS);
            }
        }
    }
    
    /**
     *  Updates a model element representing a schema from a Schema object.
     * @param schema A <code>Schema</code> instance.
     **/
    public void updateSchema(Schema schema) {
        updateDBElement(schema);
    }
    
    /**
     *  Updates a model element representing a registry from a Registry object.
     * @param registry A <code>Registry</code> instance.
     **/
    public void updateRegistry(Registry registry) {
        updateDBElement(registry);
    }
    
    /**
     *  Updates a model element representing a database from a Database object.
     * @param database A <code>Database</code> instance.
     **/
    public void updateDatabase(Database database) {
        updateDBElement(database);
    }
    
    /**
     *  Updates a model element representing a foreign key association.
     * @param fkey A <code>FKey</code> instance.
     **/
    public void updateFKey(FKey fkey) {
        updateDBAssociation(fkey);
    }
    
    /**
     *  Updates a model element representing a column from a Column object.
     * @param column A <code>Column</code> instance.
     **/
    public void updateColumn(Column column) {
        Project p = ProjectManager.getManager().getCurrentProject();
        Object mType = null;
        String typeName = column.getTypeName();
        if (typeName != null) {
            mType = p.findType(typeName.toUpperCase(), false);
        }
        if (mType == null) {
            if (column.getTypeNameJdbc() != null) {
                mType = p.findType(column.getTypeNameJdbc().toUpperCase(),
                    false);
            }
        }
        Model.getCoreHelper().setType(column.getModelElement(), mType);
        setVisibilityDefault(column.getModelElement());
        updateDBElement(column);
    }
       
    /**
     * Makes <code>Schema</code> from model object.
     * @param handle The object model.
     * @return An <code>Schema</code> object.
     */
    public Schema getSchema(Object handle) {
        if (!(representsASchema(handle))) { return null; }
        return new Schema(Model.getFacade().getName(handle),
                handle, getTags(handle));
    }
    
   /** Makes Table from model object.
     *
     * @param handle object model.
     * @return Table A table object.
     */
    public Table getTable(Object handle) {
        if (!(representsATable(handle))) { return null; }
        Table table = newTable(Model.getFacade().getName(handle),
                handle, getTags(handle)); 
        this.getTableColumnsFromModel(table, true);
        return table;
        
        // todo: Source SQL generation is indirectly called twice from the
        // following lines.
        // I think it's a bug in Argo.
        //org.argouml.uml.ui.TabSrc.setTarget(TabSrc.java:129)
        //org.argouml.uml.ui.TabSrc.setTarget(TabSrc.java:154)
    }
    
    /**
     * Make Table from model object without associations. </p>
     * Used when getting tables that are related to a table by
     * foreign keys, etc.
     * This prevents loops when there are circular associations.
     * @return Table A Table object.
     * @param handle An object model. 
     */
    public Table getTableNoAssoc(Object handle) {
        if (!(representsATable(handle))) { return null; } 
        Table table = newTable(Model.getFacade().getName(handle),
                handle, getTags(handle));
        this.getTableColumnsFromModel(table, false);
        return table;
    }
    
    /** Converts attributes models into <code>Column</code> objects.
     * @param table The table that wons the columns.
     * @param bAssoc Flag for whether or not to model associations.
     */
    protected void getTableColumnsFromModel(final Table table, boolean bAssoc) {
        Collection attrs = Model.getFacade().getAttributes(
            table.getModelElement());
        Iterator it = attrs.iterator();
        ArrayList  alColumns = new ArrayList();
        while (it.hasNext()) {
            alColumns.add(getColumnPartially(it.next(), bAssoc));
        }
        table.setColumns((Column[]) alColumns.toArray(new Column[0]));
        this.addConstraintsToAttributes(table);
    }
    
    /**
     * Makes <code>View</code> from model object. 
     * @param handle Object model. 
     * @return View The view Object. 
     */
    public View getView(Object handle) {
        if (!(representsAView(handle))) {return null; }
        View view = newView(Model.getFacade().getName(handle),
                handle, getTags(handle));
        this.getTableColumnsFromModel(view, true); 
        return view;
    }
    
    private void addConstraintsToAttributes(Table table) {
        // get all constraints and process each of them.
        Iterator it = Model.getFacade().getConstraints(
            table.getModelElement()).iterator();
        while (it.hasNext()) {
            String stmt = Model.getFacade().getBody(
                    Model.getFacade().getBody(it.next())).toString();
            // determine constraint type( not null, unique, etc ..)
            //not null constraint has self.attrName->notEmpty in it.
            int colon = stmt.indexOf(':');
            if (colon > 0) {
                String right = stmt.substring(colon + 1).trim();
                if (right.indexOf("isUnique") > 0 ) { 
                    // TableName.allInstances -> isUnique(attrName)
                    // Get the string on the right of isUnique
                    right = right.substring(
                        right.indexOf("isUnique") + 8).trim(); 
                        // remove any space in between
                    if (right.startsWith("(")) {
                        String colName = right.substring(
                                1, right.length() - 1).trim();
                        Column col = table.getColumn(colName);
                        if (col != null) {
                            col.isUnique(true);
                            // col should have an INDEX_NAME property set
                            if (col.getIndexNames().length > 0) {
                                table.recordIndex(col);
                            } else {
                                LOG.error(
                                    "INDEX_NAME tag is missing for attribute " 
                                        + colName 
                                        + " on table " + table.getName());
                            }
                        }
                    }
                } else if (right.indexOf("implies") > 0 ) { 
                    //part of unique constraint
                    right = right.substring(
                        right.indexOf("implies") + 7).trim();
                    if (right.startsWith("(")) {
                        right = right.substring(1); // remove it
                        // remove the matching ")"
                        if (right.endsWith(")")) {
                            right = right.substring(
                                0, right.length() - 1 ).trim();
                        }
                    }
                    if (right.endsWith(")")) {
                        right = right.substring(0, right.length() - 1).trim();
                    }
                    String[] stmts = right.split("and");
                    for (int i = 0; i < stmts.length; i++) {
                        if (stmts[i].indexOf('.') > 0 
                                && stmts[i].indexOf("<>") > 0)  {
                            String colName 
                                = stmts[i].substring(stmts[i].indexOf('.') + 1,
                                    stmts[i].indexOf("<>")).trim();
                            Column col = table.getColumn(colName);
                            if (col != null) {
                                if (stmts.length > 1) {
                                    col.isPartOfUniqueConstraint(true);
                                } else {
                                    col.isUnique(true);
                                }
                                // col should have an INDEX_NAME property set
                                if (col.getIndexNames().length > 0) {
                                    table.recordIndex(col);
                                } else {
                                    LOG.error(
                                        "INDEX_NAME tag is missing for attribute "
                                        + colName 
                                        + " on table " + table.getName());
                                }
                            }
                        }
                    } // end of for loop
                } else if (right.matches("self.*->notEmpty")) { 
                    String colName = right.substring(right.indexOf('.') + 1,
                        right.indexOf("->"));
                    Column col = table.getColumn(colName);
                    if (col != null) {
                        col.setAllowsNulls(false);
                    }
                } else {
                    LOG.error(
                        "We no not understand the OCL statement: " + stmt);
                }
            } //end of  if colon > 0
        } //end of  while constraints iterator
        table.markUniqueColumns();
    }
    
    /**Creates a new <code>Table</code> object.
     * 
     * @param sName  The table name.
     * @param handle The model object of the table.
     * @param properties The properties for the table.
     * @return A <code>Table</code> object.
     */
    protected Table newTable(String sName, Object handle,
            Properties properties) {
        return new Table(Model.getFacade().getName(handle),
                handle,
                getTags(handle));
    }
    
    /**Creates a new <code>View</code> object. 
     * @param sName  The view name.
     * @param handle The model object of the view.
     * @param properties The properties for the view.
     * @return A <code>View</code> object.
     */
    protected View newView(String sName, Object handle, Properties properties) {
        return new View(Model.getFacade().getName(handle),
                handle, getTags(handle));
    }
    
    /**Creates a new <code>Column</code> object.
     * 
     * @param sName  The table column.
     * @param handle The model object of the column.
     * @param properties The properties for the column.
     * @return A <code>Column</code> object.
     */
    protected Column newColumn(String sName, Object handle,
            Properties properties) {
        return new Column(Model.getFacade().getName(handle),
                handle,
                getTags(handle));
    }
    
    /**
     * Makes Column from the model.  An option is provided to not
     *  get association columns (like foreign keys) to prevent
     *  circular reference loops.
     * @return Column
     * @param bAssoc A boolean value.
     * @param handle object model.
     */
    public Column getColumnPartially(Object handle, boolean bAssoc) {
        if (!(representsAColumn(handle))) { return null; }
        
        Column col = newColumn(Model.getFacade().getName(handle),
                handle,
                getTags(handle));
        Object type = Model.getFacade().getType(handle);
        col.setTypeName( Model.getFacade().getName(type));
        
        // process the stereotype
        String name;
        Iterator its = Model.getFacade().getStereotypes(handle).iterator();
        if (its.hasNext()) {
            Object stereo = its.next();
            name = Model.getFacade().getName(stereo);
            if (name.equals(PKeyInterface.PK)) {
                // create a PKey and add it to the column
                PKey key = this.getPKey(handle);
                if (key != null) { col.setKey(key); }
            } else if ((name.equals(PKeyInterface.FK) && bAssoc)) {
                // create a FKey and add it to col
                FKey key = this.getFKey(handle);
                if (key != null) { col.setKey(key); }
            } else if ((name.equals(PKeyInterface.PFK) && bAssoc)) {
                // 1.create a PKey and add it to col
                // 2.create a FKey and add it to col.
                // The order here is important.
                PKey pkey = this.getPKey(handle);
                if (pkey != null) { col.setKey(pkey); }
                FKey fkey = this.getFKey(handle);
                if (fkey != null) { col.setKey(fkey); }
            }
        }
        return col;
    }
    
    private PKey getPKey(Object attrModel) {
        // we must be an attr: TO DO
        String tableName = null;
        Object schema = null;
        String schemaName = null;
        PKey pkey = null;
        Object table = this.getParent(attrModel);
        if (table != null) {
            tableName = Model.getFacade().getName(table);
            schema = this.getParent(table);
            if (schema != null) {
                schemaName = Model.getFacade().getName(schema);
            }
            Properties prop = this.getTags(attrModel);
            short seq = 1;
            try {
                seq = Short.parseShort(prop.getProperty("KEY_SEQ"));
            } catch (NumberFormatException e) {
                LOG.info(e.getMessage());
            }
            pkey = new PKey(schemaName,
                    tableName,
                    Model.getFacade().getName(attrModel),
                    seq,
                    prop.getProperty("PK_NAME"));
        }
        return pkey;
    }
    
    private FKey getFKey(Object attrModel) {
        // we must be an attr: TO DO
        String tableName = null;
        Object schema = null;
        String schemaName = null;
        FKey key = null;
        Object table = this.getParent(attrModel);
        if (table != null) {
            tableName = Model.getFacade().getName(table);
            schema = this.getParent(table);
            if (schema != null) {
                schemaName = Model.getFacade().getName(schema);
            }
            String attrName = Model.getFacade().getName(attrModel);
            String relatedSchemaName = null;
            String relatedTableName = null;
            String relatedColumnName = null;
            String relatedKeyName = null; 
            // get native data from the corresponding association end, 
            // association and related table.
            Object assoEnd = this.getAssociationEnd(table, attrName);
            Object asso = null;
            short seq = 1;
            String keyName = null;
            String updateRule = null;
            String deleteRule = null;
            if (assoEnd != null) {
                relatedColumnName = Model.getFacade().getName(assoEnd);
                asso = Model.getFacade().getAssociation(assoEnd);
                Properties prop = this.getTags(asso);
                keyName = prop.getProperty("FK_NAME");
                try {
                    seq = Short.parseShort(prop.getProperty("KEY_SEQ"));
                } catch (NumberFormatException e) {
                    LOG.info(e.getMessage());
                }
                updateRule = prop.getProperty("UPDATE_RULE");
                deleteRule = prop.getProperty("DELETE_RULE");
            }
            key = new FKey(schemaName, tableName, attrName, seq, keyName);
            key.setUpdateRule(updateRule);
            key.setDeleteRule(deleteRule);
            // get the related table
            Table relatedTable = this.getAssociatedTable(table, attrName);
            if (relatedTable != null) {
                // related tableName
                relatedTableName = relatedTable.getName();
                Object relTableModel = relatedTable.getModelElement();
                Object relSchemaModel = this.getParent(relTableModel);
                if (relSchemaModel != null) {
                    // related schemaName
                    relatedSchemaName = Model.getFacade().getName(
                        relSchemaModel);
                } 
                if (assoEnd != null) {
                    Column col = relatedTable.getColumn(
                        Model.getFacade().getName(assoEnd));
                    // TO DO: Make sure that Table created from model really
                    // includes Primary key information.
                    if (col != null && col.isPrimaryKey()) {
                        PKeyInterface pkey = col.getKey();
                        if (pkey != null) {
                            relatedKeyName = pkey.getKeyName();
                        }
                    }
                    key.setNativeData(
                        relatedSchemaName,
                        relatedTableName,
                        relatedColumnName,
                        relatedKeyName);
                }
            }
        }
        return key;
    }
        
    /** Return an "associated Table" by finding Table related by the given
     * asociation name.  When creating the associated Table use getTableNoAssoc 
     * as we don't want to follow FKeys in reference loops.
     * @param Object tableModel
     * @param String assoName
     */
    private Table getAssociatedTable(Object tableModel, String assoName) {
        Collection col = Model.getFacade().getAssociatedClasses(tableModel);
        //the collection may include the given class also.
        //Should it be excluded?
        Iterator it = col.iterator();
        Object assoEnd;
        Object otherTable;
        while (it.hasNext()) {
            otherTable = it.next();
            // find the associated table that has the given association name.
            assoEnd = this.getAssociationEnd(otherTable, assoName); 
            if (assoEnd != null) {
                return getTableNoAssoc(otherTable);
            }
        }
        return null;
    }
    
    /** Make DBElement from model object
     *
     * @param handle object model.
     * @return DBElement The dbuml element.
     */
    public DBElement getDBElement(Object handle) {
        DBElement dbe = getTable(handle);  // optimize for table
        if (dbe != null) { return dbe; }
        
        dbe = getColumnPartially(handle, true);
        if (dbe != null) { return dbe; }
        
        dbe = getView(handle);
        if (dbe != null) { return dbe; }
        
        dbe = getSchema(handle);
        if (dbe != null) { return dbe; }

        dbe = getRegistry(handle);
        if (dbe != null) { return dbe; }
        
        dbe = getDatabase(handle);        
        return dbe;
        
    }
    
    /**
     * Adds a Stereotype to the model
     * @param stereotype string
     * @param object
     * @return object
     */
    private Object addStereotype(String sStereo, Object obj) {
        //
        //  There is a problem with adding stereotypes to a model.
        // The code below avoids this by making
        //  the stereotype an owned element of the modelelement.
        //
        Object newStereo = null;
        if (sStereo != null && sStereo != "") {      
            Object myNS = Model.getFacade().isANamespace(obj)
                ? obj
                : ProjectManager.getManager().getCurrentProject().getModel();
            newStereo 
                = Model.getExtensionMechanismsFactory().buildStereotype(
                    obj, sStereo, myNS);
        }
        return newStereo;
    }
    
    /**
     * Sets a model object's tag values from a proterties list
     * @param properties Map
     * @param obj Model element
     */
    protected void setTags(Map properties, Object obj) {     
        Vector collTags = new Vector();
        Iterator it = properties.keySet().iterator();
        while (it.hasNext()) {
            String sKey = (String) it.next();
            Object objTag 
                = (Object) Model.getExtensionMechanismsFactory()
                    .buildTaggedValue(sKey, (String) properties.get(sKey));
            collTags.add(objTag);
        }
        Model.getExtensionMechanismsHelper().setTaggedValue(
                obj, collTags);
    }
    
    /**
     * Gets a model object's tag values making a Properties list
     * @param obj The model object.
     * @return The <code>Properties</code> of the tags.
     */
    protected Properties getTags(Object obj) {
        Properties properties = new Properties();
        for (Iterator i 
                = Model.getFacade().getTaggedValues(obj); i.hasNext();) {
            Object oTag = i.next();
            properties.setProperty(Model.getFacade().getTagOfTag(oTag),
                    (String) Model.getFacade().getValue(oTag));
        }
        return properties;
    }
    
    /** Gets the name of a model element.
     * @param handle that points out the object.
     * @return the name
     */
    public String getModelElementName(Object handle) {
        String sValue = (String) Model.getFacade().getName(handle);
        return (sValue == null ? "" : sValue);
    }
    
    /**
     *  Adds types to the package given.
     * @param sTypes String array of types to add.
     * @param pkgName The name of the package into which the types are added.
     */
    public void addTypes(String[] sTypes, String pkgName) {   
        Object pkg = this.getPackage(pkgName); 
        Project p = (Project) ProjectManager.getManager().getCurrentProject();
        for (int i = 0; i < sTypes.length; i++) {
            Object mType = (Object) p.findType(sTypes[i], false);
            if (mType == null) {
                Model.getCoreFactory().buildDataType(sTypes[i], pkg);
            }
        }
    }
    
    /**
     *  Checks base Java types are in the model.
     * @param sTypes A string array of type names.
     * @return True when types are in the model, and false otherwise.
     */
    public boolean checkTypes(String[] sTypes) {
        Project p = ProjectManager.getManager().getCurrentProject();
        for (int i = 0; i < sTypes.length; i++) {
            Object mType = (Object) p.findType(sTypes[i], false);
            if (mType == null) {  return false; }
        }
        return true;
    }
    
    /** Recognizer for model elements representing Schemas
     * @param handle candidate
     * @return true if model element represents Schemas
     */
    public boolean isDefaultSchema(Object handle) {
        if (this.representsASchema(handle)) {
            Database database = this.getOwningDatabase(handle);
            return (database != null && database.isDefaultSchema(
                Model.getFacade().getName(handle)));
        }
        return false;
    }
    
    /**
     * Gets the collection of base tables or base views of the given view.
     * @param viewModel The view model object.
     * @return A collection object.
     */
    public Collection getViewBaseModels(Object viewModel) {
        Collection bases = new Vector();
        if (this.representsAView(viewModel)) {
            Iterator iter = Model.getFacade().getClientDependencies(
                    viewModel).iterator();
            while ( iter.hasNext() ) {
                Object dep =  iter.next();
                if (this.representsADBDerive(dep)) {
                    Collection suppliers = Model.getFacade().getSuppliers(dep);
                    Iterator iter1 = suppliers.iterator();
                    while (iter1.hasNext()) {
                        bases.add(iter1.next());
                    }
                }
            }
        }
        return bases;
    }
       
    /**
     * Find a package in the model. If it does not exist, a new
     * package is created.
     * @param name The name of the package.
     * @return The package found or created.
     */
    private Object getPackage(String name) {
        Object mPackage = searchPackageInModel(name);
        if (mPackage == null) {
            mPackage =
                Model.getModelManagementFactory().buildPackage(
                    getRelativePackageName(name));
            Model.getCoreHelper().setNamespace(mPackage,
                ProjectManager.getManager().getCurrentProject().getModel());
            
            // Find the owner for this package.
            if ("".equals(getPackageName(name))) {
                Model.getCoreHelper().addOwnedElement(
                    ProjectManager.getManager().getCurrentProject().getModel(),
                        mPackage);
            } else {
                Model.getCoreHelper().addOwnedElement(
                        getPackage(getPackageName(name)),
                        mPackage);
            }
        }
        return mPackage;
    }
    
    /**
     * Search recursively for nested packages in the model. So if you
     * pass a package org.argouml.kernel , this method searches for a package
     * kernel, that is owned by a package argouml, which is owned by a
     * package org. This method is required to nest the parsed packages.
     *
     * @param name The fully qualified package name of the package we
     * are searching for.
     * @return The found package or null, if it is not in the model.
     */
    private Object searchPackageInModel(String name) {
        if ("".equals(getPackageName(name))) {
            return Model.getFacade().lookupIn(
                    ProjectManager.getManager().getCurrentProject().getModel(),
                    name);
        } else {
            Object owner = searchPackageInModel(getPackageName(name));
            return owner == null
                    ? null
                    : Model.getFacade().lookupIn(owner,
                        getRelativePackageName(name));
        }
    }
    
    /**
     * Get the relative package name from a fully qualified
     * package name. So if the parameter is 'org.argouml.kernel'
     * the method is supposed to return 'kernel' (the package
     * kernel is in package 'org.argouml').
     * @param packageName A fully qualified package name.
     * @return The relative package name.
     */
    private String getRelativePackageName(String packageName) {
        int lastDot = packageName.lastIndexOf('.');
        if (lastDot == -1) {
            return packageName;
        } else {
            return packageName.substring(lastDot + 1);
        }
    }
    
    /**
     * Get the package name from a fully specified classifier name.
     * @param name A fully specified classifier name.
     * @return The package name.
     */
    private String getPackageName(String name) {
        int lastDot = name.lastIndexOf('.');
        if (lastDot == -1) {
            return "";
        } else {
            return name.substring(0, lastDot);
        }
    }
}
