package ru.ifmo.cs.programming.lab6;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

import com.intellij.uiDesigner.core.GridConstraints;

public class App {
    private static String currentDir = System.getProperty("user.dir") + "/src/test/java/ru/ifmo/cs/programming/lab6";

    public static void main(String... args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                gui();
            }
        });
    }

    private static void gui() {
        JFrame f = new MainFrame();

        f.pack(); // установка размеров фрейма
        f.setVisible(true);
    }
}
