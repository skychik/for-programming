package ru.ifmo.cs.programming.lab5.domain;

import ru.ifmo.cs.programming.lab5.utils.FactoryWorker;
import ru.ifmo.cs.programming.lab5.utils.ManagerNotebook;

import java.lang.*;

public class Employer extends Character {

    private int profit = 0;

    public Employer(String name, String profession, int profit){
        super(name, profession);
        this.profit = profit;
    }

    public void lowerSalary(Employee employee,int subtraction) {

        if (employee.getSalary() - subtraction >= 0){
            employee.setSalary(employee.getSalary() - subtraction);
            profit += subtraction;
        } else{
            throw new ArithmeticException("Зарплата не может быть отрицательной");
        }
    }

    @Override
    public void work() {

        abstract class Responsibility {

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
        return getProfit() == other.getProfit();
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

