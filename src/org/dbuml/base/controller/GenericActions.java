/**********************************************
 *   Copyright (C) 2007 CINCOM SYSTEMS, INC.
 *   All Rights Reserved
 **********************************************/

package org.dbuml.base.controller;

import java.io.File;
import org.dbuml.base.factory.Factory;
import org.dbuml.base.database.DBMetadata;
import org.dbuml.base.database.DBMetadataCache;
import org.dbuml.base.database.UpdateTransaction;
import org.dbuml.base.database.GenericUpdateTransaction;
import org.dbuml.base.model.DBModelFacade;
import org.dbuml.base.model.Database;
import org.dbuml.base.model.Schema;
import org.dbuml.base.model.DBElement;
import org.dbuml.base.transform.DBUMLToSQL;
import org.dbuml.base.transform.DBMetadataToDBUML;
import java.sql.Connection;
import java.util.Iterator;
import javax.swing.JOptionPane;
import org.dbuml.base.i18n.Translator;
import org.dbuml.base.controller.ui.EntitiesChooserDlg;
import org.dbuml.base.controller.ui.ResultSetTable;

/**
 * DBUML actions handler.
 * @author jgrengbondai
 */
public class GenericActions implements ActionsInterface {
    
    private static final String GENSQL_FAILURE_TITLE = "GENSQL_FAILURE_TITLE";
    private static final String DB_ASSOCIATION_MUST_EXIST 
        = "DB_ASSOCIATION_MUST_EXIST";
    private static final String UNFOUND_FACTORY = "UNFOUND_FACTORY";
    private static final String UNABLE_TO_LOAD_FACTORY 
        = "UNABLE_TO_LOAD_FACTORY";
    private static final String IMPORT_FAILURE_TITLE = "IMPORT_FAILURE_TITLE";
    private static final String DB_NOT_CONNECTED = "DB_NOT_CONNECTED";
    private static final String METADATA_RETRIEVAL_PROBLEM 
        = "METADATA_RETRIEVAL_PROBLEM";
    
    
    
