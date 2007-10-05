/**********************************************
*   Copyright (C) 2007 CINCOM SYSTEMS, INC.
*   All Rights Reserved
**********************************************/

package org.dbuml.base.database;

import java.util.Arrays;
import java.util.TreeSet;

/**
 * Implementation of <code>DBReservedWords</code> for Oracle database.
 * @author rgupta
 */
public class OraReservedWords extends GenericDBReservedWords {
    
    /** Creates a new instance of OraReservedWords */
    public OraReservedWords() {
    }
    
    /**
     *This method initializes oracle reserved words .
     */
    protected void initReservedWords() {
        final String[] reservedWords = {"ACCESS", "ADD", "ALL", "ALTER", "AND",
            "ANY", "AS", "ASC", "AUDIT", "BETWEEN", "BY",
            "CHAR", "CHECK", "CLUSTER", "COLUMN", "COMMENT", "COMPRESS",
            "CONNECT", "CREATE", "CURRENT",
            "DATE", "DECIMAL", "DEFAULT", "DELETE", "DESC", "DISTINCT", "DROP",
            "ELSE", "EXCLUSIVE", "FILE", "FLOAT", "FROM", "GRANT", "GROUP",
            "HAVING",
            "IDENTITIED", "IMMEDIATE", "IN", "INCREMENT", "INDEX", "INITIAL",
            "INSERT", "INTEGER", "INTERSECT", "INTO", "IS", 
            "LEVEL", "LIKE", "LOCK", "LONG", "MAXEXTENTS", "MINUS", "MODE",
            "MODIFY", "NOAUDIT", "NOCOMPRESS", "NOT", "NOWAIT",
            "NULL", "NUMBER",
            "OF", "OFFLINE", "ON", "ONLINE", "OPTION", "OR", "ORDER", "PCTFREE",
            "PRIOR", "PRIVELEGES", "PUBLIC", 
            "RENAME", "RESOURCE", "REVOKE",
            "ROW", "ROWID", "ROWNUM", "ROWS", "SELECT", "SESSION", 
            "SET", "SHARE", "SIZE", "SMALLINT", "START", "SUCESSFUL",
            "SYNONYM", "SYSDATE",
            "TABLE", "THEN", "TO", "TRIGGER", "UID",
            "UNION", "UNIQUE", "UPDATE", 
            "USER", "USING",
            "VALIDATE", "VALUES", "VARCHAR", "VARCHAR2", "VARYING", "VIEW",
            "WHENEVER", "WITH"
        };
        
        this.setReservedWords(new TreeSet(Arrays.asList(reservedWords)));
        
            
    }
    
    
}
