/**********************************************
 *   Copyright (C) 2007 CINCOM SYSTEMS, INC.
 *   All Rights Reserved
 **********************************************/

package org.dbuml.base.i18n;

import java.util.ResourceBundle;

/**
 * Singleton class for internationalizing labels and messages.
 * @author jgrengbondai
 */
public class Translator {
    
    // subclasses must provide their specific version.
    private ResourceBundle bundle = ResourceBundle.getBundle(
            "org.dbuml.base.i18n.DBUML");
    
    /**
     * The instance of this class.
     */
    private static Translator instance = null;
    
    /**
     * subclasses must reimplement this method to return their specific version.
     * Note that the signature cannot be changed; i.e, it should return an
     * instance of org.dbuml.base.i18n.Translator
     * @return An instance of this class.
     */
    public static Translator getInstance() {
        if (instance == null) {
            new Translator();
        }
        return instance;
    }
    
    /**
     * Creates a new translator.
     */
    public Translator() {
        instance = this;
    }
    
    /**
     * Localizes the string identified by the given key.
     * @param key A String key in the property file.
     * @return The translated string.
     */
    public String localize(String key) {
        return bundle.getString(key);
    }
    
    /**
     * Localizes and formats the string identified by the given key.
     * @param key A String key in the property file.
     * @param arg1 A String for replacing {0} in the localized string.
     * @return The translated string.
     */
    public final String localize(String key, String arg1) {
        return java.text.MessageFormat.format(localize(key), 
                new Object[]{arg1});
    }
    
    /**
     * Localizes and formats the string identified by the given key.
     * @param key A String key in the property file.
     * @param arg1 A String for replacing {0} in the localized string.
     * @param arg2 A String for replacing {1} in the localized string.
     * @return The translated string.
     */
    public final String localize(String key, String arg1, String arg2) {
        return java.text.MessageFormat.format(localize(key), 
                new Object[]{arg1, arg2});
    }
    
    /**
     * It localizes and formats the string identified by the given key.
     * @param key A String key in the property file.
     * @param args An array of strings for replacing corresponding {n} in the
     * localized string.
     * @return The translated string.
     */
    public final String localize(String key, Object[] args) {
        return java.text.MessageFormat.format(localize(key), args);
    }
    
}
