/**********************************************
 *   Copyright (C) 2007 CINCOM SYSTEMS, INC.
 *   All Rights Reserved
 **********************************************/

package org.dbuml.base.model;

import java.util.List;
import java.util.Properties;
import java.util.TreeMap;
import java.util.Vector;
import org.dbuml.base.database.DBMetadataCache;
import org.dbuml.base.database.DBMetadata;
import org.dbuml.base.factory.Factory;
import org.dbuml.base.model.DBElement.KeyComparator;

/**
 * Class for modeling a Database.
 * @author jgunderson
 */



public class Database extends DBElement {
    /**
     * The stererotype string.
     */
    public static final String STEREOSTRING = "Database";
    
    /**
     * The driver property label.
     */
    public static final String DRIVER         = "Driver";
    /**
     * The URL property label
     */
    public static final String URL            = "Url";
    /**
     * The user property label.
     */
    public static final String USER           = "User";
    /**
     * The password property label.
     */
    public static final String PASSWORD       = "Password";
    /**
     * The factory label.
     */
    public static final String FACTORY        = "Factory";
    
    /**
     * Schema support label.
     */
    public static final String SUPPORTS_SCHEMA = "Supports_schema";
    
//    private static Set ansiReservedWords;
    
//    private boolean doubleQuoteIdentifier = false;
    private boolean preferreQuotedIdentifiers = false;
    
    private static final String QUOTES = "\"";
    
    private String quoteStr = QUOTES;
    
    private static final int UNKNOWN_CASE = 0;
    private static final int LOWER_CASE = 1;
    private static final int UPPER_CASE = 2;
    private static final int MIXED_CASE = 3;
    
    private int storeUnquoted = UNKNOWN_CASE;
    private int storequoted = UNKNOWN_CASE;
    
    
    private static List propNames;
    
    private Factory factory = null;
    /**
     * Creates a new instance of Database.
     */
    public Database() {
        this(null);
    }
    
    /**
     * Creates a new instance of <code>Database</code> with a given name.
     * Used when adding elements to model.
     * @param sName The name of the database.
     */
    public Database(final String sName) {
        // Properties are sorted in their respective order
        // in propNames list.
        super(sName, new TreeMap(new KeyComparator(propNames)));
        setProperty(DRIVER, "sun.jdbc.odbc.JdbcOdbcDriver");
        setProperty(URL, "jdbc:odbc:data source name");
        setProperty(USER, "Public");
        setProperty(PASSWORD, "");
        setProperty(FACTORY, "org.dbuml.base.factory.GenericFactory");
        setProperty(SUPPORTS_SCHEMA, "false");
    }
    
    /**
     * Creates a new instance of with a given name, model element, owner model
     * element and properties.  Used when retrieving elements.
     * @param sName The database name.
     * @param objModel The database model object.
     * @param properties The propeties of the database.
     */
    public Database(final String sName, final Object objModel, 
            final Properties properties) {
        this(sName);
        this._objModel = objModel;
        this.setProperties(properties);
        final DBMetadata md = DBMetadataCache.getDBMetadata(this);
        if (md != null) {
            md.initDatabase(this);
        }
    }
    
    /**
     * Get the Stereotype string
     * @return The Stereotype string
     */
    public String getStereostring() {
        return STEREOSTRING;
    }
    
    /**
     * Are we connected to the database?
     * @return True when we are connected to the database, and false when we
     * are not.
     */
    public  boolean isConnected() {
        return (DBMetadataCache.getDBMetadata(this) != null);
    }
    
    /**
     * Get the Default Schema name.
     * @return The default schema name.
     */
    public String getDefaultSchemaName() {
        return this.getName();
    }
    
    /**
     * Is the given name a default schema name?
     * @param schemaName The schema name to verify.
     * @return true when the given name if the default schema. False otherwise.
     */
    public boolean isDefaultSchema(final String schemaName) {
        return (schemaName != null 
                && schemaName.equals(this.getDefaultSchemaName()));
    }
    
