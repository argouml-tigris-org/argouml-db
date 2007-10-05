/**********************************************
*   Copyright (C) 2007 CINCOM SYSTEMS, INC.
*   All Rights Reserved
**********************************************/

package org.dbuml.base.factory;
import org.dbuml.base.database.MySQLReservedWords;

/**
 * Factory class for MySQL specific transformations and functions.
 * @author  rgupta
 */
public class MySQLFactory extends GenericFactory {
    
    /** Creates a new instance of MySQLFactory */
    public MySQLFactory() {
    }
    
    /**
     * Initializes the list of MySQL reserved words.
     */
    public void initDBReservedWords() {
        this.dbReservedWords = new MySQLReservedWords();
    }
}
