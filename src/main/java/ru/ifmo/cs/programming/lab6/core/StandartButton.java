package ru.ifmo.cs.programming.lab6.core;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class StandartButton extends JButton {

        private String fontName = "Gill Sans MT EXT Condensed";
        private Color backgroundColor;

        StandartButton(String text) {
            backgroundColor = new Color(54, 151, 175);
            setText(text);
            setBackground(backgroundColor);
            setPreferredSize(new Dimension(200,50));
            setFocusPainted(false);
            setFont(new Font(fontName, Font.CENTER_BASELINE, 15));
        }
}

