/**
 * Created by саша on 09.03.2017.
 */
package ru.ifmo.cs.programming.lab5.domain;

import ru.ifmo.cs.programming.lab5.utils.AttitudeToBoss;
import ru.ifmo.cs.programming.lab5.utils.FactoryWorker;
import ru.ifmo.cs.programming.lab5.core.MultipleSausageException;

public class ShopAssistant extends Employee {

    public ShopAssistant(String name, String profession, Integer salary, AttitudeToBoss attitude, Byte workQuality) {
        super(name, profession, salary, attitude, workQuality);
    }

    public void giveSausage(FactoryWorker worker, Product sausage){
        if(worker.getBagpack().contains(sausage)){
            throw new MultipleSausageException("Сосиска уже есть!");
        } else{
            worker.receiveSausage(sausage);
        }
    }
}
