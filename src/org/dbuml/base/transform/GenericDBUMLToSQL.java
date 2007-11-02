/**********************************************
 *   Copyright (C) 2007 CINCOM SYSTEMS, INC.
 *   All Rights Reserved
 **********************************************/

package org.dbuml.base.transform;

import org.dbuml.base.model.DBElement;
import org.dbuml.base.model.Database;
import org.dbuml.base.model.Schema;
import org.dbuml.base.model.Table;
import org.dbuml.base.model.View;
import org.dbuml.base.model.Column;
import org.dbuml.base.model.FKey;
import org.dbuml.base.model.PKeyInterface;
import java.io.StringWriter;
import java.util.Vector;
import java.util.Properties;    // added by sbalda
import java.util.Hashtable;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.Template;

/**
 * Class for transforming DBUML classes into SQL code.
 * @author jgunderson
 */
public class GenericDBUMLToSQL extends DBUMLToSQL {
    
    /**
     * VelocityEngine instance.
     */
    
    private VelocityEngine engine;
    /**
     * Velocity template for creating alter statements..
     */
    private Template templateCreateAlter;
    /**
     * Velocity template for creating alter statements for contraints.
     */
    private Template templateCreateAlterConstraints;
    
    /**
     * Velocity template for creating alter statements for foreign keys..
     */
    private Template templateCreateAlterFKConstraints;
    
    /**
     * Velocity template for creating statements top database entities.
     */
    private Template templateCreateTop;
    
    /**
     * Velocity template for generating drop table statements.
     */
    private Template templateDropTables;
    
    /**
     * Velocity template for generating drop view statements.
     */
    private Template templateDropViews;
    
    /**
     * Velocity template for generating drop schema statements.
     */
    private Template templateDropSchema;
    
    /**
     * Velocity template for generating create schema statements.
     */
    private Template templateCreateSchema;
    
    /**
     * Velocity template for generating create table statements.
     */
    private Template templateCreateTables;
    
    /**
     * Velocity template for generating create view statements.
     */
    private Template templateCreateViews;
    
