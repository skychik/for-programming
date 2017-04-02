package ru.ifmo.cs.programming.lab5.tests;


import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Test;
import ru.ifmo.cs.programming.lab5.App;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class AppTests {
    //testingDir - current directory
    private static String testingDir = System.getProperty("user.testingDir") + "/src/main/java/ru/ifmo/cs/programming/lab5/tests";

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
        FileUtils.copyFile(new File(testingDir, "input" + i + ".txt"), new File(testingDir, "input.txt"));
        App.setInputStreamReader(new FileReader(testingDir + "\\input.txt"));
        FileUtils.copyFile(new File(testingDir, "EmployeeFileTest" + i + "Input.csv"), new File(testingDir, "EmployeeFile.csv"));
        App.setFilePath(new File(testingDir, "EmployeeFile.csv"));

        //System.out.println(new Scanner(new FileReader(testingDir + "\\input.txt")).next());
        App.main(new String[0]);

        assert new File(testingDir, "EmployeeFile.csv") == new File(testingDir, "EmployeeFileTest" + i + "Output.csv");
    }

    @After
    public void after() throws IOException {
        //Files.delete(new File(testingDir, "input.txt").toPath());
        //Files.delete(new File(testingDir, "EmployeeFile.csv").toPath());
    }
}
