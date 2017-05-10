package ru.ifmo.cs.programming.lab6.core;

import ru.ifmo.cs.programming.lab5.domain.Employee;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.ArrayDeque;

public class MyTable extends JTable implements TableModelListener {

    public MyTable(TableModel model) {
        super(model);
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
