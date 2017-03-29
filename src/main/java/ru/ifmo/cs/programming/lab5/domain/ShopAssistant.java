package ru.ifmo.cs.programming.lab5.domain;

import com.sun.nio.sctp.IllegalReceiveException;
import ru.ifmo.cs.programming.lab5.App;
import ru.ifmo.cs.programming.lab5.utils.AttitudeToBoss;
import ru.ifmo.cs.programming.lab5.utils.FactoryWorker;

import static ru.ifmo.cs.programming.lab5.App.save;

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
}
