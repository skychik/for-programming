package ru.ifmo.cs.programming.lab5.test;


import org.junit.Before;
import org.junit.Test;
import ru.ifmo.cs.programming.lab5.App;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class AppTests {

    @Test
    public void test1() throws FileNotFoundException {
        App.setInputStreamReader(new FileReader("input1.txt"));
        App.setFilePath(new File("EmployeeFileTest1Input"));


    }
}
