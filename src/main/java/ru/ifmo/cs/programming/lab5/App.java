/*
 *
 * Саша: взаимодействие с файлом
 *
 * Кирилл: работа в интерактивном режиме
 *
 */
package ru.ifmo.cs.programming.lab5;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import ru.ifmo.cs.programming.lab5.domain.Employee;
import ru.ifmo.cs.programming.lab5.domain.Product;
import ru.ifmo.cs.programming.lab5.domain.ShopAssistant;
import ru.ifmo.cs.programming.lab5.utils.AttitudeToBoss;
import ru.ifmo.cs.programming.lab5.utils.FactoryWorker;

import java.io.*;
import java.util.ArrayDeque;
import java.util.Scanner;

public class App {

    public static void main(String[] args) throws Exception {

        String filePath = System.getenv("EmployeeFile");
        BufferedReader reader = new BufferedReader(new FileReader(filePath));;
        ArrayDeque deque = new ArrayDeque<Employee>() {
            void load() {
                //todo Sasha: load
                //initReader();
                throw new UnsupportedOperationException();
            }

            void save() {
                //todo Sasha: save
                throw new UnsupportedOperationException();
            }

            void remove(Employee employee) {
                throw new UnsupportedOperationException();
            }

            void remove_lower(Employee employee) {
                throw new UnsupportedOperationException();
            }

            void remove_all(Employee employee) {
                throw new UnsupportedOperationException();
            }

            //checks path to file with collection and file's existence
            //нужен ли?
            void initReader() {
                try {
                    String line = null;
                    int index = 0;
                    while ((line = fileReader.readLine()) != null) {
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
                                        fw.getBagpack().add(product);
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
                    fileReader.close();
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

        deque.load();

        InputStreamReader in = new InputStreamReader(System.in);
        Scanner scanner = new Scanner(in).useDelimiter(" ");
        Gson gson;
        String command;

        /*This is an interactive mode:*/
        while (true) {
            command = scanner.next();
            Employee employee;
            switch (command) {
                case "remove":
                    employee = gson.fromJson(in, Employee.class);
                    deque.remove(employee);
                    break;
                case "remove_lower":
                    employee = gson.fromJson(in, Employee.class);
                    deque.remove_lower(employee);
                    break;
                case "remove_all":
                    employee = gson.fromJson(in, Employee.class);
                    deque.remove_all(employee);
                    break;
                case "save":
                    deque.save();
                    break;
                case "load":
                    deque.load();
                    break;
                case "end.":
                    deque.save();
                    return;
            }
        }
    }
}

