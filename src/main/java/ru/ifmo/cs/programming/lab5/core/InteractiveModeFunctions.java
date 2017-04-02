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
    private static File filePath = new File(System.getenv("EmployeeFile"));
    private static int lineNumber = 1;

    /**
     * Removes employee from deque
     *
     * @param deque    ArrayDeque
     * @param employee Employee, that gotta be removed from deque
     */
    protected static void remove(ArrayDeque<Employee> deque, Employee employee) {
        deque.remove(employee);
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
    }

    /**
     * Метод, осуществляющий загрузку коллекции из файла
     *
     * @param deque - коллекция, в которую происходит запись
     * @author Zhurbova A.E.
     */
    protected static void load(ArrayDeque<Employee> deque) throws IOException {
        BufferedReader reader;
        String line;

        try {
            reader = new BufferedReader(new FileReader(filePath));
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("Указанного файла не существует. ");
        } catch (NullPointerException e) {
            throw new NullPointerException("Не существует переменной окружения EmployeeFile.");
        }

        while ((line = reader.readLine()) != null) {
            Employee employee = parseEmployee(line);
            deque.add(employee);
            incLineNumber();
        }

        System.out.println("Состояние очереди после считывания:");
        for (Employee aDeque : deque) {
            System.out.println(aDeque);
        }
        System.out.println();
    }

    /**
     * Метод, осуществляющий запись коллекции в файл
     *
     * @param deque - коллекция, из которой считываются данные
     * @author Zhurbova A.E.
     */
    //todo нужна стабильность
    protected static void save(ArrayDeque<Employee> deque) throws IOException {
        PrintWriter writer = new PrintWriter(filePath);

        // Стандартные настройки (кодировка, переносы строк, разделители и т.д.)
        ICsvBeanWriter csvBeanWriter = new CsvBeanWriter(writer, CsvPreference.STANDARD_PREFERENCE);

        //todo не доделано
        //for (Employee employee : deque) {
            //Вывод считанной коллекции
            for (Object aDeque : deque) {
                System.out.println(aDeque);
            }
        //}

        csvBeanWriter.close();

        //Проверка записанного в файл содержимого, посредством вывода на экран
        System.out.println(writer.toString());
    }

    private static File getFilePath() {
        return filePath;
    }

    private static void setFilePath(String filePath) {
        try {
            InteractiveModeFunctions.filePath = new File(filePath);
        } catch (NullPointerException e) {
            throw new NullPointerException("Environment variable is null. Set it");
        }
    }

    public static void setFilePath(File filePath) {
        InteractiveModeFunctions.filePath = filePath;
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
