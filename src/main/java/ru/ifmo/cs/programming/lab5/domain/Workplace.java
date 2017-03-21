/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.ifmo.cs.programming.lab5.domain;

import java.util.ArrayList;
import java.util.Objects;

/**
 *
 * @author саша и кирюша
 */

public class Workplace{

    private String name;
    private String type;
    private Employer employer;
    private ArrayList<Employee> employeeList;
    private ArrayList<Product> assortment;

    public Workplace(String name, String type, ArrayList <Product> assortment, ArrayList <Employee> employeeList){
        this.name = name;
        this.type = type;
        this.assortment = assortment;
        this.employeeList = employeeList;
    }

    public void setAssortment(ArrayList <Product> assortment){
        this.assortment = assortment;
    }

    public ArrayList <Product> getAssortment(){
        return assortment;
    }

    public void setEmployer(Employer employer){
        this.employer = employer;
    }

    public void addEmployee(Employee employee){
        //incorrect realisation 
        this.employeeList.add(employee);
    }

    public ArrayList<Employee> getEmployeeList(){
        return(employeeList);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.name);
        hash = 67 * hash + Objects.hashCode(this.type);
        hash = 67 * hash + Objects.hashCode(this.employer);
        hash = 67 * hash + Objects.hashCode(this.employeeList);
        hash = 67 * hash + Objects.hashCode(this.assortment);
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
        final Workplace other = (Workplace) obj;
        return Objects.equals(getName(), other.getName()) &&
                Objects.equals(getType(), other.getType()) &&
                Objects.equals(getEmployer(), other.getEmployer()) &&
                Objects.equals(getEmployeeList(), other.getEmployeeList()) &&
                Objects.equals(getAssortment(), other.getAssortment());
    }

    @Override
    public String toString() {
        return "Workplace{name="    + getName()
                + ", type="         + getType()
                + ", employer="     + getEmployer()
                + ", employeeList=" + getEmployeeList()
                + ", assortment="   + getAssortment() + '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Employer getEmployer() {
        return employer;
    }

    public void setEmployeeList(ArrayList<Employee> employeeList) {
        this.employeeList = employeeList;
    }
}