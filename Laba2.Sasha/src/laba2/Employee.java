/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package laba2;

import java.util.Objects;

/**
 *
 * @author саша и кирюша
 */

public class Employee extends Character{
    protected Salary salary;
    protected byte attitudeToBoss;
    protected byte workQuality;
    
    public Employee(String name, String profession, Workplace workplace, int salary ,byte attitudeToBoss, byte workQuality) {
        super(name, profession, workplace);
        this.salary = Salary.Low;
        this.attitudeToBoss = attitudeToBoss;
        this.workQuality = workQuality;
    }   
            
    @Override
    public void work() {
        if (attitudeToBoss < 0)
            workQuality --;
        else
            if (attitudeToBoss > 0) 
                workQuality ++;
    }      

    @Override
    public String toString() {
        return "Employee{" + "salary=" + salary + ", attitudeToBoss=" + attitudeToBoss + ", workQuality=" + workQuality + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.salary);
        hash = 79 * hash + this.attitudeToBoss;
        hash = 79 * hash + this.workQuality;
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
        final Employee other = (Employee) obj;
        if (this.salary != other.salary) {
            return false;
        }
        if (this.attitudeToBoss != other.attitudeToBoss) {
            return false;
        }
        if (this.workQuality != other.workQuality) {
            return false;
        }
        return true;
    }

    // todo comparator

}
