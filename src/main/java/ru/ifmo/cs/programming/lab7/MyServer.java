package ru.ifmo.cs.programming.lab7;


import ru.ifmo.cs.programming.lab7.core.MyServerThread;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.util.ArrayList;

public class MyServer {
    private static int port = 5431;
    private ArrayList<MyServerThread> threads;
    private static boolean stopIdentifier = false;
    private final int waitingTimeForNewConnection = 10000;

    public static void main(String args[]) throws IOException {
        // TODO: надо?
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        /* Если аргументы отсутствуют, порт принимает значение по умолчанию */
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }

//        /* Создаем серверный сокет на полученном порту */
//        ServerSocket serverSocket = null;
//        try {
//            serverSocket = new ServerSocket(port, 0,
//                    InetAddress.getByName("localhost")); // создаем сокет сервера на локалхосте
//        } catch (IOException e) {
//            System.out.println("Порт занят: " + port);
//            System.exit(-1);
//        }

        new MyServer();
    }

    private MyServer() {
        ServerSocketChannel serverSocketChannel = null;
        try {
            serverSocketChannel = ServerSocketChannel.open();
            SocketAddress socketAddress = new InetSocketAddress(port);
            serverSocketChannel.bind(socketAddress);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int i = 0; // счётчик подключений

        System.out.println("server is started");

        /* Слушаем порт, ждём нового подключения, после чего запускаем обработку клиента в новый вычислительный поток
         * и увеличиваем счётчик на единичку */
        threads = new ArrayList<>();
        try {
            while (true) {
                threads.add(new MyServerThread(i, serverSocketChannel.accept()));
                System.out.println("server thread number " + i + " has added");
                i++;
                if (threads.isEmpty()) {
                    Thread thread = new Thread(() -> {
                        try {
                            wait(waitingTimeForNewConnection);
                        } catch (InterruptedException ignored) {}
                        if (threads.isEmpty()) stopIdentifier = true;
                    });
                    thread.start();
                }

                if (stopIdentifier) {
                    stopIdentifier = false;
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("init error: " + e); // вывод исключений
        }
//        SocketChannel socketChannel = null;
//        try {
//            socketChannel = SocketChannel.open();
//        } catch (IOException e) {
//            System.out.println("Cannot create socket channel");
//            System.exit(-1);
//        }
//        try {
//            socketChannel.connect(new InetSocketAddress("http://jenkov.com", 80));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        int i = 0; // счётчик подключений
//
//        System.out.println("server is started");
//
//        /* Слушаем порт, ждём нового подключения, после чего запускаем обработку клиента в новый вычислительный поток
//         * и увеличиваем счётчик на единичку */
//        try {
//            while(true) {
//                //
//                new MyServerThread(i, serverSocket.accept());
//                i++;
//            }
//        } catch(Exception e) {
//            System.out.println("init error: " + e); // вывод исключений
//        }
        System.out.println("server stopped receiving connections from new clients");
    }

//    private static ArrayDeque<Employee> deque = new ArrayDeque<>();
//
//    public static ArrayDeque<Employee> getDeque() {
//        return deque;
//    }

    public static int getPort() {return port;}
}