    private static final String TEMPLATES_DIR = 
            "org/dbuml/base/transform/templates/";
    
    
    /** Creates a new instance of DBMetadataToDBUML */
    public GenericDBUMLToSQL() {
        //
        //  Make instance of Velocity engine for this transformer
        //
        engine = new VelocityEngine();
        
        try {
           
            engine.init(getEngineProperties());
            
            initTemplateDropTables(getTemplatePathDropTable());
            
            initTemplateDropViews(getTemplatePathDropView());
            
            initTemplateDropSchema(getTemplatePathDropSchema());
            
            initTemplateCreateSchema(getTemplatePathCreateSchema());
            
            initTemplateCreateTables(getTemplatePathCreateTable());
            
            initTemplateCreateViews(getTemplatePathCreateView());
            
            initTemplateCreateAlter(getTemplatePathAlter());
            
            initTemplateCreateTop(getTemplatePathCreateTop());
            
            initTemplateCreateAlterConstraints(getTemplatePathAlterConstraints());
                    
            initTemplateCreateAlterFKConstraints(getTemplatePathAlterFKConstraints()); 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /** Gets path to Velocity template for creating tables.
     *@return the template path such as "org/dbuml/base/transform/templates/<name>.vm" 
     */
    protected String getTemplatePathCreateTable() {
        return TEMPLATES_DIR + "GenericCreateTables.vm";     
    }
    
    /** Gets path to Velocity template for dropping tables.
     *@return the template path such as "org/dbuml/base/transform/templates/<name>.vm" 
     */
    protected String getTemplatePathDropTable() {
        return TEMPLATES_DIR + "GenericDropTables.vm";     
    }
    
    /** Gets path to Velocity template for creating views.
     *@return the template path such as "org/dbuml/base/transform/templates/<name>.vm" 
     */
    protected String getTemplatePathCreateView() {
        return TEMPLATES_DIR + "GenericCreateViews.vm";     
    }
    
    /** Gets path to Velocity template for dropping views.
     *@return the template path such as "org/dbuml/base/transform/templates/<name>.vm" 
     */
    protected String getTemplatePathDropView() {
        return TEMPLATES_DIR + "GenericDropViews.vm";     
    }
    
    /** Gets path to Velocity template for creating schemas.
     *@return the template path such as "org/dbuml/base/transform/templates/<name>.vm" 
     */
    protected String getTemplatePathCreateSchema() {
        return TEMPLATES_DIR + "GenericCreateSchema.vm";     
    }
    
    /** Gets path to Velocity template for dropping schemas.
     *@return the template path such as "org/dbuml/base/transform/templates/<name>.vm" 
     */
    protected String getTemplatePathDropSchema() {
        return TEMPLATES_DIR + "GenericDropSchema.vm";     
    }
    
    /** Gets path to Velocity template for altering top database entities.
     *@return the template path such as "org/dbuml/base/transform/templates/<name>.vm" 
     */
    protected String getTemplatePathAlter() {
        return TEMPLATES_DIR + "GenericCreateAlterDBUMLToSQL.vm";     
    }
    
    /** Gets path to Velocity template for creating top database entities.
     *@return the template path such as "org/dbuml/base/transform/templates/<name>.vm" 
     */
    protected String getTemplatePathCreateTop() {
        return TEMPLATES_DIR + "GenericCreateTopDBUMLToSQL.vm";     
    }
    
    /** Gets path to Velocity template for altering constraints.
     *@return the template path such as "org/dbuml/base/transform/templates/<name>.vm" 
     */
    protected String getTemplatePathAlterConstraints() {
        return TEMPLATES_DIR + "GenericCreateAlterConstraintsDBUMLToSQL.vm";     
    }
    
    /** Gets path to Velocity template for altering foreign key constraints.
     *@return the template path such as "org/dbuml/base/transform/templates/<name>.vm" 
     */
    protected String getTemplatePathAlterFKConstraints() {
        return TEMPLATES_DIR + "GenericCreateAlterFKConstraintsDBUMLToSQL.vm";     
    }   
    
    /** Initializes template. It gets a Velocitity template for the given .vm file.
     *@param vmTemplateName Path to .vm file.
     *@return A Velocity template object for the specified .vm template
     *
     */
    private Template initializeTemplate(String vmTemplateName) 
        throws Exception {
        return this.engine.getTemplate(vmTemplateName);   
    }
    
    /**
     * Initializes Velocity template for drop tables statement.
     * @param template Path to .vm template.
     * @throws java.lang.Exception For any velocity error.
     */
    protected void initTemplateDropTables(String template) throws Exception {
        templateDropTables = initializeTemplate (template);  
    }
    
    /** Gets Velocity template for drop tables statement.
     *@return drop table template.
     */
    public Template getTemplateDropTables() {
        return templateDropTables;
    }
    
    /**
     * Initializes Velocity template for create tables statement.
     * @param template Path to .vm template.
     * @throws java.lang.Exception When there is an error.
     */
    protected void initTemplateCreateTables(String template) throws Exception {
        templateCreateTables = initializeTemplate (template);  
    }
    
    /** Gets Velocity template for create tables statement.
     *@return create table template.
     */
    public Template getTemplateCreateTables() {
        return templateCreateTables;
    }
    
    
    /**
     * Initializes Velocity template for drop views statement.
     * @param template Path to .vm template.
     * @throws java.lang.Exception For any Velocity error.
     */
    protected void initTemplateDropViews(String template) throws Exception {
        templateDropViews = initializeTemplate (template);  
    }
    
    /** Gets Velocity template for drop views statement.
     *@return drop view template.
     */
    public Template getTemplateDropViews() {
        return templateDropViews;
    }
    
    /**
     * Initializes Velocity template for create view statement.
     * @param template Path to .vm template.
     * @throws java.lang.Exception For any Velocity error.
     */
    protected void initTemplateCreateViews(String template) throws Exception {
        templateCreateViews = initializeTemplate (template);  
    }
    
    /** Gets Velocity template for create views statement.
     *@return create view template.
     */
    public Template getTemplateCreateViews() {
        return templateCreateViews;
    }
    
    /**
     * Initializes Velocity template for drop schema statement.
     * @param template Path to .vm template.
     * @throws java.lang.Exception For any Velocity error.
     */
    protected void initTemplateDropSchema(String template) throws Exception {
        templateDropSchema = initializeTemplate (template);  
    }
    
    /** Gets Velocity template for drop schema statement.
     *@return drop schema template.
     */
    public Template getTemplateDropSchema() {
        return templateDropSchema;
    }
    
    /**
     * Initializes Velocity template for create schema statement.
     * @param template path to .vm template.
     * @throws java.lang.Exception For any Velocity error.
     */
    protected void initTemplateCreateSchema(String template) throws Exception {
        templateCreateSchema = initializeTemplate (template);  
    }
    
    /** Gets Velocity template for create schema statement.
     *@return create schema template.
     */
    public Template getTemplateCreateSchema() {
        return templateCreateSchema;
    }
    
    /**
     * Initializes Velocity template for create alter statements.
     * @param template Path to .vm template.
     * @throws java.lang.Exception For any Velocity error.
     */
    protected void initTemplateCreateAlter(String template) throws Exception {
        templateCreateAlter = initializeTemplate (template);      
    }
    
    /** Gets Velocity template for create alter statement.
     *@return create schema template.
     */
    public Template getTemplateCreateAlter() {
        return templateCreateAlter;
    }
    
    /**
     * Initializes Velocity template for creating top entities.
     * @param template Path to .vm template.
     * @throws java.lang.Exception For any Velocity error.
     */
    protected void initTemplateCreateTop(String template) throws Exception {
        templateCreateTop = initializeTemplate (template);      
    }
    
    /** Gets Velocity template for creating top entities.
     *@return create top template.
     */
    public Template getTemplateCreateTop() {
        return templateCreateTop;
    }
    
    /**
     * Initializes Velocity template for altering constraints.
     * @param template Path to .vm template.
     * @throws java.lang.Exception For any Velocity error.
     */
    protected void initTemplateCreateAlterConstraints(
            String template) throws Exception {
        templateCreateAlterConstraints = initializeTemplate (template);      
    }
    
    /** Gets Velocity template for altering constraints.
     *@return alter constraints template.
     */
    public Template getTemplateCreateAlterConstraints() {
        return templateCreateAlterConstraints;
    }
    
    /**
     * Initializes Velocity template for altering foreign key constraints.
     * @param template Path to .vm template.
     * @throws java.lang.Exception For any Velocity error.
     */
    protected void initTemplateCreateAlterFKConstraints(
            String template) throws Exception {
        templateCreateAlterFKConstraints = initializeTemplate (template);      
    }
    
    /** Gets Velocity template for altering foreign key constraints.
     *@return alter foreign key constraints template.
    */
    public Template getTemplateCreateAlterFKConstraints() {
        return templateCreateAlterFKConstraints;
    }
    
    /**
     * Generates SQL statement from a DBUML element.
     * @param dbe The dbuml element.
     * @param sNamespace The namespace.
     * @param sDialect A value that tells what type of SQL statement to create.
     * </p>Example:
     * </p><code>createAlterSQL</code> to generate alter statements.
     * </p><code>createTopSQL</code> to generate the create statements.
     * </p><code>dropSQLTables(/code> to generate drop table drop statements.
     * </p><code>dropSQLViews</code> to generate drop table statements.
     * </p>createAlterConstraintsSQL to generate alter statement when for 
     * constraints.
     * @return The SQL string.
     */
    public String genCreateSQL(DBElement dbe, String sNamespace,
            String sDialect) {
        VelocityContext context = new VelocityContext();
        
        if (dbe instanceof Database) {
            context.put("Database", (Database) dbe);
        } else if (dbe instanceof Schema) {
            context.put("Schema", (Schema) dbe);
        } else if (dbe instanceof View) {
            context.put("View", (View) dbe);
        } else if (dbe instanceof Table) {
            context.put("Table", (Table) dbe);
            context.put("FKCons",  makeFKConstraints((Table) dbe));
            context.put("PKCon",  makePKConstraint((Table) dbe));
        } else if (dbe instanceof Column) {
            context.put("Column", (Column) dbe);
        }
        
        context.put("Namespace", sNamespace);
        
        StringWriter writer =  new StringWriter();
        try {
            if (sDialect.equals(DBUMLToSQL.CREATE_ALTER_SQL)) {
                templateCreateAlter.merge(context, writer);
            } else if (sDialect.equals(DBUMLToSQL.CREATE_TOP_SQL)) {
                templateCreateTop.merge(context, writer);
            } else if (sDialect.equals(
                    DBUMLToSQL.CREATE_ALTER_CONSTRAINT_SQL)) {
                templateCreateAlterConstraints.merge(context, writer);
            } else if (sDialect.equals(
                    DBUMLToSQL.CREATE_ALTER_FKCONSTRAINT_SQL)) {
                templateCreateAlterFKConstraints.merge(context, writer);
            } else if (sDialect.equals(DBUMLToSQL.DROP_TABLES_SQL)) {
                templateDropTables.merge(context, writer);
            } else if (sDialect.equals(DBUMLToSQL.DROP_VIEWS_SQL)) {
                templateDropViews.merge(context, writer);
            } else if (sDialect.equals(DBUMLToSQL.DROP_SCHEMA_SQL)) {
                templateDropSchema.merge(context, writer);
            } else if (sDialect.equals(DBUMLToSQL.CREATE_SCHEMA_SQL)) {
                templateCreateSchema.merge(context, writer);
            } else if (sDialect.equals(DBUMLToSQL.CREATE_TABLES_SQL)) {
                templateCreateTables.merge(context, writer);
            } else if (sDialect.equals(DBUMLToSQL.CREATE_VIEWS_SQL)) {
                templateCreateViews.merge(context, writer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return writer.toString();
    }
    
    private Vector makeFKConstraints(Table table) {
        //
        //  We need to group composite foreign keys into FKConstraint structures
        //  becaues we need to generate a single constraint source statement for
        //  each composite foreign key.
        //
        
        Hashtable hashFKs = new Hashtable();
        Column columns[] = table.getColumns();
        for (int i = 0; i < columns.length; i++) {
            if (columns[i].isForeignKey()) {
                FKey fkey = columns[i].getKey().getForeignKey();
                String fname = fkey.getKeyName();
                FKConstraint fkconstraint = (FKConstraint) hashFKs.get(fname);
                if (fkconstraint == null) {
                    fkconstraint = new FKConstraint(fname);
                    hashFKs.put(fname, fkconstraint);
                }     
                fkconstraint.setInfo(fkey);
            }
        }
        return new Vector(hashFKs.values());
    }
    
    private PKConstraint makePKConstraint(Table table) {
        //
        //  We need to group composite primary keys into PKConstraint stuctures
        //  becaues we need to generate a single constraint source statement for
        //  the composite primary key.
        //
        
        PKConstraint pkconstraint = null;
        Column columns[] = table.getColumns();
        for (int i = 0; i < columns.length; i++) {
            if (columns[i].isPrimaryKey()) {
                PKeyInterface pkey = columns[i].getKey();
                if (pkconstraint == null) {
                    pkconstraint = new PKConstraint();
                    pkconstraint.setPkName(pkey.getKeyName());
                }
                pkconstraint.getPkColumnNames().add(pkey.getKeySequence() - 1,
                        pkey.getColumnName());
            }
        }
        return pkconstraint;
    }
    
    /*
     * This method will set all the properties need for the Velocity Engine to
     * start-up / get initilized successfully.
     */
    /**
     * Gets velocity engine properties.
     * @return Properties list of velocity engine settings.
     */
    protected Properties getEngineProperties() {
        Properties prop = new Properties();
        prop.setProperty("resource.loader", "class");
        prop.setProperty("class.resource.loader.class",
                "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        prop.setProperty("velocimacro.library",
                TEMPLATES_DIR + "VM_global_library.vm");
        prop.setProperty("velocimacro.permissions.allow.inline", "true");
        prop.setProperty("velocimacro.permissions.allow.inline.global.scope",
                "true");
        prop.setProperty("velocimacro.library.autoreload", "true");
        
        return prop;
    }
}


