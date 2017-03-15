/**
 * Created by саша on 09.03.2017.
 */
package laba2;

import java.util.Random;

public class Employer extends Character{
    private int profit;
    Exception e = new Exception();

    public Employer(String name, String profession, int profit){
        super(name, profession);
        this.profit = profit;

    }

    public void lowerSalary(Employee employee,int subtraction) throws /*NegativeSalary*/Exception{
        try{
            if (employee.salary - subtraction >= 0){
                employee.salary -= subtraction;
                profit += subtraction;
            } else{
                throw e;//new NegativeSalaryException("Зарплата не может быть отрицательной");
            }
        } catch(NegativeSalaryException e){
            employee.salary = 0;
        }
    }

    public Error getError() {
        return new Error() {
            @Override
            public String getMessage() {
                return "ёу"; //To change body of generated methods, choose Tools | Templates.
            }

        };
    }

    @Override
    public void work() {

        abstract class Responsibility{

            {
                int x = 777;
                System.out.print(x);
            }

            String responsibility;

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

        Responsibility resp = new Responsibility(profession){
            @Override
            public void sout(){
                System.out.println(this.getResponsibility());
            }
        };
        resp.sout();
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

