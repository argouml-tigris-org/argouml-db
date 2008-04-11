/**********************************************
 *   Copyright (C) 2007 CINCOM SYSTEMS, INC.
 *   All Rights Reserved
 **********************************************/
package org.dbuml.argo.uml.diagram.ui;

import java.beans.PropertyVetoException;

import javax.swing.Action;

import org.apache.log4j.Logger;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.uml.diagram.deployment.DeploymentDiagramGraphModel;
import org.argouml.uml.diagram.deployment.ui.UMLDeploymentDiagram;
import org.argouml.util.ToolBarUtility;
import org.dbuml.argo.uml.ui.ActionCreateDatabase;
import org.dbuml.argo.uml.ui.ActionCreateEdge;
import org.dbuml.argo.uml.ui.ActionCreateSchema;
import org.dbuml.argo.uml.ui.CommandEdge;
import org.dbuml.base.database.DBMetadata;
import org.dbuml.base.model.DBModelFacade;
import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.base.LayerPerspectiveMutable;
import org.tigris.gef.base.ModeCreatePolyEdge;

/**
 * Deployment diagram for DBUML models.
 */
public class DBDeploymentDiagram extends UMLDeploymentDiagram {
    
    /**
     * The logger.
     */
    protected static Logger cat = Logger.getLogger(DBDeploymentDiagram.class);
    
    ////////////////
    // our new actions
    
    /**
     * Action for creating database.
     */
    protected Action actionMDatabase = new ActionCreateDatabase();
    
    /**
     * Action for creating schema.
     */
    protected Action actionMSchema = new ActionCreateSchema();
    
    /**
     * Action for creating dependency.
     */
    protected Action actionDBDependency;
    
    /**
     * Serial number for DBUML deployment diagrams.
     */
    public static int _DiagramSerial;
    
    /**
     * Creates new DBDeployment diagram.
     */
    public DBDeploymentDiagram() {
        try {
            setName(getNewDiagramName());
        } catch (PropertyVetoException pve) {
            cat.debug(pve.getMessage());
        }
    }
    
    /**
     * Creates new DBDeployment diagram.
     * @param namespace The namespace of the diagram.
     */
    public DBDeploymentDiagram(Object namespace) {
        this();
        setNamespace(namespace);
    }
    
    /**
     * Creates a new diagram name.
     * @return String
     */
    protected String getNewDiagramName() {
        String name = null;
        name = getLabelName() + _DiagramSerial;
        _DiagramSerial++;
        if (!ProjectManager.getManager().getCurrentProject().isValidDiagramName(name)) {
            name = getNewDiagramName();
        }
        return name;
    }
    
    /**
     * Gets default label name for new deployment diagrams.
     * @return The default name.
     */
    public String getLabelName() {
        return "Database Deployment Diagram ";
    }
    
    /**
     * method to perform a number of important initializations of a
     * <I>Deployment Diagram</I>.
     * 
     * @param handle The namespece object model. 
     */
    public void setNamespace(Object handle) {
        if (!Model.getFacade().isANamespace(handle)) {
            cat.error(
                    "Illegal argument. Object " + handle + " is not a namespace");
            throw new IllegalArgumentException(
                    "Illegal argument. Object " + handle + " is not a namespace");
        }
        Object m = /*(MNamespace)*/ handle;
        super.setNamespace(m);
        DeploymentDiagramGraphModel gm = new DBDeploymentDiagramGraphModel();
        gm.setHomeModel(m);
        LayerPerspective lay =
                new LayerPerspectiveMutable(Model.getFacade().getName(m), gm);
        DBDeploymentDiagramRenderer rend = new DBDeploymentDiagramRenderer();
        lay.setGraphNodeRenderer(rend);
        lay.setGraphEdgeRenderer(rend);
        setLayer(lay);
        
        // If primitive datatypes are not defined, add them.
        String sTypes[] = DBMetadata.getPrimitiveTypes();
        if (!(DBModelFacade.getInstance().checkTypes(sTypes))) {
            DBModelFacade.getInstance().addTypes(sTypes, "types.sql");
        }
    }
    
    
    /**
     * Gets the actions from which to create a toolbar or equivilent
     * graphic triggers
     * @return An array of actions.
     */
    protected Object[] getUmlActions() {
        Object actions[] =
        {
            getActionMNode(),
            getActionMNodeInstance(),
            getActionMComponent(),
//              actionMRegistry,
            actionMDatabase,
            actionMSchema,
            getActionMComponentInstance(),
            getActionMGeneralization(),
            getActionMAbstraction(),
//                getActionMDependency(),
            getActionDBDependency(),
            getAssociationActions(),
            getActionMObject(),
            getActionMLink()
        };
        
        return actions;
    }
    
    
    /**
     * Gets the association actions.
     * @return An array of actions.
     */
    protected Object[] getAssociationActions() {
        Object[][] actions = {
            {getActionAssociation(), getActionUniAssociation() },
            {getActionAggregation(), getActionUniAggregation() },
            {getActionComposition(), getActionUniComposition() },
        };
        ToolBarUtility.manageDefault(actions, "diagram.deployment.association");
        return actions;
    }
    
    protected Action getActionDBDependency() {
        if ( actionDBDependency == null){
            CommandEdge cmd = new CommandEdge(ModeCreatePolyEdge.class, "edgeClass",
                    (Class)Model.getMetaTypes().getDependency(), "Dependency", "DBDepend");
            actionDBDependency = new ActionCreateEdge(cmd, "New Dependency");
        }
        return actionDBDependency;
    }
    
} /* end class DBSchemaDiagram */

