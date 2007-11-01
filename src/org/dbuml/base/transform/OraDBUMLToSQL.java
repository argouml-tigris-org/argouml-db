/**********************************************
 *   Copyright (C) 2007 CINCOM SYSTEMS, INC.
 *   All Rights Reserved
 **********************************************/

package org.dbuml.base.transform;

/** Class for transforming DBUML classes into SQL statements for oracle.
 *
 * @author  jgrengbondai
 */
public class OraDBUMLToSQL extends GenericDBUMLToSQL {
    private static final String TEMPLATES_DIR = 
            "org/dbuml/base/transform/templates/oracle/";
    
    /** Creates a new instance of OraDBUMLToSQL */
    public OraDBUMLToSQL() {
        super();
    }
    
    /** Gets path to Velocity template for dropping schemas.
     *@return the template path such as "org/dbuml/base/transform/templates/<name>.vm" 
     */
    protected String getTemplatePathDropSchema() {
        return TEMPLATES_DIR + "OraDropSchema.vm";     
    }
    
    /** Gets path to Velocity template for creating schemas.
     *@return the template path such as "org/dbuml/base/transform/templates/oracle/<name>.vm" 
     */
    protected String getTemplatePathCreateSchema() {
        return TEMPLATES_DIR + "OraCreateSchema.vm";     
    }
    
}
