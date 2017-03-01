/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package laba2;

import java.util.ArrayList;
import java.util.Objects;

/**
 *
 * @author саша и кирюша
 */

public class Shop extends Workplace{
    private String shopType;
    
    public Shop(String name, String shopType, ArrayList <Product> assortment, ArrayList <Employee> employeeList) {
        super(name, "Магазин", assortment, employeeList);
        this.shopType = shopType;
    }   

    @Override
    public String toString() {
        return "Shop{" + "shopType=" + shopType + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + Objects.hashCode(this.shopType);
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
        if (!Objects.equals(this.shopType, other.shopType)) {
            return false;
        }
        return true;
    }
}
