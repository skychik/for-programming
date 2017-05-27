package ru.ifmo.cs.programming.lab7.core;

import ru.ifmo.cs.programming.lab7.MyServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.channels.SocketChannel;
import java.util.Hashtable;


public class MyServerThread extends Thread {
    private SocketChannel socketChannel;
    private int num;

    public MyServerThread(int num, SocketChannel socketChannel) {
        // копируем данные
        this.num = num;
        this.socketChannel = socketChannel;

        // и запускаем новый вычислительный поток (см. ф-ю run())
        setDaemon(true);
        setPriority(NORM_PRIORITY);
        start();
    }

    public void run() {
//        try {
//            // из сокета клиента берём поток входящих данных
//            InputStream is = socketChannel.getInputStream();
//            // и оттуда же - поток данных от сервера к клиенту
//            OutputStream os = socketChannel.getOutputStream();
//
//            // буффер данных в 64 килобайта
//            byte buf[] = new byte[64*1024];
//            // читаем 64кб от клиента, результат - кол-во реально принятых данных
//            int r = is.read(buf);
//
//            // создаём строку, содержащую полученную от клиента информацию
//            String data = new String(buf, 0, r);
//
//            // добавляем данные об адресе сокета:
//            data = ""+num+": "+"\n"+data;
//
//            // выводим данные:
//            os.write(data.getBytes());
//
//            // завершаем соединение
//            socketChannel.close();
//        }
//        catch(Exception e) {
//            System.out.println("init error: "+e);// вывод исключений
//        }

//        if (askNameAndPassword(socketChannel)) {
//            sendDeque(socketChannel);
//        } else {
//            disconnect();
//        }
        Object obj = null;
        try {
            ObjectInputStream oiStream= new ObjectInputStream(socketChannel.socket().getInputStream());
            obj = oiStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        while (true) {
            if (receiveChanges() == -1) break;
        }

        try {
            socketChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private int receiveChanges() {
        //TODO: receiveChanges()
        return -1;
    }

    private static void sendDeque(SocketChannel socketChannel) {
        ObjectOutputStream ooStream = null;
        try {
            ooStream = new ObjectOutputStream(socketChannel.socket().getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            ooStream.writeObject(MyServer.getDeque());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean askNameAndPassword(SocketChannel socketChannel) {
        Object obj = null;
        try {
            ObjectInputStream oiStream= new ObjectInputStream(socketChannel.socket().getInputStream());
            obj = oiStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return true;
        //return isNameAndPasswordCorrect((Hashtable<String, String>)(String) obj);
    }

    private void disconnect() {
        // TODO: disconnect()
    }

    private static boolean isNameAndPasswordCorrect(Hashtable<String, String> nameAndPassword) {
        if (nameAndPassword == null)
            return false;
        // TODO: isNameAndPasswordCorrect()
        return true;
    }

//    private static Connection getConnection() throws NamingException, SQLException {
//        InitialContext initCtx = null;
//        initCtx = createContext();
//        String jndiName = "HrDS";
//        ConnectionPoolDataSource dataSource = (ConnectionPoolDataSource) initCtx.lookup(jndiName);
//        PooledConnection pooledConnection = dataSource.getPooledConnection();
//        return pooledConnection.getConnection(); // Obtain connection from pool
//    }
//
//    private static InitialContext createContext() throws NamingException {
//        Properties env = new Properties();
//        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.rmi.registry.RegistryContextFactory");
//        env.put(Context.PROVIDER_URL, "rmi://localhost:1099");
//        return new InitialContext(env);
//    }
}
