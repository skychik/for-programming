/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.ifmo.cs.programming.lab5.domain;

import java.util.Objects;

/**
 *
 * @author саша и кирюша
 */
public class Product {

    private String name;
    
    private int price;

    public Product(String name, int price){
        this.name = name;
        this.price = price;
    }

    public int getPrice(){
        return price;
    }

    public String getName(){
        return name;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 43 * hash + Objects.hashCode(getName());
        hash = 43 * hash + getPrice();
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
        final Product other = (Product) obj;
        return getPrice() == other.getPrice() && Objects.equals(getName(), other.getName());
    }

    @Override
    public String toString() {
        return "Product{" + "name=" + getName() + ", price=" + getPrice() + '}';
    }
}