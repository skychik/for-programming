package ru.ifmo.cs.programming.lab7.core;

import ru.ifmo.cs.programming.lab5.domain.Employee;
import ru.ifmo.cs.programming.lab5.domain.ShopAssistant;
import ru.ifmo.cs.programming.lab5.utils.AttitudeToBoss;
import ru.ifmo.cs.programming.lab5.utils.FactoryWorker;
import ru.ifmo.cs.programming.lab7.MyClient;
import ru.ifmo.cs.programming.lab7.MyServer;
import ru.ifmo.cs.programming.lab7.utils.DisconnectException;
import ru.ifmo.cs.programming.lab7.utils.MyEntry;
import ru.ifmo.cs.programming.lab7.utils.MyEntryKey;

import javax.sql.PooledConnection;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.channels.SocketChannel;
import java.sql.*;
import java.util.ArrayDeque;

import static java.time.ZoneOffset.UTC;
import static ru.ifmo.cs.programming.lab7.MyServer.getDBPassword;
import static ru.ifmo.cs.programming.lab7.MyServer.getDBUsername;
import static ru.ifmo.cs.programming.lab7.utils.MyEntryKey.*;

public class MyServerThread extends Thread {
	private final MyServer server;
	private long num; // server thread number; for exceptions
	private SocketChannel socketChannel;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private PooledConnection pooledConnection;

	public MyServerThread(MyServer server, long num, SocketChannel socketChannel) {
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
				throw new DisconnectException();
			}


			// Receiving and trying to connect to DB
			connectToDatabase();

