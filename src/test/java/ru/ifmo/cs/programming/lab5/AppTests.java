package ru.ifmo.cs.programming.lab5;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class AppTests extends App{

    /**
     * testingDir - directory with this class
     */
    private static String testingDir = System.getProperty("user.dir") + "/src/test/java/ru/ifmo/cs/programming/lab5";

    @Test(timeout = 5000)
    public void test1() throws IOException {
        int numberOfTests = 1;

        for (int i = 1; i <= numberOfTests; i++) {
            testWithFiles(i);
        }
    }

    private void testWithFiles(int testNumber) throws IOException {

        //copy input and EmployeeFile
        FileUtils.copyFile(new File(testingDir, "Test" + testNumber + "input.txt"), new File(testingDir, "input.txt"));
        App.setInputStreamReader(new FileReader(testingDir + "\\input.txt"));
        FileUtils.copyFile(new File(testingDir, "Test" + testNumber + "EmployeeFileInput.csv"),
                new File(testingDir, "EmployeeFile.csv"));
        App.setFilePath(new File(testingDir, "EmployeeFile.csv"));

        App.main(new String[0]);

        assert compareFilesContent(testingDir + "/EmployeeFile.csv", 
                testingDir + "/Test" + testNumber + "EmployeeFileOutput.csv");
    }

    @After
    public void after() {
        //Files.delete(new File(testingDir, "input.txt").toPath());
        //Files.delete(new File(testingDir, "EmployeeFile.csv").toPath());
    }

    private boolean compareFilesContent(String  f1, String f2) throws IOException{
        FileInputStream input1 = new FileInputStream(f1);
        FileInputStream input2 = new FileInputStream(f2);

        byte[] buffer1 = new byte[input1.available()];
        byte[] buffer2 = new byte[input2.available()];
        input1.read(buffer1, 0, input1.available());
        input2.read(buffer2, 0, input2.available());

        return Arrays.equals(buffer1, buffer2);
    }
}
