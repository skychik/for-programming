/*
 *
 * Саша: взаимодействие с файлом
 *
 * Кирилл: работа в интерактивном режиме
 *
 */
package ru.ifmo.cs.programming.lab5;

import ru.ifmo.cs.programming.lab5.core.InteractiveModeFunctions;
import ru.ifmo.cs.programming.lab5.domain.Employee;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Scanner;
import java.util.regex.Pattern;

public class App extends InteractiveModeFunctions {

    /*это наш дек*/
    private static ArrayDeque<Employee> deque = new ArrayDeque<>();
    //for testing(changes to new FileReader(testingDir + "\\input.txt"))
    private static InputStreamReader inputStreamReader = new InputStreamReader(System.in);

    public static void main(String[] args) {

        //i'm not sure, that the Pattern has to be that big, but it works
        setScanner(new Scanner(inputStreamReader).useDelimiter(Pattern.compile("[\\p{Space}\\r\\n\\u0085\\u2028\\u2029\\u0004]")));
        try {
            if (getFilePath() == null)
                setFilePath(new File(System.getenv("EmployeeFile")));
        } catch (NullPointerException e) {
            System.out.println("Не существует переменной окружения EmployeeFile.");
            System.exit(1);
        }

        //First loading of the deque from our File
        load(deque);

        //save deque after every shutdown
        Runtime.getRuntime().addShutdownHook(new Thread(() -> save(deque)));

        /*This is an interactive mode:*/
        interactiveMode();

        try {
            inputStreamReader.close();

        } catch (IOException e) {
            System.out.println("Can't close input stream");
            System.exit(1);
        }
    }

    private static void interactiveMode() {
        String command = null;

        System.out.println("** Type '--help' to see all commands\n" +
                "** To stop this program, type 'end'\n");
        intMode:
        while (true) {
            System.out.print("Type your command:\n");
            if (getScanner().hasNextLine()) {
                command = getScanner().next();
            } else {
                System.exit(0);
            }
                //System.out.println('\"' + command + '\"');

            switch (command) {
                case "--help":
                    help();
                    break;
                case "add":
                    add(deque);
                    break;
                case "remove":
                    remove(deque);
                    break;
                case "remove_lower":
                    remove_lower(deque);
                    break;
                case "remove_all":
                    remove_all(deque);
                    break;
                case "save":
                    new Thread(()->save(deque)).run();
                    try {
                        Thread.currentThread().sleep(500);//TODO what for
                    } catch (InterruptedException ignored) {}
                    break;
                case "load":
                    new Thread(()->load(deque)).run();
                    try {
                        Thread.currentThread().sleep(500);
                    } catch (InterruptedException ignored) {}
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

    static void setInputStreamReader(InputStreamReader inputStreamReader) {
        App.inputStreamReader = inputStreamReader;
    }
}

