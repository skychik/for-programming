package ru.ifmo.cs.programming.lab6;

import ru.ifmo.cs.programming.lab5.core.InteractiveModeFunctions;
import ru.ifmo.cs.programming.lab5.core.InteractiveModeFunctionsImpl;
import ru.ifmo.cs.programming.lab5.domain.Employee;
import ru.ifmo.cs.programming.lab6.core.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Scanner;
import java.util.regex.Pattern;

public class AppGUI extends InteractiveModeFunctionsImpl {

    //for testing(changes to new FileReader(testingDir + "\\input.txt"))
    private InputStreamReader inputStreamReader = new InputStreamReader(System.in);
    private static MainFrame frame;

    public static void main(String... args) {
        new AppGUI();
    }

    private AppGUI() {
        //i'm not sure, that Pattern has to be that big, but it works
        setScanner(new Scanner(inputStreamReader).useDelimiter(Pattern.compile(
                "[\\p{Space}\\r\\n\\u0085\\u2028\\u2029\\u0004]")));
        try {
            if (getFilePath() == null)
                setFilePath(new File(System.getProperty("user.dir") + "/src/resources/text_files/EmployeeFile.csv"));
        } catch (NullPointerException e) {
            System.out.println("Не существует переменной окружения EmployeeFile.");
            System.exit(1);
        }

        //First loading of the deque from our File
        load();

        //Save deque after every shutdown
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
	        try {
		        PrintWriter writer = new PrintWriter(getFilePath());

		        for (Employee employee : getEmployees()) {
			        writer.println(employee);
		        }

		        writer.close();
		        System.out.println("Deque saved.");
	        } catch (FileNotFoundException e) {
		        System.out.println("Невозможно произвести запись в файл по пути: " + getFilePath());
	        }
        }));

        //GUI
        SwingUtilities.invokeLater(() -> gui(this));

        //Close input stream reader
        try {
            inputStreamReader.close();
        } catch (IOException e) {
            System.out.println("Can't close input stream");
            System.exit(1);
        }
    }

    public static void gui(InteractiveModeFunctions imf) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    UIManager.getLookAndFeelDefaults().put(
                            "ScrollPane.contentMargins",
                            new Insets (0,0,0,0));
                    UIManager.getLookAndFeelDefaults().put(
                            "TableHeader:\"TableHeader.renderer\".contentMargins",
                            new Insets (0,0,0,0));
                    UIManager.getLookAndFeelDefaults().put(
                            "ScrollPane[Enabled].borderPainter", null);
                    UIManager.getLookAndFeelDefaults().put(
                            "ScrollPane[Enabled+Focused].borderPainter", null);

                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Nimbus is not available, you can set the GUI to another look and feel.");
        }

	    frame = new MainFrame(imf);

        frame.setLocationRelativeTo(null);//по центру экрана
        frame.setResizable(false);

        frame.pack();
        frame.setVisible(true);
    }

    public void setInputStreamReader(InputStreamReader inputStreamReader) {
        this.inputStreamReader = inputStreamReader;
    }

    public static JFrame getFrame() {
        return frame;
    }
}
