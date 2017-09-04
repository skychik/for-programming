// TODO: доставать из бд № емплои и хранить его, тк удаление работает не так.

package ru.ifmo.cs.programming.lab7.core;

import ru.ifmo.cs.programming.lab5.domain.Employee;
import ru.ifmo.cs.programming.lab5.domain.ShopAssistant;
import ru.ifmo.cs.programming.lab5.utils.AttitudeToBoss;
import ru.ifmo.cs.programming.lab5.utils.FactoryWorker;
import ru.ifmo.cs.programming.lab7.MyClient;
import ru.ifmo.cs.programming.lab7.MyServer;
import ru.ifmo.cs.programming.lab7.utils.MyEntry;
import ru.ifmo.cs.programming.lab7.utils.MyEntryKey;

import javax.sql.PooledConnection;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.channels.SocketChannel;
import java.sql.*;
import java.util.ArrayDeque;

import static ru.ifmo.cs.programming.lab7.utils.MyEntryKey.*;

public class MyServerThread extends Thread {
	private final MyServer server;
	private int num; // server thread number; for exceptions
	private SocketChannel socketChannel;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private PooledConnection pooledConnection;

	public MyServerThread(MyServer server, int num, SocketChannel socketChannel) {
		this.server = server;
		this.num = num;

		if (socketChannel != null) {
			this.socketChannel = socketChannel;
		} else {
			throw new NullPointerException("" + num);
		}

		// и запускаем новый вычислительный поток (см. ф-ю run())
		setDaemon(true);
		setPriority(NORM_PRIORITY);
		start();
	}

	public void run() {
		try {
			try {
				ois = new ObjectInputStream(socketChannel.socket().getInputStream());
				oos = new ObjectOutputStream(socketChannel.socket().getOutputStream());
			} catch (IOException e) {
				System.out.println(num + ": Shit_in_thread");
				e.printStackTrace();
				disconnect();
			}


			// Receiving and trying to connect to DB
			connectToDatabase();

			//boolean fl = true;
			while (/*fl*/true) {
				MyEntry request = getNewRequest();
				switch (request.getKey()) {
					case TABLE :
						sendTable();
						break;
					case TRANSACTION:
						processTransaction(request);
						break;
					case CLOSE :
						//fl = false;
						close();
						break;
					default:
						System.out.println(num + ": Shit_in_thread: incorrect type of MyEntry key");
						sendMyEntry(DISCONNECT, "incorrect format of request (key number is unknown)");
						disconnect();
				}
			}
		} catch (InterruptedException ignored){}

		System.out.println(num + ": end of thread");
	}

	private void connectToDatabase() throws InterruptedException {
		MyEntry request = getNewRequest();

		if (request.getKey() != NAME_AND_PASSWORD) {
			System.out.println(num + ": Shit_in_thread: incorrect format of request (key is null)");
			sendMyEntry(DISCONNECT, "incorrect format of request (key != NAME_AND_PASSWORD)");
			disconnect();
		}

		if (!(request.getValue() instanceof MyClient.Pair)) {
			System.out.println(num + ": Shit_in_thread: incorrect type of MyEntry value");
			sendMyEntry(DISCONNECT, "incorrect format of request (value.class != Pair)");
			disconnect();
		}

		System.out.println(num + ": " + request.getValue().toString()); // вывод имени и пароля на экран
		System.out.println(num + ": trying to connect to database...");
		try {
			String username = ((MyClient.Pair) request.getValue()).getFirst();
			String password = ((MyClient.Pair) request.getValue()).getSecond();

			pooledConnection = server.getConnectionPoolDataSource().getPooledConnection(username, password);
			System.out.println(num + ": connected to database");
		} catch (SQLException e) {
			System.out.println(num + ": " + e.getMessage());
			if (!sendMyEntry(SQLEXCEPTION, e.getMessage())) disconnect();
			connectToDatabase();
		}

		// send back that connection is approved"
		if (!sendMyEntry(OK, null)) disconnect();
	}

