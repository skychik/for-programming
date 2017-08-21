package ru.ifmo.cs.programming.lab7.core;

import com.sun.istack.NotNull;
import com.sun.rowset.CachedRowSetImpl;
import ru.ifmo.cs.programming.lab7.MyClient;
import ru.ifmo.cs.programming.lab7.MyServer;
import ru.ifmo.cs.programming.lab7.utils.MyEntry;

import javax.sql.PooledConnection;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.channels.SocketChannel;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static ru.ifmo.cs.programming.lab7.utils.MyEntry.NAME_AND_PASSWORD;
import static ru.ifmo.cs.programming.lab7.utils.MyEntry.TABLE;

public class MyServerThread extends Thread {
	private MyServer server;
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

			MyEntry request = identifyRequest();
			switch (request.getKey()) {
				case TABLE :
					sendTable(getDataFromDatabase());

			}
		} catch (InterruptedException ignored){}
	}

//	private MyEntry deserialize(ByteArrayOutputStream data, int kol) throws IOException, ClassNotFoundException {
//		// cause '00' in the end
////	    for (int i = 0; i < kol - 1; i++) {
////		    System.out.print(data.[i] + ".");
////	    }
//		System.out.println();
//		ByteArrayInputStream in = new ByteArrayInputStream(/*Arrays.copyOfRange(*/data.toByteArray()/*, 0, data.length - 1)*/);
//		ObjectInputStream is = new ObjectInputStream(in);
//		Object o = null;
//		try {
//			o = is.readObject();
//		} catch (EOFException e) {
//			System.out.println("EOF caught:"+e.getMessage());
//		}
//		return (MyEntry) o;
//	}

	private void connectToDatabase() throws InterruptedException {
		MyEntry request = identifyRequest();

		if (request.getKey() != NAME_AND_PASSWORD) {
			System.out.println(num + ": Shit_in_thread: incorrect format of request (key is null)");
			try {
				oos.writeObject(new MyEntry(-1, "incorrect format of request (key != NAME_AND_PASSWORD)"));
			} catch (IOException e1) {
				System.out.println(num + ": Shit_in_thread: can't send answer back");
				e1.printStackTrace();
				disconnect();
			}
			disconnect();
		}

		if (!(request.getValue() instanceof MyClient.Pair)) {
			System.out.println(num + ": Shit_in_thread: incorrect type of MyEntry value");
			try {
				oos.writeObject(new MyEntry(-1, "incorrect format of request (key != NAME_AND_PASSWORD)"));
			} catch (IOException e1) {
				System.out.println(num + ": Shit_in_thread: can't send answer back");
				e1.printStackTrace();
				disconnect();
			}
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
			try {
				oos.writeObject(new MyEntry(1, e.getMessage()));
			} catch (IOException e1) {
				System.out.println(num + ": Shit_in_thread: can't send answer back");
				e1.printStackTrace();
				disconnect();
			}
			connectToDatabase();
		}

		// send back that connection is approved"
		try {
			oos.writeObject(new MyEntry(0, null));
		} catch (IOException e) {
			System.out.println(num + ": Shit_in_thread: can't send answer back");
			e.printStackTrace();
			disconnect();
		}
	}

	@NotNull
	private MyEntry identifyRequest() throws InterruptedException {
//	    ObjectInputStream ois = null;
//    	try {
//		    ois = new ObjectInputStream(Channels.newInputStream(channel));
//	    } catch (IOException e) {
//		    System.out.println("Shit_occurred: can't identify request");
//		    e.printStackTrace();
//		    return null;
//	    }
//
//	    MyEntry obj = null;
//	    try {
//		    obj = (MyEntry) ois.readObject();
//	    } catch (IOException e) {
//		    System.out.println("Shit_occurred: can't identify request (2)");
//		    e.printStackTrace();
//		    return null;
//	    } catch (ClassNotFoundException e) {
//	        System.out.println("Shit occurred#12");
//		    e.printStackTrace();
//	    }

		//
		System.out.println(num + ": identifying request...");

		MyEntry entry = null;
		try {
			entry = (MyEntry) ois.readObject();
		} catch (IOException | ClassNotFoundException e) {
			System.out.println(num + ": Shit_in_thread");
			e.printStackTrace();
			disconnect();
		}

		if (entry == null) {
			System.out.println(num + ": Shit_in_thread: incorrect format of answer (entry is null)");
			disconnect();
		} else {
			return entry;
		}

		return null; // shouldn't be executed
//		System.out.println(obj);
//		//ByteBuffer buffer = ByteBuffer.allocate(128);
//		long length = ois.readLong();
//		System.out.println("length="+length);

		//
//		int count;
//		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
//		int sum = 0;
//		try {
//			do {
//				count = channel.read(buffer);
//				//System.out.println("count="+count);
//				if (count != -1) {
//					buffer.rewind();
//					for (int i = 0; i < count; i++) {
//						outStream.write((char) buffer.get());
//					}
//					sum += count;
//				}
//			} while (count != -1 && sum < length);
//			System.out.println("sum=" + sum);
//			if (sum == length) {
//				obj = deserialize(outStream, sum);
//			} else {
//				//System.out.println("sum=" + sum + ", length=" + length);
//			}
//		} catch (IOException e) {
//			System.out.println("Shit_occurred: can't identify request");
//			e.printStackTrace();
//		} catch (ClassNotFoundException e) {
//			System.out.println("Shit occurred#12");
//			e.printStackTrace();
//		}

		//
//        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
//        try {
//        	int sum = 0;
//            int kol = channel.read(buffer);
//            while (kol > 0) {
//            	sum += kol;
//                buffer.rewind();
//	            outStream.write(buffer.array());
//                //buffer.flip();
//                kol = channel.read(buffer);
//            }
//            obj = (MyEntry) deserialize(outStream, sum);
//        } catch (IOException e) {
//	        System.out.println("Shit occurred#11");
//        	e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//	        System.out.println("Shit occurred#12");
//	        e.printStackTrace();
//        }

		//return obj;
	}

	private ResultSet getDataFromDatabase() {
		System.out.println(num + ": trying to get data from database...");

		ResultSet set = null;
		try {
			Statement stat = pooledConnection.getConnection().createStatement();
			stat.execute("CREATE TABLE IF NOT EXISTS public.\"EMPLOYEE\"" +
					"(NAME varchar, " +
					"PROFESSION varchar, " +
					"SALARY integer, " +
					"ATTITUDE_TO_BOSS integer, " +
					"WORK_QUALITY integer, " +
					"AVATAR_PATH varchar, " +
					"NOTES varchar);");

			stat = pooledConnection.getConnection().createStatement();
			set = stat.executeQuery(
					"SELECT * FROM public.\"EMPLOYEE\"");

			stat.close();
			set.close();
		} catch (SQLException e) {
			System.out.println(num + ": Shit_in_thread: " + e.getSQLState());
			e.printStackTrace();
		}

		return set;
	}

	private void sendTable(ResultSet res) {
		System.out.println(num + ": trying to send table...");
		try {
			CachedRowSetImpl crs = new CachedRowSetImpl();
			crs.populate(res);
			oos.writeObject(new MyEntry(0, res.getStatement()));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	private int receiveChanges() {
		//TODO: receiveChanges()
		System.out.println(num + ": trying to receive changes");
		return -1;
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
