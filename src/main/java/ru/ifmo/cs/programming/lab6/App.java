package ru.ifmo.cs.programming.lab6;

import javax.swing.*;
import java.awt.*;

public class App {

    public static void main(String... args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                gui();
            }
        });
    }

    private static void gui() {
        JFrame f = new MainFrame();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int locationX = (screenSize.width - f.getSize().width) / 2;
        int locationY = (screenSize.height - f.getSize().height) / 2;
        f.setBounds(locationX, locationY, f.getSize().width, f.getSize().height);//по центру экрана

        f.pack();
        f.setVisible(true);
        f.setMaximumSize(new Dimension(100, 100));
    }
}