	private void sendTable() throws InterruptedException {
		try {
			// Getting data from database
			System.out.println(num + ": trying to get data from database...");

			Statement stat = pooledConnection.getConnection().createStatement();
			ResultSet res = stat.executeQuery("SELECT ID, name, profession, speciality, salary, attitude, " +
					"work_quality, avatar_path, notes FROM public.\"EMPLOYEE\", public.\"ATTITUDE_TO_BOSS\"" +
					"WHERE public.\"EMPLOYEE\".attitude_to_boss = public.\"ATTITUDE_TO_BOSS\".attitude");

			// Sending data
			System.out.println(num + ": trying to send table...");
			while (res.next()) {
				long ID = res.getLong("ID");
				String name = res.getString("name");
				String profession = res.getString("profession");
				String speciality = res.getString("speciality");
				int salary = res.getInt("salary");
				AttitudeToBoss attitudeToBoss = AttitudeToBoss.fromByteToAttitudeToBoss(
						(byte)res.getInt("attitude"));
				byte workQuality = res.getByte("work_quality");
				String avatarPath = res.getString("avatar_path");
				String notes = res.getString("notes");

				Employee employee = null;
				switch (speciality) {
					case "class ru.ifmo.cs.programming.lab5.domain.Employee":
						employee = new Employee(name, profession, salary, attitudeToBoss, workQuality);
						break;
					case "class ru.ifmo.cs.programming.lab5.utils.FactoryWorker":
						employee = new FactoryWorker(name, profession, salary, attitudeToBoss, workQuality);
						break;
					case "class ru.ifmo.cs.programming.lab5.domain.ShopAssistant":
						employee = new ShopAssistant(name, profession, salary, attitudeToBoss, workQuality);
						break;
					default:
						sendMyEntry(DISCONNECT, "Broken data: incorrect format of speciality");
						disconnect();
				}
				employee.setAvatarPath(avatarPath);
				employee.setNotes(notes);
				employee.setID(ID);

				try {
					oos.writeObject(employee);
				} catch (IOException e) {
					System.out.println(num + ": Shit_in_thread: can't send answer back");
					e.printStackTrace();
					disconnect();
				}
			}

			if (!sendMyEntry(OK, null)) disconnect();
			System.out.println(num + ": table has sent");

			stat.close();
			res.close();
		} catch (SQLException e) {
			System.out.println(num + ": Shit_in_thread: " + e.getLocalizedMessage());
			e.printStackTrace();
			sendMyEntry(SQLEXCEPTION, e.getLocalizedMessage());
			disconnect();
		}
	}

