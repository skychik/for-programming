package ru.ifmo.cs.programming.lab6.utils;


import eu.floraresearch.lablib.gui.checkboxtree.*;

import javax.swing.RowFilter;
import javax.swing.table.TableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.util.ArrayList;
import java.util.Iterator;

import static ru.ifmo.cs.programming.lab6.core.MainFrame.getTable;

public class MyCheckBoxTree extends CheckboxTree {

    public MyCheckBoxTree(DefaultMutableTreeNode rootNode) {
        super(rootNode);

        setCheckingModel(new MyTreeCheckingModel(new DefaultTreeModel(rootNode)));

        addTreeCheckingListener(new TreeCheckingListener() {
            RowFilter rowFilter;
            ArrayList<MyRowFilter> filters;
            Iterable<MyRowFilter> iterableRF;

            public void valueChanged(TreeCheckingEvent e) {
                if (e.getPath().toString().length() == 10) {
                    System.out.println("root");
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
                    if (e.isCheckedPath()) {
                        //show this
                        /*Iterable<RowFilter> filters = new Iterable() {
                            @Override
                            public Iterator<RowFilter<? super M, ? super I>> iterator() {
                                return ;
                            }
                        };*/

                        iterableRF = new Iterable<MyRowFilter>() {
                            @Override
                            public Iterator<MyRowFilter> iterator() {
                                return filters.iterator();
                            }
                        }

                        getTable().getSorter().setRowFilter(RowFilter.orFilter(iterableRF));
                    } else {
                        //hide this
                        getTable().getSorter().setRowFilter(RowFilter.notFilter(RowFilter.regexFilter(
                                "(?i)" + e.getPath().toString().substring(11, e.getPath().toString().length() - 2), 0)));
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
