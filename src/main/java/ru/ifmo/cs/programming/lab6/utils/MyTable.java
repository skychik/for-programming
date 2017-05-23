package ru.ifmo.cs.programming.lab6.utils;

import ru.ifmo.cs.programming.lab5.domain.Employee;
import ru.ifmo.cs.programming.lab5.utils.AttitudeToBoss;
import ru.ifmo.cs.programming.lab5.utils.Product;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.plaf.basic.BasicTreeUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Set;

import static ru.ifmo.cs.programming.lab6.App.getDeque;
import static ru.ifmo.cs.programming.lab6.core.MainFrame.*;

public class MyTable extends JTable implements TableModelListener {
    private TableRowSorter<TableModel> sorter;

    public MyTable(TableModel model) {
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
        tableListener();
        //this.setDefaultEditor(String.class, new DefaultCellEditor(new JComboBox(colors)));
    }

    public class MyTableCellRenderer extends DefaultTableCellRenderer {
        /**
         * Cell border thickness
         */
        private static final int BT = 2;

        /**
         * Selected cell border thickness
         */
        private static final int SBT = 1;

        /**
         * Returns renderer component
         *
         * @param table      renderable table
         * @param value      value to render
         * @param isSelected flag that indicates wether the cell is selected
         * @param hasFocus   flag that indicates wether the cell has focus
         * @param row        cell's row
         * @param column     cell's column
         *
         * @return cell renderer component
         */
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel c = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        /*Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (comp instanceof JComponent)
            ((JComponent)comp).setBorder(BorderFactory.createEmptyBorder());*/

            //c.setOpaque(true);
            //c.setForeground(Color.WHITE);
            //c.setBorder(BorderFactory.createEmptyBorder(BT, BT, BT, BT));
            c.setOpaque(true);

            c.setForeground(isSelected ?
                    MyColor.whiteTextColor :
                    MyColor.whiteTextColor.brighter());
            c.setBackground(isSelected ?
                    MyColor.backgroundColor :
                    MyColor.backgroundEighthAlphaColor);
            c.setBorder(hasFocus ?
                    BorderFactory.createLineBorder(MyColor.whiteTextColor, SBT) :
                    BorderFactory.createEmptyBorder(BT, BT, BT, BT));

//        return c;
//        if (!(value instanceof String)) {
//            c.setForeground(Color.red);
//            c.setBackground(Color.white);
//            //c.setText("Table element is not a java.util.Date!");
//            //setIcon(null);
//            return c;
//        }
//        /*Date date = (Date) value;
//        setText(format.format(date));*/
///*
//        // using fixed colors
//        setForeground(isSelected ? Color.white : Color.black);
//        setBackground(isSelected ? Color.blue : Color.white);
//        setBorder(hasFocus ?
//            BorderFactory.createLineBorder(Color.white, SBT) :
//            BorderFactory.createEmptyBorder(BT, BT, BT, BT));
//*/
///*
//        // using system colors
//        setForeground(isSelected ? SystemColor.textHighlightText : SystemColor.textText);
//        setBackground(isSelected ? SystemColor.textHighlight : SystemColor.text);
//        setBorder(hasFocus ?
//            BorderFactory.createLineBorder(SystemColor.textHighlightText, SBT) :
//            BorderFactory.createEmptyBorder(BT, BT, BT, BT));
//*/
            // using L&F colors
//        setForeground(isSelected ?
//                UIManager.getColor("Table.selectionForeground") :
//                UIManager.getColor("Table.foreground"));
//        setBackground(isSelected ?
//                UIManager.getColor("Table.selectionBackground") :
//                UIManager.getColor("Table.background"));
//        setBorder(hasFocus ?
//                BorderFactory.createLineBorder(UIManager.getColor("Table.selectionForeground"), SBT) :
//                BorderFactory.createEmptyBorder(BT, BT, BT, BT));

            return c;
        }
    }

    public TableRowSorter<TableModel> getSorter() {
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
    private void tableListener(){
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

    private void setAvatar(String avatarPath) {
        File avatarFile = new File(avatarPath);
        try {
            Image avatarImage = ImageIO.read(avatarFile);
            avatar.setIcon(new ImageIcon(avatarImage.getScaledInstance(250,250,1)));
            avatar.setBackground(new Color(0,0,0,0));
        } catch (IOException e) {
            //TODO: and what?

        } catch (NullPointerException e) {
            avatar.setIcon(new ImageIcon(getClass().getResource("images/standartAvatar.jpg")));
            avatar.setBackground(new Color(0,0,0,0));
        }
    }

    private boolean checkClicks(MouseEvent e) {
        return e.getClickCount() != 1;
    }
}
