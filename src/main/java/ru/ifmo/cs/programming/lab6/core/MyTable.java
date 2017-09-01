package ru.ifmo.cs.programming.lab6.core;

import ru.ifmo.cs.programming.lab5.core.InteractiveModeFunctions;
import ru.ifmo.cs.programming.lab5.domain.Employee;
import ru.ifmo.cs.programming.lab5.utils.AttitudeToBoss;
import ru.ifmo.cs.programming.lab6.utils.MyColor;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import static ru.ifmo.cs.programming.lab6.core.MainFrame.*;

class MyTable extends JTable implements TableModelListener {
    private TableRowSorter<TableModel> sorter;
    private InteractiveModeFunctions imf;

//    private static ArrayList<VisibilityOfSpeciality> visibilitiesOfSpecialities = new List<>();

    static class VisibilityOfSpeciality implements Map.Entry {
        String key;
        Boolean value;

        VisibilityOfSpeciality(String key, Boolean value) {
            this.key = key;
            this.value = value;
        }

        VisibilityOfSpeciality() {
            this.key = "";
            this.value = true;
        }


        @Override
        public String getKey() {
            return "Employee";
        }

        @Override
        public Boolean getValue() {
            return value;
        }

        @Override
        public Object setValue(Object value) {
            if (this.value == value) {
                this.value = (Boolean) value;
                return true;
            } else {
                this.value = (Boolean) value;
                return false;
            }
        }
    }

    MyTable(TableModel model, InteractiveModeFunctions imf) {
        super(model);

        this.imf = imf;

        this.setOpaque(false);
        //this.setBackground(AppGUI.backgroundEighthAlphaColor);

        setDefaultRenderer(Integer.class, new MyTableCellRenderer());
        setDefaultRenderer(String.class, new MyTableCellRenderer());
        setDefaultRenderer(Byte.class, new MyTableCellRenderer());
        setDefaultRenderer(AttitudeToBoss.class, new MyTableCellRenderer());

        getTableHeader().setReorderingAllowed(false);

        sorter = new TableRowSorter<>(model);
        this.setRowSorter(sorter);
        tableListener();
        //this.setDefaultEditor(String.class, new DefaultCellEditor(new JComboBox(colors)));
//
//        initVisibilityOfSpeciality();
//
//        visibilitiesOfSpecialities. {
//            @Override
//            public void onChanged(Change<? extends String, ? extends Boolean> change) {
//                System.out.println("vos");
//                if (change.getValueAdded() == change.getValueRemoved()) {
//                    try {
//                        throw new IOException();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                } else try {
//                    getTable().getSorter().setRowFilter(getTable().getRowFilter());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
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
    private void tableListener() {
        this.addMouseListener(new java.awt.event.MouseListener(){

            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println();
                if (checkClicks(e)) {
                    Employee employee = imf.getEmployees()[getSelectedRow()];
	                imf.remove(employee);
	                updateUI();
                    getTabbedPane().setSelectedIndex(1);
                    CommandTab.setAvatar(employee.getAvatarPath());
                    CommandTab.setNotes(employee.getNotes());
                    CommandTab.setNameField(employee.getName());
                    CommandTab.setProfessionComboBox(employee.getProfession());
                    CommandTab.setBg(employee.getAttitudeToBoss().toString());
                    CommandTab.setClassList(employee.getClass().getSimpleName());
                    CommandTab.setSalarySlider(employee.getSalary());
                    CommandTab.setWorkQualityStepper(employee.getWorkQuality());
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
            CommandTab.getAvatar().setIcon(new ImageIcon(avatarImage.getScaledInstance(250,250,1)));
            CommandTab.getAvatar().setBackground(new Color(0,0,0,0));
        } catch (IOException e) {
            //TODO: and what?

        } catch (NullPointerException e) {
            CommandTab.getAvatar().setIcon(new ImageIcon(getClass().getResource("images/standardAvatar.jpg")));
            CommandTab.getAvatar().setBackground(new Color(0,0,0,0));
        }
    }

    private boolean checkClicks(MouseEvent e) {
        return e.getClickCount() != 1;
    }

//    private void initVisibilityOfSpeciality() {
//        visibilityOfSpeciality.put("[Employee]", false);
//        visibilityOfSpeciality.put("[Employee, Factory Worker]", false);
//        visibilityOfSpeciality.put("[Employee, Shop Assistant]", false);
//    }
//
//    private RowFilter getRowFilter() throws IOException {
//        StringBuilder format = new StringBuilder();
//
//        for (Boolean key : visibilityOfSpeciality.values()) {
//            if (key) format.append('t');
//                else format.append('f');
//        }
//
//        System.out.println(visibilityOfSpeciality.values().toString());
//        System.out.println(visibilityOfSpeciality.keySet().toString());
//
//        switch (format.toString()) {
//            case "ttt":
//                return RowFilter.regexFilter(
//                        "", 1);//everything
//            case "fff":
//                return new RowFilter<TableModel, Integer>() {
//                        @Override
//                        public boolean include(Entry<? extends TableModel, ? extends Integer> entry) {
//                            return false;
//                        }
//                    };
//            case "tff":
//                return RowFilter.regexFilter(
//                            "(?i)Employee", 1);
//            case "ttf":
//            case "ftf":
//                return RowFilter.notFilter(RowFilter.regexFilter(
//                        "(?i)Shop Assistant", 1));
//            case "tft":
//            case "fft":
//                return RowFilter.notFilter(RowFilter.regexFilter(
//                        "(?i)Fabric Worker", 1));
//            case "ftt":
//            default:
//                throw new IOException(format.toString());
//        }
//    }
//
//    static void changeVisibilityOfSpeciality(String speciality, Boolean b) throws IOException {
//        visibilityOfSpeciality.put(speciality, b);
//    }

    /*public ObservableMap<String, Boolean> getVisibilityOfSpeciality() {
        return visibilityOfSpeciality;
    }*/
}
