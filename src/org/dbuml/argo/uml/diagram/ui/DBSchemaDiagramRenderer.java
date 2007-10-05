/**********************************************
 *   Copyright (C) 2007 CINCOM SYSTEMS, INC.
 *   All Rights Reserved
 **********************************************/
package org.dbuml.argo.uml.diagram.ui;

import org.tigris.gef.presentation.FigNode;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.base.Layer;

import org.argouml.uml.diagram.static_structure.ui.ClassDiagramRenderer;

import java.util.Map;

/**
 * Rendere for DBUML schema diagram.
 */
public class DBSchemaDiagramRenderer extends ClassDiagramRenderer {
    //*  Need to convert to DBModelFacade
    
    /**
     * Return a Fig that can be used to represent the given node
     * @param gm see the supre class.
     * @param lay  see the super class
     * @param node see the super class
     * @param styleAttributes see the super class
     * @return An instance of FigNode.
     */
    public FigNode getFigNodeFor(GraphModel gm, Layer lay, Object node, Map styleAttributes) {
        
        FigNode fg = FigureFactory.getInstance().getFigure(gm, node);
        if (fg != null) {
            return fg;
        } else {
            return  super.getFigNodeFor(gm, lay, node, styleAttributes);
        }
        
    }
}