	private void processTransaction(MyEntry request) throws InterruptedException {
		System.out.println(num + ": processing transaction");
		if (!(request.getValue() instanceof ArrayDeque)) {
			System.out.println(num + ": Shit_in_thread: incorrect type of MyEntry value");
			sendMyEntry(DISCONNECT, "incorrect format of request (value.class != ArrayDeque)");
			disconnect();
		}
		ArrayDeque deque = (ArrayDeque) request.getValue();

		// Making connection to DB
		Connection con = null;
		try {
			con = pooledConnection.getConnection();
			con.setAutoCommit(false);
		} catch (SQLException e) {
			System.out.println(num + ": " + e.getMessage());
			sendMyEntry(DISCONNECT, e.getMessage());
			disconnect();
		}

		// Transaction
		try {
			int i = 1;
			while (!deque.isEmpty()) {
				System.out.println(num + ": " + i + " part");
				i++;

				Object query = deque.remove();
				if (!(query instanceof MyEntry)) {
					System.out.println(num + ": Shit_in_thread: incorrect type of an element");
					sendMyEntry(DISCONNECT, "incorrect format of request (query.class != MyEntry)");
					disconnect();
				}

				switch (((MyEntry) query).getKey()) {
					case INSERT:
					case REMOVE:
						Object obj = ((MyEntry) query).getValue();
						if (obj instanceof Employee) {
							Employee employee = (Employee) obj;
							PreparedStatement stat = null;
							switch (((MyEntry) query).getKey()) {
								case INSERT:
									System.out.println(num + ": inserting");
									stat = con.prepareStatement(" insert into public.\"EMPLOYEE\"" +
											" (NAME, PROFESSION, SPECIALITY, SALARY, ATTITUDE_TO_BOSS, WORK_QUALITY, " +
											"AVATAR_PATH, NOTES) values (?, ?, ?, ?, ?, ?, ?, ?)");
									break;
								case REMOVE:
									System.out.println(num + ": removing");
									stat = con.prepareStatement("delete from public.\"EMPLOYEE\" where " +
											"id in (select id from public.\"EMPLOYEE\" where " +
											"NAME = ? AND PROFESSION = ? AND SPECIALITY = ? AND SALARY = ? AND " +
											"ATTITUDE_TO_BOSS = ? AND WORK_QUALITY = ? AND AVATAR_PATH = ? AND " +
											"NOTES = ? limit 1)");
							}
							stat.setString(1, employee.getName());
							stat.setString(2, employee.getProfession());
							stat.setString(3, employee.getClass().toString());
							stat.setInt(4, employee.getSalary());
							stat.setByte(5, employee.getAttitudeToBoss().getAttitude());
							stat.setByte(6, employee.getWorkQuality());
							stat.setString(7, employee.getAvatarPath());
							stat.setString(8, employee.getNotes());
							stat.executeUpdate();
							stat.close();
						} else {
							System.out.println(num + ": Shit_in_thread: incorrect type of MyEntry value");
							sendMyEntry(DISCONNECT, "incorrect format of request (value.class != Employee)");
							disconnect();
						}
						break;
					case CLEAR:
						System.out.println(num + ": clearing");
						Statement stat = con.createStatement();
						stat.executeUpdate("delete from public.\"EMPLOYEE\"");
						break;
					default:
						System.out.println(num + ": Shit_in_thread: unexpected request key");
				}
			}

			con.commit();
		} catch (SQLException e) {
			System.out.println(num + ": Shit_in_thread: " + e.getLocalizedMessage());
			e.printStackTrace();
			sendMyEntry(ROLLBACK, e.getLocalizedMessage());

			System.out.println(num + ": trying to rollback...");
			try {
				con.rollback();
				if (!sendMyEntry(OK, null)) disconnect();
				System.out.println("rolled back");
			} catch (SQLException e1) {
				System.out.println(num + ": Shit_in_thread: can't rollback");
				e1.printStackTrace();
				sendMyEntry(DISCONNECT, e1.getLocalizedMessage());
				disconnect();
			}
		}

		if (!sendMyEntry(OK, null)) disconnect();

		// Closing connection
		try {
			con.close();
		} catch (SQLException e) {
			System.out.println(num + ": Shit_in_thread: " + e.getLocalizedMessage());
			e.printStackTrace();
			sendMyEntry(DISCONNECT, e.getLocalizedMessage());
			disconnect();
		}

		System.out.println(num + ": processed");
	}

	private MyEntry getNewRequest() throws InterruptedException {
		System.out.println(num + ": getting new request...");
		MyEntry entry = null;
		try {
			entry = (MyEntry) ois.readObject();
		} catch (IOException e) {
			System.out.println(num + ": Shit_in_thread: can't get new request");
			disconnect();
		} catch (ClassNotFoundException e) {
			System.out.println(num + ": Shit_in_thread: incorrect format of reply (wrong class format)");
			e.printStackTrace();
			disconnect();
		}

		if (entry == null) {
			System.out.println(num + ": Shit_in_thread: incorrect format of answer (entry is null)");
			disconnect();
			return null; // shouldn't be executed
		} else {
			return entry;
		}
	}

	private boolean sendMyEntry(MyEntryKey key, Object value) {
		try {
			oos.writeObject(new MyEntry(key, value));
		} catch (IOException e) {
			System.out.println(num + ": Shit_in_thread: can't send answer back");
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private void close() throws InterruptedException {
		System.out.println(num + ": trying to end the thread...");
		disconnect();
	}

	private void disconnect() throws InterruptedException {
		System.out.println(num + ": trying to disconnect");
		try {
			socketChannel.close();
		} catch (IOException e) {
			System.out.println(num + ": Shit_in_thread: can't close channel");
			e.printStackTrace();
			System.exit(1);
		}
		server.getThreads().remove(this);
		System.out.println(num + ": disconnected");
		throw new InterruptedException();
	}
}
