package ru.ifmo.cs.programming.lab6;

import javax.swing.*;

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
        JFrame f = new MainPanel();

        Background background =  new Background(new ImageIcon(currentDir + "/background.jpg").getImage());

        f.getContentPane().add(background);

        f.pack(); // установка размеров фрейма
        f.setVisible(true);
    }
}
