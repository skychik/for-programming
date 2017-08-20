/*
Сервер не принимает/обрабатывает отправленное от клиента
 */
package ru.ifmo.cs.programming.lab7;

import org.postgresql.ds.PGConnectionPoolDataSource;
import ru.ifmo.cs.programming.lab7.core.MyServerThread2;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

public class MyServer {
    private static int port = 5431;
    private ArrayList<MyServerThread2> threads = new ArrayList<>();
//    private static boolean stopIdentifier = false;
//    private final int waitingTimeForNewConnection = 10000;
    private Selector selector;
	PGConnectionPoolDataSource connectionPoolDataSource;
//    HashMap<SocketChannel, PooledConnection> pooledConnections;

    public static void main(String args[]) throws IOException {
        try {
            Class.forName("org.postgresql.Driver"); // loading driver
        } catch (ClassNotFoundException e) {
            System.out.println("Shit_occurred#0: cannot load postgresql driver");
            System.exit(1);
        }

        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }
        // Если аргументы отсутствуют, порт принимает значение по умолчанию

        new MyServer();
    }

    private MyServer() {
    	connectionPoolDataSource = new PGConnectionPoolDataSource();

        ServerSocketChannel serverSocketChannel = null;

        try {
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false); // nonblocking I/O
            serverSocketChannel.bind(new InetSocketAddress(port));
        } catch (IOException e) {
            System.out.println("Shit_occurred#1: cannot open nonblocking server socket channel on port " + port);
            System.exit(1);
        }

        // Create the selector
        try {
            selector = Selector.open();
        } catch (IOException e) {
            System.out.println("Shit_occurred#2: cannot create selector");
            System.exit(1);
        }

        //
        try {
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (ClosedChannelException e) {
            System.out.println("Shit_occurred#3: channel is closed");
            System.exit(1);
        }

        System.out.println("server has started");

        // Infinite server loop
        while (true) {
            // Проверяем, если ли какие-либо активности - входящие соединения или входящие данные в
            // существующем соединении. Если никаких активностей нет, выходим из цикла и снова ждём.
            try {
	            int numberOfKeys = selector.select();
	            System.out.println(numberOfKeys + " new actions");
                if (numberOfKeys == 0) {
	                continue;
                }
            } catch (IOException e) {
                System.out.println("Shit_occurred#4: selector is closed");
            }

            // Get keys
            Set keys = selector.selectedKeys();

            Iterator iterator = keys.iterator();
            while (iterator.hasNext()) {
	            System.out.println("new action:");
	            SelectionKey key = (SelectionKey) iterator.next();

                // Remove the current key
                iterator.remove();

                if (key.isAcceptable()) {
	                System.out.println("trying to accept new client");
	                // Принимаем входящее соединение
                    SocketChannel acceptedSocketChannel;
                    try {
                        acceptedSocketChannel = serverSocketChannel.accept();
                        //acceptedSocketChannel.configureBlocking(false); // Non Blocking I/O
                    } catch (IOException e) {
                        System.out.println("Shit_occurred#5: cannot register income socket channel");
                        continue;
                    }

                    MyServerThread2 newThread = new MyServerThread2(this, 0, acceptedSocketChannel);
	                threads.add(newThread);

//	                // recording to the selector (reading and writing)
//                    try {
//                        acceptedSocketChannel.register(selector, SelectionKey.OP_READ);
//                    } catch (ClosedChannelException e) {
//                        System.out.println("Shit_occurred#6: Channel is closed");
//                    }

	                System.out.println("Accepted new client");
//                    pooledConnections.put(acceptedSocketChannel, null);

                    //continue;
                }

//                if (key.isReadable()) {
//	                System.out.println("New income from client");
//	                SocketChannel client = (SocketChannel) key.channel();
//                    processInput(client);
//                }
//                threads.add(new MyServerThread(num, serverSocketChannel.accept()));
//                System.out.println("server thread number " + num + " has added");
//                num++;
//                if (threads.isEmpty()) {
//                    Thread thread = new Thread(() -> {
//                        try {
//                            wait(waitingTimeForNewConnection);
//                        } catch (InterruptedException ignored) {
//                        }
//                        if (threads.isEmpty()) stopIdentifier = true;
//                    });
//                    thread.start();
//                }
//
//                if (stopIdentifier) {
//                    stopIdentifier = false;
//                    break;
//                }
            }
        }
//        } catch (Exception e) {
//            System.out.println("init error: " + e); // вывод исключений
//        }
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
//        System.out.println("server stopped receiving connections from new clients");
    }

//    private static ArrayDeque<Employee> deque = new ArrayDeque<>();
//
//    public static ArrayDeque<Employee> getDeque() {
//        return deque;
//    }

	public PGConnectionPoolDataSource getConnectionPoolDataSource() {
    	return connectionPoolDataSource;
	}

    public static int getPort() {
        return port;
    }
}