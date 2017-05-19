package ru.ifmo.cs.programming.lab6.utils;


import eu.floraresearch.lablib.gui.checkboxtree.*;

import javax.swing.RowFilter;
import javax.swing.table.TableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import java.util.ArrayList;

import static ru.ifmo.cs.programming.lab6.core.MainFrame.getTable;

public class MyCheckBoxTree extends CheckboxTree {

    public MyCheckBoxTree(DefaultMutableTreeNode rootNode) {
        super(rootNode);

        setCheckingModel(new MyTreeCheckingModel(new DefaultTreeModel(rootNode)));

        addTreeCheckingListener(new TreeCheckingListener() {
            RowFilter<TableModel, Integer> rowFilter;
            boolean isRowFilterEmpty = true;

            public void valueChanged(TreeCheckingEvent e) {
                if (isRowFilterEmpty) {
                    isRowFilterEmpty = false;
                    rowFilter = RowFilter.notFilter(RowFilter.regexFilter(""));
                }

                if (e.getPath().toString().length() == 10) {
                    //root

                    if (e.isCheckedPath()) {
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
                    //not root

                    ArrayList<RowFilter<TableModel, Integer>> filters = new ArrayList<>();
                    filters.add(rowFilter);

                    if (e.isCheckedPath()) {
                        //show this

                        System.out.println(e.getPath().toString().substring(11, e.getPath().toString().length() - 1));

                        filters.add(RowFilter.regexFilter(
                                "(?i)" + e.getPath().toString().substring(11, e.getPath().toString().length() - 1), 1));

                        rowFilter = RowFilter.orFilter(filters);
                    } else {
                        //hide this

                        System.out.println(e.getPath().toString().substring(11, e.getPath().toString().length() - 1));

                        filters.add(RowFilter.notFilter(RowFilter.regexFilter(
                                "(?i)" + e.getPath().toString().substring(11, e.getPath().toString().length() - 1), 1)));

                        rowFilter = RowFilter.andFilter(filters);
                    }

                    getTable().getSorter().setRowFilter(rowFilter);
                }
            }
        });

        setCellRenderer(new MyCheckBoxTreeCellRenderer());

        //setShowsRootHandles(true);
        //this.addMouseListener(new );
    }
}
