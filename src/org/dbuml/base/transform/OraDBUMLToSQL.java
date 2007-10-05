/**********************************************
 *   Copyright (C) 2007 CINCOM SYSTEMS, INC.
 *   All Rights Reserved
 **********************************************/

package org.dbuml.base.transform;

/** Class for transforming DBUML classes into SQL statements for oracle.
 *
 * @author  rgupta
 */
public class OraDBUMLToSQL extends GenericDBUMLToSQL { 
    
    /** Creates a new instance of OraDBUMLToSQL */
    public OraDBUMLToSQL() {
        super();
        try {
            initTemplateDropSchema("org/dbuml/base/transform/OraDropSchema.vm");
            initTemplateCreateSchema(
                    "org/dbuml/base/transform/OraCreateSchema.vm");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
