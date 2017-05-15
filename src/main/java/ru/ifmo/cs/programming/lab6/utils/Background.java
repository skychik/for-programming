package ru.ifmo.cs.programming.lab6.utils;

import javax.swing.*;
import java.awt.*;

public class Background extends JPanel {
    private Image img;

    public Background(Image img) {
        this.img = img;
        setLayout(null);
    }

    /*public void setSize(JFrame frame){
        setPreferredSize(new Dimension(frame.getWidth(), frame.getHeight()));
    }*/

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw the background image.
        g.drawImage(img/*.getScaledInstance(1000, 1000, Image.SCALE_DEFAULT)*/, 0, 0, null);
    }
}