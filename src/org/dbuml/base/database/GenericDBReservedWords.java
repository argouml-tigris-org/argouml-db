/**********************************************
 *   Copyright (C) 2007 CINCOM SYSTEMS, INC.
 *   All Rights Reserved
 **********************************************/

package org.dbuml.base.database;


import java.util.Arrays;
import java.util.TreeSet;

/**
 * Class for standard SQL reserved words.
 * @author  rgupta
 */

public class GenericDBReservedWords extends DBReservedWords {
    
    
    /**
     * Loads the set of reserved words with the standard SQL reserved words.
     */
    protected void initReservedWords() {
        final String[] reserved92 = {"ABSOLUTE", "ACTION", "ADD", "ALL",
            "ALLOCATE", "ALTER", "AND", "ANY", "ARE", "AS", "ASC",
            "ASSERTION", "AT", "BETWEEN", "BIT", "BIT_LENGTH", "BOTH", "BY",
            "CALL", "CASCADE", "CASCADED", "CASE", "CAST", "CATALOG", "CHAR",
            "CHAR_LENGTH", "CHARACTER", "CHARACTER_LENGTH", "CHECK", "CLOSE",
            "COELESCE", "COLLATE", "COLLATION", "COLUMN", "COMMIT", "CONDITION",
            "CONNECT", "CONNECTION", "CONSTRAINT", "CONSTRAINTS", "CONTAINS",
            "CONTINUE", "CONVERT", "CORRESPONDING", "COUNT", "CREATE", "CROSS",
            "CURRENT", "CURRENT_DATE", "CURRENT_PATH", "CURRENT_TIME",
            "CURRENT_TIMESTAMP", "CURRENT_USER", "CURSOR",
            "DATE", "DAY", "DEALLOCATE", "DEC", "DECIMAL", "DECLARE", "DEFAULT",
            "DEFERRABLE", "DEFERRED", "DELETE", "DESC", "DESCRIBE",
            "DESCRIPTOR", "DETERMINISTIC", "DIAGNOSTICS", "DISCONNECT",
            "DISTINCT", "DO", "DOMAIN", "DOUBLE", "DROP", "ELSE", "ELSIF",
            "END", "ESCAPE", "EXCEPT", "EXCEPTION", "EXEC", "EXECUTE", "EXISTS",
            "EXIT", "EXTERNAL", "EXTRACT", "FALSE", "FETCH", "FIRST", "FLOAT",
            "FOR", "FOREIGN", "FOUND", "FROM", "FULL", "FUNCTION", "GET", "GET",
            "GLOBAL", "GO", "GOTO", "GRANT", "GROUP", "HANDLER", "HAVING",
            "HOUR", "IDENTITY", "IF", "IMMEDIATE", "IN", "INDICATOR",
            "INITIALLY", "INNER", "INOUT", "INPUT", "INSENSITIVE", "INSERT",
            "INT", "INTEGER", "INTERSECT", "INTERVAL", "INTO", "IS",
            "ISOLATION", "JOIN", "KEY", "LANGUAGE", "LAST", "LEADING",
            "LEAVE", "LEFT", "LEVEL", "LIKE", "LOCAL",
            "LOOP", "LOWER",
            "MATCH", "MAX", "MIN", "MINUTE", "MODULE", "MONTH",
            "NAMES", "NATIONAL", "NATURAL", "NCHAR", "NEXT", "NO", "NOT",
            "NULL", "NULLIF", "NUMERIC",
            "OCTET_LENGTH", "OF", "ON", "ONLY", "OPEN", "OPTION", "OR", "ORDER",
            "OUT", "OUTER", "OUTPUT", "OVERLAPS",
            "PAD", "PARAMETER", "PARTIAL", "PATH", "POSITION", "PRECISION",
            "PREPARE", "PRESERVE", "PRIMARY", "PRIOR", "PRIVELEGES",
            "PROCEDURE", "PUBLIC", "READ", "REAL", "REFERENCES", "RELATIVE",
            "REPEAT", "RESIGNAL", "RESTRICT", "RETURN", "RETURNS", "REVOKE",
            "RIGHT", "ROLLBACK", "ROUTINE", "ROWS", "SCHEMA", "SCROLL",
            "SECOND", "SECTION", "SELECT", "SESSION",
            "SESSION_USER", "SET", "SIGNAL", "SIZE", "SMALLINT", "SOME",
            "SPACE", "SPECIFIC", "SQL", "SQLCODE", "SQLERROR", "SQLEXCEPTION",
            "SQLSTATE", "SQLWARNING", "SUBSTRING", "SUM", "SYSTEM_USER",
            "TABLE", "TEMPORARY", "THEN", "TIME", "TIMESTAMP", "TIMEZONE_HOUR",
            "TIMEZONE_MINUTE", "TO", "TRAILING", "TRANSACTION", "TRANSLATE",
            "TRIM", "TRUE",
            "UNDO", "UNION", "UNIQUE", "UNKNOWN", "UNTIL", "UPDATE", "UPPER",
            "USAGE", "USER", "USING",
            "VALUE", "VALUES", "VARCHAR", "VARYING", "VIEW",
            "WHEN", "WHENEVER", "WHERE", "WHILE", "WITH", "WORK", "WRITE",
            "YEAR", "ZONE"
                
        };
        
        this.setReservedWords(new TreeSet(Arrays.asList(reserved92)));
        
    }
}
