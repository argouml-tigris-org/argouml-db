/**********************************************
 *   Copyright (C) 2007 CINCOM SYSTEMS, INC.
 *   All Rights Reserved
 **********************************************/
package org.dbuml.argo.uml.ui;

import javax.swing.JMenuItem;
import org.argouml.ui.ProjectBrowser;
import org.argouml.kernel.ProjectManager;
import org.argouml.moduleloader.ModuleInterface;
import org.argouml.ui.cmd.GenericArgoMenuBar;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.model.Model;
import org.dbuml.argo.language.GeneratorSQL;

import org.apache.log4j.Logger;

import org.dbuml.argo.model.ArgoDBModelFacade;
import org.dbuml.argo.uml.diagram.ui.FigureFactory;
import org.dbuml.base.i18n.Translator;


/**
 * This class implements Argo's ModuleInterface which has methods
 * that are called by Argo's module loader during startup.  Any jar
 * in the /ext folder with a manifest defining a class that implements
 * ModuleInterface is loaded and enable is called.
 */
public class DBModule implements ModuleInterface {
    //
    // We implement these ModuleInterface methods which are called by the module loader
    // in startup.  Any jar in the /ext folder with a manifest definting a class that
    // implements ModuleInterface is loaded and enable is called.
    //
    
    /**
     * Logger.
     */
    private static final Logger LOG =
            Logger.getLogger(DBModule.class);
    
    
    public DBModule() {
        
    }
    
    private static DBDeploymentDiagramMenu _deployAction = null;
    
    private static JMenuItem _deployMenu = null;
    
    private static DBSchemaDiagramMenu _schemaAction = null;
    
    private static JMenuItem _schemaMenu = null;
    
    private boolean _initialized = false;
    
    /**
     * 
     * @return 
     */
    public boolean enable() {
        //add dbuml resource location
        
        
        //Argo issue 4822
        //dbuml:removed
//        ResourceLoaderWrapper.addResourceLocation("/org/dbuml/argo/Images");
        
        //dbuml:removed
//        org.argouml.ui.ProjectBrowser.getInstance().getExplorerPane().getExplorerTree().setCellRenderer(
//                new org.dbuml.argo.uml.ui.DBUMLTreeCellRenderer());
        
        //
        // get create diagrams menu bar
        //
        GenericArgoMenuBar menubar =
                (GenericArgoMenuBar) ProjectBrowser.getInstance().getJMenuBar();
        
        ProjectBrowser.getInstance().setAppName(this.getName());
        //
        // Add menuitem to creatediagram menu for schema diagrams
        // todo:  we need to add these items to the Create Diagrams menu
        //        not the tools menu.  See issue 4779.
        //
        if (_schemaMenu == null) {
            _schemaMenu = new JMenuItem("New Database Schema Diagram",
//				      ResourceLoaderWrapper.lookupIconResource("ClassDiagram"));
                    ResourceLoaderWrapper.lookupIconResource("DBSchemaDiagram"));
        }
        if (_schemaAction == null) {
            _schemaAction = new DBSchemaDiagramMenu();
        }
        _schemaMenu.addActionListener(_schemaAction);
        menubar.getTools().add(_schemaMenu);
        
        //
        // Add menuitem to creatediagram menu for deploymennt diagrams
        //
        if (_deployMenu == null) {
            _deployMenu = new JMenuItem("New Database Deployment Diagram",
//				      ResourceLoaderWrapper.lookupIconResource("DeploymentDiagram"));
                    ResourceLoaderWrapper.lookupIconResource("DBDeploymentDiagram"));
        }
        if (_deployAction == null) {
            _deployAction = new DBDeploymentDiagramMenu();
        }
        _deployMenu.addActionListener(_deployAction);
        menubar.getTools().add(_deployMenu);
        
        this.initializeFactories();
        
        //create default dbuml diagrams
//        createDefaultDiagrams();
        
        LOG.info("*** DBUML module initialized");
        _initialized = true;
        return _initialized;
    }
    
    /**Initializes all the factories.
     */
    protected void initializeFactories() {
        initializeFigureFactory();
        initializeDBModelFactory();
        initializeTranslator();
        initializeGeneratorSQL();
    }
    
    /** Initializes the FigureFactory.
     */
    protected void initializeFigureFactory() {
        FigureFactory.getInstance();    
    }
    
    /** Initializes the DBModelFactory.
     */
    protected void initializeDBModelFactory() {
        // If subclass has not instantiated an ArgoDBModleFacade, instatiate the generic one.
        ArgoDBModelFacade.getInstance();  
    }
    
    /** Initializes the Translator.
     */
    protected void initializeTranslator() {
        Translator.getInstance(); 
    }
    
    /** Initializes the GeneratorSQL.
     */
    protected void initializeGeneratorSQL() {
        // make SQL language
        GeneratorSQL.getInstance();  
    }
    
    
    /** Creates default DBUML diagrams.
     */
    public static void createDefaultDiagrams() {
        
        //Lifted and also called from org.argouml.ui.cmd.ActionNew.actionPerformed. 
        
        //Without the following, we will get misterious null pointer exception.
        Model.getPump().flushModelEvents();
        Model.getPump().stopPumpingEvents();
        Model.getPump().flushModelEvents();
        
        org.argouml.kernel.Project p = org.argouml.kernel.ProjectManager.getManager().getCurrentProject();
        
        // remove the current project
        p.remove();
        
        // create an empty project without any diagrams.
        p = ProjectManager.getManager().makeEmptyProject(false);
        
        // Create a root model.
        Object model = Model.getModelManagementFactory().createModel();
        Model.getCoreHelper().setName(model,"untitledmodel");
        p.setRoot(model);
        p.setCurrentNamespace(model);
        p.addMember(model);
       
        // create and add DB-UML diagrams.
        org.dbuml.argo.uml.diagram.ui.DBDeploymentDiagram d = new org.dbuml.argo.uml.diagram.ui.DBDeploymentDiagram(p.getModel());
        p.addDiagram(d);
        p.addDiagram(new org.dbuml.argo.uml.diagram.ui.DBSchemaDiagram(p.getModel()));
        p.setActiveDiagram(d);
        
        //refresh the project, otherwise nothing is shown in the explorer.
        ProjectManager.getManager().setCurrentProject(p);
        
        // disable saving.
        ProjectManager.getManager().setSaveEnabled(false);
            
    }
    
    
    
    /**
     * 
     * @return 
     */
    public boolean disable() {
        return true;
    }
    
    
    /**
     * 
     * @return 
     */
    public String getName() { return "DB-UML"; }
    
    /**
     * 
     * @param type 
     * @return 
     */
    public String getInfo(int type) {
        String info = null;
        switch (type) {
            case DESCRIPTION:
                info = "SQL UML Modeling";
                break;
            case AUTHOR:
                info = "jgunderson@cincom.com";
                break;
            case VERSION:
                info = "1.0.0";
                break;
            default:
               
        }
        return info;
    }
    
}
