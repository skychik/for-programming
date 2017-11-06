package ru.ifmo.cs.programming.lab5.domain;

import ru.ifmo.cs.programming.lab5.utils.AttitudeToBoss;

import java.time.ZonedDateTime;

public class ShopAssistant extends Employee {

    public ShopAssistant(String name, String profession, Integer salary, AttitudeToBoss attitude, Byte workQuality) {
        super(name, profession, salary, attitude, workQuality);
        setSpeciality(this.getClass().getSimpleName());
    }

    public ShopAssistant(String name, String profession, Integer salary, AttitudeToBoss attitude, Byte workQuality, ZonedDateTime creating_time) {
        super(name, profession, salary, attitude, workQuality, creating_time);
        setSpeciality(this.getClass().getSimpleName());
    }

    public ShopAssistant() {
        super();
    }

//    public void giveSausage(FactoryWorker worker, Product sausage) {
//        if(worker.getBagpack().contains(sausage)){
//            throw new IllegalReceiveException(sausage.getName() + " уже есть!");
//        } else{
//            worker.receiveSausage(sausage);
//        }
//    }

    public String toString(){
        return ("ShopAssistant;" + getName()  +
                ";" + getProfession() +
                ";" + getSalary() +
                ";" + returnAttitude_to_boss().toString() +
                ";" + getWork_quality() +
                ";" + getAvatar_path() +
                ";{" + getNotes()) + "}";
    }

    @Override
    public String getSpeciality() {
        return "Shop Assistant";
    }
}
