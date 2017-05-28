package ru.ifmo.cs.programming.lab7.core;

import org.postgresql.ds.PGConnectionPoolDataSource;
import ru.ifmo.cs.programming.lab5.domain.Employee;
import ru.ifmo.cs.programming.lab5.utils.AttitudeToBoss;
import ru.ifmo.cs.programming.lab7.MyClient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.channels.SocketChannel;
import java.sql.*;
import java.util.ArrayDeque;

import static ru.ifmo.cs.programming.lab7.MyServer.getDeque;
import static ru.ifmo.cs.programming.lab7.MyServer.getPort;


public class MyServerThread extends Thread {
    private SocketChannel socketChannel;
    private int num;// will be used for exceptions

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
        MyClient.Pair<String, String> nameAndPassword = askNameAndPassword(socketChannel);
        String username = nameAndPassword.getFirst();
        String password = nameAndPassword.getSecond();
//        try {
//            getDataFromDatabase(getDeque(), username, password);
//        } catch (SQLException e1) {
//            e1.printStackTrace();
//            disconnect();
//        }
        PGConnectionPoolDataSource connectionPoolDataSource = new PGConnectionPoolDataSource();

        try {
            connectionPoolDataSource.getPooledConnection(username, password);
        } catch (SQLException e) {
            e.printStackTrace();
            disconnect();
        }

        sendDeque(socketChannel);

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

    private static MyClient.Pair<String, String> askNameAndPassword(SocketChannel socketChannel) {
        Object obj = null;
        try {
            ObjectInputStream oiStream= new ObjectInputStream(socketChannel.socket().getInputStream());
            obj = oiStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return (MyClient.Pair<String, String>) obj;
    }

    private void getDataFromDatabase(ArrayDeque<Employee> deque, String username, String password) throws SQLException {
        Connection conn = DriverManager.getConnection(
                "jdbc:tcp:localhost:" + getPort() + ":postgres", username, password);
        Statement stat = conn.createStatement();
        ResultSet res = stat.executeQuery(
                "SELECT * FROM public.\"EMPLOYEE\"");
        deque.clear();
        while (res.next()) {
            String name = res.getString("NAME");
            String profession = res.getString("PROFESSION");
            int salary = res.getInt("SALARY");
            AttitudeToBoss attitudeToBoss = (AttitudeToBoss)res.getObject("ATTITUDE_TO_BOSS");
            byte workQuality = res.getByte("WORK_QUALITY");
            String avatarPath = res.getString("AVATAR_PATH");
            String notes = res.getString("NOTES");

            Employee employee = new Employee(name, profession, salary, attitudeToBoss, workQuality);
            employee.setAvatarPath(avatarPath);
            employee.setNotes(notes);

            deque.add(employee);
        }

        stat.close();
        conn.close();
    }

    private static void sendDeque(SocketChannel socketChannel) {
        ObjectOutputStream ooStream = null;
        try {
            ooStream = new ObjectOutputStream(socketChannel.socket().getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            ooStream.writeObject(getDeque());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int receiveChanges() {
        //TODO: receiveChanges()
        return -1;
    }

    private void disconnect() {
        // TODO: disconnect()
    }
}
