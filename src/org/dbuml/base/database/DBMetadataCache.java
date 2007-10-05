/**********************************************
 *   Copyright (C) 2007 CINCOM SYSTEMS, INC.
 *   All Rights Reserved
 **********************************************/
package org.dbuml.base.database;

import java.util.Vector;
import org.dbuml.base.model.DBElement;

/**
 * Class for caching database metadata.
 */
public class DBMetadataCache {
    
    private static Vector cache = new Vector();
    
    /**
     * Creates new DBMetadataCache.
     */
    public DBMetadataCache() {
    }
    
    /**
     * Gets the database metadata for the specified repository.
     * @param repository The database or registry.
     * @return An instance of <code>DBMetadata</code>.
     */
    public static DBMetadata getDBMetadata(DBElement repository) {
        DBMetadata dbmetadata = null;
        boolean found = false;
        for (int i = 0; i < cache.size(); i++) {
            dbmetadata = (DBMetadata) cache.get(i);
            if (dbmetadata.getRepository().getModelElement() 
                == repository.getModelElement()) {
                found = true;
                break;
            }
        }
        return (found ? dbmetadata : null);
    }
    
    /**
     * Adds the specified DBMetadata into the cache.
     * @param dbmetadata The DBMetadata to cache.
     */
    public static void addDBMetadata(DBMetadata dbmetadata) {
        cache.add(dbmetadata);
    }
    
    /**
     * Removes the specified DBMetadata from the cache.
     * @param dbmetadata The <code>DBMetadata</code> instance to remove from
     * the cache.
     */
    public static void removeDBMetadata(DBMetadata dbmetadata) {
        cache.remove(dbmetadata);
    }
    
}
