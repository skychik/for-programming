package ru.ifmo.cs.programming.lab6.utils;

import javax.swing.*;

public class MyRowFilter extends RowFilter{
    @Override
    public boolean include(Entry entry) {
        return false;
    }
}
