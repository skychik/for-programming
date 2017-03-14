/**
 * Created by саша on 09.03.2017.
 */

package laba2;
import java.util.Objects;
import java.util.Scanner;

public class Employee extends Character implements Comparable{
    int salary;
    AttitudeToBoss attitudeToBoss;
    byte workQuality;

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
        if (attitudeToBoss == AttitudeToBoss.HATE)
            workQuality = -128;
        if (attitudeToBoss == AttitudeToBoss.LOW){
            if (workQuality > -108) workQuality -= 20;
            else workQuality = -128;
        }
        if (attitudeToBoss == AttitudeToBoss.NORMAL){
            if (workQuality > 107) workQuality += 20;
            else workQuality = 127;
        }
        if (attitudeToBoss == AttitudeToBoss.HIGH)
            workQuality = 127;
    }

    @Override
    public String toString() {
        return "Employee{" + "salary=" + salary + ", attitudeToBoss=" + attitudeToBoss.attitude + ", workQuality=" + workQuality + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + this.salary;
        hash = 29 * hash + Objects.hashCode(this.attitudeToBoss);
        hash = 29 * hash + this.workQuality;
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
        return this.salary == other.salary &&
                this.attitudeToBoss == other.attitudeToBoss &&
                this.workQuality == other.workQuality;

    }

    public void parseEmployee(String line) {
        Scanner sc = new Scanner(line);
        sc.useDelimiter(",");
        while (line.length() < 6) {
            System.out.println("Неверно задан объект в строке " + line + ". Описаны не все параметры.");
            System.exit(0);
        }
        while (sc.hasNext()){
        try {
            int index = 0;
            switch (index){
                case 0 : {
                    index++;
                    break;
                }
                case 1 : {
                    index++;
                    break;
                }
                default : {break;}
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
                    fw = new FactoryWorker(name, profession, salary, attitudeToBoss, workQuality);
                    while (sc.hasNext()) try {
                        String[] name_and_price = sc.next().split("- ");
                        Product product = new Product(name_and_price[0], Integer.parseInt(name_and_price[1]));
                        fw.bagpack.add(product);
                    } catch (NumberFormatException e) {
                        System.out.println("Неверно задан формат предмета. В ячейке надо указать название продукта и цену через \"-\".");
                        System.exit(0);
                    }
                    deq.addFirst(fw);
                    break;
                }
                case "ShopAssistant": {
                    shAs = new ShopAssistant(name, profession, salary, attitudeToBoss, workQuality);
                    deq.addFirst(shAs);
                    break;
                }
                case "Employee": {
                    emp = new Employee(name, profession, salary, attitudeToBoss, workQuality);
                    deq.addFirst(emp);
                    break;
                }
                default: {
                    System.out.println("Приложение не обрабатывает указанный класс.");
                    System.exit(0);
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Исправьте значения в строке " + line + ". Столбцы C и E должны содержать числа.");
            System.exit(0);
        }
    }
    }
}
    // todo comparator

    @Override
    public int compareTo(Object o) {
        return this.toString().compareTo(o.toString());
    }
}
