package ru.ifmo.cs.programming.lab7;

import org.jetbrains.annotations.NotNull;
import ru.ifmo.cs.programming.lab5.domain.Employee;
import ru.ifmo.cs.programming.lab6.AppGUI;
import ru.ifmo.cs.programming.lab7.utils.MyEntry;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.util.Objects;
import java.util.regex.Pattern;

import static ru.ifmo.cs.programming.lab6.AppGUI.getDeque;
import static ru.ifmo.cs.programming.lab7.utils.MyEntry.NAME_AND_PASSWORD;
import static ru.ifmo.cs.programming.lab7.utils.MyEntry.TABLE;

public class MyClient extends Thread {

    private static Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

//	/*это наш дек*/
//	private static ArrayDeque<Employee> deque = new ArrayDeque<>();

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
		    System.out.println("Unknown host: " + host);
		    System.exit(1);
	    } catch (IOException e) {
		    System.out.println("Can't create socket on  " + host + ":" + port);
		    System.exit(1);
	    }

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
	        //Runtime.getRuntime().addShutdownHook(new Thread(this::disconnect));
	        try {
		        oos = new ObjectOutputStream(socket.getOutputStream());
		        ois = new ObjectInputStream(socket.getInputStream());
	        } catch (IOException e) {
		        System.out.println("Shit_occurred: problems with input/output stream(s)");
		        disconnect();
	        }

	        connect(null);
	        new Thread(AppGUI::gui);
	        initDeque();
        } catch (InterruptedException ignored){}
        System.exit(0);
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

    private void/*Connection*/ connect(String msg) throws InterruptedException {
        System.out.println("trying to connect to server...");
        Pair nameAndPassword = guiNameAndPassword(msg);
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
        System.out.println(nameAndPassword.toString());

        MyEntry reply = query(NAME_AND_PASSWORD, nameAndPassword);

        if (reply.getKey() == 0)
        	System.out.println("connected to database");
        if (reply.getKey() == 1) {
	        System.out.println("Shit_occurred: " + reply.getValue());
	        connect(reply.getValue().toString());
        }
	    if (reply.getKey() == -1) {
            System.out.println("Shit_occurred: " + reply.getValue());
            disconnect();
        }
    }

	@NotNull
	private Pair guiNameAndPassword(String msg) throws InterruptedException {
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

		if (msg != null) {
			JPanel message = new JPanel(new GridLayout(1, 0, 2, 2));
			JLabel l = new JLabel(msg, SwingConstants.CENTER);
			l.setForeground(Color.red);
			message.add(l);
			panel.add(message, BorderLayout.SOUTH);
		}

		Frame frame = new Frame("CRUD application");
		frame.setIconImage(new ImageIcon(
				System.getProperty("user.dir") + "/src/resources/images/icon.png").getImage());

		int result = JOptionPane.showConfirmDialog(frame, panel, "Sign in", JOptionPane.DEFAULT_OPTION);
		if (result == JOptionPane.CLOSED_OPTION) {
			disconnect();
		}
		return new Pair(username.getText(), new String(password.getPassword()));
	}

//	private long byteLengthOfObject (MyEntry entry) throws IOException {
//		ByteArrayOutputStream byteObject = new ByteArrayOutputStream();
//		ObjectOutputStream s = new ObjectOutputStream(byteObject);
//		s.writeObject(entry);
//		s.flush();
//		s.close();
//		//System.out.println("length=" + byteObject.toByteArray().length);
//		return byteObject.size();
//	}

