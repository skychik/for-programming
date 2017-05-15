package ru.ifmo.cs.programming.lab6.core;

import ru.ifmo.cs.programming.lab5.domain.Employee;
import ru.ifmo.cs.programming.lab5.utils.AttitudeToBoss;
import ru.ifmo.cs.programming.lab5.utils.Product;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Set;

public class MyTableModel implements TableModel {
    private Set<TableModelListener> listeners = new HashSet<>();
    private ArrayDeque<Employee> deque;

    public MyTableModel(ArrayDeque<Employee> deque) {
        super();
        this.deque = deque;
    }

    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return String.class;
            case 1:
                return String.class;
            case 2:
                return Integer.class;
            case 3:
                return AttitudeToBoss.class;
            case 4:
                return Byte.class;
            default:
                return Product.class;
        }
    }

    public int getColumnCount() {
        return 5;
    }

    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return "Name";
            case 1:
                return "Profession";
            case 2:
                return "Salary";
            case 3:
                return "Attitude to boss";
            case 4:
                return "Work quality";
        }
        return "";
    }

    public int getRowCount() {
        return deque.size();
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Employee employee = (Employee) deque.toArray()[rowIndex];
        switch (columnIndex) {
            case 0:
                return employee.getName();
            case 1:
                return employee.getProfession();
            case 2:
                return employee.getSalary();
            case 3:
                return employee.getAttitudeToBoss();
            case 4:
                return employee.getWorkQuality();
            case 5:
                return employee.getAvatarPath();
            case 6:
                return employee.getNotes();
        }
        return "";
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

    }

    @Override
    public void addTableModelListener(TableModelListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeTableModelListener(TableModelListener listener) {
        listeners.remove(listener);
    }
}
