package ru.ifmo.cs.programming.lab5.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import ru.ifmo.cs.programming.lab5.domain.Employee;
import ru.ifmo.cs.programming.lab5.utils.FactoryWorker;

import java.io.*;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Scanner;

import static ru.ifmo.cs.programming.lab5.domain.Employee.parseEmployee;

public class InteractiveModeFunctionsImpl implements InteractiveModeFunctions {

    private ArrayDeque<Employee> deque = new ArrayDeque<>();
    /*файл, который хранит deque*/
    private static File filePath = null;
    private static int lineNumber = 1;
    private Scanner scanner;
    //for working with Gson library
    private Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
//    private static final InteractiveModeFunctionsImpl instance = new InteractiveModeFunctionsImpl();
//
//    public static InteractiveModeFunctionsImpl getInstance() {
//        return instance;
//    }

    protected void help() {
        System.out.println("** You can use commands:\n" +
                "add {}, remove {}, remove_lower {}, remove_all {}, save, load, show, end\n");
    }

    public void add() {
        String obj = jsonObject(scanner);

        if (obj == null) return;//if incorrect input (more closing brackets than opening)
        //System.out.println(obj);

	    Employee employee;
	    try {
		    employee = gson.fromJson(obj, Employee.class);
	    } catch (JsonSyntaxException e) {
		    System.out.println(e.getMessage());
		    return;
	    }

        add(employee);
    }

    public void add(Employee employee) {
	    deque.add(employee);

	    System.out.println("добавлено");
    }

    public void remove() {
        String obj = jsonObject(scanner);
        if (obj == null) return;//if incorrect input (more closing brackets than opening)
        //System.out.println(obj);
        Employee employee = gson.fromJson(obj, Employee.class);
        //assert employee.equals(new Employee("Sasha", "programmer", 1, LOW, (byte) 4)) :
        //        employee.toString();
        remove(employee);
    }

    public void remove(Employee employee) {
	    deque.remove(employee);
	    System.out.println("First met employee, which you typed, removed");
    }

    protected void remove_lower() {
        String obj = jsonObject(scanner);
        if (obj == null) return;//if incorrect input (more closing brackets than opening)
        //System.out.println(obj);
        Employee employee = gson.fromJson(obj, Employee.class);
        Arrays.sort(deque.toArray(new Employee[0]));
        while (!deque.isEmpty() && (employee.compareTo(deque.peekFirst())) > 0) {
            deque.removeFirst();
        }
        System.out.println("Removed all employees, which are lower, than your typed employee");
    }

    protected void remove_all() {
        String obj = jsonObject(scanner);
        if (obj == null) return;//if incorrect input (more closing brackets than opening)
        //System.out.println(obj);
        Employee employee = gson.fromJson(obj, FactoryWorker.class);
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

    public void clear() {
    	deque.clear();
	    System.out.println("Deque has cleared");
    }

    protected void load() {

        BufferedReader reader = null;
        String line;

        try {
            reader = new BufferedReader(new FileReader(getFilePath()));
        } catch (FileNotFoundException e) {
            System.out.println("Невозможно считать данные из файла по пути: " + getFilePath());
            System.exit(1);
        } catch (NullPointerException e) {
            System.out.println("Не существует переменной окружения EmployeeFile.");
            System.exit(1);
        }

        ArrayDeque<Employee> bufferedDeque = deque.clone();
        deque.clear();

        try {
            while ((line = reader.readLine()) != null) {
                Employee employee = parseEmployee(line);
                deque.add(employee);
                incLineNumber();
            }
        } catch (IOException e){
            deque = bufferedDeque;
            System.out.println("Cannot read from file");
            System.exit(1);
        }

        show();

        try {
            reader.close();
        } catch (IOException e) {
            System.out.println("Can't close input thread");
            System.exit(1);
        }
    }

    public void save() {
        ArrayDeque<Employee> clone = deque.clone();

	    Thread t = new Thread(() -> {
		    try {
			    PrintWriter writer = new PrintWriter(getFilePath());

			    for (Employee employee : clone) {
				    writer.println(employee);
			    }

			    writer.close();
			    System.out.println("Deque saved.");
		    } catch (FileNotFoundException e) {
			    System.out.println("Невозможно произвести запись в файл по пути: " + getFilePath());
		    }
	    });
	    t.start();/*
	    SaveDequeThread thread = new SaveDequeThread(clone);
        thread.start();*/
    }

	@Override
	public int getSize() {
		return deque.size();
	}

	@Override
	public Employee[] getEmployees() {
		return (Employee[]) deque.toArray();
	}

	protected void show() {
        System.out.println("Current employees now are(might be not saved):\n");
        if (!deque.isEmpty()) {
            for (Employee employee : deque) {
                System.out.println(employee);
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
    private String jsonObject(Scanner scanner) {

        int numberOfOpeningBrackets = 0;
        int numberOfClosingBrackets = 0;
        StringBuilder obj = new StringBuilder();
        String line;

        while (true) {
            line = scanner.nextLine();
            obj.append(line.trim());

            if ((obj.length() != 0) && (obj.charAt(0) != '{')) {
                System.out.println("Incorrect json format. You typed: \'" + obj + "\'");
                return null;
            }

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
            if (numberOfOpeningBrackets == numberOfClosingBrackets) {
                if ((obj.length() != 0) && (obj.charAt(obj.length() - 1) != '}')) {
                    System.out.println("Incorrect json format. You typed: \'" + obj + "\'");
                    return null;
                }
                return obj.toString();
            }
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

    public static void setFilePath(File filePath) {
        InteractiveModeFunctionsImpl.filePath = filePath;}

    protected static File getFilePath() { return filePath; }

    /**
     * Метод, осуществляющий увеличение на единицу номера считываемой строки
     *
     * @author Zhurbova A.E.
     */
    private void incLineNumber() {
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

    protected Scanner getScanner() {
        return scanner;
    }

    protected void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }
}
