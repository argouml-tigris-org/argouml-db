/**********************************************
 *   Copyright (C) 2007 CINCOM SYSTEMS, INC.
 *   All Rights Reserved
 **********************************************/

package org.dbuml.argo.uml.diagram.ui;

import java.util.Vector;

/**
 * Interface for DBUML additional figure methods.
 * @author  jgunderson
 */
public interface DBFigure {
    /**
     * Gets dbuml added actions.
     * @return A vector of actions.
     */
    public abstract Vector getAdditionalPopUpActions();
    /**
     * Gets DBUML figure type name.
     * @return The figure type name.
     */
    public abstract String getDBFigName();
}

