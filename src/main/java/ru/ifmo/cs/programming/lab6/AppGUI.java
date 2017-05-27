package ru.ifmo.cs.programming.lab6;

import ru.ifmo.cs.programming.lab5.core.InteractiveModeFunctions;
import ru.ifmo.cs.programming.lab5.domain.Employee;
import ru.ifmo.cs.programming.lab5.utils.SaveDequeThread;
import ru.ifmo.cs.programming.lab6.core.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Scanner;
import java.util.regex.Pattern;

import static ru.ifmo.cs.programming.lab5.core.InteractiveModeFunctions.*;

public class AppGUI {

    /*это наш дек*/
    private static ArrayDeque<Employee> deque = new ArrayDeque<>();
    //for testing(changes to new FileReader(testingDir + "\\input.txt"))
    private static InputStreamReader inputStreamReader = new InputStreamReader(System.in);

    private static JFrame frame;

    public static void main(String... args) {
        //i'm not sure, that Pattern has to be that big, but it works
        InteractiveModeFunctions.setScanner(new Scanner(inputStreamReader).useDelimiter(Pattern.compile(
                "[\\p{Space}\\r\\n\\u0085\\u2028\\u2029\\u0004]")));
        try {
            if (getFilePath() == null)
                setFilePath(new File(System.getProperty("user.dir") + "/src/resources/text_files/EmployeeFile.csv"));
        } catch (NullPointerException e) {
            System.out.println("Не существует переменной окружения EmployeeFile.");
            System.exit(1);
        }

        //First loading of the deque from our File
        load(deque);

        //Save deque after every shutdown
        Runtime.getRuntime().addShutdownHook(new SaveDequeThread(deque));

        //GUI
        SwingUtilities.invokeLater(AppGUI::gui);

        //Close input stream reader
        try {
            inputStreamReader.close();
        } catch (IOException e) {
            System.out.println("Can't close input stream");
            System.exit(1);
        }
    }

    public static void gui() {
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

        frame = new MainFrame(deque);

        frame.setLocationRelativeTo(null);//по центру экрана
        frame.setResizable(false);

        frame.pack();
        frame.setVisible(true);
    }

    public static void setInputStreamReader(InputStreamReader inputStreamReader) {
        AppGUI.inputStreamReader = inputStreamReader;
    }

    public static ArrayDeque<Employee> getDeque() {
        return deque;
    }

    public static JFrame getFrame() {
        return frame;
    }
}
