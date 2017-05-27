package ru.ifmo.cs.programming.lab7.core;

import java.io.Serializable;
import java.util.Hashtable;

public class MyHashtable<K, V> extends Hashtable<K, V> implements Serializable{
    public MyHashtable() {
        super();
    }
}
