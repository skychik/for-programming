package ru.ifmo.cs.programming.lab6.utils;
//
import ru.ifmo.cs.programming.lab6.core.MyMenu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by саша on 05.09.2017.
 */
public class Clock extends JLabel implements ActionListener{

    public static DateFormat getDateFormat() {
        return dateFormat;
    }


    public static void setDateFormat(DateFormat dateFormat) {
        Clock.dateFormat = dateFormat;
    }

    private static DateFormat dateFormat;

    public Clock(){
//        dateFormat = new SimpleDateFormat("Y-MMMM-dd HH:mm:ss", MyMenu.getMyLocale());
        setFont(new Font("sans-serif", Font.PLAIN, 14));
        setForeground(MyColor.whiteTextColor);
//        setHorizontalAlignment(SwingConstants.LEFT);
        Timer t = new Timer(1000, this);
        t.start();
    }

    public void actionPerformed(ActionEvent e) {
        Date d = new Date();
        setText(dateFormat.format(d));
    }
}
