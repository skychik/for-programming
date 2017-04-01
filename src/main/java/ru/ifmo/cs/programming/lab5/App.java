/*
 *
 * Саша: взаимодействие с файлом
 *
 * Кирилл: работа в интерактивном режиме
 *
 */
package ru.ifmo.cs.programming.lab5;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.ifmo.cs.programming.lab5.domain.Employee;
import ru.ifmo.cs.programming.lab5.utils.FactoryWorker;

import java.io.*;
import java.util.ArrayDeque;
import java.util.Scanner;

import ru.ifmo.cs.programming.lab5.core.InteractiveModeFunctions;

import static ru.ifmo.cs.programming.lab5.utils.AttitudeToBoss.LOW;
import static ru.ifmo.cs.programming.lab5.utils.AttitudeToBoss.NORMAL;

public class App extends InteractiveModeFunctions {

    /*это наш дек*/
    private static ArrayDeque<Employee> deque = new ArrayDeque<>();
    private static InputStreamReader inputStreamReader = new InputStreamReader(System.in);
    private static Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
    private static Scanner scanner = new Scanner(inputStreamReader);//todo has to stop at enter

    public static void main(String[] args) throws Exception {

        //First loading of the deque from our File
        load(deque);

        //save deque after every shutdown
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                save(deque);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));

        /*This is an interactive mode:*/
        interactiveMode();

        save(deque);
    }

    private static void interactiveMode() throws IOException {
        String command;

        intMode:
        while (true) {
            System.out.println("Write your command:");
            command = scanner.next();

            String obj;
            Employee employee;

            switch (command) {
                case "remove":
                    obj = jsonObject(scanner);
                    if (obj == null) continue;//if incorrect input: more closing brackets than opening

                    System.out.println(obj);
                    //todo учитывать остаток после вычленения одной команды из потока ввода
                    employee = gson.fromJson(obj, Employee.class);

                    assert employee.equals(new Employee("Sasha", "programmer", 1, LOW, (byte) 4)) :
                            employee.toString();

                    remove(deque, employee);
                    break;
                case "remove_lower":
                    obj = jsonObject(scanner);
                    if (obj == null) continue;//if incorrect input: more closing brackets than opening

                    System.out.println(obj);
                    employee = gson.fromJson(obj, Employee.class);
                    remove_lower(deque, employee);
                    break;
                case "remove_all":
                    obj = jsonObject(scanner);
                    if (obj == null) continue;//if incorrect input: more closing brackets than opening

                    System.out.println(obj);
                    Employee emp = gson.fromJson(obj, FactoryWorker.class);

                    ArrayDeque<Employee> arrayDeque = new ArrayDeque<>();
                    arrayDeque.add(new FactoryWorker("Pasha", "programmer", 18, NORMAL, (byte) 21));

                    assert (emp instanceof FactoryWorker);

                    assert emp.equals(arrayDeque.peekFirst()) :
                            arrayDeque.peekFirst().toString() +
                                    '\n' +
                                    emp.toString();

                    remove_all(deque, emp);
                    break;
                case "save":
                    save(deque);
                    break;
                case "load":
                    load(deque);
                    break;
                case "end":
                    save(deque);
                    break intMode;
            }
        }
    }

    /**
     * Returns String, which contains object in json format
     *
     * @param scanner has thread from System.in
     * @return String which contains object in json format
     */
    private static String jsonObject(Scanner scanner) {
        int numberOfOpeningBrackets = 0;
        int numberOfClosingBrackets = 0;
        StringBuilder obj = new StringBuilder();
        String line;

        while (true) {
            //System.out.println('#');
            line = scanner.nextLine();
            obj.append(line.trim());

            //countNumberOfBrackets
            for (int pos = 0; pos < line.length(); ++pos) {
                char ch = line.charAt(pos);

                // здесь делаем что хотим с символом
                switch (ch) {
                    case '{':
                        numberOfOpeningBrackets++;
                        break;
                    case '}':
                        numberOfClosingBrackets++;
                        break;
                }
            }

            if (numberOfOpeningBrackets == numberOfClosingBrackets) return obj.toString();
//                /*//Gson should do this itself
//                if (line.trim().toCharArray()[line.length() - 1] == '}')
//                    return;
//                else {
//                    System.out.println("Wrong input format: didn't end with '}'");
//                    obj = null;
//                    return;
//                }*/
            if (numberOfOpeningBrackets < numberOfClosingBrackets) {
                System.out.println(
                        "Number of closing figure brackets can't be more, than number of opening figure brackets");
                return null;
            }
        }
    }

    public static void setInputStreamReader(InputStreamReader inputStreamReader) {
        App.inputStreamReader = inputStreamReader;
    }
}

