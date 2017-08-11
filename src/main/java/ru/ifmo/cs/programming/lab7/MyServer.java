package ru.ifmo.cs.programming.lab7;

import org.postgresql.ds.PGConnectionPoolDataSource;
import ru.ifmo.cs.programming.lab7.utils.MyEntry;

import javax.sql.PooledConnection;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static ru.ifmo.cs.programming.lab7.utils.MyEntry.NAME_AND_PASSWORD;

public class MyServer {
    private static int port = 5431;
//    private ArrayList<MyServerThread> threads;
//    private static boolean stopIdentifier = false;
//    private final int waitingTimeForNewConnection = 10000;
    private Selector selector;
	PGConnectionPoolDataSource connectionPoolDataSource;
    HashMap<SocketChannel, PooledConnection> pooledConnections;

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
                        while (acceptedSocketChannel == null) // тк неблокирующий
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

                    pooledConnections.put(acceptedSocketChannel, null);

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
        //sendTable(getDataFromDatabase(pc), acceptedSocketChannel);

        MyEntry request = identifyRequest(socketChannel);

        switch (request.getKey()) {
            case NAME_AND_PASSWORD:
	            System.out.println(request.getValue().toString()); // вывод имени и пароля на экран
	            System.out.println("trying to connect to database");

	            if (!(request.getValue() instanceof MyClient.Pair)) {
		            System.out.println("Shit_occurred#: incorrect type of MyEntry value");
		            // send back NULL
	            }

                if (pooledConnections.get(socketChannel) != null) {
                    System.out.println("Shit_occurred");
                    // send back NULL
                }

                try {
                    pooledConnections.replace(socketChannel, connectToDatabase((MyClient.Pair) request.getValue()));
                } catch (SQLException e) {
	                try {
                        ObjectOutputStream ooStream = new ObjectOutputStream(socketChannel.socket().getOutputStream());
                        ooStream.writeChars("connected to database");
                    } catch (IOException e1) {
                        e.printStackTrace();
                    }
                }

	            // send back "connected to database"
                try {
                    ObjectOutputStream ooStream = new ObjectOutputStream(socketChannel.socket().getOutputStream());
                    ooStream.writeChars("connected to database");
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    private MyEntry identifyRequest(SocketChannel channel) {
        MyEntry obj = null;
        ByteBuffer buffer = ByteBuffer.allocate(8192);
        try {
            if (channel.read(buffer) == 0)
                System.out.println("Shit_occurred#?: empty income");
            obj = (MyEntry) deserialize(buffer.array());
        } catch (IOException e) {
            System.out.println("Shit occurred#11");
        } catch (ClassNotFoundException e) {
            System.out.println("Shit occurred#12");
        }

        return obj;
    }

    private static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        return is.readObject();
    }

    @Deprecated
    private MyClient.Pair askNameAndPassword(SocketChannel socketChannel) {
        System.out.println("trying to receive username and password");
        Object obj = null;
        try {
            ByteBuffer buffer = ByteBuffer.allocate(8192);
            int bytesRead = socketChannel.read(buffer);
            if (bytesRead > 0) {
                buffer.flip();
                InputStream bais = new ByteArrayInputStream(buffer.array(), 0, buffer.limit());
                ObjectInputStream oiStream = new ObjectInputStream(bais);
                obj = oiStream.readObject();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(13);
        }
        return new MyClient.Pair((String) obj);
    }

    private PooledConnection connectToDatabase(MyClient.Pair nameAndPassword) throws SQLException {
        String username = nameAndPassword.getFirst();
        String password = nameAndPassword.getSecond();

        PooledConnection pc = connectionPoolDataSource.getPooledConnection(username, password);

        System.out.println("connected to database");

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