    /**
     * Is the given word reserved?
     * @param word The word to verify.
     * @return True when the given word is reserved, and false otherwise.
     */
    protected boolean isReserved(final String word) {
        
        if (factory == null) {
            String sf = this.getProperty(FACTORY);
            if (sf != null) {
                factory = Factory.getFactory(sf);
            } 
        }
        // return (factory != null && factory.isReservedWord(word));
        return (factory != null 
                && factory.getDBReservedWords().isReserved(word));
    }
    
    /**
     * Sets whether or not all identifiers should be quoted. The default is
     * false.
     * @param preferred False for wrapping all database identifiers with the
     * preferred quotes.
     */
    public void quoteIdentifiers(boolean preferred) {
        preferreQuotedIdentifiers = preferred;
    }
    
    /**
     * Determine wheher or not this database quotes all identifiers. 
     * The default is false.
     * @return Ture when this database prefers quotes, and false when it does
     * not.
     */
    public boolean preferresQuotes() {
        return preferreQuotedIdentifiers;
    }
    
    /** Adds the preferred quote string around the given identifier when needed.
     *@param identifier An identifier.
     *@return The identifier surrounded by the preferred quote string when
     * necessary or the identifier.
     */
    public String fixName(final String identifier) {
        return fixName(identifier, false);
    }
    
    /** Adds the preferred quote string around the given identifier when needed.
     *@param str An identifier.
     *@param importing A true or false value that tells whether or not the 
     * identifier came from the database. jdbc driver seems to not include
     * double quote into quoted identifiers like table names.
     *@return The identifier surrounded by the preferred quote string when
     * necessary or the identifier.
     */
    public String fixName(final String str , boolean importing) {
        String retStr = str;
        if (!isQuoted(str)) {
            if (importing) {
                if ( needsQuotes(str) 
                    || includesSpace(str) 
                    || isReserved(str)) {
                    retStr = quoteStr + str + quoteStr;
                }
            } else if (
                    preferresQuotes() 
                    || includesSpace(str) 
                    || isReserved(str)) {
                retStr = quoteStr + str + quoteStr;
            }
        }
        return retStr;
        
    }
    
    /** Remove quotes from the given identifier.
     *@param str An identifier
     *@return The stripped identifier when necessary or simply the identifier.
     */
    public String removeQuote(final String str) {
        String retVal = str;
        if (this.isQuoted(str)) {
            retVal = str.substring(1, str.length() - 1); 
        }
        return retVal;
    }
    
    /** Sets the Qoute String for this database. It's usally obtained from the
     * metadata.
     *@param quote The quote string
     */
    public void setIdentifierQuoteString(final String quote) {
        this.quoteStr = quote;
    }
    
    private static boolean includesSpace(final String str) {
//        if (str == null) return false;
//        String str2 = str.trim();
//        if (str2 == null || str2.length() == 0) return false;
//        return str2.indexOf(" ") > 0;
        boolean retVal = false;
        if (str != null) {
            String str2 = str.trim();
            if (str2 != null && str2.length() > 0) {
                retVal = str2.indexOf(' ') > 0; 
            }
        }
        return retVal;
    }
    
    private boolean isQuoted(final String str) {
        return str != null 
                && str.startsWith(quoteStr) 
                && str.endsWith(quoteStr);
    }
    
    private boolean needsQuotes(final String str) {
        
        // This is usefull only for identifying imported non reserved 
        // identifier. for example, if the database usually stores unquoted 
        // identifiers as UPPERCASE, when we see either a lowercase or mixed
        // case identifier we can conclude that the identifier must have been
        // quoted.
        
        boolean retVal = false;
        
        if (str != null) {
            if ((this.storeUnquoted == UNKNOWN_CASE 
                    || this.storequoted == UNKNOWN_CASE) //// we cannot tell  
                    || (this.storeUnquoted == this.storequoted )) { // ambiguous
                retVal = false;
            }
            if (!retVal) {
                char[] chars = str.toCharArray();
                if (this.storeUnquoted == LOWER_CASE) {
                    retVal = isUpperCase(chars) || isMixedCase(chars);
                } else if (this.storeUnquoted == UPPER_CASE) {
                    retVal = isLowerCase(chars) || isMixedCase(chars); 
                } else if (this.storeUnquoted == MIXED_CASE) {
                    retVal = isLowerCase(chars) || isUpperCase(chars);    
                }  
            }
        }
        return retVal;
    }
    
