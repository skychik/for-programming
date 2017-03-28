/*
 *
 * Саша: взаимодействие с файлом
 *
 * Кирилл: работа в интерактивном режиме
 *
 */
package ru.ifmo.cs.programming.lab5;

import com.google.gson.Gson;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;
import ru.ifmo.cs.programming.lab5.domain.Employee;
import ru.ifmo.cs.programming.lab5.utils.FactoryWorker;

import java.io.*;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Scanner;

import static ru.ifmo.cs.programming.lab5.domain.Employee.parseEmployee;
import static ru.ifmo.cs.programming.lab5.utils.AttitudeToBoss.LOW;
import static ru.ifmo.cs.programming.lab5.utils.AttitudeToBoss.NORMAL;

public class App {

    private static int lineNumber = 1;

    public static void main(String[] args) throws Exception {

        //        //BufferedReader fileReader = new BufferedReader(new FileReader(filePath));
//        /*
//        String filePath = System.getenv("EmployeeFile");
//        BufferedReader reader = new BufferedReader(new FileReader(filePath));
//        */
//        //
//        //The same thing, but also checks path to file with collection and file's existence:
//        /*
//        try {
//            String filePath = System.getenv("EmployeeFile");
//            BufferedReader reader = new BufferedReader(new FileReader(filePath));
//        } catch(NullPointerException e){
//            System.out.println("Не существует переменной окружения EmployeeFile. Создайте её, указав путь к файлу.");
//            System.exit(0);
//        } catch (FileNotFoundException e) {
//            System.out.println("Нет файла с данными о коллекции, путь к которому указан в переменной окружения EmployeeFile");
//            System.exit(0);
//        }
//        */

        /*это наш дек*/
        ArrayDeque<Employee> deque = new ArrayDeque<>();

        String filePath = System.getenv("EmployeeFile");

        //First loading of the deque from our File
        //load(deque);

        InputStreamReader in = new FileReader("src\\main\\java\\ru\\ifmo\\cs\\programming\\lab5\\input.txt")
                /*InputStreamReader(System.in)*/; //input stream
        Scanner scanner = new Scanner(in);//has to stop at enter
        Gson gson = new Gson();
        String command;

        //todo Let gson.fromJson work with other classes, which extend Employee
        /*This is an interactive mode:*/
        System.out.println("Write your command:");
        intMode: while (true) {
            command = scanner.next();
            System.out.println("!switch");

            String obj;
            Employee employee;

            switch (command) {
                case "remove":
                    System.out.println("command: \'" + command + '\'');
                    obj = jsonFormatedObject(scanner);
                    if (obj == null) continue;//if incorrect input: more closing brackets than opening
                    System.out.println(obj);
                    //todo учитывать остаток после вычленения одной команты из потока ввода
                    employee = gson.fromJson(obj, Employee.class);

                    assert employee.equals(new Employee("Sasha", "programmer", 1, LOW, (byte) 4)) :
                            employee.toString();

                    remove(deque, employee);
                    break;
                case "remove_lower":
                    System.out.println("command: \'" + command + '\'');
                    obj = jsonFormatedObject(scanner);
                    if (obj == null) continue;//if incorrect input: more closing brackets than opening
                    System.out.println(obj);
                    employee = gson.fromJson(obj, Employee.class);
                    remove_lower(deque, employee);
                    break;
                case "remove_all":
                    System.out.println("command: \'" + command + '\'');
                    obj = jsonFormatedObject(scanner);
                    if (obj == null) continue;//if incorrect input: more closing brackets than opening
                    System.out.println(obj);
                    employee = gson.fromJson(obj, Employee.class);

                    ArrayDeque<Employee> arrayDeque = new ArrayDeque<>();
                    arrayDeque.add(new FactoryWorker("Pasha", "programmer", 18, NORMAL, (byte) 21));

                    assert employee.equals(arrayDeque.peekFirst()) :
                            arrayDeque.peekFirst().toString();

                    remove_all(deque, employee);
                    break;
                case "save":
                    System.out.println("command: \'" + command + '\'');
                    save(deque, filePath);
                    break;
                case "load":
                    System.out.println("command: \'" + command + '\'');
                    load(deque, filePath);
                    break;
                case "end":
                    System.out.println("command: \'" + command + '\'');
                    //save(deque);
                    break intMode;
            }
            System.out.println("!end");
        }
    }

    private static String jsonFormatedObject(Scanner scanner) {
        int numberOfOpeningBrackets = 0;
        int numberOfClosingBrackets = 0;
        StringBuilder obj = new StringBuilder();
        String line;

        while (true) {
            //System.out.println('#');
            line = scanner.nextLine();
            obj.append(line.trim());
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

            if (numberOfOpeningBrackets == numberOfClosingBrackets) return obj.toString();
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

    private static void remove(ArrayDeque<Employee> deque, Employee employee) {
        deque.remove(employee);
    }

    private static void remove_lower(ArrayDeque<Employee> deque, Employee employee) {
        Arrays.sort(deque.toArray(new Employee[0]));
        while (!deque.isEmpty() && (employee.compareTo(deque.peekFirst())) > 0){
            deque.removeFirst();
        }
    }
    private static void remove_all(ArrayDeque<Employee> deque, Employee employee) {
        ArrayDeque <Employee> anotherDeque = new ArrayDeque<>();
        //put all Employees < employee in anotherDeque
        while (!deque.isEmpty() && (employee.compareTo(deque.peekFirst()) > 0))
            anotherDeque.addFirst(deque.pollFirst());
        //remove all Employees == employee from deque
        while (!deque.isEmpty() && (employee.compareTo(deque.peekFirst()) == 0))
            deque.removeFirst();
        //put all Employees from anotherDeque back in deque
        while (!anotherDeque.isEmpty())
            deque.addFirst(anotherDeque.pollFirst());
    }

    private static void load(ArrayDeque<Employee> deque, String filePath) throws IOException {
        try {

            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;

            while ((line = reader.readLine()) != null) {
                Employee employee = parseEmployee(line);
                deque.add(employee);
                incLineNumber();
            }

        } catch (FileNotFoundException e){ //ToDO IOEx. - ?
            System.out.println("Указанного файла не существует.");
        } catch (NullPointerException e){
            System.out.println("Не существует переменной окружения EmployeeFile.");
        }

        //Состояние очереди после считывания
        System.out.println(deque.toString());
    }

    private static void save(ArrayDeque<Employee> deque, String filepath) throws IOException {

        File file = new File(filepath);

        PrintWriter writer = new PrintWriter(file);
        // Стандартные настройки (кодировка, переносы строк, разделители и т.д.)
        ICsvBeanWriter csvBeanWriter = new CsvBeanWriter(writer, CsvPreference.STANDARD_PREFERENCE);

        for (Employee employee : deque) {
            //Запись имени класса + объект
            csvBeanWriter.write(employee.getClass().getSimpleName() + employee);
        }

        csvBeanWriter.close();

        //Проверка записанного в файл содержимого, посредством вывода на экран
        System.out.println(writer.toString());
    }

    private static void incLineNumber(){
        lineNumber++;
    }

    public static int getLineNumber(){
        return (lineNumber);
    }
}

