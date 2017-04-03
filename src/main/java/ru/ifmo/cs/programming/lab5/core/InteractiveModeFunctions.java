package ru.ifmo.cs.programming.lab5.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
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

public class InteractiveModeFunctions {

    /*файл, который хранит deque*/
    private static File filePath = null;
    private static int lineNumber = 1;
    private static String obj;
    private static Employee employee;
    private static Scanner scanner;//TODO: has to stop at enter
    //for working with Gson library
    private static Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();

    /**
     * Show out all supported commands
     */
    //TODO: make helping output
    protected static void help() {
        System.out.println("** You can use commands:\n" +
                "add {}, remove {}, remove_lower {}, remove_all {}, save, load, show, end\n");
    }

    /**
     * Add Employee to deque
     *
     * @param deque Works with this deque
     */
    protected static void add(ArrayDeque<Employee> deque) {
        obj = jsonObject(scanner);
        if (obj == null) return;//if incorrect input (more closing brackets than opening)
        //System.out.println(obj);
        try {
            employee = gson.fromJson(obj, Employee.class);
        } catch (JsonSyntaxException e) {
            System.out.println(e.getMessage());
            return;
        }
        deque.add(employee);
    }

    /**
     * Removes employee from deque
     *
     * @param deque ArrayDeque
     */
    protected static void remove(ArrayDeque<Employee> deque) {
        obj = jsonObject(scanner);
        if (obj == null) return;//if incorrect input (more closing brackets than opening)
        //System.out.println(obj);
        //TODO: учитывать остаток после вычленения одной команды из потока ввода
        employee = gson.fromJson(obj, Employee.class);
        //assert employee.equals(new Employee("Sasha", "programmer", 1, LOW, (byte) 4)) :
        //        employee.toString();
        deque.remove(employee);
        System.out.println("First employee, which you typed, removed");
    }

    /**
     * Removes all employees from deque, which are lower(compares .toStrings), than this employee
     *
     * @param deque    ArrayDeque
     */
    protected static void remove_lower(ArrayDeque<Employee> deque) {
        obj = jsonObject(scanner);
        if (obj == null) return;//if incorrect input (more closing brackets than opening)
        //System.out.println(obj);
        employee = gson.fromJson(obj, Employee.class);
        Arrays.sort(deque.toArray(new Employee[0]));
        while (!deque.isEmpty() && (employee.compareTo(deque.peekFirst())) > 0) {
            deque.removeFirst();
        }
        System.out.println("Removed all employees, which are lower, than your typed employee");
    }

    /**
     * Removes all Emloyees = this employee
     *
     * @param deque    ArrayDeque
     */
    protected static void remove_all(ArrayDeque<Employee> deque) {
        obj = jsonObject(scanner);
        if (obj == null) return;//if incorrect input (more closing brackets than opening)
        //System.out.println(obj);
        Employee emp = gson.fromJson(obj, FactoryWorker.class);
        //ArrayDeque<Employee> arrayDeque = new ArrayDeque<>();
        //arrayDeque.add(new FactoryWorker("Pasha", "programmer", 18, NORMAL, (byte) 21));

        //assert (emp instanceof FactoryWorker);

        //assert emp.equals(arrayDeque.peekFirst()) :
        //        arrayDeque.peekFirst().toString() +
        //                '\n' +
        //                emp.toString();
        ArrayDeque<Employee> anotherDeque = new ArrayDeque<>();
        /*
          Puts all Employees < employee in anotherDeque
         */
        while (!deque.isEmpty() && (employee.compareTo(deque.peekFirst()) > 0))
            anotherDeque.addFirst(deque.pollFirst());
        /*
          Removes all Employees == employee from deque
         */
        while (!deque.isEmpty() && (employee.compareTo(deque.peekFirst()) == 0))
            deque.removeFirst();
        /*
          Puts all Employees from anotherDeque back in deque
         */
        while (!anotherDeque.isEmpty())
            deque.addFirst(anotherDeque.pollFirst());
        System.out.println("All employees, which are the same with your typed employee, removed from deque");
    }

    /**
     * Метод, осуществляющий загрузку коллекции из файла
     *
     * @param deque - коллекция, в которую происходит запись
     * @author Zhurbova A.E.
     */
    //TODO: не доделано
    protected static void load(ArrayDeque<Employee> deque) {
        BufferedReader reader = null;
        String line;

        try {
            reader = new BufferedReader(new FileReader(filePath));
        } catch (FileNotFoundException e) {
            System.out.println("Указанного файла не существует. ");
            System.exit(1);
        } catch (NullPointerException e) {
            System.out.println("Не существует переменной окружения EmployeeFile.");
            System.exit(1);
        }

        try {
            while ((line = reader.readLine()) != null) {
                Employee employee = parseEmployee(line);
                deque.add(employee);
                incLineNumber();
            }
        } catch (IOException e){
            System.out.println("Cannot load file");
            System.exit(1);
        }

        show(deque);
    }

    /**
     * Метод, осуществляющий запись коллекции в файл
     *
     * @param deque - коллекция, из которой считываются данные
     * @author Zhurbova A.E.
     */
    //TODO: нужна стабильность(не доделано)
    protected static void save(ArrayDeque<Employee> deque) {
        PrintWriter writer;

        try{
            writer = new PrintWriter(filePath);
        } catch (FileNotFoundException e) {
            System.out.println("Cannot save current Employees: File (" + filePath + ") not found");
            return;
        }

        // Стандартные настройки (кодировка, переносы строк, разделители и т.д.)
        ICsvBeanWriter csvBeanWriter = new CsvBeanWriter(writer, CsvPreference.STANDARD_PREFERENCE);

        //for (Employee employee : deque) {
            //Вывод считанной коллекции
            for (Employee aDeque : deque) {
                System.out.println(aDeque);
            }
        //}

        try {
            csvBeanWriter.close();
        } catch (IOException e) {
            System.out.println("Cannot close file with Employees");
            return;
        }

        //Проверка записанного в файл содержимого, посредством вывода на экран
        //System.out.println(writer.toString());
        System.out.println("saved");
    }

    /**
     * Show all Employees from this deque
     *
     * @param deque Current deque
     */
    protected static void show(ArrayDeque<Employee> deque) {
        System.out.println("Current employees now are the same with the file:");
        if (!deque.isEmpty()) {
            for (Employee aDeque : deque) {
                System.out.println(aDeque);
            }
        } else System.out.println("empty");
        System.out.println();
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

    public static void setFilePath(File filePath) {InteractiveModeFunctions.filePath = filePath;}

    public static File getFilePath() { return filePath; }

    /**
     * Метод, осуществляющий увеличение на единицу номера считываемой строки
     *
     * @author Zhurbova A.E.
     */
    private static void incLineNumber() {
        lineNumber++;
    }

    /**
     * Метод, возвращающий номер считываемой строки
     *
     * @author Zhurbova A.E.
     */
    public static int getLineNumber() {
        return lineNumber;
    }

    public static Scanner getScanner() {
        return scanner;
    }

    public static void setScanner(Scanner scanner) {
        InteractiveModeFunctions.scanner = scanner;
    }
}
