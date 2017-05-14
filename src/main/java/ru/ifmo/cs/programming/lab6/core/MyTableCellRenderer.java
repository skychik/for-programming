package ru.ifmo.cs.programming.lab6.core;

import ru.ifmo.cs.programming.lab6.App;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

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

        //c.setOpaque(true);
        //c.setForeground(Color.WHITE);
        //c.setBorder(BorderFactory.createEmptyBorder(BT, BT, BT, BT));
        c.setOpaque(true);
        //c.setBackground(App.defEighthAlphaColor);

        c.setForeground(isSelected ?
                UIManager.getColor("Table.selectionForeground") :
                UIManager.getColor("Table.foreground"));
        c.setBackground(isSelected ?
                App.defColor :
                App.defHalfAlphaColor);
        c.setBorder(hasFocus ?
                BorderFactory.createLineBorder(UIManager.getColor("Table.selectionForeground"), SBT) :
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
