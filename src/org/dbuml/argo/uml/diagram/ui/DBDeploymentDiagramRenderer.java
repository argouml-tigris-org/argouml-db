/**********************************************
 *   Copyright (C) 2007 CINCOM SYSTEMS, INC.
 *   All Rights Reserved
 **********************************************/

package org.dbuml.argo.uml.diagram.ui;


import org.tigris.gef.base.Layer;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.FigNode;

import org.argouml.uml.diagram.deployment.ui.DeploymentDiagramRenderer;

import java.util.Map;

/**
 * Rendere class for DBUML deployment diagrams.
 */
public class DBDeploymentDiagramRenderer extends DeploymentDiagramRenderer {
    //*  Need to convert to DBModelFacade
    
    /**
     * Returns a Fig that can be used to represent the given node
     * @param gm see super class.
     * @param lay see super class.
     * @param node see super class.
     * @param styleAttributes see super class.
     * @return see super class.
     * @see org.argouml.uml.diagram.deployment.ui.DeploymentDiagramRenderer#getFigNodeFor
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
