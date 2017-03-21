/**
 * Created by саша on 09.03.2017.
 */
package ru.ifmo.cs.programming.lab5.domain;

import ru.ifmo.cs.programming.lab5.utils.AttitudeToBoss;

import java.lang.*;
import java.util.Objects;

public class Employee extends Character implements Comparable{

    private int salary;

    private AttitudeToBoss attitudeToBoss;

    private byte workQuality;

    public Employee(String name, String profession, int salary, AttitudeToBoss attitudeToBoss, byte workQuality) {
        super(name, profession);
        this.salary = salary;
        this.attitudeToBoss = attitudeToBoss;
        this.workQuality = workQuality;
    }

    @Override
    public void work() {
        if (getAttitudeToBoss() == AttitudeToBoss.HATE)
            setWorkQuality((byte)(getWorkQuality() - 128));
        if (getAttitudeToBoss() == AttitudeToBoss.LOW){
            if (getWorkQuality() > -108)
                setWorkQuality((byte)(getWorkQuality() - 20));
            else
                setWorkQuality((byte)(getWorkQuality() - 128));
        }
        if (getAttitudeToBoss() == AttitudeToBoss.NORMAL){
            if (getWorkQuality() > 107)
                setWorkQuality((byte)(getWorkQuality() + 20));
            else
                setWorkQuality((byte)(getWorkQuality() + 127));
        }
        if (getAttitudeToBoss() == AttitudeToBoss.HIGH)
            setWorkQuality((byte)127);
    }

    @Override
    public String toString() {
        return "Employee{salary=" + getSalary()
            + ", attitudeToBoss=" + getAttitudeToBoss().getAttitude()
            + ", workQuality="    + getWorkQuality() + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + getSalary();
        hash = 29 * hash + Objects.hashCode(getAttitudeToBoss());
        hash = 29 * hash + getWorkQuality();
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
        return getSalary() == other.getSalary() &&
            getAttitudeToBoss() == other.getAttitudeToBoss() &&
            getWorkQuality() == other.getWorkQuality();
    }

    @Override
    public int compareTo(Object o) {
        return this.toString().compareTo(o.toString());
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public AttitudeToBoss getAttitudeToBoss() {
        return attitudeToBoss;
    }

    public void setAttitudeToBoss(AttitudeToBoss attitudeToBoss) {
        this.attitudeToBoss = attitudeToBoss;
    }

    public byte getWorkQuality() {
        return workQuality;
    }

    public void setWorkQuality(byte workQuality) {
        this.workQuality = workQuality;
    }
}
