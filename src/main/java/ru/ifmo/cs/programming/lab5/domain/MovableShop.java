package ru.ifmo.cs.programming.lab5.domain;

import ru.ifmo.cs.programming.lab5.core.Movement;

import java.util.ArrayList;

public class MovableShop extends Shop implements Movement {

    public MovableShop(String name, String shopType, ArrayList <Product> assortment, ArrayList <Employee> employeeList) {
        super(name, shopType, assortment, employeeList);
    }

    @Override
    public void move(String address) {
        System.out.println("Передвижной магазин \"" + getName() + "\" направляется по адресу:" + address);
    }
}