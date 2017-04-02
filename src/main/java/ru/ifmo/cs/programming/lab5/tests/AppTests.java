package ru.ifmo.cs.programming.lab5.tests;


import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Test;
import ru.ifmo.cs.programming.lab5.App;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class AppTests {
    //dir - current directory
    private static String dir = System.getProperty("user.dir") + "/src/main/java/ru/ifmo/cs/programming/lab5/tests";

    //how many tests
    private static int numberOfTests = 1;

    @Test(timeout = 5000)
    public void test1() throws Exception {
        for (int i = 1; i <= numberOfTests; i++) {
            testWithFiles(i);
        }
    }

    private void testWithFiles(int i) throws Exception {
        //copy input and EmployeeFile
        FileUtils.copyFile(new File(dir, "input" + i + ".txt"), new File(dir, "input.txt"));
        App.setInputStreamReader(new FileReader(dir + "\\input.txt"));
        FileUtils.copyFile(new File(dir, "EmployeeFileTest" + i + "Input.csv"), new File(dir, "EmployeeFile.csv"));
        App.setFilePath(new File(dir, "EmployeeFile.csv"));

        //System.out.println(new Scanner(new FileReader(dir + "\\input.txt")).next());
        App.main(new String[0]);

        assert new File(dir, "EmployeeFile.csv") == new File(dir, "EmployeeFileTest" + i + "Output.csv");
    }

    @After
    public void after() throws IOException {
        //Files.delete(new File(dir, "input.txt").toPath());
        //Files.delete(new File(dir, "EmployeeFile.csv").toPath());
    }
}
