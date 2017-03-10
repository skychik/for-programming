/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package laba2;

/**
 *
 * @author саша и кирюша
 */

public class ShopAssistant extends Employee{
    public ShopAssistant(String name, String profession, Workplace workplace, int salary, byte attitude, byte workQuality) {
        super(name, profession, workplace, salary, attitude, workQuality);
    }
    public void giveSausage(FactoryWorker worker, Product sausage){
        worker.receiveSausage(sausage);
    }
}