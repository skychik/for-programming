package ru.ifmo.cs.programming.lab5.domain;

import com.sun.nio.sctp.IllegalReceiveException;
import ru.ifmo.cs.programming.lab5.utils.AttitudeToBoss;
import ru.ifmo.cs.programming.lab5.utils.FactoryWorker;
import ru.ifmo.cs.programming.lab5.utils.Product;

public class ShopAssistant extends Employee {

    public ShopAssistant(String name, String profession, Integer salary, AttitudeToBoss attitude, Byte workQuality) {
        super(name, profession, salary, attitude, workQuality);
    }

    public ShopAssistant() {
        super();
    }

    public void giveSausage(FactoryWorker worker, Product sausage) {
        if(worker.getBagpack().contains(sausage)){
            throw new IllegalReceiveException(sausage.getName() + " уже есть!");
        } else{
            worker.receiveSausage(sausage);
        }
    }

    public String toString(){
        return ("ShopAssistant{name=" + getName()  +
                ", profession=" + getProfession() +
                ", salary=" + getSalary() +
                ", attitudeToBoss=" + getAttitudeToBoss().toString() +
                ", workQuality=" + getWorkQuality() +
                "}");
    }
}
