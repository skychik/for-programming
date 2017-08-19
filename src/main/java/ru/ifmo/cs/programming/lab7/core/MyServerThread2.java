package ru.ifmo.cs.programming.lab7.core;

import ru.ifmo.cs.programming.lab7.MyClient;
import ru.ifmo.cs.programming.lab7.utils.MyEntry;

import javax.sql.PooledConnection;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static ru.ifmo.cs.programming.lab7.utils.MyEntry.NAME_AND_PASSWORD;

public class MyServerThread2 extends Thread {
	int num; // server thread number; for exceptions
	SocketChannel socketChannel;

	public MyServerThread2 (int num, SocketChannel socketChannel) {
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
		if (socketChannel.)// TODO: START FROM HERE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		System.out.println("New income from client");
		processInput();
	}

	private void processInput(SocketChannel socketChannel) {
		System.out.println("Started processing new input info:");
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

//                if (pooledConnections.get(socketChannel) != null) {
//                    System.out.println("Shit_occurred");
//                    // send back NULL
//                }

				try {
					pooledConnections.put(socketChannel, connectToDatabase((MyClient.Pair) request.getValue()));
				} catch (SQLException e) {
					System.out.println(e.getMessage());
					try {
						//ObjectOutputStream ooStream = new ObjectOutputStream(socketChannel.socket().getOutputStream());
						socketChannel.read(ByteBuffer.wrap("connected to database".getBytes()));
					} catch (IOException e1) {
						e.printStackTrace();
					}
				}

				// send back "connected to database"
				try {
					//ObjectOutputStream ooStream = new ObjectOutputStream(socketChannel.socket().getOutputStream());
					socketChannel.read(ByteBuffer.wrap("connected to database".getBytes()));
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	private MyEntry identifyRequest(SocketChannel channel) {
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
		System.out.println("identifying request");
		MyEntry obj = null;
		ByteBuffer buffer = ByteBuffer.allocate(128);
		//
		long length = readLength(channel);
		System.out.println("length="+length);
		//
		int count;
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		int sum = 0;
		try {
			do {
				count = channel.read(buffer);
				//System.out.println("count="+count);
				if (count != -1) {
					buffer.rewind();
					for (int i = 0; i < count; i++) {
						outStream.write((char) buffer.get());
					}
					sum += count;
				}
			} while (count != -1 && sum < length);
			System.out.println("sum=" + sum);
			if (sum == length) {
				obj = deserialize(outStream, sum);
			} else {
				//System.out.println("sum=" + sum + ", length=" + length);
			}
		} catch (IOException e) {
			System.out.println("Shit_occurred: can't identify request");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.out.println("Shit occurred#12");
			e.printStackTrace();
		}

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

		return obj;
	}

	private long readLength(SocketChannel channel) {
		ByteBuffer length = ByteBuffer.allocate(8);
		int count;
		int sum = 0;
		try {
			do {
				count = channel.read(length);
				if (count != -1) {
					//length.rewind();
					sum += count;
					System.out.println("count=" + count + "sum=" + sum);
					if (count > 0) {
						for (int i = 0; i < length.array().length; i++) {
							System.out.print(length.array()[i] + ".");
						}
					}
				}
			} while (count != -1 && sum < 8);

			if (sum != 8) {
				System.out.println("sum=" + sum);
				for (int i = 0; i < length.array().length; i++) {
					System.out.print(length.array()[i] + ".");
				}
				throw new IOException();
			}
		} catch (IOException e) {
			System.out.println("Shit_occurred: can't identify length of request");
			e.printStackTrace();
			return -1;
		}

		System.out.println("sum=" + sum);
		for (int i = 0; i < length.array().length; i++) {
			System.out.print(length.array()[i] + ".");
		}

		length.rewind();

		return length.getLong();
	}

	private MyEntry deserialize(ByteArrayOutputStream data, int kol) throws IOException, ClassNotFoundException {
		// cause '00' in the end
//	    for (int i = 0; i < kol - 1; i++) {
//		    System.out.print(data.[i] + ".");
//	    }
		System.out.println();
		ByteArrayInputStream in = new ByteArrayInputStream(/*Arrays.copyOfRange(*/data.toByteArray()/*, 0, data.length - 1)*/);
		ObjectInputStream is = new ObjectInputStream(in);
		Object o = null;
		try {
			o = is.readObject();
		} catch (EOFException e) {
			System.out.println("EOF caught:"+e.getMessage());
		}
		return (MyEntry) o;
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
}
