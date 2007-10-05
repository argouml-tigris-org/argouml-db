/**********************************************
*   Copyright (C) 2007 CINCOM SYSTEMS, INC.
*   All Rights Reserved
**********************************************/

package org.dbuml.base.transform;

import org.dbuml.base.model.DBElement;

/**
 * Abstract class for transforming DBUML classes into SQL code.
 * @author jgunderson
 */
public abstract class DBUMLToSQL {
    /** Flag for generating alter statements.
     */
    public static final String CREATE_ALTER_SQL = "createAlterSQL";
    
    /** Flag for generating SQL statements for top elements.
     */
    public static final String CREATE_TOP_SQL = "createTopSQL";
    
    /** Flag for generating create table statements.
     */
    public static final String CREATE_TABLES_SQL = "createSQLTables";
    
    /** Flag for generating create view statements.
     */
    public static final String CREATE_VIEWS_SQL = "createSQLViews";
    
    /** Flag for generating create schema statements.
     */
    public static final String CREATE_SCHEMA_SQL = "createSQLSchema";
    
    /** Flag for generating alter statements for SQL constraints.
     */
    public static final String CREATE_ALTER_CONSTRAINT_SQL =
            "createAlterConstraintsSQL";
    
    /** Flag for generating alter statements for foreign keys.
     */
    public static final String CREATE_ALTER_FKCONSTRAINT_SQL =
            "createAlterFKConstraintsSQL";
    
    /** Flag for generating drop statements for tables.
     */
    public static final String DROP_TABLES_SQL = "dropSQLTables";
    
    /** Flag for generating drop statements for views.
     */
    public static final String DROP_VIEWS_SQL = "dropSQLViews";
    
    /** Flag for generating drop statements for schemas.
     */
    public static final String DROP_SCHEMA_SQL = "dropSQLSchema";
    
    /**
     * Creates a new instance of DBMetadataToDBUML.
     */
    public DBUMLToSQL() {
    }
    
    /**
     * Generates SQL statement from a DBUML element.
     * @param dbe The dbuml element.
     * @param sNamespace The namespace.
     * @param sDialect A value that tells what type of create statement
     * to create.
     * </p>Example:
     * </p>createAlterSQL to generate alter statements.
     * </p>createTopSQL to generate the create statements.
     * </p>dropSQLTables to generate drop table drop statements.
     * </p>dropSQLViews to generate drop table statements.
     * </p>createAlterConstraintsSQL to generate alter statement when
     * for constraints.
     * @return The SQL string.
     */
    public abstract String genCreateSQL(DBElement dbe, String sNamespace,
            String sDialect);
    
}
