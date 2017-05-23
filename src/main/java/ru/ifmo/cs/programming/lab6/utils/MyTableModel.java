package ru.ifmo.cs.programming.lab6.utils;

import ru.ifmo.cs.programming.lab5.domain.Employee;
import ru.ifmo.cs.programming.lab5.utils.AttitudeToBoss;
import ru.ifmo.cs.programming.lab5.utils.Product;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Set;

public class MyTableModel implements TableModel {
    private HashSet<TableModelListener> listeners = new HashSet<>();
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
                return String.class;
            case 3:
                return Integer.class;
            case 4:
                return AttitudeToBoss.class;
            case 5:
                return Byte.class;
            default:
                return Product.class;
        }
    }

    public int getColumnCount() {
        return 6;
    }

    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return "Name";
            case 1:
                return "Speciality";
            case 2:
                return "Profession";
            case 3:
                return "Salary";
            case 4:
                return "Attitude to boss";
            case 5:
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
                return employee.getSpeciality();
            case 2:
                return employee.getProfession();
            case 3:
                return employee.getSalary();
            case 4:
                return employee.getAttitudeToBoss();
            case 5:
                return employee.getWorkQuality();
            case 6://TODO: TAKE AWAY FROM HERE
                return employee.getAvatarPath();
            case 7://TODO: TAKE AWAY FROM HERE
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
