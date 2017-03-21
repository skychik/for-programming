/**
 * Created by саша on 09.03.2017.
 * Updated by леша on 21.03.2017.
 */
package ru.ifmo.cs.programming.lab5.domain;

import ru.ifmo.cs.programming.lab5.utils.FactoryWorker;
import ru.ifmo.cs.programming.lab5.utils.ManagerNotebook;
import ru.ifmo.cs.programming.lab5.core.NegativeSalaryException;

import java.lang.*;

public class Employer extends Character {

    private int profit;

    public Employer(String name, String profession, int profit){
        super(name, profession);
        this.profit = profit;

    }

    public void lowerSalary(Employee employee,int subtraction) {
        try{
            if (employee.getSalary() - subtraction >= 0){
                employee.setSalary(employee.getSalary() - subtraction);
                profit += subtraction;
            } else{
                throw new NegativeSalaryException();
                throw new NegativeSalaryException("Зарплата не может быть отрицательной");
            }
        } catch(NegativeSalaryException e){
            employee.setSalary(0);
        }
    }

    @Override
    public void work() {

        abstract class Responsibility {

            {
                // WAT ?
                int x = 777;
                System.out.print(x);
            }

            private String responsibility;
            private String responsibility;

            Responsibility(String profession){
                System.out.println("pre");
                responsibility = "Выполняет обязанности: " + profession;
                System.out.println("post");
            }
            public String getResponsibility(){
                return responsibility;
            }

            abstract void sout();
        }

        Responsibility resp = new Responsibility(getProfession()) {
            @Override
            public void sout(){
                System.out.println(this.getResponsibility());
            }
        };
        resp.sout(); // мама плачет (skychik: "Понятие не имею, че это и зачем")
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
        if (getProfit() != other.getProfit()) {
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

    public int getProfit() {
        return profit;
    }
}

