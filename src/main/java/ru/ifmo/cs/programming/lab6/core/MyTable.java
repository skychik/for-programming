package ru.ifmo.cs.programming.lab6.core;

import ru.ifmo.cs.programming.lab5.domain.Employee;
import ru.ifmo.cs.programming.lab5.utils.AttitudeToBoss;
import ru.ifmo.cs.programming.lab6.App;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.plaf.ColorUIResource;
import javax.swing.event.TreeModelEvent;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import static ru.ifmo.cs.programming.lab6.App.getDeque;
import static ru.ifmo.cs.programming.lab6.core.MainFrame.*;

class MyTable extends JTable implements TableModelListener {
    private TableRowSorter<TableModel> sorter;

    MyTable(TableModel model) {
        super(model);

        this.setOpaque(false);
        //this.setBackground(App.backgroundEighthAlphaColor);

        setDefaultRenderer(Integer.class, new MyTableCellRenderer());
        setDefaultRenderer(String.class, new MyTableCellRenderer());
        setDefaultRenderer(Byte.class, new MyTableCellRenderer());
        setDefaultRenderer(AttitudeToBoss.class, new MyTableCellRenderer());

        getTableHeader().setReorderingAllowed(false);
        //setShowVerticalLines(true);


        sorter = new TableRowSorter<>(model);
        this.setRowSorter(sorter);
        tableListner();
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
    public void tableListner(){
        this.addMouseListener(new java.awt.event.MouseListener(){

            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println();
                if (checkClicks(e)) {
                    Employee employee = (Employee) getDeque().toArray()[getSelectedRow()];
                    getDeque().remove(employee);
                    updateUI();
                    getTabbedPane().setSelectedIndex(1);
                    setAvatar(employee.getAvatarPath());
                    setNotes(employee.getNotes());
                    setNameField(employee.getName());
                    setProfessionComboBox(employee.getProfession());
                    setBg(employee.getAttitudeToBoss().toString());
                    setClassList(employee.getClass().getSimpleName());
                    setSalarySlider(employee.getSalary());
                    setWorkQualityStepper(employee.getWorkQuality());
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }

    private boolean checkClicks(MouseEvent e){
        boolean doubleClick;
        if(e.getClickCount() == 1){
            doubleClick = false;
        }else{
            doubleClick = true;
        }
        return doubleClick;
    }
}
