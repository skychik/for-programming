package ru.ifmo.cs.programming.lab5;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Test;

import java.io.*;

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

        //System.out.println(new Scanner(new FileReader(testingDir + "\\input.txt")).next());
        App.main(new String[0]);

        InputStream fileReader1 = new FileInputStream(new File(testingDir, "EmployeeFile.csv"));
        InputStream fileReader2 = new FileInputStream(new File(testingDir, "Test" + testNumber + "EmployeeFileOutput.csv"));

        //char[] c1 = fileReader1.
        //int lenght1 = fileReader1.read(c1);

        assert new FileReader(new File(testingDir, "EmployeeFile.csv")).equals(//TODO поправить
                new FileReader(new File(testingDir, "Test" + testNumber + "EmployeeFileOutput.csv")));
    }

    @After
    public void after() {
        //Files.delete(new File(testingDir, "input.txt").toPath());
        //Files.delete(new File(testingDir, "EmployeeFile.csv").toPath());
    }
}
