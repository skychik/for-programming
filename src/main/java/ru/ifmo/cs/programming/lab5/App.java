/*
 *
 * Саша: взаимодействие с файлом
 *
 * Кирилл: работа в интерактивном режиме
 *
 */
package ru.ifmo.cs.programming.lab5;

import com.google.gson.Gson;
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
                initReader();
            }

            void save() throws IOException {
            }

            void remove(Employee employee){

            }

            void remove_lower(Employee employee) {

            }

            void remove_all() {

            }

            //checks path to file with collection and file's existence
            //нужен ли?
            void initReader(){
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

        deque.load();

        BufferedReader bufferedReader = new BufferedReader();

        JsonReader jsonReader = new JsonReader();

        /**This is an interactive mode:*/
        while (true){
            String command = scanner.next();
            Employee employee;

            switch (command){
                case "remove" :
                case "remove_lower" :
                    employee = readEmployee(reader);
                    break;
            }


        }
    }

    /*public List<Employee> readJsonStream(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {
            return readEmployeesArray(reader);
        } finally {
            reader.close();
        }
    }*/

    /*public List<Employee> readEmployeesArray(JsonReader reader) throws IOException {
        List<Employee> employees = new ArrayList<Employee>();

        reader.beginArray();
        while (reader.hasNext()) {
            employees.add(readEmployee(reader));
        }
        reader.endArray();
        return employees;
    }*/

    public Employee readEmployee(JsonReader reader) throws IOException {
        String name = null;
        String profession = null;
        int salary = 0;
        AttitudeToBoss attitudeToBoss = null;
        byte workQuality = 0;

        reader.beginObject();
        while (reader.hasNext()) {
            String nextName = reader.nextName();
            if (nextName.equals("name")) {
                name = reader.nextString();
            } else if (nextName.equals("profession")) {
                profession = reader.nextString();
            } else if (nextName.equals("salary")) {
                salary = reader.nextInt();
            } else if (nextName.equals("attitudeToBoss") && reader.peek() != JsonToken.NULL) {
                attitudeToBoss = readAttitudeToBoss(reader);
            } else if (nextName.equals("workQuality")) {
                int i = reader.nextInt();
                if ((i > Byte.MAX_VALUE)||(i < Byte.MIN_VALUE))
                    throw new ByteOverflowException("workQuality value isn't a byte value");
                workQuality = (byte) i;
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new Employee(name, profession, salary, attitudeToBoss, workQuality);
    }

    public AttitudeToBoss readAttitudeToBoss(JsonReader reader) throws IOException {
        List<Double> doubles = new ArrayList<Double>();

        reader.beginArray();
        while (reader.hasNext()) {
            doubles.add(reader.nextDouble());
        }
        reader.endArray();
        return doubles;
    }

    /*public User readUser(JsonReader reader) throws IOException {
        String username = null;
        int followersCount = -1;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("name")) {
                username = reader.nextString();
            } else if (name.equals("followers_count")) {
                followersCount = reader.nextInt();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new User(username, followersCount);
    }*/

}

