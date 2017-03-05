/*
Дописать PrintWriter - записаь данных в файл;
 */
package laba2;

import java.util.ArrayDeque;
import java.io.BufferedReader;
import java.io.File;
import java.io.PrintWriter;
import java.io.FileNotFoundException; 
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Laba2 {

    public static void main(String[] args) throws IOException {
        try {//checks path to file with collection and it's existence  
            String fileWay = System.getenv("EmployeeFile");
            BufferedReader reader = new BufferedReader(new FileReader(fileWay));
        } catch(NullPointerException e){
            System.out.println("Не существует переменной окружения EmployeeFile. Создайте её, указав путь к файлу.");
            System.exit(0);
        } catch (FileNotFoundException e) {
            System.out.println("Нет файла с данными о коллекции");
            System.exit(0);
        }
        //тут должно быть заполнение коллекции из файла
        while {
            reader = new BufferedReader(new InputStreamReader(System.in));
            String command = reader.readLine().trim();
            if (command.equals(save));
        }
            /* try {
            //PrintWriter обеспечит возможности записи в файл
            PrintWriter out = new PrintWriter(fileWay);

            try {
                //Записываем текст в файл
                out.print(""); //Дописать
            } finally {
                
                //После чего мы должны закрыть файл
                //Иначе файл не запишется 
                
                out.close();
            }
        } catch(IOException e) {
            throw new RuntimeException(e);
        }*/
        }
}
