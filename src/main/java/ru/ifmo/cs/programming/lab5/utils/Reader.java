package ru.ifmo.cs.programming.lab5.utils;

import ru.ifmo.cs.programming.lab5.domain.Employee;
import ru.ifmo.cs.programming.lab5.domain.ShopAssistant;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Scanner;

//todo зачем он
public class Reader {


    public Reader(ArrayDeque<Employee> deque, String filePath) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                Scanner sc = new Scanner(line);
                sc.useDelimiter(",");
                int index = 0;
                String className = sc.next();
                switch (className) {
                    case "FactoryWorker": {
                        FactoryWorker fw = new FactoryWorker();
                        while (sc.hasNext()) {
                            for (index++; index != 6; ) {//todo передавать весь FactoryWorker и сканер в метод
                                fw.parseEmployee(sc.next(), index, lineNumber);
                            }
                        }
                        if (index < 5) {
                            System.out.println("Заданы не все параметры в строке " + lineNumber);
                            System.exit(0);
                        }
                        while (sc.hasNext()) {
                            fw.parseFactoryWorker(sc.next(), index, lineNumber);
                        }
                        deque.add(fw);
                        break;
                    }
                    case "ShopAssistant": {
                        ShopAssistant shAs = new ShopAssistant();
                        for (index++; index != 6; ) {//todo передавать весь ShopAssistant и сканер в метод
                            shAs.parseEmployee(sc.next(), index, lineNumber);
                        }
                        if (index != 5) {
                            System.out.println("Неверное количество параметров в строке " + lineNumber);
                            System.exit(0);
                        }
                        deque.add(shAs);
                        break;
                    }
                    case "Employee": {
                        Employee emp = new Employee();
                        for (index++; index != 6; ) { //todo передавать весь Employee и сканер в метод
                            emp.parseEmployee(sc.next(), index, lineNumber);
                        }
                        if (index != 5) {
                            System.out.println("Неверное количество параметров в строке " + lineNumber);
                            System.exit(0);
                        }
                        deque.add(emp);
                        break;
                    }
                    default: {
                        System.out.println("Приложение не обрабатывает указанный класс.");
                        System.exit(0);
                    }
                }
                lineNumber++;
            }
        } catch (FileNotFoundException e){
            System.out.println("Указанного файла не существует.");
        } catch (IOException e) {
            System.out.println("Ничего не задано.");
        }
    }
}