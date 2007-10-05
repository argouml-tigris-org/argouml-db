/**********************************************
 *   Copyright (C) 2007 CINCOM SYSTEMS, INC.
 *   All Rights Reserved
 **********************************************/

package org.dbuml.base.database;


import java.util.Set;

/**
 * Abstract class for SQL reserved words.
 * @author rgupta
 */
public abstract class DBReservedWords {
    
    /**
     * A set of reserved words.
     */
    private Set reservedWords;
    
    /**
     * Loads the set of reserved words with the standard SQL reserved words.
     */
    protected abstract void initReservedWords();
    
    
    /**
     * Checks whether or not the given word is reverved.
     * @param word The word to check.
     * @return <code>true</code> when the word is reserved,
     * and <code>false</code> otherwise.
     */
    public boolean isReserved(String word) {
        
        if (this.reservedWords == null) {
            this.initReservedWords();
        }
        return (reservedWords != null  
                && this.reservedWords.contains(word.toUpperCase()));
        
    }
    
    protected void setReservedWords(Set resSet) {
        // TO DO: copy the set.
        this.reservedWords = resSet;
    }
    
    
    
}
