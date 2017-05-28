package ru.ifmo.cs.programming.lab7;


import ru.ifmo.cs.programming.lab5.domain.Employee;
import ru.ifmo.cs.programming.lab7.core.MyServerThread;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.util.ArrayDeque;

public class MyServer {
    public static void main(String args[]) throws IOException {
//        /* Если аргументы отсутствуют, порт принимает значение по умолчанию */
//        int port = 5432;
//        if (args.length > 0) {
//            port = Integer.parseInt(args[0]);
//        }
//
//        /* Создаем серверный сокет на полученном порту */
//        ServerSocket serverSocket = null;
//        try {
//            serverSocket = new ServerSocket(port, 0,
//                    InetAddress.getByName("localhost")); // создаем сокет сервера на локалхосте
//        } catch (IOException e) {
//            System.out.println("Порт занят: " + port);
//            System.exit(-1);
//        }
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        int port = 5432;
        SocketAddress socketAddress = new InetSocketAddress(port);
        serverSocketChannel.bind(socketAddress);

        int i = 0; // счётчик подключений

        System.out.println("server is started");

        /* Слушаем порт, ждём нового подключения, после чего запускаем обработку клиента в новый вычислительный поток
         * и увеличиваем счётчик на единичку */
        try {
            while (true) {
                //
                new MyServerThread(i, serverSocketChannel.accept());
                i++;
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
    }

    private static ArrayDeque<Employee> deque = new ArrayDeque<>();

    public static ArrayDeque<Employee> getDeque() {
        return deque;
    }

    public static int getPort() {return 5432;}
}
