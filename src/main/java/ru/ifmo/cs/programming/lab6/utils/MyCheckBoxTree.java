package ru.ifmo.cs.programming.lab6.utils;


import eu.floraresearch.lablib.gui.checkboxtree.*;

import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.table.TableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static eu.floraresearch.lablib.gui.checkboxtree.QuadristateButtonModel.State.CHECKED;
import static ru.ifmo.cs.programming.lab6.core.MyTableTab.getTable;

public class MyCheckBoxTree extends CheckboxTree {
    private RowFilter<TableModel, Integer> rowFilter = RowFilter.notFilter(RowFilter.regexFilter(""));

    public MyCheckBoxTree(DefaultMutableTreeNode rootNode) {
        super(rootNode);

        DefaultTreeCheckingModel treeCheckingModel = new DefaultTreeCheckingModel(new DefaultTreeModel(rootNode));
        setCheckingModel(treeCheckingModel);

        /*System.out.println(Arrays.toString(getCheckingPaths()));
        for (TreePath path : getCheckingPaths()) {
            treeCheckingModel.addCheckingPath(path);
        }*/

        setExpandability(false);

        //getCheckingModel().setCheckingMode(TreeCheckingModel.CheckingMode.SIMPLE);

        addTreeCheckingListener(new MyTreeCheckingListener());

        setCellRenderer(new MyCheckBoxTreeCellRenderer());

        //setShowsRootHandles(true);
        //this.addMouseListener(new );
    }

    public class MyCheckBoxTreeCellRenderer extends DefaultCheckboxTreeCellRenderer {
        MyCheckBoxTreeCellRenderer() {
            super();

            this.label.setBackgroundNonSelectionColor(MyColor.whiteTextColor);

            //this.checkBox.setState(CHECKED);
        }

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            Component c = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

            label.setForeground(MyColor.whiteTextColor);

            return c;
        }
    }

    class MyTreeCheckingListener implements TreeCheckingListener {
        final String root = "[Employee]";
        final Map<String, Boolean> map = new HashMap<>();

        public void valueChanged(TreeCheckingEvent event) {

            /*map.put("Employee", false);
            map.put("Factory Worker", false);
            map.put("Shop Assistant", false);*/

            //if root
            if (Objects.equals(event.getPath().toString(), root)) {
                //System.out.println("root");
                if (event.isCheckedPath()) {
                    //show everything
                    getTable().getSorter().setRowFilter(null);
                } else {
                    //hide everything
                    getTable().getSorter().setRowFilter(new RowFilter<TableModel, Integer>() {
                        @Override
                        public boolean include(Entry<? extends TableModel, ? extends Integer> entry) {
                            return false;
                        }
                    });
                }
            } else {
                ArrayList<RowFilter<TableModel, Integer>> filters = new ArrayList<>();
                filters.add(rowFilter);

                if (event.isCheckedPath()) {
                    //show this
                    //System.out.println("not root1");
                    filters.add(RowFilter.regexFilter(
                            "(?i)" + event.getPath().toString().substring(11, event.getPath().toString().length() - 1), 1));

                    rowFilter = RowFilter.orFilter(filters);
                } else {
                    //hide this
                    //System.out.println("not root2");
                    filters.add(RowFilter.notFilter(RowFilter.regexFilter(
                            "(?i)" + event.getPath().toString().substring(11, event.getPath().toString().length() - 1), 1)));

                    rowFilter = RowFilter.andFilter(filters);
                }

                getTable().getSorter().setRowFilter(rowFilter);
            }
        }
    }

    private void setExpandability(boolean b) {
        if (!b) {
            addTreeExpansionListener(new TreeExpansionListener() {
                @Override
                public void treeExpanded(TreeExpansionEvent event){}

                @Override
                public void treeCollapsed(TreeExpansionEvent event) {
                    expandAll();
                }
            });
        }
    }

//    private boolean wasChangedFromCheckedIntoGray(TreePath path) {
//        if (Objects.equals(path.toString(), "[Employee]")) {
//            QuadristateCheckbox checkbox = (QuadristateCheckbox)path.getLastPathComponent();
//
//        } /*else {
//            throw new Exception("");
//        }*/
//    }

    /*private void expandAll() {
        int row = 0;
        while (row < this.getRowCount()) {
            this.expandRow(row);
            row++;
        }
    }*/
}
