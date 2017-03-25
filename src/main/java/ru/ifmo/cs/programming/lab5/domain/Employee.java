/**
 * Created by саша on 09.03.2017.
 */
package ru.ifmo.cs.programming.lab5.domain;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import ru.ifmo.cs.programming.lab5.core.ByteOverflowException;
import ru.ifmo.cs.programming.lab5.utils.AttitudeToBoss;
import ru.ifmo.cs.programming.lab5.utils.FactoryWorker;

import java.io.IOException;
import java.lang.*;
import java.util.Objects;
import java.util.Scanner;

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

    public Employee parseEmployee(String line) {
        Scanner sc = new Scanner(line);
        sc.useDelimiter(",");
        while (line.length() < 6) {//todo почему while
            System.out.println("Неверно задан объект в строке " + line + ". Описаны не все параметры.");
            System.exit(0);
        }
        while (sc.hasNext()/*todo зачем тут проверка*/) {
            try {
                int index = 0;
                switch (index) { //todo WAAAAAAAAAAAAAAT
                    case 0: {
                        index++;
                        break;
                    }
                    case 1: {
                        index++;
                        break;
                    }
                    default: {
                        break;
                    }
                }
                String objClass = sc.next();
                String name = sc.next();
                String profession = sc.next();
                Integer salary = Integer.parseInt(sc.next());
                AttitudeToBoss attitudeToBoss = null;
                switch (sc.next()) {
                    case "HATE": {
                        attitudeToBoss = AttitudeToBoss.HATE;
                        break;
                    }
                    case "LOW": {
                        attitudeToBoss = AttitudeToBoss.LOW;
                        break;
                    }
                    case "NORMAL": {
                        attitudeToBoss = AttitudeToBoss.NORMAL;
                        break;
                    }
                    case "HIGH": {
                        attitudeToBoss = AttitudeToBoss.HIGH;
                        break;
                    }
                    case "DEFAULT": {
                        attitudeToBoss = AttitudeToBoss.DEFAULT;
                        break;
                    }
                    default: {
                        System.out.println("Неверно указано значение в ячейке " + line + "D.");
                        System.exit(0);
                    }
                }
                Byte workQuality = Byte.parseByte(sc.next());
                switch (objClass) {
                    case "FactoryWorker": {
                        FactoryWorker fw = new FactoryWorker(name, profession, salary, attitudeToBoss, workQuality);
                        while (sc.hasNext()) try {
                            String[] name_and_price = sc.next().split("- ");
                            Product product = new Product(name_and_price[0], Integer.parseInt(name_and_price[1]));
                            fw.addProduct(product);
                        } catch (NumberFormatException e) {
                            System.out.println("Неверно задан формат предмета. В ячейке надо указать название продукта и цену через \"-\".");
                            System.exit(0);
                        }
                        return fw;
                    }
                    case "ShopAssistant": {
                        ShopAssistant shAs = new ShopAssistant(name, profession, salary, attitudeToBoss, workQuality);
                        return shAs;//todo поддержка не только factoryWorker
                    }
                    case "Employee": {
                        Employee emp = new Employee(name, profession, salary, attitudeToBoss, workQuality);
                        return emp;
                    }
                    default: {
                        System.out.println("Приложение не обрабатывает указанный класс.");
                        System.exit(0);
                    }
                }
            } catch (NumberFormatException e) {//todo зачем это отлавливать
                System.out.println("Исправьте значения в строке " + line + ". Столбцы C и E должны содержать числа.");
                System.exit(0);
            }
        }
        return null;
    }
}
