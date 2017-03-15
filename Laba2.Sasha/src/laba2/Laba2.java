/*
Просмотреть блоки try-catch
 */
package laba2;

import java.io.*;
import java.util.ArrayDeque;
import java.util.Scanner;

public class Laba2 {

    public static void main(String[] args) throws IOException {
        String filePath = System.getenv("EmployeeFile");
        BufferedReader reader = new BufferedReader(new FileReader(filePath));;
        ArrayDeque deque = new ArrayDeque<Employee>() {


            void load() {
                initReader();
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
                        ArrayDeque deq = new ArrayDeque<Employee>() {
                        };
                        Scanner sc = new Scanner(line);
                        sc.useDelimiter(",");
                        String next = sc.next();
                        if (line.length() < 6) {
                            System.out.println("Неверно задан объект в строке " + line + ". Описаны не все параметры.");
                            System.exit(0);
                        }
                        try {
                            String objClass = next;
                            String name = next;
                            String profession = next;
                            Integer salary = Integer.parseInt(next);
                            AttitudeToBoss attitudeToBoss = null;
                            switch (next) {
                                case "HATE": {
                                    attitudeToBoss = AttitudeToBoss.HATE;
                                    break;
                                }
                                case "LOW": {
                                    attitudeToBoss = AttitudeToBoss.LOW;
                                    break;
                                }
                                case "NORMAL": {
                                    attitudeToBoss = AttitudeToBoss.NORMAL;
                                    break;
                                }
                                case "HIGH": {
                                    attitudeToBoss = AttitudeToBoss.HIGH;
                                    break;
                                }
                                case "DEFAULT": {
                                    attitudeToBoss = AttitudeToBoss.DEFAULT;
                                    break;
                                }
                                default: {
                                    System.out.println("Неверно указано значение в ячейке " + line + "D.");
                                    System.exit(0);
                                }
                            }
                            Byte workQuality = Byte.parseByte(next);
                            switch (objClass) {
                                case "FactoryWorker": {
                                    fw = new FactoryWorker(name, profession, salary, attitudeToBoss, workQuality);
                                    while (sc.hasNext()) try {
                                        String[] name_and_price = next.split("- ");
                                        Product product = new Product(name_and_price[0], Integer.parseInt(name_and_price[1]));
                                        fw.bagpack.add(product);
                                    } catch (NumberFormatException e) {
                                        System.out.println("Неверно задан формат предмета. В ячейке надо указать название продукта и цену через \"-\".");
                                        System.exit(0);
                                    }
                                    deq.addFirst(fw);
                                    break;
                                }
                                case "ShopAssistant": {
                                    shAs = new ShopAssistant(name, profession, salary, attitudeToBoss, workQuality);
                                    deq.addFirst(shAs);
                                    break;
                                }
                                case "Employee": {
                                    emp = new Employee(name, profession, salary, attitudeToBoss, workQuality);
                                    deq.addFirst(emp);
                                    break;
                                }
                                default: {
                                    System.out.println("Приложение не обрабатывает указанный класс.");
                                    System.exit(0);
                                }
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Исправьте значения в строке " + line + ". Столбцы C и E должны содержать числа.");
                            System.exit(0);
                        }
                    }
                    reader.close();
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

        {
            Scanner in = new Scanner(System.in);
            String command = in.next();
            String element = in.next();
            String [] param = element.split(", ");
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
        }
    }

}

