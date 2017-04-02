package ru.ifmo.cs.programming.lab5;


import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class AppTests {
    /**
     * testingDir - current directory
     */
    private static String testingDir = System.getProperty("user.dir") + "/src/test/java/ru/ifmo/cs/programming/lab5";

    /**
     * how many tests
     */
    private static int numberOfTests = 1;

    @Test(timeout = 5000)
    public void test1() throws Exception {
        for (int i = 1; i <= numberOfTests; i++) {
            testWithFiles(i);
        }
    }

    private void testWithFiles(int testNumber) throws Exception {
        //copy input and EmployeeFile
        FileUtils.copyFile(new File(testingDir, "Test" + testNumber + "input.txt"), new File(testingDir, "input.txt"));
        App.setInputStreamReader(new FileReader(testingDir + "\\input.txt"));
        FileUtils.copyFile(new File(testingDir, "Test" + testNumber + "EmployeeFileInput.csv"),
                new File(testingDir, "EmployeeFile.csv"));
        App.setFilePath(new File(testingDir, "EmployeeFile.csv"));

        //System.out.println(new Scanner(new FileReader(testingDir + "\\input.txt")).next());
        App.main(new String[0]);

        assert new File(testingDir, "EmployeeFile.csv") ==
                new File(testingDir, "Test" + testNumber + "EmployeeFileOutput.csv");
    }

    @After
    public void after() throws IOException {
        //Files.delete(new File(testingDir, "input.txt").toPath());
        //Files.delete(new File(testingDir, "EmployeeFile.csv").toPath());
    }
}