//    private byte[] getBytes(Object obj) {
//        byte[] bytes = new byte[0];
//        try {
//            ByteArrayOutputStream bos = new ByteArrayOutputStream();
//            ObjectOutputStream out = new ObjectOutputStream(bos);
//            out.writeObject(obj);
//            out.flush();
//            bytes = bos.toByteArray();
//        } catch (IOException ignored) {}
//        return bytes;
//    }
//
//    private boolean guiTryAgain() {
//        JPanel panel = new JPanel();
//        JPanel label = new JPanel();
//
//        label.add(new JLabel("Username", SwingConstants.RIGHT));
//        panel.add(label);
//
//        int reply = JOptionPane.showConfirmDialog(
//                getFrame(), panel, "Try again?", JOptionPane.YES_NO_OPTION);
//        return reply == JOptionPane.YES_OPTION;
//    }

    private ResultSet receiveTable() throws InterruptedException {
        System.out.println("trying to receive table...");
	    MyEntry reply = query(TABLE, null);

	    if (reply.getKey() != 0) {
		    System.out.println("Shit_occurred: " + reply.getValue());
		    disconnect();
	    }

        return (ResultSet) reply.getValue();
    }

    private MyEntry query(int requestCode, Object obj) throws InterruptedException {
	    MyEntry request = new MyEntry(requestCode, obj);
	    try {
		    oos.writeObject(request);
		    oos.flush();
	    } catch (IOException e) {
		    System.out.println("Shit_occurred: can't send a request to the server");
		    disconnect();
	    }

	    System.out.println("receiving answer...");
	    MyEntry reply = null;
	    try {
		    reply = (MyEntry) ois.readObject();
	    } catch (IOException e) {
		    System.out.println("Shit_occurred: can't get a reply from server");
		    disconnect();
	    } catch (ClassNotFoundException e) {
		    System.out.println("Shit_occurred: incorrect format of reply (wrong class format)");
		    disconnect();
	    }

	    if (reply == null) {
		    System.out.println("Shit_occurred: reply is null");
		    disconnect();
	    }

	    if (reply.getKey() == null) {
		    System.out.println("Shit_occurred: incorrect format of reply (key is null)");
		    disconnect();
	    }

	    return reply;
    }

    private void initDeque() throws InterruptedException {
    	//ResultSet res = receiveTable();
        getDeque().clear();

	    try {
		    oos.writeObject(new MyEntry(TABLE, null));

		    Object reply = null;
		    try {
			    reply = ois.readObject();
		        while (reply instanceof Employee) {
		            getDeque().add((Employee) reply);
		            reply = ois.readObject();
		        }
		    } catch (ClassNotFoundException e) {
			    System.out.println("Shit_occurred: incorrect format of reply (wrong class format)");
			    disconnect();
		    }
	        if (reply instanceof MyEntry) {
	        	if (((MyEntry) reply).getKey() == 0) {
	        		// ok
		        } else {
			        System.out.println("Shit_occurred: ???");
			        // ?
		        }
	        } else {
		        System.out.println("Shit_occurred: incorrect format of reply (expected MyEntry)");
		        disconnect();
	        }
	    } catch (IOException e) {
		    System.out.println("Shit_occurred: can't send a request to the server");
		    disconnect();
	    }
//        try {
//        	while (res.next()) {
//		        String name = res.getString("NAME");
//		        String profession = res.getString("PROFESSION");
//		        int salary = res.getInt("SALARY");
//		        AttitudeToBoss attitudeToBoss = (AttitudeToBoss)res.getObject("ATTITUDE_TO_BOSS");
//		        byte workQuality = res.getByte("WORK_QUALITY");
//		        String avatarPath = res.getString("AVATAR_PATH");
//		        String notes = res.getString("NOTES");
//		        Employee employee = new Employee(name, profession, salary, attitudeToBoss, workQuality);
//		        employee.setAvatarPath(avatarPath);
//		        employee.setNotes(notes);
//
//		        getDeque().add(employee);
//	        }
//        } catch (SQLException e) {
//	        System.out.println("Shit_occurred: SQLException in making table from ResultSet");
//	        e.printStackTrace();
//	        disconnect();
//        }
    }

    private void disconnect() throws InterruptedException {
        System.out.println("trying to disconnect...");
        try {
            socket.close();
            System.out.println("socket closed");
        } catch (IOException e) {
            e.printStackTrace();
        }
	    System.out.println("disconnected");
	    throw new InterruptedException();
    }

	public static class Pair implements Serializable {
		private String first;
		private String second;

		Pair(String first, String second) {
			super();
			this.first = first;
			this.second = second;
		}

		/**
		 * @param str - "(first, second)"
		 */
		public Pair(String str) throws IllegalArgumentException {
			super();
			Pattern p = Pattern.compile("\\(\\w*, \\w*\\)");
			if (!p.matcher(str).matches()) throw new IllegalArgumentException();

			p = Pattern.compile("[,()]");
			String[] ans = p.split(str);
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
						((Objects.equals(this.first, otherPair.first) ||
								( this.first != null && otherPair.first != null &&
										this.first.equals(otherPair.first))) &&
								(Objects.equals(this.second, otherPair.second) ||
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
}

