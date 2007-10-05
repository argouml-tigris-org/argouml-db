/**********************************************
*   Copyright (C) 2007 CINCOM SYSTEMS, INC.
*   All Rights Reserved
**********************************************/

package org.dbuml.base.database;

import java.util.Arrays;
import java.util.TreeSet;

/**
 * Implementation of <code>DBReservedWords</code> for MYSQL database.
 * @author rgupta
 */
public class MySQLReservedWords extends GenericDBReservedWords {
    
    /** Creates a new instance of MySQLReservedWords */
    public MySQLReservedWords() {
    }
    
   /**
     *This method initializes MySQL reserved words.
     */ 
    protected void initReservedWords() {
        final String[] reservedWords = {"ADD", "ALL", "ALTER", "ANALYZE", 
            "AND", "AS", "ASC", "ASENSITIVE", "AUTO_INCREMENT",
            "BDB", "BEFORE", "BERKELEYDB", "BETWEEN", "BIGINT",
            "BINARY", "BLOB", "BOTH", "BTREE", "BY",
            "CALL", "CASCADE", "CASE", "CHANGE", "CHAR", "CHARACTER", 
            "CHECK", "COLLATE", "COLUMN", "COLUMNS", "CONDITION", 
            "CONNECTION", "CONSTRAINT", "CONTINUE", "CREATE",
            "CROSS", "CURRENT_DATE", "CURRENT_TIME", "CURRENT_TIMESTAMP", 
            "CURSOR", "DATABASE", "DATABASES", "DAY_HOUR",
            "DAY_MINUTE", "DAY_SECOND", "DEC", "DECIMAL", "DECLARE", "DEFAULT",
            "DELAYED", "DELETE", "DESC", "DESCRIBE", "DETERMINISTIC", 
            "DISTINCT", "DISTINCTROW", "DIV", "DOUBLE", "DROP",
            "ELSE", "ELSEIF", "ENCLOSED", "ERRORS", "ESCAPED", "EXISTS", 
            "EXIST", "EXPLAIN", "FALSE", "FETCH", "FIELDS", "FOR", "FORCE", 
            "FOREIGN", "FOUND", "FLOAT", "FULLTEXT", "FROM",
            "GRANT", "GROUP", "HAVING", "HASH", "HIGH_PRIORITY", 
            "HOUR_MICROSECOND", "HOUR_MINUTE", "HOUR_SECOND",
            "IF", "IGNORE", "IN", "INDEX", "INFILE", "INNER", "INNODB", 
            "INOUT", "INSENSITIVE", "INSERT", "INT", "INTEGER", 
            "INTERVAL", "INTO", "IS", "ITERATE", "JOIN",
            "KEY", "KEYS", "KILL", "LEADING", "LEAVE", "LEFT", "LIKE",
            "LIMIT", "LINES", "LOAD", "LOCALTIME",
            "LOCALTIMESTAMP", "LOCK", "LONG", "LONGBLOB",
            "LONGTEXT", "LOOP", "LOW_PRIORITY", "MASTER_SERVER_ID", 
            "MATCH", "MEDIUMBLOB", "MEDIUMINT", "MEDIUMTEXT", "MIDDLEIN",
            "MINUTE_SECOND", "MOD", "MRG_MYISAM",
            "NATURAL", "NOT", "NULL", "NUMERIC",
            "ON", "OPTIMIZE", "OPTION", "OPTIONALLY", "OR", "ORDER", 
            "OUT", "OUTER", "OUTFILE", "PRECISION", "PRIMARY", "PRIVILEGES",
            "PROCEDURE", "PURGE", "READ", "REAL", "REFERENCES", "REGEXP", 
            "RENAME", "REPEAT", "REPLACE", "REQUIRE", "RESTRICT", "RTREE",
            "RETURN", "RETURNS", "REVOKE", "RIGHT", "RLIKE",
            "SECOND_MICROSECOND", "SELECT", "SENSITIVE", "SEPARATOR", "SET",
            "SHOW", "SMALLINT", "SOME", "SONAME", "SPATIAL", "SPECIFIC",
            "SQL", "SQLEXCEPTION", "SQLSTATE", "SQL_BIG_RESULT", 
            "SQL_CALC_FOUND_ROWS", "SQL_SMALL_RESULT", "SSL", "STARTING", 
            "STRIPED", "STRAIGHT_JOIN", "TABLE", "TABLES", "TERMINATED", 
            "THEN", "TIMESTAMPADD", "TIMESTAMPDIFF", "TINYBLOB", "TINYINT",
            "TINYTEXT", "TO", "TRAILING", "TRUE", "TYPES", "UNDO", "UNION",
            "UNIQUE", "UNLOCK", "UNSIGNED", "UPDATE", "USAGE", "USE", 
            "USER_RESOURCES", "USING", "UNTIL", "VALUES", "VARBINARY", 
            "VARCHAR", "VARCHARACTER", "VARYING", "WARNINGS", "WHEN", 
            "WHERE", "WHILE", "WITH", "WRITE", "XOR", "YEAR_MONTH", "ZEROFILL"
        };
        
        this.setReservedWords(new TreeSet(Arrays.asList(reservedWords)));
       
    }
     
}
