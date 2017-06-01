package ru.ifmo.cs.programming.lab7.core;

import org.postgresql.ds.PGConnectionPoolDataSource;
import ru.ifmo.cs.programming.lab7.MyClient;

import javax.sql.PooledConnection;
import java.io.*;
import java.nio.channels.SocketChannel;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MyServerThread extends Thread {
    private SocketChannel socketChannel;
    private int num;// will be used for exceptions
    private OutputStream socketout;
    private InputStream socketin;

    public MyServerThread(int num, SocketChannel socketChannel) {
        // копируем данные
        this.num = num;
        if (socketChannel != null) {
            this.socketChannel = socketChannel;
        } else {
            throw new NullPointerException("" + num);
        }

        try {
            socketout = socketChannel.socket().getOutputStream();
            socketin = socketChannel.socket().getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

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
        PooledConnection pc = connectToDatabase(askNameAndPassword(socketChannel));

        sendTable(getDataFromDatabase(pc));

        while (true)
            if (receiveChanges() == -1) break;

        try {
            socketChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private MyClient.Pair<String, String> askNameAndPassword(SocketChannel socketChannel) {
        System.out.println(num + ": trying to receive username and password");
        Object obj = null;
        try {
            ObjectInputStream oiStream = new ObjectInputStream(socketChannel.socket().getInputStream());
            obj = oiStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return (MyClient.Pair<String, String>) obj;
    }

    private PooledConnection connectToDatabase(MyClient.Pair<String, String> nameAndPassword) {
        System.out.println(num + ": trying to connect to database");
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
                ObjectOutputStream ooStream = new ObjectOutputStream(socketout);
                ooStream.writeChars(e.getLocalizedMessage());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            System.out.println(num + ": Cannot connect to database: " + e.getLocalizedMessage());
            return connectToDatabase(askNameAndPassword(socketChannel));
        }

        System.out.println(num + ": connected to database");

        try {
            ObjectOutputStream ooStream = new ObjectOutputStream(socketout);
            ooStream.writeChars("connected to database");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return pc;
    }

    private ResultSet getDataFromDatabase(PooledConnection pc) {
        System.out.println(num + ": trying to get data from database");
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

    private void sendTable(ResultSet res) {
        System.out.println(num + ": trying to send table");
        try {
            ObjectOutputStream ooStream = new ObjectOutputStream(socketout);
            ooStream.writeObject(res);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int receiveChanges() {
        //TODO: receiveChanges()
        System.out.println(num + ": trying to receive changes");
        return -1;
    }

    private void disconnect() {
        // TODO: disconnect()
        System.out.println(num + ": trying to disconnect");
        System.exit(1);
    }
}
