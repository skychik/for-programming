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

import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Scanner;

import static ru.ifmo.cs.programming.lab5.utils.AttitudeToBoss.LOW;

public class App {

    public static void main(String[] args) throws Exception {

        String filePath = System.getenv("EmployeeFile");

        //BufferedReader fileReader = new BufferedReader(new FileReader(filePath));
        /*
        String filePath = System.getenv("EmployeeFile");
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        */
        //
        //The same thing, but also checks path to file with collection and file's existence:
        /*
        try {
            String filePath = System.getenv("EmployeeFile");
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
        } catch(NullPointerException e){
            System.out.println("Не существует переменной окружения EmployeeFile. Создайте её, указав путь к файлу.");
            System.exit(0);
        } catch (FileNotFoundException e) {
            System.out.println("Нет файла с данными о коллекции, путь к которому указан в переменной окружения EmployeeFile");
            System.exit(0);
        }
        */

        ArrayDeque deque = new ArrayDeque<Employee>();

        //First loading of the deque from our File
        //load(deque);

        InputStreamReader in = new FileReader("src\\main\\java\\ru\\ifmo\\cs\\programming\\lab5\\input.txt")
                /*InputStreamReader(System.in)*/; //input stream
        Scanner scanner = new Scanner(in);//has to stop at enter
        Gson gson = new Gson();
        String command;

        Employee employee;
        Employee employeeFromRemove = new Employee("Sasha", "programmer", 0, LOW, (byte) 4);

        //todo Let gson.fromJson work with other classes, which extend Employee
        /*This is an interactive mode:*/
        System.out.println("Write your command:");
        intMode: while (true) {
            command = scanner.next();
            //System.out.println("!switch");
            String obj;
            switch (command) {
                case "remove":
                    //System.out.println("command: \'" + command + '\'');
                    obj = jsonFormatedObject(scanner);
                    if (obj == null) continue;//if incorrect input: more closing brackets than opening
                    //System.out.println(obj);
                    //todo учитывать остаток после вычленения одной команты из потока ввода
                    employee = gson.fromJson(obj, Employee.class);
                    assert employee.equals(employeeFromRemove) : employee.toString();
                    //remove(deque, employee);
                    break;
                case "remove_lower":
                    //System.out.println("command: \'" + command + '\'');
                    obj = jsonFormatedObject(scanner);
                    if (obj == null) continue;//if incorrect input: more closing brackets than opening
                    //System.out.println(obj);
                    employee = gson.fromJson(obj, Employee.class);
                    remove_lower(deque, employee);
                    break;
                case "remove_all":
                    //System.out.println("command: \'" + command + '\'');
                    obj = jsonFormatedObject(scanner);
                    if (obj == null) continue;//if incorrect input: more closing brackets than opening
                    //System.out.println(obj);
                    employee = gson.fromJson(obj, Employee.class);
                    remove_all(deque, employee);
                    break;
                case "save":
                    //System.out.println("command: \'" + command + '\'');
                    save(deque);
                    break;
                case "load":
                    //System.out.println("command: \'" + command + '\'');
                    load(deque);
                    break;
                case "end":
                    //System.out.println("command: \'" + command + '\'');
                    //save(deque);
                    break intMode;
            }
            //System.out.println("!end");
        }
    }

    private static String jsonFormatedObject(Scanner scanner) {
        int numberOfOpeningBrackets = 0;
        int numberOfClosingBrackets = 0;
        String obj = "";
        String line;

        while (true) {
            //System.out.println('#');
            line = scanner.nextLine();
            obj += line.trim();
            //countNumberOfBrackets
            for (int pos = 0; pos < line.length(); ++pos)
            {
                char ch = line.charAt(pos);
                // здесь делаем что хотим с символом
                switch (ch){
                    case '{':
                        numberOfOpeningBrackets++;
                        break;
                    case '}':
                        numberOfClosingBrackets++;
                        break;
                }
            }

            if (numberOfOpeningBrackets == numberOfClosingBrackets) return obj;
                /*//Gson should do this itself
                if (line.trim().toCharArray()[line.length() - 1] == '}')
                    return;
                else {
                    System.out.println("Wrong input format: didn't end with '}'");
                    obj = null;
                    return;
                }*/
            if (numberOfOpeningBrackets < numberOfClosingBrackets) {
                System.out.println(
                        "Number of closing figure brackets can't be more, than number of opening figure brackets");
                return null;
            }
        }
    }

    private static void load(ArrayDeque deque) {
        //todo Sasha: load
        //initReader();
        throw new UnsupportedOperationException();
    }

    private static void save(ArrayDeque deque) {
        //todo Sasha: save
        throw new UnsupportedOperationException();
    }

    private static void remove(ArrayDeque deque, Employee employee) {
        throw new UnsupportedOperationException();
    }

    private static void remove_lower(ArrayDeque deque, Employee employee) {
        throw new UnsupportedOperationException();
    }

    private static void remove_all(ArrayDeque deque, Employee employee) {
        throw new UnsupportedOperationException();
    }

    //checks path to file with collection and file's existence
    //нужен ли?
    /*static void initReader() {
        try {
            String line = null;
            int index = 0;
            while ((line = fileReader.readLine()) != null) {
                Employee emp;             //  Создание объектов
                FactoryWorker fw;         //  Класса Employee или его наследников
                ShopAssistant shAs;       //
                ArrayDeque deq = new ArrayDeque<Employee>();
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
    }*/
}

