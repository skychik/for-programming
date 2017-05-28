package ru.ifmo.cs.programming.lab7;

import org.postgresql.ds.PGConnectionPoolDataSource;

import javax.sql.PooledConnection;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.SQLException;

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
        Connection c = connect();

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

    private Connection connect() {
        Pair<String, String> nameAndPassword =
                guiNameAndPassword();
        String username = nameAndPassword.getFirst();
        String password = nameAndPassword.getSecond();

        PGConnectionPoolDataSource ds = new PGConnectionPoolDataSource();
        ds.setServerName("localhost");
        ds.setDatabaseName("postgres");

        PooledConnection pc = null;
        try {
            pc = ds.getPooledConnection(username, password);
            return pc.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Cannot get (pooled) connection");

            if (guiException()) {
                return connect();
            } else disconnect();
        }

        return null;
    }

    private Pair<String, String> guiNameAndPassword() {
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

        return new Pair<>(username.getText(), new String(password.getPassword()));
    }

    public class Pair<A, B> {
        private A first;
        private B second;

        Pair(A first, B second) {
            super();
            this.first = first;
            this.second = second;
        }

        public int hashCode() {
            int hashFirst = first != null ? first.hashCode() : 0;
            int hashSecond = second != null ? second.hashCode() : 0;

            return (hashFirst + hashSecond) * hashSecond + hashFirst;
        }

        public boolean equals(Object other) {
            if (other instanceof Pair) {
                Pair otherPair = (Pair) other;
                return
                        ((  this.first == otherPair.first ||
                                ( this.first != null && otherPair.first != null &&
                                        this.first.equals(otherPair.first))) &&
                                (  this.second == otherPair.second ||
                                        ( this.second != null && otherPair.second != null &&
                                                this.second.equals(otherPair.second))) );
            }

            return false;
        }

        public String toString()
        {
            return "(" + first + ", " + second + ")";
        }

        public A getFirst() {
            return first;
        }

        public B getSecond() {
            return second;
        }
    }

    private boolean guiException() {
        JPanel panel = new JPanel();
        JPanel label = new JPanel();

        label.add(new JLabel("Username", SwingConstants.RIGHT));
        panel.add(label);

        int reply = JOptionPane.showConfirmDialog(
                getFrame(), panel, "Try again?", JOptionPane.YES_NO_OPTION);
        return reply == JOptionPane.YES_OPTION;
    }

    private void disconnect() {
        System.exit(1);
    }
}

