/**********************************************
 *   Copyright (C) 2007 CINCOM SYSTEMS, INC.
 *   All Rights Reserved
 **********************************************/

package org.dbuml.base.controller;

import org.dbuml.base.model.DBModelFacade;
import org.dbuml.base.model.Table;
import org.dbuml.base.model.Column;
import java.io.File; // sbalda on dec 1 05
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.dbuml.base.i18n.Translator;
import org.dbuml.base.controller.ui.EntitiesChooserDlg;

import org.dbuml.base.controller.ui.ConstraintDlg;

/**
 * DBUML figure actions handler.
 * @author  jgrengbondai
 */
public class GenericFigureActions extends GenericActions
        implements FigureActionsInterface {
    private static JFrame frame; //for FileChooser in generateSource,
    //by sbalda 2005 Dec 8.
    
    /** Creates a new instance of FigureActions. */
    public GenericFigureActions() {
    }
    
    /**
     * Imports database tables into the model.
     * @param schemaModel  The schema model object.
     */
    public void importTablesFromCatalog(Object schemaModel)  {
        importTablesFromCatalog(schemaModel, null, false);
    }
    
    /**
     * Updates the database with SQL DDL derived from the model.
     * @param selectedItem The model object.
     */
    public void updateCatalog(Object selectedItem) { 
        updateCatalog(selectedItem, false);
    }
    
    //modified by sbalda on Dec 2 2005
    /**
     * Generates SQL statements for the specified object model.
     * @param selectedItem The object model.
     */
    public void generateSource(Object selectedItem) {
        
            /* Code intent
             * present the dialog box to determine the file name.
             * call GenericActions:generateSource(Object,File)
             * [ it should generate the source, save the source in the file and
             * return the source as string ]
             */
        
        //show the fileChooser save dialog
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int result = fileChooser.showSaveDialog(frame);
        
        // If cancel button selected return
        if (result != JFileChooser.CANCEL_OPTION) {
            // Obtain selected file
            File fileName = fileChooser.getSelectedFile();

            // now to call generateSource(object,file) of GenericActions class
            generateSource(selectedItem, fileName);
        }
    }
    
    /**
     * Imports database schemas into the model.
     * @param selectedItem The database model.
     */
    public void importSchemas(Object selectedItem) {
        importSchemas(selectedItem, null, false);
    }
    
    /**
     * Adds not null constraint to a table.
     * @param tableModel The table model.
     */
    public void createConstraintNotNull(Object tableModel) {
        Table table = DBModelFacade.getInstance().getTable(tableModel);
        if (table != null) {
            Collection collection = new ArrayList();
            Iterator it = table.getColumnsVector().iterator();
            Column col;
            while (it.hasNext()) {
                col = (Column) it.next();
                if (col.allowsNulls()
                        && (!col.isPrimaryKey() || !col.isForeignKey())) {
                    collection.add(col.getName());
                }
            }
            
            String[] choices = EntitiesChooserDlg.selectData(
                    Translator.getInstance().localize("CONSTRAINT_NOTNULL"),
                    (String[]) collection.toArray(new String[]{}) );
            CreateActions.createConstraintNotNull(tableModel, choices);
        }
        
    }
    
    /**
     * Adds unique constraint to a table.
     * @param tableModel The table model.
     */
    public void createConstraintUnique(Object tableModel) {
        Table table = DBModelFacade.getInstance().getTable(tableModel);
        if (table != null) {
            Collection collection = new ArrayList();
            Iterator it = table.getColumnsVector().iterator();
            Column col;
            while (it.hasNext()) {
                col = (Column) it.next();
                if (!col.isPrimaryKey()
                        && !col.isForeignKey()) {
                    collection.add(col.getName());
                }
            }
            
            String[] choices = ConstraintDlg.selectData(
                    (String[]) collection.toArray(new String[]{}));
            
            ConstraintDlg dlg = ConstraintDlg.getInstance();
            CreateActions.createConstraintUnique(
                    table, choices, dlg.getIndexName());
        }
    }
    
    /**
     * Import database views into the model.
     * @param schemaModel The schema model.
     */
    public void importViews(Object schemaModel)  {
        importViews(schemaModel, null, false);
    }
    
    /**
     * Adds a column to a view.
     * @param viewModel The view model object.
     */
    public void createColumnForView(Object viewModel) {
        // Get all base tables/views for the given view.
        final Collection baseCollection 
            = DBModelFacade.getInstance().getViewBaseModels(viewModel);
        if (baseCollection.size() == 0) {
            Util.showMessageDialog(null,
                    Translator.getInstance().localize("NO_BASE_TABLE_FORVIEW"),
                    Translator.getInstance().localize("NO_BASE_TABLE"),
                    JOptionPane.ERROR_MESSAGE);
        } else {
        
            // put all the columns from the bases together in a map
            final Map allColsMap = new Hashtable(); // key = baseName.colName,
            // value = Column
            Iterator it = baseCollection.iterator();
            Table table = null;
            while (it.hasNext()) {
                final Object baseModel = it.next();
                table = DBModelFacade.getInstance().getTable(baseModel);
                if (table == null) {
                    table = DBModelFacade.getInstance().getView(baseModel);
                }
                if (table != null) {
                    final String tableName = table.getName();
                    // prefix the colNames with the table Name.
                    final Column[] cols = table.getColumns();
                    for (int i = 0; i < cols.length; i++) {
                        allColsMap.put(tableName + "." 
                                + cols[i].getName(), cols[i]);
                    }
                }
            }
            table = null;
            if (allColsMap.isEmpty()) {
                Util.showMessageDialog(null,
                        Translator.getInstance().localize(
                            "NO_BASE_ATTRS_FORVIEW"),
                        Translator.getInstance().localize("NO_ATTRS"),
                        JOptionPane.ERROR_MESSAGE);
            } else {
                final String[] choices = EntitiesChooserDlg.selectData(
                        Translator.getInstance().localize("ADD_COLUMN_FORVIEW"),
                        (String[]) allColsMap.keySet().toArray(new String[]{}));
                // TO DO: perform the following in Actions so that
                // BatchActions can
                // directly call it.

                CreateActions.createColumnForView(viewModel ,
                        allColsMap, choices );
            }
        }
        
    }
    
    /**
     * Imports databases into the model.
     * @param selectedItem The parent model object.
     */
    public void importDatabases(Object selectedItem) {
        Util.showMessageDialog(null,
                Translator.getInstance().localize("IMPORT_DATABASES"),
                Translator.getInstance().localize("NOT_IMPLEMENTED"),
                JOptionPane.ERROR_MESSAGE);
    }
    
}
