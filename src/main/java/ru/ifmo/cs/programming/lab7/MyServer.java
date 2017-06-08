package ru.ifmo.cs.programming.lab7;


import org.postgresql.ds.PGConnectionPoolDataSource;

import javax.sql.PooledConnection;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Set;

public class MyServer {
    private static int port = 5431;
//    private ArrayList<MyServerThread> threads;
//    private static boolean stopIdentifier = false;
//    private final int waitingTimeForNewConnection = 10000;
    private Selector selector;

    public static void main(String args[]) throws IOException {
        try {
            Class.forName("org.postgresql.Driver"); // TODO: надо?
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
            serverSocketChannel.configureBlocking(false);
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

        // Register server to selector (type OP_ACCEPT)
        try {
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (ClosedChannelException e) {
            System.out.println("Shit_occurred#3: second parameter does not correspond to an operation\n" +
                    "that is supported by server socket channel");
            System.exit(1);
        }

        System.out.println("server is started");

        // Infinite server loop
        while (true) {
            // Проверяем, если ли какие-либо активности - входящие соединения или входящие данные в
            // существующем соединении. Если никаких активностей нет, выходим из цикла и снова ждём.
            try {
                if (selector.select() == 0) continue;
            } catch (IOException e) {
                System.out.println("Shit_occurred#4: selector is closed");
            }

            // Get keys
            Set keys = selector.selectedKeys();

            Iterator iterator = keys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = (SelectionKey) iterator.next();

                // Remove the current key
                iterator.remove();

                if (key.isAcceptable()) {
                    // Принимаем входящее соединение
                    SocketChannel acceptedSocketChannel;
                    try {
                        acceptedSocketChannel = serverSocketChannel.accept();
                        acceptedSocketChannel.configureBlocking(false); // Non Blocking I/O
                    } catch (IOException e) {
                        System.out.println("Shit_occurred#5: cannot register income socket channel");
                        continue;
                    }

                    // recording to the selector (reading and writing)
                    try {
                        acceptedSocketChannel.register(selector, SelectionKey.OP_READ);
                    } catch (ClosedChannelException e) {
                        System.out.println("Shit_occurred#6: Channel is closed");
                    }

                    PooledConnection pc = connectToDatabase(acceptedSocketChannel);

                    sendTable(getDataFromDatabase(pc), acceptedSocketChannel);

                    continue;
                }

                if (key.isReadable()) {
                    SocketChannel client = (SocketChannel) key.channel();
                    processInput(client);
                }
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

    private void processInput(SocketChannel socketChannel) {
        OutputStream socketOut = null;
        InputStream socketIn = null;
        try {
            socketOut = socketChannel.socket().getOutputStream();
            socketIn = socketChannel.socket().getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        switch (identifyRequest(socketIn).getFirst()) {
            //case : disconnect();
        }

        // -----------------------

        while (true)
            if (receiveChanges() == -1) break;

        try {
            socketChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private MyClient.Pair identifyRequest(InputStream in) {
        ObjectInputStream stream = null;
        try {
            stream = new ObjectInputStream(in);
        } catch (IOException e) {
            System.out.println("Shit occurred#10: cannot identify request");

        }

        MyClient.Pair obj = null;
        try {
            obj = (MyClient.Pair) stream.readObject();
        } catch (IOException e) {
            System.out.println("Shit occurred#11");
        } catch (ClassNotFoundException e) {
            System.out.println("Shit occurred#12");
        }

        return obj;
    }

    private MyClient.Pair askNameAndPassword(SocketChannel socketChannel) {
        System.out.println("trying to receive username and password");
        Object obj = null;
        try {
            ObjectInputStream oiStream = new ObjectInputStream(socketChannel.socket().getInputStream());
            obj = oiStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return (MyClient.Pair) obj;
    }

    private PooledConnection connectToDatabase(SocketChannel socketChannel) {
        MyClient.Pair nameAndPassword = askNameAndPassword(socketChannel);
        System.out.println(nameAndPassword.toString());
        System.out.println("trying to connect to database");
        String username = nameAndPassword.getFirst();
        String password = nameAndPassword.getSecond();
        //        try {
        //            getDataFromDatabase(getDeque(), username, password);
        //        } catch (SQLException e1) {
        //            e1.printStackTrace();
        //            disconnect();
        //        }
        PGConnectionPoolDataSource connectionPoolDataSource = new PGConnectionPoolDataSource();

        PooledConnection pc = null;
        try {
            pc = connectionPoolDataSource.getPooledConnection(username, password);
        } catch (SQLException e) {
            try {
                ObjectOutputStream ooStream = new ObjectOutputStream(socketChannel.socket().getOutputStream());
                ooStream.writeChars(e.getLocalizedMessage());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            System.out.println("Cannot connect to database: " + e.getLocalizedMessage());
            return connectToDatabase(socketChannel);
        }

        System.out.println("connected to database");

        try {
            ObjectOutputStream ooStream = new ObjectOutputStream(socketChannel.socket().getOutputStream());
            ooStream.writeChars("connected to database");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return pc;
    }

    private ResultSet getDataFromDatabase(PooledConnection pc) {
        System.out.println("trying to get data from database");
        ResultSet set = null;
        try {
            Statement stat = pc.getConnection().createStatement();
            set = stat.executeQuery(
                    "SELECT * FROM public.\"EMPLOYEE\"");
            stat.close();
            set.close();
        } catch (SQLException e) {
            System.out.println((e.getSQLState()));
        }
        return set;
    }

    private void sendTable(ResultSet res, SocketChannel channel) {
        System.out.println("trying to send table");
        try {
            ObjectOutputStream ooStream = new ObjectOutputStream(channel.socket().getOutputStream());
            ooStream.writeObject(res);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int receiveChanges() {
        //TODO: receiveChanges()
        System.out.println("trying to receive changes");
        return -1;
    }

    private void disconnect() {
        // TODO: disconnect()
        System.out.println("trying to disconnect");
        System.exit(1);
    }

    public static int getPort() {
        return port;
    }
}