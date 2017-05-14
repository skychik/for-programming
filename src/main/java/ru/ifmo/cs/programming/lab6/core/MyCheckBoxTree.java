package ru.ifmo.cs.programming.lab6.core;

import com.jidesoft.swing.CheckBoxTree;
import com.jidesoft.swing.CheckBoxTreeSelectionModel;

import javax.swing.event.TableModelListener;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.util.HashSet;
import java.util.Set;

class MyCheckBoxTree extends CheckBoxTree {
    private CheckBoxTreeSelectionModel checkBoxTreeSelectionModel;

    MyCheckBoxTree(DefaultMutableTreeNode rootNode) {
        super(rootNode);

        this.setCellRenderer(new MyCheckBoxTreeCellRenderer());

        checkBoxTreeSelectionModel = createCheckBoxTreeSelectionModel(new DefaultTreeModel(rootNode));

        /*this.addTreeCheckingListener(new TreeCheckingListener() {
            public void valueChanged(TreeCheckingEvent e) {
                System.out.println("Checked paths changed: user clicked on "
                        + (e.getLeadingPath().getLastPathComponent()));
            }
        });*/
    }
}
