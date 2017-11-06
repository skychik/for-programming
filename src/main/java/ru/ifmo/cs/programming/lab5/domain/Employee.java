package ru.ifmo.cs.programming.lab5.domain;

import ru.ifmo.cs.programming.lab5.AppCmdLine;
import ru.ifmo.cs.programming.lab5.utils.AttitudeToBoss;
import ru.ifmo.cs.programming.lab5.utils.FactoryWorker;
import ru.ifmo.cs.programming.lab6.utils.HasSpeciality;
import ru.ifmo.cs.programming.lab8.MyAnnotation;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Scanner;

@MyAnnotation(name = "table_name")
public class Employee extends Character implements Comparable, HasSpeciality, Serializable {
    @MyAnnotation(name = "id")
    private long id;
    private int salary = 0;
    private String speciality;
    private AttitudeToBoss attitude_to_boss = AttitudeToBoss.NONE;
    private byte work_quality = 0;
    private String avatar_path = System.getProperty("user.dir") + "\\src\\resources\\images\\" + "standardAvatar.jpg";
    private String notes;
    private ZonedDateTime creating_time;

    public Employee(String name, String profession, int salary, AttitudeToBoss attitude_to_boss, byte work_quality) {
        super(name, profession);
        this.salary = salary;
        this.attitude_to_boss = attitude_to_boss;
        this.work_quality = work_quality;
        creating_time = ZonedDateTime.now();
        speciality = "Employee";
    }

    public Employee(String name, String profession, int salary, AttitudeToBoss attitude_to_boss, byte work_quality, ZonedDateTime creating_time) {
        super(name, profession);
        this.salary = salary;
        this.attitude_to_boss = attitude_to_boss;
        this.work_quality = work_quality;
        this.creating_time = creating_time;
        speciality = "Employee";
    }

    public Employee() {
        super();
    }

    @Override
    public void work() {}

    @Override
    public String toString() {
        return ("Employee;" + getName()  +
                ";" + getProfession() +
                ";" + getSalary() +
                ";" + returnAttitude_to_boss().toString() +
                ";" + getWork_quality() +
                ";" + getAvatar_path() +
                ";{" + getNotes() + "}" +
                ";" + getCreating_time());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + getSalary();
        hash = 29 * hash + Objects.hashCode(returnAttitude_to_boss());
        hash = 29 * hash + getWork_quality();
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
            returnAttitude_to_boss() == other.returnAttitude_to_boss() &&
            getWork_quality() == other.getWork_quality();
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
        } else if (!Objects.equals(this.returnAttitude_to_boss(), employee.returnAttitude_to_boss())) {
            return this.returnAttitude_to_boss().compareTo(employee.returnAttitude_to_boss());
        } else if (!Objects.equals(this.getWork_quality(), employee.getWork_quality())) {
            return this.getWork_quality() - employee.getWork_quality();
        } else return 0;
    }

    public int getSalary() {
        return salary;
    }

    protected void setSalary(int salary) {
        this.salary = salary;
    }

    public AttitudeToBoss returnAttitude_to_boss() {
        return attitude_to_boss;
    }

    public byte getAttitude_to_boss() {
        return attitude_to_boss.getAttitude();
    }

    protected void setAttitude_to_boss(AttitudeToBoss attitude_to_boss) {
        this.attitude_to_boss = attitude_to_boss;
    }

    public byte getWork_quality() {
        return work_quality;
    }

    protected void setWork_quality(byte work_quality) {
        this.work_quality = work_quality;
    }

