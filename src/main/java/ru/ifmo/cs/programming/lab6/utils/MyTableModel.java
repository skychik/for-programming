package ru.ifmo.cs.programming.lab6.utils;

import ru.ifmo.cs.programming.lab5.core.InteractiveModeFunctions;
import ru.ifmo.cs.programming.lab5.domain.Employee;
import ru.ifmo.cs.programming.lab5.utils.AttitudeToBoss;
import ru.ifmo.cs.programming.lab5.utils.Product;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.HashSet;

public class MyTableModel implements TableModel {
    private HashSet<TableModelListener> listeners = new HashSet<>();
    private InteractiveModeFunctions imf;

    public MyTableModel(InteractiveModeFunctions imf) {
        super();
        this.imf = imf;
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
                return ("name");
            case 1:
                return ("speciality");
            case 2:
                return ("profession");
            case 3:
                return ("salary");
            case 4:
                return ("attitudeToBoss");
            case 5:
                return ("workQuality");
        }
        return "";
    }

    public int getRowCount() {
        return imf.getSize();
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Employee employee = imf.getEmployees()[rowIndex];
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
                return employee.returnAttitude_to_boss();
            case 5:
                return employee.getWork_quality();
            case 6://TODO: TAKE AWAY FROM HERE
                return employee.getAvatar_path();
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
