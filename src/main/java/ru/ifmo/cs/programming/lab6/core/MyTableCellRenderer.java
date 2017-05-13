package ru.ifmo.cs.programming.lab6.core;

import ru.ifmo.cs.programming.lab6.App;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class MyTableCellRenderer extends JLabel implements TableCellRenderer {
//    /**
//     * date format
//     */
//    private final DateFormat format;
//
//    /**
//     * calendar icon
//     */
//    private final Icon icon;
//
//    /**
//     * Cell border thickness
//     */
//    public static final int BT = 2;
//
//    /**
//     * Selected cell border thickness
//     */
//    private static final int SBT = 1;

//    /**
//     * Constructs renderer
//     *
//     * @param format date format to use. See <code>java.text.SimpleDateFormat</code>
//     *               for description
//     */
//    public DateCellRenderer(String format) {
//        this.format = new SimpleDateFormat(format);
//        setBorder(BorderFactory.createEmptyBorder(BT, BT, BT, BT));
//        setOpaque(true);
//        icon = new ImageIcon(getClass().getResource("/calendar.png"));
//    }
    public MyTableCellRenderer() {
        super();
    }

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
        setOpaque(true);
        setForeground(Color.WHITE);
        setBackground(App.defColor);

        return this;
//        if (!(value instanceof Date)) {
//            setForeground(Color.red);
//            setBackground(Color.white);
//            setText("Table element is not a java.util.Date!");
//            setIcon(null);
//            return this;
//        }
//        Date date = (Date) value;
//        setText(format.format(date));
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
//        // using L&F colors
//        setForeground(isSelected ?
//                UIManager.getColor("Table.selectionForeground") :
//                UIManager.getColor("Table.foreground"));
//        setBackground(isSelected ?
//                UIManager.getColor("Table.selectionBackground") :
//                UIManager.getColor("Table.background"));
//        setBorder(hasFocus ?
//                BorderFactory.createLineBorder(UIManager.getColor("Table.selectionForeground"), SBT) :
//                BorderFactory.createEmptyBorder(BT, BT, BT, BT));
//        setIcon(icon);
//        return this;
    }
}
