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
        ArrayDeque deque = new ArrayDeque<Employee>(){
            String filePath;
            BufferedReader reader;

            void load(){
                initReader();

            }

            void save(){

            }

            //void remove(){

            //}

            void remove_lower(){

            }

            void remove_all(){

            }

            void initReader(){       //checks path to file with collection and file's existence
                try {
                    filePath = System.getenv("EmployeeFile");
                    reader = new BufferedReader(new FileReader(filePath));
                } catch(NullPointerException e){
                    System.out.println("Не существует переменной окружения EmployeeFile. Создайте её, " +
                                        "указав путь к файлу.");
                    System.exit(0);
                } catch (FileNotFoundException e) {
                    System.out.println("Нет файла с данными о коллекции, путь к которому указан в переменной " +
                                        "окружения EmployeeFile");
                    System.exit(0);
                }
            }
        };

        // todo заполнить автоматически коллекцию при запуске

        try {                                               //checks path to file with collection and file's existence
            String filePath = System.getenv("EmployeeFile");
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
        } catch(NullPointerException e){
            System.out.println("Не существует переменной окружения EmployeeFile. Создайте её, указав путь к файлу.");
            System.exit(0);
        } catch (FileNotFoundException e) {
            System.out.println("Нет файла с данными о коллекции, путь к которому указан в переменной окружения EmployeeFile");
            System.exit(0);
        }

        //deque.load();

        /*while {
            reader = new BufferedReader(new InputStreamReader(System.in));
            String command = reader.readLine().trim();
            if (command.equals(save));
        }*/
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

        public void load (){

        }
}
