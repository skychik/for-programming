package ru.ifmo.cs.programming.lab6.core;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class StandartButton extends JButton {

        private String fontName = "Gill Sans MT EXT Condensed";
        private Color backgroundColor;

        StandartButton(String text) {
            super(text);
            //setMinimumSize(new Dimension(200, 50));
            backgroundColor = new Color(54, 151, 175);
            setText(text);
            setBackground(backgroundColor);
            setPreferredSize(new Dimension(200,50));
            setFocusPainted(false);
            //setBorderPainted(false);
            //setContentAreaFilled(false);

            Border line = new LineBorder(Color.BLACK);
            Border margin = new EmptyBorder(5, 15, 5, 15);
            Border compound = new CompoundBorder(line, margin);
            setBorder(compound);

            setBackground(new Color(54, 151, 175));
            setFont(new Font(fontName, Font.CENTER_BASELINE, 15));
        }
}

