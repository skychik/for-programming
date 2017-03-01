/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package laba2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Objects;

/**
 *
 * @author саша и кирюша
 */

public class FactoryWorker extends Employee{
    private ArrayDeque<Product> bagpack;
    
    public FactoryWorker(String name, String profession, Workplace workplace, int salary, byte attitudeToBoss, byte workQuality) {
        super(name, profession, workplace, salary, attitudeToBoss, workQuality);
        bagpack = new ArrayDeque<Product>();
    }   
    
    public void changeQuality(byte up){
        super.workQuality += up;
    }   
    
    public void changeAttitude (byte up){
        super.attitudeToBoss += up;
    } 
    
    public void receiveSausage(Product sausage){
        bagpack.add(sausage);
        byte up = 5;
        this.changeQuality(up);
        this.changeAttitude(up);
    }

    @Override
    public String toString() {
        return "FactoryWorker{" + "bagpack=" + bagpack + '}';
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
        if (!Objects.equals(this.bagpack, other.bagpack)) {
            return false;
        }
        return true;
    }
    
    public void sign(ManagerNotebook notebook){
        notebook.addSign(this.getName());
    }  
    
    public void save() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            public void load() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            public void remove(Object o) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            public void remove_lower(Object o) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            public void removea_all(Object o) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
}