			//boolean fl = true;
			while (/*fl*/true) {
				MyEntry request = getNewRequest();
				switch (request.getKey()) {
					case TABLE :
						System.out.println(num + ": trying to get data from database...");
						sendTable();
						break;
					case TRANSACTION:
						System.out.println(num + ": processing transaction");
						processTransaction(request);
						break;
					case CLOSE :
						System.out.println(num + ": the closure request is received");
						throw new DisconnectException();
					default:
						System.out.println(num + ": Shit_in_thread: incorrect type of MyEntry key");
						sendMyEntry(DISCONNECT, "incorrect format of request (key number is unknown)");
						throw new DisconnectException();
				}
			}
		} catch (DisconnectException ignored){}

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
	}

	private void connectToDatabase() throws DisconnectException {
		MyEntry request = getNewRequest();

		if (request.getKey() != NAME_AND_PASSWORD) {
			System.out.println(num + ": Shit_in_thread: incorrect format of request (key is null)");
			sendMyEntry(DISCONNECT, "incorrect format of request (key != NAME_AND_PASSWORD)");
			throw new DisconnectException();
		}

		if (!(request.getValue() instanceof MyClient.Pair)) {
			System.out.println(num + ": Shit_in_thread: incorrect type of MyEntry value");
			sendMyEntry(DISCONNECT, "incorrect format of request (value.class != Pair)");
			throw new DisconnectException();
		}

		System.out.println(num + ": " + request.getValue().toString()); // вывод имени и пароля на экран
		System.out.println(num + ": trying to connect to database...");
		try {
			String username = ((MyClient.Pair) request.getValue()).getFirst();
			String password = ((MyClient.Pair) request.getValue()).getSecond();

			pooledConnection = server.getConnectionPoolDataSource().getPooledConnection(getDBUsername(), getDBPassword());
			// Making connection to DB
			Connection con = pooledConnection.getConnection();
			PreparedStatement stat = con.prepareStatement("SELECT username, password FROM public.\"USERS\"" +
					"WHERE username = ? AND password = ?");
			stat.setString(1, username);
			stat.setString(2, password);
			ResultSet res = stat.executeQuery();
			if (!res.next()) {
				if (!sendMyEntry(SQLEXCEPTION, "Wrong name and password combination")) throw new DisconnectException();
				connectToDatabase();
			}
			System.out.println(num + ": connected to database");
		} catch (SQLException e) {
			System.out.println(num + ": " + e.getMessage());
			if (!sendMyEntry(SQLEXCEPTION, e.getMessage())) throw new DisconnectException();
			connectToDatabase();
		}

		// send back that connection is approved"
		if (!sendMyEntry(OK, null)) throw new DisconnectException();
	}

	private void sendTable() throws DisconnectException {
		try {
			// Getting data from database

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

				Employee employee;
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
						throw new DisconnectException();
				}
				employee.setAvatarPath(avatarPath);
				employee.setNotes(notes);
				employee.setID(ID);

				try {
					oos.writeObject(employee);
				} catch (IOException e) {
					System.out.println(num + ": Shit_in_thread: can't send answer back");
					e.printStackTrace();
					throw new DisconnectException();
				}
			}

			if (!sendMyEntry(OK, null)) throw new DisconnectException();
			System.out.println(num + ": table has sent");

			stat.close();
			res.close();
		} catch (SQLException e) {
			System.out.println(num + ": Shit_in_thread: " + e.getLocalizedMessage());
			e.printStackTrace();
			sendMyEntry(SQLEXCEPTION, e.getLocalizedMessage());
			throw new DisconnectException();
		}
	}

	private void processTransaction(MyEntry request) throws DisconnectException {
		if (!(request.getValue() instanceof ArrayDeque)) {
			System.out.println(num + ": Shit_in_thread: incorrect type of MyEntry value");
			sendMyEntry(DISCONNECT, "incorrect format of request (value.class != ArrayDeque)");
			throw new DisconnectException();
		}
		ArrayDeque deque = (ArrayDeque) request.getValue();

		// Making connection to DB
		Connection con;
		try {
			con = pooledConnection.getConnection();
			con.setAutoCommit(false);
		} catch (SQLException e) {
			System.out.println(num + ": " + e.getMessage());
			sendMyEntry(DISCONNECT, e.getMessage());
			throw new DisconnectException();
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
					throw new DisconnectException();
				}

				switch (((MyEntry) query).getKey()) {
					case INSERT:
					case REMOVE:
						Object obj = ((MyEntry) query).getValue();
						if (obj instanceof Employee) {
							Employee employee = (Employee) obj;
							PreparedStatement stat;
							switch (((MyEntry) query).getKey()) {
								case INSERT:
									System.out.println(num + ": inserting");
									stat = con.prepareStatement(" insert into public.\"EMPLOYEE\"" +
											" (NAME, PROFESSION, SPECIALITY, SALARY, ATTITUDE_TO_BOSS, WORK_QUALITY, " +
											"AVATAR_PATH, NOTES, CREATING_TIME) values (?, ?, ?, ?, ?, ?, ?, ?, ?)");
									break;
								case REMOVE:
									System.out.println(num + ": removing");
									stat = con.prepareStatement("delete from public.\"EMPLOYEE\" where " +
											"id in (select id from public.\"EMPLOYEE\" where " +
											"NAME = ? AND PROFESSION = ? AND SPECIALITY = ? AND SALARY = ? AND " +
											"ATTITUDE_TO_BOSS = ? AND WORK_QUALITY = ? AND AVATAR_PATH = ? AND " +
											"NOTES = ? AND CREATING_TIME = ? limit 1)");
									break;
								default:
									throw new DisconnectException();
							}
							stat.setString(1, employee.getName());
							stat.setString(2, employee.getProfession());
							stat.setString(3, employee.getClass().toString());
							stat.setInt(4, employee.getSalary());
							stat.setByte(5, employee.getAttitudeToBoss().getAttitude());
							stat.setByte(6, employee.getWorkQuality());
							stat.setString(7, employee.getAvatarPath());
							stat.setString(8, employee.getNotes());
							stat.setTimestamp(9, Timestamp.from(employee.getCreatingTime().
									withZoneSameInstant(UTC).toInstant()));
							stat.executeUpdate();
							stat.close();
						} else {
							System.out.println(num + ": Shit_in_thread: incorrect type of MyEntry value");
							sendMyEntry(DISCONNECT, "incorrect format of request (value.class != Employee)");
							throw new DisconnectException();
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
				if (!sendMyEntry(OK, null)) throw new DisconnectException();
				System.out.println("rolled back");
			} catch (SQLException e1) {
				System.out.println(num + ": Shit_in_thread: can't rollback");
				e1.printStackTrace();
				sendMyEntry(DISCONNECT, e1.getLocalizedMessage());
				throw new DisconnectException();
			}
		}

		if (!sendMyEntry(OK, null)) throw new DisconnectException();

		// Closing connection
		try {
			con.close();
		} catch (SQLException e) {
			System.out.println(num + ": Shit_in_thread: " + e.getLocalizedMessage());
			e.printStackTrace();
			sendMyEntry(DISCONNECT, e.getLocalizedMessage());
			throw new DisconnectException();
		}

		System.out.println(num + ": processed");
	}

	@org.jetbrains.annotations.NotNull
	private MyEntry getNewRequest() throws DisconnectException {
		System.out.println(num + ": getting new request...");
		MyEntry entry;
		try {
			entry = (MyEntry) ois.readObject();
		} catch (IOException e) {
			System.out.println(num + ": Shit_in_thread: can't get new request");
			throw new DisconnectException();
		} catch (ClassNotFoundException e) {
			System.out.println(num + ": Shit_in_thread: incorrect format of reply (wrong class format)");
			e.printStackTrace();
			throw new DisconnectException();
		}

		if (entry == null) {
			System.out.println(num + ": Shit_in_thread: incorrect format of answer (entry is null)");
			throw new DisconnectException();
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
}
