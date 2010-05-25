/**********************************************
 *   Copyright (C) 2007 CINCOM SYSTEMS, INC.
 *   All Rights Reserved
 **********************************************/
package org.dbuml.argo.uml.diagram.ui;


import java.awt.Rectangle;
import java.beans.PropertyVetoException;

import javax.swing.Action;
import javax.swing.ImageIcon;

import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.uml.diagram.DiagramElement;
import org.argouml.uml.diagram.static_structure.ClassDiagramGraphModel;
import org.argouml.uml.diagram.static_structure.ui.ClassDiagramRenderer;
import org.argouml.uml.diagram.static_structure.ui.UMLClassDiagram;
import org.argouml.uml.ui.foundation.core.ActionAddOperation;
import org.argouml.util.ToolBarUtility;
import org.dbuml.argo.model.ArgoDBModelFacade;
import org.dbuml.argo.uml.ui.ActionCreateColumn;
import org.dbuml.argo.uml.ui.ActionCreateEdge;
import org.dbuml.argo.uml.ui.ActionCreatePKEY;
import org.dbuml.argo.uml.ui.ActionCreateSchema;
import org.dbuml.argo.uml.ui.ActionCreateTable;
import org.dbuml.argo.uml.ui.ActionCreateView;
import org.dbuml.argo.uml.ui.CommandEdge;
import org.dbuml.base.database.DBMetadata;
import org.dbuml.base.i18n.Translator;
import org.dbuml.base.model.DBModelFacade;
import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.base.LayerPerspectiveMutable;
import org.tigris.gef.base.ModeCreatePolyEdge;

/**
 *Schema diagram for DBUML.
 */
public class DBSchemaDiagram extends UMLClassDiagram {
    
    /*
     **  our new actions
     */
    /**
     * Action for creating table.
     */
    protected static Action actionTable = new ActionCreateTable();
    /**
     * Action for creating view.
     */
    protected static Action actionView = new ActionCreateView();
    /**
     * Action for creating schema.
     */
    protected static Action actionSchema = new ActionCreateSchema();
    /**
     * Action for creating foreign key.
     */
    protected static Action actionCreateFKEY = null;
    /**
     * Action for creating view derive link.
     */
    protected static Action actionCreateViewDerive = null;
    
    /*
     ** For some reasson getActionOpertion is private so we have to provide the action and getter
     */
    private Action actionOperation;
    
    /**
     * Serail number for schema diagrams.
     */
    public static int _DiagramSerial;
    
    /**
     * Creates new DBSchemaDiagram.
     */
    public DBSchemaDiagram() {
        super();
    }
    
    /**
     * Creates new DBSchemaDiagram.
     * @param name The diagram name
     * @param m the namespace for the new diagram
     */
    public DBSchemaDiagram(String name, Object m) {
        super(name, m);
    }
    
    /**
     * Creates new DBSchemaDiagram.
     * @param m the namespace for the new diagram
     */
    public DBSchemaDiagram(Object m) {
        super(m);
        try {
            setName(getNewDiagramName());
        } catch (PropertyVetoException pve) {
            System.err.println(pve.getMessage());
        }
    }
    
    //
    //Creates a new diagramname.
    // @return String
    //
    /**
     * Makes aa new daiagram name.
     * @return A default name.
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
     * Gets a label fornew diagram names.
     * @return The default new diagram name label.
     */
    public String getLabelName() {
        return "Database Schema Diagram ";
    }
    
    /**
     * Set the namespace of the diagram.
     * @param handle The namespace.
     */
    public void setNamespace(Object handle) {
        super.setNamespace(handle);
        ClassDiagramGraphModel gm = this.getDBGraphModel();
        gm.setHomeModel(handle);
        
        LayerPerspective lay = new LayerPerspectiveMutable(DBModelFacade.getInstance().getModelElementName(handle), gm);
        setLayer(lay);
        ClassDiagramRenderer rend = new DBSchemaDiagramRenderer(); // singleton
        lay.setGraphNodeRenderer(rend);
        lay.setGraphEdgeRenderer(rend);
    }
    
    /**
     * Gets the actions from which to create a toolbar or equivilent
     * graphic trigger.
     * @see org.argouml.uml.diagram.ui.UMLDiagram#getUmlActions()
     * @return An array of actions.
     */
    protected Object[] getUmlActions() {
        Object actions[] = {
            actionSchema,
            actionTable,
            actionView,
            getAssociationActions(),
            getActionGeneralization(),
            null,
            getActionCreateFKEY(),
            getActionCreateDerive(),
            ActionCreateColumn.getInstance(),
            ActionCreatePKEY.getInstance(),
            getActionOperation(),
        };
        
        return actions;
    }
    
    /**
     * @return Returns the actionOperation.
     */
    private Action getActionOperation() {
        if (actionOperation == null) {
            actionOperation = new ActionAddOperation();
        }
        return actionOperation;
    }
    
    private Object[] getAssociationActions() {
        // This calls the getters to fetch actions even though the
        // action variables are defined is instances of this class.
        // This is because any number of action getters could have
        // been overridden in a descendant and it is the action from
        // that overridden method that should be returned in the array.
        Object[] actions = {
            getActionAssociation(),
            getActionUniAssociation(),
        };
        ToolBarUtility.manageDefault(actions, "diagram.class.association");
        return actions;
    }
    
    //
    // Return an array of edge actions for the dbuml profile
    //
    private Action getActionCreateFKEY() {
        if ( actionCreateFKEY == null) {
            CommandEdge cmd = new CommandEdge(ModeCreatePolyEdge.class, "edgeClass",
                    (Class)Model.getMetaTypes().getAssociation(), "Usage", "FK");
            actionCreateFKEY = new ActionCreateEdge(cmd, Translator.getInstance().localize("FKEY_LINK"));
            actionCreateFKEY.putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/org/dbuml/argo/Images/FKey.gif")));
        }
        return actionCreateFKEY;
    }
    
    private Action getActionCreateDerive() {
        if ( actionCreateViewDerive == null) {
            CommandEdge cmd = new CommandEdge(ModeCreatePolyEdge.class, "edgeClass",
                    (Class)Model.getMetaTypes().getDependency(), "Dependency", "DERIVE");
            actionCreateViewDerive = new ActionCreateEdge(cmd, Translator.getInstance().localize("DERIVE_DEP"));
            actionCreateViewDerive.putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/org/dbuml/argo/Images/ViewDerive.gif")));
        }
        return actionCreateViewDerive;
    }
    
    /**
     * Get the graph model for this diagram.
     * @return The graph model.
     */
    protected DBSchemaDiagramGraphModel getDBGraphModel() {
        return new DBSchemaDiagramGraphModel();
    }
    
    
    public DiagramElement createDiagramElement(
            final Object modelElement,
            final Rectangle bounds) {
    	DiagramElement figNode = null;
   		if (ArgoDBModelFacade.getInstance().representsASchema(modelElement)) {
   			figNode = new FigSchema(modelElement, bounds.x, bounds.y);
   		} else if (ArgoDBModelFacade.getInstance().representsATable(modelElement)) {
   			figNode = new FigTable(modelElement, bounds.x, bounds.y, bounds.width, bounds.height);
   		}
   		if (figNode == null) {
   			figNode = super.createDiagramElement(modelElement, bounds);
   		}
        return figNode;
    }

} /* end class DBSchemaDiagram */

