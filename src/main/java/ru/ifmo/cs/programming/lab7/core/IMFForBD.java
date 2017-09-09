package ru.ifmo.cs.programming.lab7.core;

import ru.ifmo.cs.programming.lab5.core.InteractiveModeFunctions;
import ru.ifmo.cs.programming.lab5.domain.Employee;
import ru.ifmo.cs.programming.lab7.utils.MyEntry;
import ru.ifmo.cs.programming.lab7.utils.MyEntryKey;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayDeque;
import java.util.Arrays;

import static ru.ifmo.cs.programming.lab7.utils.MyEntryKey.*;

public class IMFForBD implements InteractiveModeFunctions {
	private ObjectOutputStream oos = null;
	private ObjectInputStream ois = null;

	private ArrayDeque<Employee> deque = new ArrayDeque<>();
	private ArrayDeque<MyEntry> buff = new ArrayDeque<>();

	private DequeMemento undo;
	private class DequeMemento {
		private ArrayDeque<Employee> savepoint = new ArrayDeque<>();

		DequeMemento() { savepoint = deque.clone(); }

		ArrayDeque<Employee> getSavepoint() { return savepoint; }
	}

	@Override
	public void add(Employee employee) {
		deque.add(employee);
		buff.add(new MyEntry(INSERT, employee));
		System.out.println("Added: " + employee);
	}

	@Override
	public void update(Employee oldEmployee, Employee newEmployee) {
//		deque.remove(oldEmployee);
//		deque.add(newEmployee);
//		buff.add(new MyEntry(UPDATE, new employee));
//		System.out.println("Added: " + employee);
	}

	@Override
	public void remove(Employee employee) {
		deque.remove(employee);
		buff.add(new MyEntry(REMOVE, employee));
		System.out.println("Removed: " + employee);
	}

	@Override
	public void clear() {
		deque.clear();
		buff.add(new MyEntry(CLEAR, null));
		System.out.println("cleared");
	}

	@Override
	public void save() {
		Thread t = new Thread(() -> {
			System.out.println("saving...");

			System.out.println(Arrays.toString(buff.toArray()));

			try {
				oos.writeObject(new MyEntry(TRANSACTION, buff.clone()));
			} catch (IOException e) {
				exit("Shit_occurred: can't send transaction(save)");
			}

			MyEntry entry = null;
			try {
				entry = (MyEntry) ois.readObject();
			} catch (IOException e) {
				exit("Shit_occurred: can't get an answer");
			} catch (ClassNotFoundException e) {
				exit("Shit_occurred: incorrect format of an answer (wrong class format)");
			}

			switch (entry.getKey()) {
				case OK:
					buff.clear();
					undo = new DequeMemento();
					System.out.println("saved");
					break;
				case ROLLBACK:
					MyEntry ent = null;
					try {
						ent = (MyEntry) ois.readObject();
					} catch (IOException e) {
						exit("Shit_occurred: can't get an answer");
					} catch (ClassNotFoundException e) {
						exit("Shit_occurred: incorrect format of an answer (wrong class format)");
					}
					switch (ent.getKey()) {
						case OK:
							deque = undo.getSavepoint();
							break;
						case DISCONNECT:
							exit(ent.getValue().toString());
							break;
						default:
							exit("Unexpected key(rollback): " + entry.getKey());
					}
					break;
				case DISCONNECT:
					exit(entry.getValue().toString());
					break;
				default:
					exit("Unexpected key(saving): " + entry.getKey());
			}
		});
		t.start();
	}

	@Override
	public void exit() {
		save();
		// TODO: выкидывать плашку на сохранение
		System.out.println("trying to exit...");
		try {
			oos.writeObject(new MyEntry(MyEntryKey.CLOSE, null));
		} catch (IOException e) {
			System.out.println("can't tell server about disconnecting normally");
		}
		System.exit(0);
	}

	@Override
	public void exit(String msg) {
		System.out.println("Shit_occurred: " + msg);
		// show dialog
		Frame frame = new Frame("CRUD application");
		JOptionPane.showMessageDialog(frame, new JLabel("Shit_occurred: " + msg), "Ошибка",
				JOptionPane.ERROR_MESSAGE);
		exit();
	}

	@Override
	public int getSize() {
		return deque.size();
	}

	@Override
	public Employee[] getEmployees() {
		Employee[] employees = new Employee[deque.size()]; // создаем массив длиною, равной размеру списка
		return deque.toArray(employees);
	}

	public void setOos(ObjectOutputStream oos) {
		this.oos = oos;
	}

	public void setOis(ObjectInputStream ois) {
		this.ois = ois;
	}

	public void setDeque(ArrayDeque<Employee> deque) {
		this.deque = deque;
		undo = new DequeMemento();
	}
}
