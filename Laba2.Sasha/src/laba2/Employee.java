/**
 * Created by саша on 09.03.2017.
 */

package laba2;
import java.util.Objects;

public class Employee extends Character implements Comparable{
    int salary;
    AttitudeToBoss attitudeToBoss;
    byte workQuality;

    public Employee(String name, String profession, int salary, AttitudeToBoss attitudeToBoss, byte workQuality) {
        super(name, profession);
        this.salary = salary;
        this.attitudeToBoss = attitudeToBoss;
        this.workQuality = workQuality;
    }

    public Employee() {
        super();
    }

    @Override
    public void work() {
        if (attitudeToBoss == AttitudeToBoss.HATE)
            workQuality = -128;
        if (attitudeToBoss == AttitudeToBoss.LOW){
            if (workQuality > -108) workQuality -= 20;
            else workQuality = -128;
        }
        if (attitudeToBoss == AttitudeToBoss.NORMAL){
            if (workQuality > 107) workQuality += 20;
            else workQuality = 127;
        }
        if (attitudeToBoss == AttitudeToBoss.HIGH)
            workQuality = 127;
    }

    @Override
    public String toString() {
        return "Employee{" + "salary=" + salary + ", attitudeToBoss=" + attitudeToBoss.attitude + ", workQuality=" + workQuality + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + this.salary;
        hash = 29 * hash + Objects.hashCode(this.attitudeToBoss);
        hash = 29 * hash + this.workQuality;
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
        final Employee other = (Employee) obj;
        return this.salary == other.salary &&
                this.attitudeToBoss == other.attitudeToBoss &&
                this.workQuality == other.workQuality;

    }

    @Override
    public int compareTo(Object o) {
        return this.toString().compareTo(o.toString());
    }
}
