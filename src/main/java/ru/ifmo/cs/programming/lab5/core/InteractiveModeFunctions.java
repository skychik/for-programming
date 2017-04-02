package ru.ifmo.cs.programming.lab5.core;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;
import ru.ifmo.cs.programming.lab5.domain.Employee;

import java.io.*;
import java.util.ArrayDeque;
import java.util.Arrays;

import static ru.ifmo.cs.programming.lab5.domain.Employee.parseEmployee;

public class InteractiveModeFunctions {

    /*файл, который хранит deque*/
    private static File filePath = null;
    private static int lineNumber = 1;

    /**
     * Removes employee from deque
     *
     * @param deque    ArrayDeque
     * @param employee Employee, that gotta be removed from deque
     */
    protected static void remove(ArrayDeque<Employee> deque, Employee employee) {
        deque.remove(employee);
        System.out.println("First employee, which you typed, removed");
    }

    /**
     * Removes all employees from deque, which are lower(compares .toStrings), than this employee
     *
     * @param deque    ArrayDeque
     * @param employee Employee
     */
    protected static void remove_lower(ArrayDeque<Employee> deque, Employee employee) {
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
     * @param employee Employee
     */
    protected static void remove_all(ArrayDeque<Employee> deque, Employee employee) {
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

    protected static void add(ArrayDeque<Employee> deque, Employee employee) {
        deque.add(employee);
    }

    protected static void show(ArrayDeque<Employee> deque) {
        System.out.println("Current employees now are the same with the file:");
        if (!deque.isEmpty()) {
            for (Employee aDeque : deque) {
                System.out.println(aDeque);
            }
        } else System.out.println("empty");
        System.out.println();
    }

    public static void setFilePath(File filePath) {
        //if (filePath)
            InteractiveModeFunctions.filePath = filePath;
    }

    public static File getFilePath() {
        return filePath;
    }

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
        return (lineNumber);
    }
}
