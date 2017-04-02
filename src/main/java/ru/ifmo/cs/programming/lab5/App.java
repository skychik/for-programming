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
import ru.ifmo.cs.programming.lab5.core.InteractiveModeFunctions;
import ru.ifmo.cs.programming.lab5.domain.Employee;
import ru.ifmo.cs.programming.lab5.utils.FactoryWorker;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Scanner;
import java.util.regex.Pattern;

public class App extends InteractiveModeFunctions {

    /*это наш дек*/
    private static ArrayDeque<Employee> deque = new ArrayDeque<>();
    //for testing(changes to new FileReader(testingDir + "\\input.txt"))
    private static InputStreamReader inputStreamReader = new InputStreamReader(System.in);
    //for working with Gson library
    private static Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
    private static Scanner scanner;//TODO: has to stop at enter

    public static void main(String[] args) throws Exception {
        scanner = new Scanner(inputStreamReader).useDelimiter(Pattern.compile("\\s"));
        if (getFilePath() == null)
            setFilePath(new File(System.getenv("EmployeeFile")));

        //First loading of the deque from our File
        load(deque);

        //save deque after every shutdown
        Runtime.getRuntime().addShutdownHook(new Thread(() -> save(deque)));

        /*This is an interactive mode:*/
        interactiveMode();
    }

    private static void interactiveMode() {
        String command;

        System.out.println("** To stop this program, type 'end'\n");

        intMode:
        while (true) {
            System.out.print("Write your command:\n");
            command = scanner.next();
                System.out.println('\"' + command + '\"');

            String obj;
            Employee employee;

            switch (command) {
                case "add":
                    obj = jsonObject(scanner);
                    if (obj == null) continue;//if incorrect input (more closing brackets than opening)
                    //System.out.println(obj);
                    try {
                        employee = gson.fromJson(obj, Employee.class);
                    } catch (IllegalStateException e) {
                        System.out.println(e.getMessage());
                        continue;
                    }
                    add(deque, employee);
                    break;
                case "remove":
                    obj = jsonObject(scanner);
                    if (obj == null) continue;//if incorrect input (more closing brackets than opening)
                    //System.out.println(obj);
                    //TODO: учитывать остаток после вычленения одной команды из потока ввода
                    employee = gson.fromJson(obj, Employee.class);
                    //assert employee.equals(new Employee("Sasha", "programmer", 1, LOW, (byte) 4)) :
                    //        employee.toString();
                    remove(deque, employee);
                    break;
                case "remove_lower":
                    obj = jsonObject(scanner);
                    if (obj == null) continue;//if incorrect input (more closing brackets than opening)
                    //System.out.println(obj);
                    employee = gson.fromJson(obj, Employee.class);
                    remove_lower(deque, employee);
                    break;
                case "remove_all":
                    obj = jsonObject(scanner);
                    if (obj == null) continue;//if incorrect input (more closing brackets than opening)
                    //System.out.println(obj);
                    Employee emp = gson.fromJson(obj, FactoryWorker.class);
                    //ArrayDeque<Employee> arrayDeque = new ArrayDeque<>();
                    //arrayDeque.add(new FactoryWorker("Pasha", "programmer", 18, NORMAL, (byte) 21));

                    //assert (emp instanceof FactoryWorker);

                    //assert emp.equals(arrayDeque.peekFirst()) :
                    //        arrayDeque.peekFirst().toString() +
                    //                '\n' +
                    //                emp.toString();
                    remove_all(deque, emp);
                    break;
                case "save":
                    save(deque);
                    break;
                case "load":
                    load(deque);
                    break;
                case "show":
                    show(deque);
                    break;
                case "end":
                    break intMode;
                default:
                    System.out.println("No such command: '" + command + "'");
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

            if ((numberOfOpeningBrackets == 0) && (numberOfClosingBrackets == 0))
                    continue;
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

