package ru.ifmo.cs.programming.lab5.utils;

import ru.ifmo.cs.programming.lab5.domain.Employee;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayDeque;

import static ru.ifmo.cs.programming.lab5.core.InteractiveModeFunctions.getFilePath;

public class SaveDequeThread extends Thread {
    public SaveDequeThread(ArrayDeque<Employee> deque) {
        super(new Runnable() {
            PrintWriter writer;

            @Override
            public void run() {
                try {
                    writer = new PrintWriter(getFilePath());

                    for (Employee employee : deque) {
                        writer.println(employee);
                    }

                    writer.close();
                } catch (FileNotFoundException e) {
                    System.out.println("Невозможно произвести запись в файл по пути: " + getFilePath());
                }
            }
        });
    }
}