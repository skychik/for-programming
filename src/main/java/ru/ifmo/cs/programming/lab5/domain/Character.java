package ru.ifmo.cs.programming.lab5.domain;

import ru.ifmo.cs.programming.lab5.core.Movement;

import java.util.*;

public abstract class Character implements Movement {

    private String name;

    private String profession;

    public Character(String name, String profession){
        this.name = name;
        this.profession = profession;
    }

    public Character() {}

    public String getName(){
        return this.name;
    }

    abstract public void work();

    @Override
    public void move(String address) {
        System.out.println(getName() + " направляется по адресу:" + address);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.name);
        hash = 47 * hash + Objects.hashCode(this.profession);
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
        final Character other = (Character) obj;
        return Objects.equals(getName(), other.getName()) &&
                Objects.equals(getProfession(), other.getProfession());
    }

    @Override
    public String toString() {
        return "Character{name=" + getName() + ", profession=" + getProfession() + '}';
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }
}

