package ru.ifmo.cs.programming.lab7;

import ru.ifmo.cs.programming.lab7.core.MyHashtable;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Hashtable;

import static ru.ifmo.cs.programming.lab6.AppGUI.getFrame;
import static ru.ifmo.cs.programming.lab6.AppGUI.gui;

public class MyClient extends Thread {
    private static Socket socket = null;

    public static void main(String args[]) {
        if (args.length > 0) {
            if (args.length == 1) {
                new MyClient(args[0]);
            } else {
                new MyClient(args[0], Integer.parseInt(args[1]));
            }
        } else {
            new MyClient();
        }
    }

    private MyClient(String host, int port) {
        /* Создаем сокет для полученной пары хост/порт */
        try {
            // открываем сокет и коннектимся к host:port, получаем сокет сервера
            socket = new Socket(host, port);
        } catch (UnknownHostException e) {
            System.out.println("Неизвестный хост: " + host);
            System.exit(-1);
        } catch (IOException e) {
            System.out.println("Ошибка ввода/вывода при создании сокета " + host + ":" + port);
            System.exit(-1);
        }

        // запускаем новый вычислительный поток (см. ф-ю run())
        start();
    }

    private MyClient(String host) {
        this(host, 5432);
    }

    private MyClient() {
        this("localhost", 5432);
    }

    public void run() {
        gui();
        connect();

//        try {
//            // берём поток вывода и выводим туда первый аргумент, заданный при вызове, адрес открытого сокета и его порт
//            args[0] = args[0]+"\n" + socket.getInetAddress().getHostAddress() + ":" + socket.getLocalPort();
//            socket.getOutputStream().write(args[0].getBytes());
//
//            // читаем ответ
//            byte buf[] = new byte[64*1024];
//            int r = socket.getInputStream().read(buf);
//            String data = new String(buf, 0, r);
//
//            // выводим ответ в консоль
//            System.out.println(data);
//        } catch(Exception e) {
//            System.out.println("init error: "+e); // вывод исключений
//        }
    }

    private MyHashtable<String, String> askNameAndPassword() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        JPanel label = new JPanel(new GridLayout(0, 1, 2, 2));

        label.add(new JLabel("Username", SwingConstants.RIGHT));
        label.add(new JLabel("Password", SwingConstants.RIGHT));
        panel.add(label, BorderLayout.WEST);

        JPanel controls = new JPanel(new GridLayout(0, 1, 2, 2));
        JTextField username = new JTextField();
        controls.add(username);
        JPasswordField password = new JPasswordField();
        controls.add(password);
        panel.add(controls, BorderLayout.CENTER);

        JOptionPane.showConfirmDialog(
                getFrame(), panel, "Sign in", JOptionPane.DEFAULT_OPTION);

        MyHashtable<String, String> loginInformation = new MyHashtable<>();
        loginInformation.put("user", username.getText());
        loginInformation.put("pass", new String(password.getPassword()));

        return loginInformation;
    }

    private void connect() {
        MyHashtable<String, String> nameAndPassword = askNameAndPassword();
        try {
            new ObjectOutputStream(socket.getOutputStream()).writeObject(nameAndPassword);
        } catch (IOException e) {
            e.printStackTrace();
            connect();
        }
    }
}

