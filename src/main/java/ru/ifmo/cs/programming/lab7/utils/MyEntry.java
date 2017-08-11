package ru.ifmo.cs.programming.lab7.utils;

import java.util.AbstractMap;

public class MyEntry<V> extends AbstractMap.SimpleEntry {
    public static final int NAME_AND_PASSWORD = 0;

    public MyEntry(Integer key, V value) {
        super(key, value);
    }

    @Override
    public Integer getKey() {
        return getKey();
    }
}
