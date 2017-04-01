package ru.ifmo.cs.programming.lab5.domain;

import ru.ifmo.cs.programming.lab5.core.Movement;
import ru.ifmo.cs.programming.lab5.utils.Product;

import java.util.ArrayDeque;
import java.util.ArrayList;

public class MovableShop extends Shop implements Movement {

    public MovableShop(String name, String shopType, ArrayList <Product> assortment, ArrayDeque<Employee> employeeDeque) {
        super(name, shopType, assortment, employeeDeque);
    }

    @Override
    public void move(String address) {
        System.out.println("Передвижной магазин \"" + getName() + "\" направляется по адресу:" + address);
    }
}