/**********************************************
 *   Copyright (C) 2007 CINCOM SYSTEMS, INC.
 *   All Rights Reserved
 **********************************************/
/*
 * ExplorerTree.java
 *
 * Created on September 4, 2007, 3:52 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.dbuml.argo.ui.explorer;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.util.Vector;

import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import org.argouml.ui.explorer.DnDExplorerTree;

import org.argouml.ui.ProjectBrowser;
import org.argouml.ui.targetmanager.TargetManager;


/**
 *This class adds DBUML actions to models on the Explorer tree view.
 *To use it, we have to replace DnDExplorerTree in org.argouml.ui.NavigatorPane
 *and rebuild argo. 
 *
 * @author jgrengbondai
 */
public class ExplorerTree extends DnDExplorerTree {
    
    /** Creates a new instance of ExplorerTree. */
    public ExplorerTree() {
        super();
        this.addMouseListener(new ExplorerMouseListener(this));
        this.setCellRenderer(new org.dbuml.argo.uml.ui.DBUMLTreeCellRenderer() );
    }
    
    //The following MouseListener Class is lift from the ExplorerTree class in argo.
    //We couldn't not simply extend it because it has an package scope.
    //The ExplorerPopup is replaced with DBExplorerPopup.
    
    /**
     * Listens to mouse events coming from the *JTree*,
     * on right click, brings up the pop-up menu.
     */
    class ExplorerMouseListener extends MouseAdapter {

        private JTree mLTree;

        /**
         * The constructor.
         * @param newtree
         */
        public ExplorerMouseListener(JTree newtree) {
            super();
            mLTree = newtree;
        }

        /**
         * @see java.awt.event.MouseListener#mousePressed(
         *         java.awt.event.MouseEvent)
         *
         * Brings up the pop-up menu.
         */
        public void mousePressed(MouseEvent me) {
            if (me.isPopupTrigger()) {
                me.consume();
                showPopupMenu(me);
            }
        }

        /**
         * @see java.awt.event.MouseListener#mouseReleased(
         *         java.awt.event.MouseEvent)
         *
         * Brings up the pop-up menu.
         *
         * On Windows and Motif platforms, the user brings up a popup menu
         * by releasing the right mouse button while the cursor is over a
         * component that is popup-enabled.
         * 
         */
        public void mouseReleased(MouseEvent me) {
            if (me.isPopupTrigger()) {
                me.consume();
                showPopupMenu(me);
            }
        }

        /**
         * @see java.awt.event.MouseListener#mouseClicked(
         *         java.awt.event.MouseEvent)
         *
         * Brings up the pop-up menu.
         */
        public void mouseClicked(MouseEvent me) {
            if (me.isPopupTrigger()) {
                me.consume();
                showPopupMenu(me);
            }
            if (me.getClickCount() >= 2) {
                myDoubleClick();
            }
        }

        /**
         * Double-clicking on an item attempts
         * to show the item in a diagram.
         */
        private void myDoubleClick() {
            Object target = TargetManager.getInstance().getTarget();
            if (target != null) {
                Vector show = new Vector();
                show.add(target);
                ProjectBrowser.getInstance().jumpToDiagramShowing(show);
            }
        }

        /**
         * Builds a pop-up menu for extra functionality for the Tree.
         *
         * @param me The mouse event.
         */
        public void showPopupMenu(MouseEvent me) {

            TreePath path = getPathForLocation(me.getX(), me.getY());
            if (path == null) {
                return;
            }

            /*
             * We preserve the current (multiple) selection,
             * if we are over part of it ...
             */
            if (!isPathSelected(path)) {
                /* ... otherwise we select the item below the mousepointer. */
                getSelectionModel().setSelectionPath(path);
            }

            Object selectedItem =
                ((DefaultMutableTreeNode) path.getLastPathComponent())
                        .getUserObject();
            //Replaced ExplorerPopup with DBExplorerPopup
            JPopupMenu popup = new DBExplorerPopup(selectedItem, me);

            if (popup.getComponentCount() > 0) {
                popup.show(mLTree, me.getX(), me.getY());
            }
        }

    } /* end class ExplorerMouseListener */
    
}
