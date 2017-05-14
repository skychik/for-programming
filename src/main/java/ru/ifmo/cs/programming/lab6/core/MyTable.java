package ru.ifmo.cs.programming.lab6.core;

import ru.ifmo.cs.programming.lab5.utils.AttitudeToBoss;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

class MyTable extends JTable implements TableModelListener {
    private TableRowSorter<TableModel> sorter;

    MyTable(TableModel model) {
        super(model);
        //this.setOpaque(false);
        //this.setForeground(Color.WHITE);
        //this.setBackground(defEighthAlphaColor);
        this.setDefaultRenderer(Integer.class, new MyTableCellRenderer());
        this.setDefaultRenderer(String.class, new MyTableCellRenderer());
        this.setDefaultRenderer(Byte.class, new MyTableCellRenderer());
        this.setDefaultRenderer(AttitudeToBoss.class, new MyTableCellRenderer());
        this.getTableHeader().setReorderingAllowed(false);

        sorter = new TableRowSorter<>(model);
        this.setRowSorter(sorter);
        //this.setDefaultEditor(String.class, new DefaultCellEditor(new JComboBox(colors)));
    }

    TableRowSorter<TableModel> getSorter() {
        return sorter;
    }

    /*public void tableChanged(TableModelEvent e) {
        int row = e.getFirstRow();
        int column = e.getColumn();
        TableModel model = (TableModel)e.getSource();
        String columnName = model.getColumnName(column);
        Object data = model.getValueAt(row, column);

        // Do something with the data...
    }*/
}
