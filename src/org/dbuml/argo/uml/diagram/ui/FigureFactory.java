/**********************************************
 *   Copyright (C) 2007 CINCOM SYSTEMS, INC.
 *   All Rights Reserved
 **********************************************/

package org.dbuml.argo.uml.diagram.ui;

import org.dbuml.base.model.DBModelFacade;
import org.tigris.gef.graph.GraphModel;
import  org.tigris.gef.presentation.FigNode;

/**
 * DBUML figure class factory.
 * @author jgunderson
 */
public class FigureFactory {
    /**
     * The factory instance.
     */
    protected static FigureFactory instance;
    
    /** Creates a new instance of FigureFactory */
    public FigureFactory() {
        instance = this;
    }
    
    /**
     * Get instance.  This is set by implementing class *
     * @return The instance of this factory.
     */
    public static FigureFactory getInstance() {
        if (instance == null) {
            new FigureFactory();
        }
        return instance;
            
    }
    
    
    /**
     * Creates a figure.
     * @param gm he graph model.
     * @param handle The figure model element.
     * @return A figure.
     */
    public FigNode getFigure(GraphModel gm, Object handle) {
        if (DBModelFacade.getInstance().representsASchema(handle)) {
            return new FigSchema( handle);
        } else if (DBModelFacade.getInstance().representsADatabase(handle)) {
            return new FigDatabase(gm, handle);
        } else if (DBModelFacade.getInstance().representsATable(handle)) {
            return new FigTable(gm, handle);
        } else if (DBModelFacade.getInstance().representsAView(handle)) {
            return new FigView(gm, handle);
        } else if (DBModelFacade.getInstance().representsARegistry(handle)) {
            return new FigRegistry(gm, handle);
        }
        return null;
    }
    
    /**
     * Creates a dbuml figure.
     * @param handle The uml model for the figure.
     * @return A dbuml figure.
     */
    public FigNode getFigure(Object handle) {
        return getFigure(null, handle);
    }
    
    
}