//    public static Employee readEmployee(JsonReader reader) throws IOException {
//        String name = null;
//        String profession = null;
//        int salary = 0;
//        AttitudeToBoss attitude_to_boss = AttitudeToBoss.NONE;
//        byte work_quality = 0;
//
//        reader.beginObject();
//        while (reader.hasNext()) {
//            String nextName = reader.nextName();
//            if (nextName.equals("name")) {
//                name = reader.nextString();
//            } else if (nextName.equals("profession")) {
//                profession = reader.nextString();
//            } else if (nextName.equals("salary")) {
//                salary = reader.nextInt();
//            } else if (nextName.equals("attitude_to_boss") && reader.peek() != JsonToken.NULL) {
//                attitude_to_boss = AttitudeToBoss.readAttitudeToBoss(reader);
//            } else if (nextName.equals("work_quality")) {
//                int i = reader.nextInt();
//                if ((i > Byte.MAX_VALUE)||(i < Byte.MIN_VALUE))
//                    throw new IllegalArgumentException("work_quality value isn't a byte value");
//                work_quality = (byte) i;
//            } else {
//                reader.skipValue();
//            }
//        }
//        reader.endObject();
//        return new Employee(name, profession, salary, attitude_to_boss, work_quality);
//    }

    /**
     * Метод, определяет класс объекта и вызывает методы по преобразованию строки в объект,
     * возвращает объект класса Employee
     * @author Zhurbova A.E.
     * @param line - строка, в которой хранятся данные об объекте
     */
    public static Employee parseEmployee(String line) {
        Scanner sc;
        Employee employee;
        String className;

        try {
            sc = new Scanner(line);
            sc.useDelimiter(";");

            className = sc.next();
        } catch (NoSuchElementException e){
            System.out.println("Ничего не было считано, т.к. файл пуст.");
            return null;
        }

        switch (className) {
            case "FactoryWorker": {
                FactoryWorker fw = new FactoryWorker();
                fw.stringToEmployee(sc);
                fw.parseFactoryWorker(sc);
                employee = fw;
                break;
            }
            case "ShopAssistant": {
                ShopAssistant shAs = new ShopAssistant();
                shAs.stringToEmployee(sc);
                employee = shAs;
                break;
            }
            case "Employee": {
                Employee emp = new Employee();
                emp.stringToEmployee(sc);
                employee = emp;
                break;
            }
            default: {
                System.out.println("Приложение не обрабатывает указанный в строке " + AppCmdLine.getLineNumber() + " класс.");
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
    protected void stringToEmployee(Scanner sc) {
        int index = 1;
        try {
            for (; sc.hasNext() && index < 9; index++)
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
                            this.attitude_to_boss = AttitudeToBoss.HATE;
                            break;
                        }
                        case "LOW": {
                            attitude_to_boss = AttitudeToBoss.LOW;
                            break;
                        }
                        case "NORMAL": {
                            attitude_to_boss = AttitudeToBoss.NORMAL;
                            break;
                        }
                        case "HIGH": {
                            attitude_to_boss = AttitudeToBoss.HIGH;
                            break;
                        }
                        case "DEFAULT": {
                            attitude_to_boss = AttitudeToBoss.DEFAULT;
                            break;
                        }
                        default: {
                            System.out.println("Неверно указано значение в ячейке E" + AppCmdLine.getLineNumber() + ".");
                            return;
                        }
                    }
                    break;
                    }
                case 5 : {
                    this.work_quality = Byte.parseByte(sc.next());
                    break;
                }
                case  6 : {
                    this.avatar_path = sc.next();
                    break;
                }
                case  7 : {
                    this.notes = sc.next().replace("{","").replace("}","");
                    break;
                }
                case 8 : {
//                    this.creating_time = OffsetDateTime.parse(sc.next());
//                    this.creating_time = LocalDateTime.parse(sc.next());
                    this.creating_time = ZonedDateTime.parse(sc.next());
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Исправьте значения в строке " + AppCmdLine.getLineNumber() + ". Столбцы D и F должны содержать числа.");
            System.exit(1);
        }

    }

    public void setAvatar_path(String path){
        avatar_path = path;
    }

    public String getAvatar_path(){
        return avatar_path;
    }

    public void setNotes(String notes){
        this.notes = notes;
    }

    public String getNotes(){
        return notes;
    }

    public ZonedDateTime getCreating_time(){
        return creating_time;
    }

    @Override
    public String getSpeciality() {
        return "Employee";
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public void setId(long id) {
    	this.id = id;
    }

    public long getId() {
    	return id;
    }
}

