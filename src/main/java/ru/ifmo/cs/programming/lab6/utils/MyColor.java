package ru.ifmo.cs.programming.lab6.utils;

import java.awt.*;
import java.awt.color.ColorSpace;

public class MyColor extends Color {
    public final static Color backgroundEighthAlphaColor = new Color(112, 122, 130, 32);
    public final static Color backgroundHalfAlphaColor = new Color(112, 122, 130, 128);
    public final static Color backgroundColor = new Color(112, 122, 130);
    public final static Color whiteTextColor = new Color(241, 242, 243);

    public MyColor(int r, int g, int b) {
        super(r, g, b);
    }

    public MyColor(int r, int g, int b, int a) {
        super(r, g, b, a);
    }

    public MyColor(int rgb) {
        super(rgb);
    }

    public MyColor(int rgba, boolean hasalpha) {
        super(rgba, hasalpha);
    }

    public MyColor(float r, float g, float b) {
        super(r, g, b);
    }

    public MyColor(float r, float g, float b, float a) {
        super(r, g, b, a);
    }

    public MyColor(ColorSpace cspace, float[] components, float alpha) {
        super(cspace, components, alpha);
    }
}
