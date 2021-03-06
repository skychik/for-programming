package ru.ifmo.cs.programming.lab7.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.ifmo.cs.programming.lab5.domain.Employee;

import java.io.File;
import java.util.Scanner;

public class EmployeeService {

    /*файл, который хранит deque*/
    private static File filePath = null;
    private static int lineNumber = 1;
    private static String obj;
    private static Employee employee;
    private static Scanner scanner;
    //for working with Gson library
    private static Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
    private static final EmployeeService instance = new EmployeeService();

    public static EmployeeService getInstance() {
        return instance;
    }

//    public void help() {
//        System.out.println("** You can use commands:\n" +
//                "add {}, remove {}, remove_lower {}, remove_all {}, save, load, show, end\n");
//    }

    public Employee getEmployee() {
        return new Employee();
    }

/*    public void addEmployee(Employee employee) {

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

        System.out.println("добавлено");
    }

    public void updateEmployee(Employee employee) {
        return;
    }

    public void deleteEmployee(Employee employee) {
        obj = jsonObject(scanner);
        if (obj == null) return;//if incorrect input (more closing brackets than opening)
        //System.out.println(obj);
        employee = gson.fromJson(obj, Employee.class);
        //assert employee.equals(new Employee("Sasha", "programmer", 1, LOW, (byte) 4)) :
        //        employee.toString();
        deque.remove(employee);
        System.out.println("First met employee, which you typed, removed");
    }*/

//    public void remove_lower(ArrayDeque<Employee> deque) {
//        obj = jsonObject(scanner);
//        if (obj == null) return;//if incorrect input (more closing brackets than opening)
//        //System.out.println(obj);
//        employee = gson.fromJson(obj, Employee.class);
//        Arrays.sort(deque.toArray(new Employee[0]));
//        while (!deque.isEmpty() && (employee.compareTo(deque.peekFirst())) > 0) {
//            deque.removeFirst();
//        }
//        System.out.println("Removed all employees, which are lower, than your typed employee");
//    }
//
//    public void deleteAllEmployees(ArrayDeque<Employee> deque) {
//        obj = jsonObject(scanner);
//        if (obj == null) return;//if incorrect input (more closing brackets than opening)
//        //System.out.println(obj);
//        employee = gson.fromJson(obj, FactoryWorker.class);
//        //ArrayDeque<Employee> arrayDeque = new ArrayDeque<>();
//        //arrayDeque.add(new FactoryWorker("Pasha", "programmer", 18, NORMAL, (byte) 21));
//
//        //assert (emp instanceof FactoryWorker);
//
//        //assert emp.equals(arrayDeque.peekFirst()) :
//        //        arrayDeque.peekFirst().toString() +
//        //                '\n' +
//        //                emp.toString();
//        ArrayDeque<Employee> anotherDeque = new ArrayDeque<>();
//        /*
//          Puts all Employees < employee in anotherDeque
//         */
//        while (!deque.isEmpty() && (employee.compareTo(deque.peekFirst()) > 0))
//            anotherDeque.addFirst(deque.pollFirst());
//        /*
//          Removes all Employees == employee from deque
//         */
//        while (!deque.isEmpty() && (employee.compareTo(deque.peekFirst()) == 0))
//            deque.removeFirst();
//        /*
//          Puts all Employees from anotherDeque back in deque
//         */
//        while (!anotherDeque.isEmpty())
//            deque.addFirst(anotherDeque.pollFirst());
//        System.out.println("All employees, which are the same with your typed employee, removed from deque");
//    }
//
////    public void load(ArrayDeque<Employee> deque) {
////
////        BufferedReader reader = null;
////        String line;
////
////        try {
////            reader = new BufferedReader(new FileReader(getFilePath()));
////        } catch (FileNotFoundException e) {
////            System.out.println("Невозможно считать данные из файла по пути: " + getFilePath());
////            System.exit(1);
////        } catch (NullPointerException e) {
////            System.out.println("Не существует переменной окружения EmployeeFile.");
////            System.exit(1);
////        }
////
////        ArrayDeque<Employee> bufferedDeque = deque;
////        deque.clear();
////
////        try {
////            while ((line = reader.readLine()) != null) {
////                Employee employee = parseEmployee(line);
////                deque.add(employee);
////                incLineNumber();
////            }
////        } catch (IOException e){
////            deque = bufferedDeque;
////            System.out.println("Cannot read from file");
////            System.exit(1);
////        }
////
////        show(deque);
////
////        try {
////            reader.close();
////        } catch (IOException e) {
////            System.out.println("Can't close input thread");
////            System.exit(1);
////        }
////    }
////
////    public void save(ArrayDeque<Employee> deque) {
////        ArrayDeque<Employee> bufferedDeque = deque.clone();
////
////        SaveDequeThread thread = new SaveDequeThread(bufferedDeque);
////        thread.start();
////        System.out.println("Deque saved.");
////    }
////
////    public void show(ArrayDeque<Employee> deque) {
////        System.out.println("Current employees now are(might be not saved):\n");
////        if (!deque.isEmpty()) {
////            for (Employee employee : deque) {
////                System.out.println(employee);
////            }
////        } else System.out.println("empty");
////        System.out.println();
////    }
//
//    /**
//     * Returns String, which contains object in json format
//     *
//     * @param scanner has thread from System.in
//     * @return String which contains object in json format
//     */
//    private static String jsonObject(Scanner scanner) {
//
//        int numberOfOpeningBrackets = 0;
//        int numberOfClosingBrackets = 0;
//        StringBuilder obj = new StringBuilder();
//        String line;
//
//        while (true) {
//            line = scanner.nextLine();
//            obj.append(line.trim());
//
//            if ((obj.length() != 0) && (obj.charAt(0) != '{')) {
//                System.out.println("Incorrect json format. You typed: \'" + obj + "\'");
//                return null;
//            }
//
//            //countNumberOfBrackets
//            for (int pos = 0; pos < line.length(); ++pos) {
//                char ch = line.charAt(pos);
//
//                // здесь делаем что хотим с символом
//                switch (ch) {
//                    case '{':
//                        numberOfOpeningBrackets++;
//                        break;
//                    case '}':
//                        numberOfClosingBrackets++;
//                        break;
//                }
//            }
//
//            if ((numberOfOpeningBrackets == 0) && (numberOfClosingBrackets == 0))
//                continue;
//            if (numberOfOpeningBrackets == numberOfClosingBrackets) {
//                if ((obj.length() != 0) && (obj.charAt(obj.length() - 1) != '}')) {
//                    System.out.println("Incorrect json format. You typed: \'" + obj + "\'");
//                    return null;
//                }
//                return obj.toString();
//            }
////                /*//Gson should do this itself
////                if (line.trim().toCharArray()[line.length() - 1] == '}')
////                    return;
////                else {
////                    System.out.println("Wrong input format: didn't end with '}'");
////                    obj = null;
////                    return;
////                }*/
//            if (numberOfOpeningBrackets < numberOfClosingBrackets) {
//                System.out.println(
//                        "Number of closing figure brackets can't be more, than number of opening figure brackets");
//                return null;
//            }
//        }
//    }
//
//    public static void setFilePath(File filePath) {
//        EmployeeService.filePath = filePath;}
//
//    public static File getFilePath() { return filePath; }
//
//    /**
//     * Метод, осуществляющий увеличение на единицу номера считываемой строки
//     *
//     * @author Zhurbova A.E.
//     */
//    private static void incLineNumber() {
//        lineNumber++;
//    }
//
//    /**
//     * Метод, возвращающий номер считываемой строки
//     *
//     * @author Zhurbova A.E.
//     */
//    public static int getLineNumber() {
//        return lineNumber;
//    }
//
//    protected static Scanner getScanner() {
//        return scanner;
//    }
//
//    public static void setScanner(Scanner scanner) {
//        EmployeeService.scanner = scanner;
//    }
}
