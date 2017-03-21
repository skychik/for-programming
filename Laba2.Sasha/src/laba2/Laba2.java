/*
Просмотреть блоки try-catch
 */
package laba2;

import com.google.gson.Gson;

import java.io.*;
import java.util.ArrayDeque;
import java.util.Scanner;

public class Laba2 {

    public static void main(String[] args) throws IOException {
        java.lang.String filePath = System.getenv("EmployeeFile");
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        ArrayDeque deque = new ArrayDeque<Employee>() {

            void load() throws IOException {
                String line;

                while ((line = reader.readLine()) != null){
                    Scanner sc = new Scanner(line);
                    sc.useDelimiter(",");
                    String className = sc.next();
                    switch (className) {
                        case "FactoryWorker": {
                            FactoryWorker fw = new FactoryWorker();
                            fw.parseFactoryWorker(line);

//                        deq.addFirst(fw);
                        break;

                        }
                        case "ShopAssistant" : {
                            ShopAssistant shAs = new ShopAssistant();
                            shAs.parseEmployee(line);
//                            deq.addFirst(shAs);
                            break;
                        }
                        case "Employee" : {
                            Employee emp = new Employee();
                            emp.parseEmployee(line);
//                            deq.addFirst(emp);
                            break;
                        }
                        default : {
                            System.out.println("Приложение не обрабатывает указанный класс.");
                            System.exit(0);
                        }
                    }
                }
            }


            void save() throws IOException {
            }

            //void remove(){

            //}

            void remove_lower() {

            }

            void remove_all() {

            }

            void initReader() {       //checks path to file with collection and file's existence
                try {
                    String line = null;
                    int index = 0;
                    while ((line = reader.readLine()) != null) {
                        Employee emp;             //  Создание объектов
                        FactoryWorker fw;         //  Класса Employee или его наследников
                        ShopAssistant shAs;       //
                        ArrayDeque deq = new ArrayDeque<Employee>() {}}


                } catch (NullPointerException e) {
                    System.out.println("Не существует переменной окружения EmployeeFile. Создайте её, " +
                            "указав путь к файлу.");
                    System.exit(0);
                } catch (FileNotFoundException e) {
                    System.out.println("Нет файла с данными о коллекции, путь к которому указан в переменной " +
                            "окружения EmployeeFile");
                    System.exit(0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };


        // todo заполнить автоматически коллекцию при запуске

//        try {                                               //checks path to file with collection and file's existence
//            String filePath = System.getenv("EmployeeFile");
//            BufferedReader reader = new BufferedReader(new FileReader(filePath));
//        } catch(NullPointerException e){
//            System.out.println("Не существует переменной окружения EmployeeFile. Создайте её, указав путь к файлу.");
//            System.exit(0);
//        } catch (FileNotFoundException e) {
//            System.out.println("Нет файла с данными о коллекции, путь к которому указан в переменной окружения EmployeeFile");
//            System.exit(0);
//        }

        //deque.load();

    //        try {
    //            //PrintWriter обеспечит возможности записи в файл
    //            PrintWriter out = new PrintWriter(filePath);
    //
    //            try {
    //                for (int i = 0; i < deque.size(); i++){
    //
    //                }
    //            } finally {
    //                out.close();
    //            }
    //        } catch (IOException e) {
    //            System.out.println("e = " + e);
    //        }
    //    }

        Scanner scanner = new Scanner(System.in);

        while (){
            String command = scanner.next();
            String element = scanner.next();
            String[] param = element.split(", ");
            if (param.length < 6) {
                System.out.println("Неверно задан элемент: введены не все параметры.");
                System.exit(0);
            }
            for (int i = 0; i != param.length; i++){
                param[i] = param[i].split(": ")[1];
                int length = param[i].length() - 1;
                param[i] = param[i].substring(1, length);
            }
            String forCompare = param[0] + "{name =" + param[1] + ", profession=" + param[2]
                                                     + ", salary=" + param[3] + ", attitudeToBoss=" + param[4]
                                                                              + ", workQuality=" + param[5] + "}";
            for (int i = param.length; i > 6; i--){
                Product[] products;
//                products[param.length - i] = new Product(param[param.length - i].);
            }
//        {
//            Scanner in = new Scanner(System.in);
//            String command = in.next();
//            String element = in.next();
//            element.substring(1, element.length() - 1);
//            ArrayDeque<String[]> param = null;
//            param.add(element.split(": "));
//            if (param.size() < 6) {
//                System.out.println("Недостаточное количество параметров.");
//                System.exit(0);
//            }}
//            for (int i = 0; i != param.length; i++){
//                param[i] = param[i].split(": ")[1];
//                int lengthElem = param[i].length() - 1;
//                param[i] = param[i].substring(1, lengthElem);
//            }
//            String forCompare = param[0] + "{name =" + param[1] + ", profession=" + param[2]
//                                                     + ", salary=" + param[3] + ", attitudeToBoss=" + param[4]
//                                                                              + ", workQuality=" + param[5] + "}";
//            for (int i = param.length; i > 6; i--){
//                Product[] products;
////                products[param.length - i] = new Product(param[param.length - i].);}}
        }
}




