package ru.ifmo.cs.programming.lab6;

import ru.ifmo.cs.programming.lab5.core.InteractiveModeFunctions;
import ru.ifmo.cs.programming.lab5.domain.Employee;
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

public class App {

    /*это наш дек*/
    private static ArrayDeque<Employee> deque = new ArrayDeque<>();
    //for testing(changes to new FileReader(testingDir + "\\input.txt"))
    private static InputStreamReader inputStreamReader = new InputStreamReader(System.in);
    public static Color defEighthAlphaColor = new Color(152, 156, 153, 32);
    public static Color defHalfAlphaColor = new Color(152, 156, 153, 128);
    public static Color defColor = new Color(152, 156, 153);

    public static void main(String... args) {
        //i'm not sure, that Pattern has to be that big, but it works
        InteractiveModeFunctions.setScanner(new Scanner(inputStreamReader).useDelimiter(Pattern.compile("[\\p{Space}\\r\\n\\u0085\\u2028\\u2029\\u0004]")));
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
        Runtime.getRuntime().addShutdownHook(new Thread(() -> save(deque)));

        //GUI
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, you can set the GUI to another look and feel.
        }

        SwingUtilities.invokeLater(App::gui);

        //Close input stream reader
        try {
            inputStreamReader.close();
        } catch (IOException e) {
            System.out.println("Can't close input stream");
            System.exit(1);
        }
    }

    private static void gui() {
        JFrame f = new MainFrame(deque);

        /*Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int locationX = (screenSize.width - f.getSize().width) / 2;
        int locationY = (screenSize.height - f.getSize().height) / 2;
        f.setBounds(locationX, locationY, f.getSize().width, f.getSize().height);//по центру экрана*/
        f.setLocationRelativeTo(null);//вроде тоже самое

        f.pack();
        f.setVisible(true);
        f.setMaximumSize(new Dimension(1000, 1000));
    }

    static void setInputStreamReader(InputStreamReader inputStreamReader) {
        App.inputStreamReader = inputStreamReader;
    }

    public static ArrayDeque<Employee> getDeque() {
        return deque;
    }
}
