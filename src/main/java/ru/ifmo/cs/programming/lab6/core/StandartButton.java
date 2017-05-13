package ru.ifmo.cs.programming.lab6.core;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class StandartButton extends JButton {

        private String fontName = "Gill Sans MT EXT Condensed";

        StandartButton(String text) {
            setText(text);
            //setMinimumSize(new Dimension(200, 50));
            setPreferredSize(new Dimension(200,50));
            setBackground(new Color(54, 151, 175));
            setFocusPainted(false);
            setFont(new Font(fontName, Font.CENTER_BASELINE, 15));
        }
}

