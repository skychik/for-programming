package ru.ifmo.cs.programming.lab5.domain;

import ru.ifmo.cs.programming.lab5.utils.Product;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Objects;

public class Shop extends Workplace {

    private String shopType;

    public Shop(String name, String shopType, ArrayList <Product> assortment, ArrayDeque<Employee> employeeDeque) {
        super(name, "Магазин", assortment, employeeDeque);
        this.shopType = shopType;
    }

    @Override
    public String toString() {
        return "Shop{shopType=" + getShopType() + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + Objects.hashCode(getShopType());
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Shop other = (Shop) obj;
        return Objects.equals(getShopType(), other.getShopType());
    }

    public String getShopType() {
        return shopType;
    }
}