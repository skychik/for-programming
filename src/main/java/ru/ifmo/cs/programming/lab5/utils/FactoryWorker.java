package ru.ifmo.cs.programming.lab5.utils;

import ru.ifmo.cs.programming.lab5.App;
import ru.ifmo.cs.programming.lab5.domain.Employee;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class FactoryWorker extends Employee {

    private ArrayList<Product> bagpack = new ArrayList<>();

    public FactoryWorker() {super();}

    public FactoryWorker(String name, String profession, int salary, AttitudeToBoss attitudeToBoss, byte workQuality) {
        super(name, profession, salary, attitudeToBoss, workQuality);
        bagpack = new ArrayList<>();
    }

    public void changeQuality(byte up){
        setWorkQuality((byte)(getWorkQuality() + up));
    }

    public void raiseAttitude(){
        if (getAttitudeToBoss() == AttitudeToBoss.HATE)
            setAttitudeToBoss(AttitudeToBoss.LOW);
        if (getAttitudeToBoss() == AttitudeToBoss.LOW)
            setAttitudeToBoss(AttitudeToBoss.DEFAULT);
        if (getAttitudeToBoss() == AttitudeToBoss.DEFAULT)
            setAttitudeToBoss(AttitudeToBoss.NORMAL);
        if (getAttitudeToBoss() == AttitudeToBoss.NORMAL)
            setAttitudeToBoss(AttitudeToBoss.HIGH);
    }

    public void receiveSausage(Product sausage) {
        bagpack.add(sausage);
        byte up = 5;
        this.changeQuality(up);
        this.raiseAttitude();
    }

    @Override
    public String toString() {
        return ("FactoryWorker{name=" + getName()  +
                ", profession=" + getProfession() +
                ", salary=" + getSalary() +
                ", attitudeToBoss=" + getAttitudeToBoss().toString() +
                ", workQuality=" + getWorkQuality() +
                ", bagpack=" + bagpack) +
                "}";
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.bagpack);
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
        final FactoryWorker other = (FactoryWorker) obj;
        return Objects.equals(this.bagpack, other.bagpack);
    }

    public void sign(ManagerNotebook notebook){
        notebook.addSign(this.getName());
    }

    public ArrayList<Product> getBagpack() {
        return bagpack;
    }

    public void addProduct (Product product){
        bagpack.add(product);
    }

    /**
     * Метод, преобразующий оставшуюся часть строки в коллекцию bagPack,
     * принадлежащую классу FactoryWorker
     * @author Zhurbova A.E.
     * @param scanner - строка, в которой хранятся данные о содержимом bagpack
     */
    public void parseFactoryWorker(Scanner scanner) {

        try {
            Product product;

            while (scanner.hasNext()){
                String[] anything = scanner.next().split(" : ");

                if (anything.length == 2) {
                    product = new Product(anything[0], Integer.parseInt(anything[1]));
                } else {
                    System.out.println("Неверно заданы предметы багажа в строке " + App.getLineNumber() +
                            ". Должны быть указаны название и цена, разделенные \":\".");
                    System.exit(1);
                    return;
                }

                addProduct(product);
            }
        } catch (NumberFormatException e){
            System.out.println("Неверно заданы предметы багажа в строке " + App.getLineNumber() +
                    ". Должны быть указаны название и цена, разделенные \":\".");
            System.exit(1);
        }
    }
}
