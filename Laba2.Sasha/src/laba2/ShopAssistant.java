/**
 * Created by саша on 09.03.2017.
 */
package laba2;

public class ShopAssistant extends Employee{

    public ShopAssistant(String name, String profession, Integer salary, AttitudeToBoss attitude, Byte workQuality) {
        super(name, profession, salary, attitude, workQuality);
    }

    public void giveSausage(FactoryWorker worker, Product sausage){
        if(worker.bagpack.contains(sausage)){
            throw new MultipleSausageException("Сосиска уже есть!");
        } else{
            worker.receiveSausage(sausage);
        }
    }
}