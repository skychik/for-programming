/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package laba2;

import java.util.ArrayList;
import java.util.Objects;

/**
 *
 * @author саша и кирюша
 */

public class Workplace{
    protected String name;
    protected String type;
    protected Employer employer;
    protected ArrayList<Employee> employeeList; 
    protected ArrayList<Product> assortment;
    
    Workplace(String name, String type, ArrayList <Product> assortment, ArrayList <Employee> employeeList){
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
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.type, other.type)) {
            return false;
        }
        if (!Objects.equals(this.employer, other.employer)) {
            return false;
        }
        if (!Objects.equals(this.employeeList, other.employeeList)) {
            return false;
        }
        if (!Objects.equals(this.assortment, other.assortment)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Workplace{" + "name=" + name + ", type=" + type + ", employer=" + employer + ", employeeList=" + employeeList + ", assortment=" + assortment + '}';
    }
    
}


