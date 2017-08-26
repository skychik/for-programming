package ru.ifmo.cs.programming.lab7.core;

import ru.ifmo.cs.programming.lab5.core.InteractiveModeFunctions;
import ru.ifmo.cs.programming.lab5.domain.Employee;
import ru.ifmo.cs.programming.lab7.utils.MyEntry;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayDeque;

public class IMFForBD implements InteractiveModeFunctions {
	private ObjectOutputStream oos = null;

	private ArrayDeque<Employee> deque = new ArrayDeque<>();
	private String sql = null;

	@Override
	public void add(Employee employee) {
		// sql = "sql";
		save();
		throw new UnsupportedOperationException();
	}

	@Override
	public void remove(Employee employee) {
		// sql = "sql";
		save();
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear() {
		// sql = "sql";
		save();
		throw new UnsupportedOperationException();
	}

	@Override
	public void save() {
		// oos.writeObject(new MyEntry(QUERY, sql);
		// sql = null;
		throw new UnsupportedOperationException();
	}

	@Override
	public void exit() {
		try {
			oos.writeObject(new MyEntry(MyEntry.CLOSE, null));
		} catch (IOException e) {
			System.out.println("can't tell server about disconnecting normally");
		}
		System.exit(0);
	}

	@Override
	public int getSize() {
		return deque.size();
	}

	@Override
	public Employee[] getEmployees() {
		return (Employee[]) deque.toArray();
	}

	public void setOos(ObjectOutputStream oos) {
		this.oos = oos;
	}

	public void setDeque(ArrayDeque<Employee> deque) {
		this.deque = deque;
	}
}
