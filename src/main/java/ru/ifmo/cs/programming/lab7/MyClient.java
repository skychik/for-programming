package ru.ifmo.cs.programming.lab7;

import ru.ifmo.cs.programming.lab5.domain.Employee;
import ru.ifmo.cs.programming.lab5.utils.AttitudeToBoss;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.regex.Pattern;

import static ru.ifmo.cs.programming.lab6.AppGUI.*;

public class MyClient extends Thread implements Serializable {
    private static Socket socket = null;
    private transient InputStream socketIn;
    private transient OutputStream socketOut;

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
            System.out.println("Ошибка при создании сокета на " + host + ":" + port);
            System.exit(-1);
        }

        Runtime.getRuntime().addShutdownHook(new Thread(this::disconnect));

        // запускаем новый вычислительный поток (см. ф-ю run())
        start();
    }

    private MyClient(String host) {
        this(host, 5431);
    }

    private MyClient() {
        this("localhost", 5431);
    }

    public void run() {
        try {
            socketOut = socket.getOutputStream();
            socketIn = socket.getInputStream();

            connect();
            initDeque(receiveTable());
            gui();

        } catch (IOException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
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

    private void/*Connection*/ connect() throws IOException {
        System.out.println("trying to connect to server");
        Pair nameAndPassword =
                guiNameAndPassword();
//        String username = nameAndPassword.getFirst();
//        String password = nameAndPassword.getSecond();

//        PGConnectionPoolDataSource ds = new PGConnectionPoolDataSource();
//        ds.setServerName("localhost");
//        ds.setDatabaseName("postgres");
//
//        PooledConnection pc = null;
//        try {
//            pc = ds.getPooledConnection(username, password);
//            return pc.getConnection();
//        } catch (SQLException e) {
//            e.printStackTrace();
//            System.out.println("Cannot get (pooled) connection");
//
//            if (guiTryAgain()) {
//                return connect();
//            } else disconnect();
//        }
//
//        return null;
        socketOut.write(nameAndPassword.toString().getBytes());
        socketOut.flush();

        System.out.println("receiving answer...");
        byte[] buffer = new byte[8192];// хз на сколько расчитано
        // ByteArrayOutputStream используется, как накопитель байт, чтобы
        //   потом превратить в строку все полученные данные.
        //   преобразовывать часть потока в строку опасно, т.к.
        //   если данные идут в многобайтной кодировке, один символ может
        //   быть разрезан между чтениями
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // InputStream.read( byte[] ) возвращает количество прочитанных байт
        //   и -1, если поток кончился (сервер закрыл соединение)
        for ( int received; (received = socketIn.read( buffer )) != -1; ) {
            // записываем прочитанное из потока, от 0 до количества считанных
            baos.write( buffer, 0, received );
        }
        String reply = baos.toString();

        System.out.print("server: ");
        if (reply != null) System.out.println(reply);
            else System.out.println("null");

        if (!Objects.equals(reply, "connected to database")) connect();
    }

    private Pair guiNameAndPassword() {
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

        Frame frame = new Frame("CRUD application");
        frame.setIconImage(new ImageIcon(
                System.getProperty("user.dir") + "/src/resources/images/icon.png").getImage());

        JOptionPane.showConfirmDialog(
                frame, panel, "Sign in", JOptionPane.DEFAULT_OPTION);

        return new Pair(username.getText(), new String(password.getPassword()));
    }

    public class Pair {
        private transient String first;
        private transient String second;

        Pair(String first, String second) {
            super();
            this.first = first;
            this.second = second;
        }

        /**
         *
         * @param string - "(first, second)"
         */
        Pair(String string) throws IllegalArgumentException {
            super();
            Pattern p = Pattern.compile("\\(\\w*, \\w*\\)");
            if (!p.matcher(string).matches()) throw new IllegalArgumentException();

            p = Pattern.compile("[,()]");
            String[] ans = p.split(string);
            first = ans[1];
            second = ans[2];
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

        /**
         *
         * @return "(first, second)"
         */
        public String toString()
        {
            return "(" + first + ", " + second + ")";
        }

        public String getFirst() {
            return first;
        }

        public String getSecond() {
            return second;
        }
    }

    private boolean guiTryAgain() {
        JPanel panel = new JPanel();
        JPanel label = new JPanel();

        label.add(new JLabel("Username", SwingConstants.RIGHT));
        panel.add(label);

        int reply = JOptionPane.showConfirmDialog(
                getFrame(), panel, "Try again?", JOptionPane.YES_NO_OPTION);
        return reply == JOptionPane.YES_OPTION;
    }

    private ResultSet receiveTable() throws IOException, ClassNotFoundException {
        System.out.println("trying to receive table");
        ObjectInputStream dataInputStream = new ObjectInputStream(socketIn);
        return (ResultSet) dataInputStream.readObject();
    }

    private void initDeque(ResultSet res) throws SQLException {
        getDeque().clear();
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

            getDeque().add(employee);
        }
    }

    private void disconnect() {
        System.out.println("trying to disconnect");
        try {
            socket.close();
            System.out.println("socket closed");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

