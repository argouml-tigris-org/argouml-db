/**********************************************
 *   Copyright (C) 2007 CINCOM SYSTEMS, INC.
 *   All Rights Reserved
 **********************************************/

package org.dbuml.argo.uml.ui;

import java.awt.Component;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.diagram.ui.UMLDiagram;

/**
 * This class extends Argo's UMLTreeCellRenderer to include DBUML names and icons
 * in the tree explorer.
 * @author jgrengbondai
 */
// Copied and modified the base class to include dbuml class names.
public class DBUMLTreeCellRenderer extends org.argouml.uml.ui.UMLTreeCellRenderer {
    
    private static String name = Translator.localize("label.name");
    private static String typeName = Translator.localize("label.type");
    
    /** Creates a new instance of DBUMLTreeCellRenderer */
    public DBUMLTreeCellRenderer() {
    }
    
    public Component getTreeCellRendererComponent(
        JTree tree,
        Object value,
        boolean sel,
        boolean expanded,
        boolean leaf,
        int row,
        boolean hasFocusParam) {

	if (value instanceof DefaultMutableTreeNode) {
	    value = ((DefaultMutableTreeNode) value).getUserObject();
	}

        Component r =
            super.getTreeCellRendererComponent(
                tree,
                value,
                sel,
                expanded,
                leaf,
                row,
                hasFocusParam);

        if (r instanceof JLabel) {
            JLabel lab = (JLabel) r;

            // setting the icon
            Icon icon = ResourceLoaderWrapper.getInstance().lookupIcon(value);
            if (icon != null) {
                lab.setIcon(icon);
            }

            // setting the tooltip to type and name
            String type = null;
            
            //dbuml start: get dbuml types and icons.
//            if (Model.getFacade().isAModelElement(value)) {
//                    type = Model.getFacade().getUMLClassName(value);
//            } else if (value instanceof UMLDiagram) {
//                type = ((UMLDiagram) value).getLabelName();
//            }
            if ( null != org.dbuml.argo.model.ArgoDBModelFacade.getInstance() &&
                    org.dbuml.argo.model.ArgoDBModelFacade.getInstance().representsADbumlElement(value)) {
                type = org.dbuml.argo.model.ArgoDBModelFacade.getInstance().getDBUMLClassName(value);
                if (type != null) {
                    icon = ResourceLoaderWrapper.getInstance().lookupIcon(type);
                    if (icon != null) {
                        lab.setIcon(icon);
                    }
                }
            }
            if (type == null) { // dbuml condition
               //dbuml : include original code here
                if (Model.getFacade().isAModelElement(value)) {
                    type = Model.getFacade().getUMLClassName(value);
                } else if (value instanceof UMLDiagram) {
                    type = ((UMLDiagram) value).getLabelName();
                }
                // dbuml : end of original code 
            }
            //dbuml end

            if (type != null) {
                StringBuffer buf = new StringBuffer();
                buf.append("<html>");
                buf.append(name);
                buf.append(' ');
                buf.append(lab.getText());
                buf.append("<br>");
                buf.append(typeName);
                buf.append(' ');
                buf.append(type);
                lab.setToolTipText(buf.toString());
            } else {
                lab.setToolTipText(lab.getText());
            }
        }
        return r;
    }
    
}