    /**
     * Sets whether or not this database stores all unquoted indentifiers in 
     * lower case.
     * @param yes For specifying that this database prefers lower cases
     * identifiers.
     */
    public void likesLowerCaseIdentifier(boolean yes) {
        if (yes) {
            storeUnquoted = LOWER_CASE;
        }
    }
    
    /**
     * Set whether or not this database stores all quoted indentifiers in lower
     * case.
     * @param yes boolean flag.
     */
    public void likesLowerCaseQuotedIdentifier(boolean yes) {
        if (yes) {
            storequoted = LOWER_CASE;
        }
    }
    
    /**
     * Sets whether or not this database stores all unquoted indentifiers in 
     * upper case.
     * @param yes boean flag.
     */
    public void likesUpperCaseIdentifier(boolean yes) {
        if (yes) {
            storeUnquoted = UPPER_CASE;
        }
    }
    
    /**
     * Sets whether or not this database stores all quoted indentifiers in upper
     * case.
     * @param yes booelan flag.
     */
    public void likesUpperCaseQuotedIdentifier(boolean yes) {
        if (yes) {
            storequoted = UPPER_CASE;
        }
    }
    
    /**
     * Set whether or not this database stores all unquoted indentifiers in 
     * mixed case.
     * @param yes boolean flag.
     */
    public void likesMixedCaseIdentifier(boolean yes) {
        if (yes) {
            storeUnquoted = MIXED_CASE;
        }
    }
    
    /**
     * Set whether or not this database stores all quoted indentifiers in mixed
     * case.
     * @param yes boolean flag.
     */
    public void likesMixedCaseQuotedIdentifier(boolean yes) {
        if (yes) {
            storequoted = MIXED_CASE;
        }
    }
    
    private static boolean isUpperCase(char[] chars) {
        boolean retVal = true;
        for (int i = 0; i < chars.length; i++) {
            if (Character.isLetter(chars[i]) 
                    && !Character.isUpperCase(chars[i])) {
                retVal = false;
                break;
            }
        }
        return retVal;
    }
    
    private static boolean isLowerCase(char[] chars) {
        boolean retVal = true;
        for (int i = 0; i < chars.length; i++) {
            if (Character.isLetter(chars[i]) 
                    && !Character.isLowerCase(chars[i])) {
                retVal = false;
            }
        }
        return retVal;
    }
    
    private static boolean isMixedCase(char[] chars) {
        return !isLowerCase(chars) && !isUpperCase(chars);
    }
    
    
    /**
     * Gets the default package name for datatypes.
     * @return The package name.
     */
    public String getDefaultTypesPkgName() {
        String url = this.getProperty(URL);
        String pkg = "";
        if (url != null && (url.trim() != null)) {
            url = url.replaceAll(":",  ".");
            int jdbcOdbc = url.indexOf("jdbc.odbc");
            if (jdbcOdbc == -1) {
                // no jdbc:odbc
                // next .
                if (url.indexOf("jdbc") >= 0) {
                    int secondDot = url.indexOf('.', 5); // pass jdbc.
                    pkg = url.substring(0, secondDot);
                } else {
                    // old logic.
                    int lastDot = url.lastIndexOf('.');
                    if (lastDot > -1) {
                        // remove the database name from the package.
                        pkg = url.substring(0, lastDot);
                    }
                }
            } else {
                // use full url when jdbc:odbc is present.
                pkg = url;
            }
        }
        return "types." + pkg;
    }
    
    static {
        propNames = new Vector();
        propNames.add(DRIVER);
        propNames.add(URL);
        propNames.add(USER);
        propNames.add(PASSWORD);
        propNames.add(FACTORY);
        propNames.add(SUPPORTS_SCHEMA);
        
    }
    
    
    
}
