package ru.ifmo.cs.programming.lab6.utils;

import javax.swing.*;

public class MyRowFilter<O, O1> extends RowFilter<Object, Object> {
    @Override
    public boolean include(Entry entry) {
        return false;
    }
}
