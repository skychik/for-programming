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
   
public class Employer extends Character {
    private int profit;
    
    public Employer(String name, String profession, Workplace workplace, int profit){
        super(name, profession, workplace);
        this.profit = profit;

    }
    
    public void lowerSalary(Employee employee){
        if (employee.salary == Salary.High) employee.salary = Salary.Normal;
        if (employee.salary == Salary.Normal) employee.salary = Salary.Low;
    }
    
    @Override
    public void work() {
        System.out.println("Выполняет обязанности: " + profession);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + this.profit;
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
        final Employer other = (Employer) obj;
        if (this.profit != other.profit) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Employer{" + "profit=" + profit + '}';
    }
    
    public void makeNote(FactoryWorker worker, ManagerNotebook notebook){
        notebook.setOwner(this);
        notebook.addWorker(worker);
        worker.sign(notebook);
    }
}