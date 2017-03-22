/**
 * Created by саша on 09.03.2017.
 */
package ru.ifmo.cs.programming.lab5.domain;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import ru.ifmo.cs.programming.lab5.core.ByteOverflowException;
import ru.ifmo.cs.programming.lab5.utils.AttitudeToBoss;

import java.io.IOException;
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

    public static Employee readEmployee(JsonReader reader) throws IOException {
        String name = null;
        String profession = null;
        int salary = 0;
        AttitudeToBoss attitudeToBoss = null;
        byte workQuality = 0;

        reader.beginObject();
        while (reader.hasNext()) {
            String nextName = reader.nextName();
            if (nextName.equals("name")) {
                name = reader.nextString();
            } else if (nextName.equals("profession")) {
                profession = reader.nextString();
            } else if (nextName.equals("salary")) {
                salary = reader.nextInt();
            } else if (nextName.equals("attitudeToBoss") && reader.peek() != JsonToken.NULL) {
                attitudeToBoss = AttitudeToBoss.readAttitudeToBoss(reader);
            } else if (nextName.equals("workQuality")) {
                int i = reader.nextInt();
                if ((i > Byte.MAX_VALUE)||(i < Byte.MIN_VALUE))
                    throw new ByteOverflowException("workQuality value isn't a byte value");
                workQuality = (byte) i;
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new Employee(name, profession, salary, attitudeToBoss, workQuality);
    }
}
