/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package laba2;

import java.util.*;

/**
 *
 * @author саша и кирюша
 */

public class Laba2 {
    public static void main(String[] args) {
        
        //
        //Creation
        //
        
        Product sausage;
        {
            int price = 40;
            sausage = new Product("Бутерброд с сосиской", price);
        }
        
        Shop larek;
        {
            ArrayList <Product> larekAssort = new ArrayList <Product>();
            larekAssort.add(sausage);
            
            ArrayList <Employee> employeeLarekList = new ArrayList <Employee>();

            larek = new MovableShop("-", "Ларек", larekAssort, employeeLarekList);
        }
        
        Workplace factory;
        {
            Product macaroni;
            {
                int price = 40;
                macaroni = new Product("Макароны", price); 
            }
            
            ArrayList <Product> fabricAssort = new ArrayList <Product>();
            fabricAssort.add(macaroni);
            
            ArrayList <Employee> employeeFabricList = new ArrayList <Employee>();

            factory = new Workplace("-", "Фабрика", fabricAssort, employeeFabricList);
        }
        
        ShopAssistant saleswoman;
        {
            int salary = 20000;
            byte attitude = 30;
            byte workQuality = 30;
            saleswoman = new ShopAssistant("-", "Продавщица", larek, salary, attitude, workQuality);
            larek.addEmployee(saleswoman);
        }
        
        Employer scuperfild;
        {
            int profit = 100000;
            scuperfild = new Employer("Скуперфильд", "Владелец Фабрики", factory, profit);
            larek.setEmployer(scuperfild);
        }
        
        ArrayList <FactoryWorker> workers = new ArrayList<FactoryWorker>();
        {
            int workerProfit = 20000;
            byte attitude = 30; 
            byte workQuality = 30;
            for(int i = 0; i<5; i++){
                workers.add(new FactoryWorker("Рабочий " + Integer.toString(i + 1), "Рабочий", factory, workerProfit, attitude, workQuality));
                factory.addEmployee(workers.get(i));
            }
        }
        
        Employer fabricManager;
        {
            int profit = 50000;
            fabricManager = new Employer("", "Управляющий фабрики", factory, profit);
            factory.setEmployer(fabricManager);
        }
        
        ManagerNotebook notebook = new ManagerNotebook(fabricManager);
        
        //
        //Beggining
        //
        
        for(int i = 0; i < workers.size(); i++){
            if(larek.getAssortment().contains(sausage)){
                saleswoman.giveSausage(workers.get(i), sausage);
                scuperfild.lowerSalary(workers.get(i)/*, sausage.getPrice()*/);
                fabricManager.makeNote(workers.get(i), notebook);
            }
        }
        
    }
}


