package ru.ifmo.cs.programming.lab5.domain;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import ru.ifmo.cs.programming.lab5.App;
import ru.ifmo.cs.programming.lab5.utils.AttitudeToBoss;
import ru.ifmo.cs.programming.lab5.utils.FactoryWorker;

import java.io.IOException;
import java.lang.*;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Scanner;

public class Employee extends Character implements Comparable {

    private int salary = 0;
    private AttitudeToBoss attitudeToBoss = AttitudeToBoss.NONE;
    private byte workQuality = 0;
    private String avatarPath = System.getProperty("user.dir") + "\\src\\resources\\images\\" + "standartAvatar.jpg";
    private String notes;

    public Employee(String name, String profession, int salary, AttitudeToBoss attitudeToBoss, byte workQuality) {
        super(name, profession);
        this.salary = salary;
        this.attitudeToBoss = attitudeToBoss;
        this.workQuality = workQuality;
    }

    public Employee() {
        super();
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
        return ("Employee," + getName()  +
                "," + getProfession() +
                "," + getSalary() +
                "," + getAttitudeToBoss().toString() +
                "," + getWorkQuality());
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
        Employee employee = (Employee) o;
        if (!Objects.equals(this.getName(), employee.getName())) {
            return this.getName().compareTo(employee.getName());
        } else if (!Objects.equals(this.getProfession(), employee.getProfession())) {
            return this.getProfession().compareTo(employee.getProfession());
        } else if (!Objects.equals(this.getSalary(), employee.getSalary())) {
            return this.getSalary() - employee.getSalary();
        } else if (!Objects.equals(this.getAttitudeToBoss(), employee.getAttitudeToBoss())) {
            return this.getAttitudeToBoss().compareTo(employee.getAttitudeToBoss());
        } else if (!Objects.equals(this.getWorkQuality(), employee.getWorkQuality())) {
            return this.getWorkQuality() - employee.getWorkQuality();
        } else return 0;
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
        AttitudeToBoss attitudeToBoss = AttitudeToBoss.NONE;
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
                    throw new IllegalArgumentException("workQuality value isn't a byte value");
                workQuality = (byte) i;
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new Employee(name, profession, salary, attitudeToBoss, workQuality);
    }

    /**
     * Метод, определяет класс объекта и вызывает методы по преобразованию строки в объект,
     * возвращает объект класса Employee
     * @author Zhurbova A.E.
     * @param line - строка, в которой хранятся данные об объекте
     */
    public static Employee parseEmployee(String line) {
        Scanner sc;
        int index;
        Employee employee;
        String className;

        try {
            sc = new Scanner(line);
            sc.useDelimiter(",");

            className = sc.next();
        } catch (NoSuchElementException e){
            System.out.println("Ничего не было считано, т.к. файл пуст.");
            return null;
        }

        switch (className) {
            case "FactoryWorker": {
                FactoryWorker fw = new FactoryWorker();
                index = fw.stringToEmployee(sc);
                if (index < 6) {
                    System.out.println("Заданы не все параметры в строке " + App.getLineNumber());
                    return null;
                }
                fw.parseFactoryWorker(sc);
                employee = fw;
                break;
            }
            case "ShopAssistant": {
                ShopAssistant shAs = new ShopAssistant();
                index = shAs.stringToEmployee(sc);
                if (index < 6 || sc.hasNext()) {
                    System.out.println("Неверное количество параметров в строке " + App.getLineNumber());
                    return null;
                }
                employee = shAs;
                break;
            }
            case "Employee": {
                Employee emp = new Employee();
                index = emp.stringToEmployee(sc);
                if (index < 6 || sc.hasNext()) {
                    System.out.println("Неверное количество параметров в строке " + App.getLineNumber());
                    return null;
                }
                employee = emp;
                break;
            }
            default: {
                System.out.println("Приложение не обрабатывает указанный в строке " + App.getLineNumber() + " класс.");
                return null;
            }
        }
        return employee;
    }

    /**
     * Метод, преобразующий строку в параметры объекта класса Employee
     * @author Zhurbova A.E.
     * @param sc - строка, в которой хранятся данные объекта
     */
    protected int stringToEmployee(Scanner sc) {
        int index = 1;
        try {
            for (; sc.hasNext() && index < 6; index++)
            switch (index){
                case 1 : {
                    this.setName(sc.next());
                    break;
                }
                case 2 : {
                    this.setProfession(sc.next());
                    break;
                }
                case 3 : {
                    this.salary = Integer.parseInt(sc.next());
                    break;
                }
                case 4 : {
                    switch (sc.next().toUpperCase()) {
                        case "HATE": {
                            this.attitudeToBoss = AttitudeToBoss.HATE;
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
                            System.out.println("Неверно указано значение в ячейке E" + App.getLineNumber() + ".");
                            return 0;
                        }
                    }
                    break;
                    }
                case 5 : {
                    this.workQuality = Byte.parseByte(sc.next());
                    break;
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Исправьте значения в строке " + App.getLineNumber() + ". Столбцы D и F должны содержать числа.");
            System.exit(1);
        }
        return index;
    }

    public void setAvatarPath(String path){
        avatarPath = path;
    }

    public String getAvatarPath(){
        return avatarPath;
    }

    public void setNotes(String notes){
        this.notes = notes;
    }

    public String getNotes(){
        return notes;
    }
}

