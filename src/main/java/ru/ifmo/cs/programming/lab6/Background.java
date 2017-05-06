package ru.ifmo.cs.programming.lab6;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;

public class Background extends JPanel {
    private Image img;

    public Background(String img) {
        this(new ImageIcon(img).getImage());
    }

    public Background(Image img) {
        this.img = img;
        Dimension size = new Dimension(1100, 700);//TODO: ?
        setPreferredSize(size);
//        setMinimumSize(size);//TODO: ?
//        setMaximumSize(new Dimension(1200, 1200));//TODO: ?
//        setSize(size);
        setLayout(null);
    }

    /*public void setSize(JFrame frame){
        setPreferredSize(new Dimension(frame.getWidth(), frame.getHeight()));
    }*/

    // TODO: сделать стандартную иконку, если нет изображения по указанному адресу
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw the background image.
        g.drawImage(img, 0, 0, null);
    }
}