    /**
     * Connects to JDBC database.
     * @param selectedItem An object model for the database. 
     */
    public void connect(Object selectedItem) {
        // Get the owning Database and get the Factory for the Database.
        // Only a figure representing Database can request
        // this so we are sure we have an "owning Database".
        
        Database database = DBModelFacade.getInstance().getOwningDatabase(
                selectedItem);
        Factory factory = Factory.getFactory(database.getProperty(
                Database.FACTORY));
        if (factory != null) {
            DBMetadata dbmeta = DBMetadataCache.getDBMetadata(database);
            if (dbmeta == null) { // connect
                
                try {
                    dbmeta = factory.newDBMetadata(database,
                            factory.getConnectionFactory().getConnection(
                            database));
                    DBMetadataCache.addDBMetadata(dbmeta);
                } catch (Exception e) {
                    Util.showMessageDialog(null,
                            Translator.getInstance().localize(
                            "CONNECT_FAILURE_MSG",
                            database.getName(),
                            e.getMessage()),
                            Translator.getInstance().localize(
                            "CONNECT_FAILURE_TITLE"),
                            JOptionPane.ERROR_MESSAGE);
                }
                
            } else { // disconnect
                try {
                    factory.getConnectionFactory().closeConnection(
                            (Connection) dbmeta.getConnection());
                    DBMetadataCache.removeDBMetadata(dbmeta);
                } catch (Exception e) {
                    Util.showMessageDialog(null,
                            Translator.getInstance().localize(
                            "DISCONNECT_FAILURE_MSG",
                            database.getName(),
                            e.getMessage()),
                            Translator.getInstance().localize(
                            "DISCONNECT_FAILURE_TITLE"),
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            Util.showMessageDialog(null,
                    Translator.getInstance().localize("DB_FACTORY_NOTFOUND",
                    database.getProperty(Database.FACTORY)),
                    Translator.getInstance().localize("CONNECT_FAILURE_TITLE"),
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    //
    // Connect to registry defined by Registry element.  Generic JDBC does
    // not support registries.
    //
    /**
     * Connect to registry defined by Registry element. Generic
     * JDBC does not
     * support registries.
     * @param selectedItem An object model for the registry.
     */
    public void connectRegistry(Object selectedItem) {
    }
    
    /**
     * Generates SQL statement for the given object model.
     * @param handle The object model. 
     * @param sDialect A value that tells what type of SQL statement to create.
     * </p>Example:
     * </p><code>createAlterSQL</code> to generate alter statements.
     * </p><code>createTopSQL</code> to generate create statements.
     * </p><code>dropSQLTables(/code> to generate drop table drop statements.
     * </p><code>dropSQLViews</code> to generate drop table statements.
     * </p>createAlterConstraintsSQL to generate alter statement when 
     * for constraints.
     * @return An SQL statement. 
     */
    public String generateSQL(Object handle, String sDialect) {
        //
        // If this is not a DBUML Profile element return error string
        //
        String retVal = null;
        DBElement dbe = DBModelFacade.getInstance().getDBElement(handle);
        if (dbe == null) {
            retVal = Translator.getInstance().localize(GENSQL_FAILURE_TITLE)
                    + Translator.getInstance().localize("NOT_DBUML");
        } else {
        
            //
            // If there is not an owning Database, return error string
            //
            Database database = DBModelFacade.getInstance().getOwningDatabase(
                    handle);
            if (database == null) {
                retVal = Translator.getInstance().localize(
                        GENSQL_FAILURE_TITLE)
                        + Translator.getInstance().localize(
                            DB_ASSOCIATION_MUST_EXIST);
            } else {

                dbe.setOwningDatabase(database);

                String sFactName = database.getProperty(Database.FACTORY);

                if (sFactName == null) {
                    retVal = Translator.getInstance().localize(
                            GENSQL_FAILURE_TITLE)
                            + Translator.getInstance().localize(
                                UNFOUND_FACTORY);
                } else {
                    //
                    // If unable to load factory return error string
                    //
                    Factory factory = Factory.getFactory(sFactName);
                    if (factory == null) {
                        retVal = Translator.getInstance().localize(
                                GENSQL_FAILURE_TITLE)
                                + Translator.getInstance().localize(
                                    UNABLE_TO_LOAD_FACTORY);
                    } else {
                        DBUMLToSQL trans = factory.getDBUMLToSQL();
                        retVal = trans.genCreateSQL(dbe,
                                DBModelFacade.getInstance().getDBNamespace(
                                handle),
                                sDialect);
                    }
                }
            }
        }
        return retVal;
        
    }
    
    //
    //  Generate SQL for element recursively
    //
    /**
     * Generates SQL statement for the given object model.
     * @param handle The object model. 
     * @param sDialect A value that tells what type of SQL statement to create.
     * </p>Example:
     * </p><code>createAlterSQL</code> to generate alter statements.
     * </p><code>createTopSQL</code> to generate create statements.
     * </p><code>dropSQLTables(/code> to generate drop table drop statements.
     * </p><code>dropSQLViews</code> to generate drop table statements.
     * </p>createAlterConstraintsSQL to generate alter statement when for
     * constraints.
     * @return An SQL statement. 
     */
    public String generateSQLDeep(Object handle, String sDialect) {
        String sSQL = generateSQL(handle, sDialect);
        Iterator children = DBModelFacade.getInstance().getChildrenForUpdate(
                handle).iterator();
        while ( children.hasNext() ) {
            Object child =  children.next();
            sSQL = sSQL + generateSQLDeep(child, sDialect);
        }
        return sSQL;
    }
    
    
    /**
     * Browse instances of selected table or view model.
     * @param selectedItem A table or view model.
     */
    public void browseInstances(Object selectedItem) {
        //Get owning Database
        Database database = DBModelFacade.getInstance().getOwningDatabase(
                selectedItem);
        if (database == null) {
            Util.showMessageDialog(null,
                    Translator.getInstance().localize(
                        DB_ASSOCIATION_MUST_EXIST),
                    Translator.getInstance().localize(IMPORT_FAILURE_TITLE),
                    JOptionPane.ERROR_MESSAGE);
        } else {
            DBMetadata dbmd = DBMetadataCache.getDBMetadata(database);
            if (dbmd == null) {
                Util.showMessageDialog(null,
                        Translator.getInstance().localize(DB_NOT_CONNECTED,
                        database.getName()),
                        Translator.getInstance().localize(IMPORT_FAILURE_TITLE),
                        JOptionPane.ERROR_MESSAGE);
            } else {
                String sSelect = "SELECT * FROM ";
                String sNS = DBModelFacade.getInstance().getDBNamespace(
                        selectedItem);
                if (sNS != "") {
                    sNS = database.fixName(sNS);
                    sSelect = sSelect + sNS + ".";
                }
                String tableName 
                    = DBModelFacade.getInstance().getModelElementName(
                        selectedItem);
                sSelect = sSelect + database.fixName(tableName);
                new ResultSetTable((Connection) dbmd.getConnection(),
                        database, sSelect);
            }
        }
    }
    
    private static boolean validDB(Database database) {
        boolean status = true;
        if (database == null) {
            Util.showMessageDialog(null,
                    Translator.getInstance().localize(
                    DB_ASSOCIATION_MUST_EXIST),
                    Translator.getInstance().getInstance().localize(
                    IMPORT_FAILURE_TITLE),
                    JOptionPane.ERROR_MESSAGE);
            status = false;
        }
        return status;
    }
    
    private static boolean connected(DBMetadata dbmd, Database database) {
        boolean status = true;
        if (dbmd == null) {
            Util.showMessageDialog(null,
                    Translator.getInstance().localize(DB_NOT_CONNECTED,
                    database.getName()),
                    Translator.getInstance().localize(IMPORT_FAILURE_TITLE),
                    JOptionPane.ERROR_MESSAGE);
            status = false;
        } 
        return status;
    }
     
    /**
     * Imports database schemas into the model.
     * @param selectedItem The database model. 
     * @param choices An array of schema names.
     * @param batch A boolean flag for whether or not this method is being
     * invoked in a batch mode.
     */
    public void importSchemas(Object selectedItem, String[] choices,
            boolean batch) {
        // Get the owning Database for the selected item
        
        Database database = DBModelFacade.getInstance().getOwningDatabase(
                selectedItem);
        if (validDB(database)) {
            DBMetadata dbmd = DBMetadataCache.getDBMetadata(database);
            if (connected(dbmd, database)) {
                if (!batch) {
                    Object[] oSchemas = null;
                    try {
                        oSchemas = dbmd.getSchemas();
                    } catch (java.sql.SQLException e) {
                        Util.showMessageDialog(null,
                                Translator.getInstance().localize(
                                METADATA_RETRIEVAL_PROBLEM,
                                database.getName(),
                                e.getMessage()),
                                Translator.getInstance().localize(
                                IMPORT_FAILURE_TITLE),
                                JOptionPane.ERROR_MESSAGE);
                    }

                    if (oSchemas != null && oSchemas.length == 0) {
                        Util.showMessageDialog(null,
                                Translator.getInstance().localize(
                                "SCHEMAS_NOT_DEFINED",
                                database.getName()),
                                Translator.getInstance().localize(
                                IMPORT_FAILURE_TITLE),
                                JOptionPane.ERROR_MESSAGE);
                    } else {

                        choices = EntitiesChooserDlg.selectData(
                                Translator.getInstance().localize(
                                "CHOOSE_SCHEMA"),
                                (String[]) oSchemas);
                    }
                }

                if (choices != null && choices.length > 0) {
                    //Get the factory, get its transformer & build Schemas
                    String sFactName = database.getProperty(Database.FACTORY);
                    if (sFactName == null) {
                        Util.showMessageDialog(null,
                            Translator.getInstance().localize(
                                UNFOUND_FACTORY),
                            Translator.getInstance().localize(
                                IMPORT_FAILURE_TITLE),
                                JOptionPane.ERROR_MESSAGE);
                    } else {
                        Factory factory = Factory.getFactory(sFactName);
                        if (factory == null) {
                            Util.showMessageDialog(null,
                                Translator.getInstance().localize(
                                    UNABLE_TO_LOAD_FACTORY, sFactName ),
                                Translator.getInstance().localize(
                                    IMPORT_FAILURE_TITLE),
                                JOptionPane.ERROR_MESSAGE);
                        } else {

                            DBMetadataToDBUML trans 
                                = factory.getDBMetadataToDBUML();

                            for (int i = 0; i < choices.length; i++) {
                                Object obj 
                                    = DBModelFacade.getInstance().findChild(
                                        choices[i], database.getModelElement());
                                if (obj == null) {
                                    trans.schema(database.getModelElement(),
                                            choices[i], dbmd);
                                } else {
                                    trans.updateSchema(obj);
                                }
                            }
                        }
                    }
                    
                }
            }
            
        }
    }
    
    private static String[] getChoices(final Database database,
            final Schema schema, final DBMetadata dbmd, boolean forView) {
        String[] selected = null;
        Object[] oTables = null;
        String noDataLabel = "NO_TABLE_FOR_SCHEMA";
        String type = "IMPORT_TABLES";
        if (forView) {
            noDataLabel = "NO_VIEW_FOR_SCHEMA";
            type = "IMPORT_VIEWS";
        }
        try {
            if (forView) {
                oTables = dbmd.getViewsInSchema(schema.getName());   
            } else {
                oTables = dbmd.getTablesInSchema(schema.getName());
            }
        } catch (java.sql.SQLException e) {
            Util.showMessageDialog(null,
                    Translator.getInstance().localize(
                    METADATA_RETRIEVAL_PROBLEM,
                    database.getName(),
                    e.getMessage()),
                    Translator.getInstance().localize(IMPORT_FAILURE_TITLE),
                    JOptionPane.ERROR_MESSAGE);
        }
        if (oTables != null && oTables.length == 0) {
            Util.showMessageDialog(null,
                    Translator.getInstance().localize(noDataLabel,
                    schema.getName(), database.getName()),
                    Translator.getInstance().localize(IMPORT_FAILURE_TITLE),
                    JOptionPane.ERROR_MESSAGE);
            selected = null;
        } else {

            selected = EntitiesChooserDlg.selectData(
                    Translator.getInstance().localize(type),
                    (String[]) oTables);
        } 
        return selected;
    }
    
    private static void importTables(Object schemaModel, String[] choices,
            boolean forView, boolean batch) {
        final Schema schema = DBModelFacade.getInstance().getSchema(
                schemaModel);
        final Database database = DBModelFacade.getInstance().getOwningDatabase(
                schema.getModelElement());
        if (validDB(database)) {
            DBMetadata dbmd = DBMetadataCache.getDBMetadata(database);
            if (connected(dbmd, database)) {
                if (!batch) {
                    choices = getChoices(database, schema, dbmd, forView);
                }
                if (choices != null && choices.length > 0) {
                    //Get the factory, get its transformer & build Table
                    String sFactName = database.getProperty(Database.FACTORY);
                    if (sFactName == null) {
                        Util.showMessageDialog(null,
                                Translator.getInstance().localize(
                                    UNFOUND_FACTORY),
                                    Translator.getInstance().localize(
                                IMPORT_FAILURE_TITLE),
                                JOptionPane.ERROR_MESSAGE);
                    } else {
                        Factory factory = Factory.getFactory(sFactName);
                        if (factory == null) {
                            Util.showMessageDialog(null,
                                    Translator.getInstance().localize(
                                        UNABLE_TO_LOAD_FACTORY, sFactName ),
                                    Translator.getInstance().localize(
                                        IMPORT_FAILURE_TITLE),
                                    JOptionPane.ERROR_MESSAGE);
                        } else {
                            DBMetadataToDBUML trans 
                                = factory.getDBMetadataToDBUML();
                            // loop here
                            for (int i = 0; i < choices.length; i++) {
                                try {
                                    // look for this table in the model
                                    Object objTable 
                                        = DBModelFacade.getInstance()
                                            .lookupIn(
                                                schema.getModelElement()
                                                , choices[i]);
                                    if (forView) {
                                        Object objView = objTable;
                                        if (objView == null) {
                                            trans.view(choices[i],
                                                    schema, dbmd);
                                        } else {
                                            trans.updateView(objView,
                                                    schema.getName(), dbmd);
                                        }    
                                    } else {
                                        if (objTable == null) {
                                            trans.table(choices[i],
                                                    schema, dbmd);
                                        } else {
                                            trans.updateTable(objTable,
                                                    schema.getName(), dbmd);
                                        }
                                    }
                                } catch (java.sql.SQLException e) {
                                    Util.showMessageDialog(null,
                                            Translator.getInstance().localize(
                                                METADATA_RETRIEVAL_PROBLEM,
                                                database.getName(),
                                                e.getMessage()),
                                            Translator.getInstance().localize(
                                                IMPORT_FAILURE_TITLE),
                                            JOptionPane.ERROR_MESSAGE);
                                    break;
                                }
                            }
                        }
                    }
                } // if-choices
            }
        }
    }
    
    
    /**
     * Imports database tables into the model.
     * @param schemaModel  The schema model object.
     * @param choices An array of table names.
     * @param batch A boolean flag for whether or not this method is being
     * invoked in a batch mode.
     */
    public void importTablesFromCatalog(Object schemaModel, String[] choices,
            boolean batch) {
        importTables(schemaModel, choices, false, batch); // forView=false
    }
    
    /**
     * Imports database views into the model.
     * @param schemaModel  The schema model object.
     * @param choices An array of view names.
     * @param batch A boolean flag for whether or not this method is being
     * invoked in batch mode.
     */
    public void importViews(Object schemaModel, String[] choices,
            boolean batch) {
        importTables(schemaModel, choices, true, batch); // forView=true
    }
    
    /**
     * Imports database attributes types into the model.
     * @param selectedItem The database model.
     */
    public void importAttributeTypes(Object selectedItem) {
        
        //----------------------------------------
        //Get owning Database
        Database database = DBModelFacade.getInstance().getOwningDatabase(
                selectedItem);
        
        if (validDB(database)) {
            DBMetadata dbmd = DBMetadataCache.getDBMetadata(database);
            if (connected(dbmd, database)) {
                String[] attrNames = null;
                try {
                    attrNames = dbmd.getNewTypes();
                } catch (java.sql.SQLException e) {
                    Util.showMessageDialog(null,
                            Translator.getInstance().localize(
                                METADATA_RETRIEVAL_PROBLEM,
                                database.getName(),
                                e.getMessage()),
                            Translator.getInstance().localize(
                                IMPORT_FAILURE_TITLE),
                                JOptionPane.ERROR_MESSAGE);
                }

                if (attrNames != null && attrNames.length == 0) {
                    Util.showMessageDialog(null,
                            Translator.getInstance().localize(
                            "NO_ATTR_TYPES_IN_DB", database.getName()),
                            Translator.getInstance().localize(
                            IMPORT_FAILURE_TITLE), JOptionPane.ERROR_MESSAGE);
                } else {

                    String[] choices = EntitiesChooserDlg.selectData(
                            Translator.getInstance().localize(
                            "IMPORT_ATTR_TYPES"), attrNames);
                    if (choices.length > 0) {
                        DBModelFacade.getInstance().addTypes(choices,
                                database.getDefaultTypesPkgName());
                    } 
                }
            }
        }
    }
    
    /**
     * Imports databases into the model.
     * @param selectedItem The parent model.
     * @param choices The names of the database. 
     * @param batch A boolean flag for whether or not this method is
     * being invoked in batch mode.
     */
    public void importDatabases(Object selectedItem, String[] choices,
            boolean batch) {
        Util.showMessageDialog(null,
                Translator.getInstance().localize("IMPORT_DATABASES"),
                Translator.getInstance().localize("NOT_IMPLEMENTED"),
                JOptionPane.ERROR_MESSAGE);
    }
    
    
    /**
     * Updates the database with SQL DDL derived from the model.
     * @param selectedItem The model object. 
     * @param update A flag for whether or not to update the entity. 
     */
    public void updateCatalog(Object selectedItem, boolean update) {
        try {
            if (update) {
                updateCatalogUpdate(selectedItem);
            } else {
                updateCatalogReplace(selectedItem);
            }
        } catch (Exception e) {
            Util.showMessageDialog(null,
                    e.getMessage(),
                    Translator.getInstance().localize("UPDATE_CATALOG_PROBLEM"),
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Updates the database with SQL DDL derived from the model.
     * @param selectedItem The model to update.
     * @throws Exception When there is an error.
     */
    public void updateCatalogUpdate(Object selectedItem) throws Exception {
        throw new Exception("CatalogUpdate is not yet implemented!");
    }
    
    /**
     * Replaces the database with SQL DDL derived from the model.
     * @param selectedItem The model to replace.
     * @throws java.sql.SQLException When there is an error in the database. 
     */
    public void updateCatalogReplace(Object selectedItem)
        throws java.sql.SQLException {
        Database database = DBModelFacade.getInstance().getOwningDatabase(
                selectedItem);
        DBMetadata dbmd = DBMetadataCache.getDBMetadata(database);
        UpdateTransaction uTrans = new GenericUpdateTransaction(
                (Connection) dbmd.getConnection());
        
        //
        //  First generate and run drops.  We ignore drop failures
        //  ss entities might not exist.
        //
        String sSQL =  generateSQLDeep(selectedItem, "dropSQLViews");
        sSQL = sSQL + generateSQLDeep(selectedItem, "dropSQLTables");
        sSQL = sSQL + generateSQLDeep(selectedItem, "dropSQLSchema");
        
        if (!sSQL.equals("")) {
            try {
                uTrans.executeUpdate(sSQL);
            } catch (java.sql.SQLException e) {
                uTrans.rollback();
                throw e;
                //second level to handle error.
            }
        }
        
        //  Now run create tops and alter creates.  
        //If failures we reset and throw,
        //  otherwise we commit.
        
        sSQL = generateSQLDeep(selectedItem, DBUMLToSQL.CREATE_SCHEMA_SQL);
        sSQL = sSQL + generateSQLDeep(selectedItem,
                DBUMLToSQL.CREATE_TABLES_SQL);
        sSQL = sSQL + generateSQLDeep(selectedItem,
                DBUMLToSQL.CREATE_VIEWS_SQL);
        sSQL = sSQL + generateSQLDeep(selectedItem,
                DBUMLToSQL.CREATE_ALTER_CONSTRAINT_SQL);
        sSQL = sSQL + generateSQLDeep(selectedItem,
                DBUMLToSQL.CREATE_ALTER_FKCONSTRAINT_SQL);
        if (!sSQL.equals("")) {
            try { 
                uTrans.executeUpdate(sSQL);
            } catch (java.sql.SQLException e) {
                uTrans.rollback();
                throw e;
                //second level to handle error.
            }
            uTrans.commit();
        }
    }
   
    /**
     * Generates SQL statements for the specified object model.
     * @param selectedItem The object model.
     * @param file The file to write the SQL statement into.
     */
    public void generateSource(Object selectedItem, File file) {
        FileOutput fo = new FileOutput("Generate Source", file);
        String sSQL = this.generateSource(selectedItem, true, true);
        try {
            // save the Source Generated to a File.
            fo.saveData(sSQL);
            // displaying the SQL Generated to the user
            fo.setSize(300, 500);
            fo.setVisible(true);
            fo.addText(sSQL);
        } catch (Exception e) {
            Util.showMessageDialog(null,
                    e.getMessage(),
                    Translator.getInstance().localize(
                        "GENERATE_SOURCE_PROBLEM"),
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Generates complete SQL statements for the specified object model.
     * @param selectedItem The object model.
     * @param includeDrop boolean flag for whether or not to include drop
     * statements the generated SQL.
     * @param includeAlter boolean flag for whether or not to include alter 
     * statements the generated SQL.
     * @return The SQL string.
     */
    public String generateSource(Object selectedItem,
                                 boolean includeDrop,
                                 boolean includeAlters) {
        StringBuffer buf = new StringBuffer(256);
        if (includeDrop) {
            if (DBModelFacade.getInstance().representsASchema(selectedItem) && 
                !DBModelFacade.getInstance().isDefaultSchema(selectedItem)) {
                    buf.append(generateSQLDeep(selectedItem, "dropSQLSchema"));     
            } else {
                buf.append(generateSQLDeep(selectedItem, "dropSQLViews"));
                buf.append(generateSQLDeep(selectedItem, "dropSQLTables"));
            }
        }
        buf.append(generateSQLDeep(selectedItem, "createSQLSchema"));
        buf.append(generateSQLDeep(selectedItem, "createSQLTables"));
        buf.append(generateSQLDeep(selectedItem, "createSQLViews"));
        if (includeAlters) {
            buf.append(generateSQLDeep(selectedItem, "createAlterConstraintsSQL"));
            buf.append(generateSQLDeep(selectedItem, 
                    "createAlterFKConstraintsSQL"));
        }
        return buf.toString();
    }
    
    
}



