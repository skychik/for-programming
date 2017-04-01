package ru.ifmo.cs.programming.lab5.tests;


import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.rules.Timeout;
import ru.ifmo.cs.programming.lab5.App;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.Scanner;

public class AppTests {
    private static String dir;

    @Test(timeout = 5000)
    public void test1() throws Exception {
        //dir - current directory
        dir = System.getProperty("user.dir") + "/src/main/java/ru/ifmo/cs/programming/lab5/tests";
        //copy input and EmployeeFile
        FileUtils.copyFile(new File(dir, "input1.txt"), new File(dir, "input.txt"));
        App.setInputStreamReader(new FileReader(dir + "\\input.txt"));
        FileUtils.copyFile(new File(dir, "EmployeeFileTest1Input.csv"), new File(dir, "EmployeeFile.csv"));
        App.setFilePath(new File(dir, "EmployeeFile.csv"));

        System.out.println(new Scanner(new File(dir, "input.txt")).next());
        App.main(new String[0]);

        assert new File(dir, "EmployeeFile.csv") == new File(dir, "EmployeeFileTest1Output.csv");
    }

    @After
    public void after() throws IOException {
        //Files.delete(new File(dir, "input.txt").toPath());
        //Files.delete(new File(dir, "EmployeeFile.csv").toPath());
    }
}
