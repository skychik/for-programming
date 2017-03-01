/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package laba2;

import java.util.ArrayList;

/**
 *
 * @author саша и кирюша
 */

public class MovableShop extends Shop implements Movement{
    
    public MovableShop(String name, String shopType, ArrayList <Product> assortment, ArrayList <Employee> employeeList) {
        super(name, shopType, assortment, employeeList);
    }

    @Override
    public void move(String address) {
        System.out.println("Передвижной магазин \"" + this.name + "\" направляется по адресу:" + address);
    }   
}