package ru.ifmo.cs.programming.lab5.utils;

import ru.ifmo.cs.programming.lab5.App;
import ru.ifmo.cs.programming.lab5.domain.Employee;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

public class FactoryWorker extends Employee {

    private ArrayList<Product> bagpack = null;

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
        return ("FactoryWorker;" + getName()  +
                ";" + getProfession() +
                ";" + getSalary() +
                ";" + getAttitudeToBoss().toString() +
                ";" + getWorkQuality() +
                ";" + getAvatarPath() +
                ";{" + getNotes() + "}");
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
        bagpack = null;
    }

    /**
     * Метод, преобразующий оставшуюся часть строки в коллекцию bagPack,
     * принадлежащую классу FactoryWorker
     * @author Zhurbova A.E.
     * @param scanner - строка, в которой хранятся данные о содержимом bagpack
     */
    public void parseFactoryWorker(Scanner scanner) {
        first : {
            try {
                Product product;
                Boolean foundClosingSquareBracket = false;
                int itemNumber = 0;
                String name = null;

                while (scanner.hasNext()) {
                    String[] nameAndPrice = scanner.next().split(":");

                    itemNumber++;
                    String a= "Name";
                    if (a.equals(nameAndPrice[0].replaceFirst("\\[", "")) || nameAndPrice[0].equals("") || nameAndPrice.length < 2){break first;}
                    if (nameAndPrice.length == 2 || nameAndPrice.length == 1) {
                        if ((itemNumber == 1) && (nameAndPrice[0].trim().charAt(0) != '[') &&
                                (nameAndPrice[0].substring(1).equals("")) && (nameAndPrice[1].substring(1).equals(""))) {
                            System.out.println("Неверно задан " + itemNumber + " предмет багажа в строке " + App.getLineNumber() +
                                    ". Должны быть указаны название и цена, разделенные \":\", предметы разделены \",\"" +
                                    " и указаны в квадратных скобках.(name = \"" + nameAndPrice[0].trim() + "\")");
                            System.exit(1);
                            return;
                        }

                        String stringForPrice = nameAndPrice[1].trim();
                        if (stringForPrice.charAt(stringForPrice.length() - 1) == ']') foundClosingSquareBracket = true;

                        name = nameAndPrice[0].replaceFirst("\\[", "").trim();
                        int price = Integer.parseInt(nameAndPrice[1].replace("]", "").trim());
                        product = new Product(name, price);
                    } else {if (name.equals("")){return;}
                        System.out.println("Неверно задан " + itemNumber + " предмет багажа в строке " + App.getLineNumber() +
                                ". Должны быть указаны название и цена, разделенные \":\", предметы разделены \",\".");
                        System.exit(1);
                        return;
                    }

                    addProduct(product);

                    if (foundClosingSquareBracket) break;
                }

                if (scanner.hasNext()) {
                    System.out.println("Неверное количество аргументов в строке " + App.getLineNumber() + "." +
                        "(не считалось: \"" + scanner.next() + "\")");
                    System.exit(1);
                }
            } catch (NumberFormatException e) {
                System.out.println("Неверно заданы предметы багажа в строке " + App.getLineNumber() +
                        ". Цена(после \":\") должна быть целым числом.");
                System.exit(1);
            }
        }
    }
}
