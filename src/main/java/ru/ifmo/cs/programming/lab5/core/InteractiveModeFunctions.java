package ru.ifmo.cs.programming.lab5.core;

import ru.ifmo.cs.programming.lab5.domain.Employee;

import java.io.IOException;

public interface InteractiveModeFunctions {
	void add(Employee employee) throws IOException;
	void remove(Employee employee) throws IOException;
	void clear() throws IOException;
	void save() throws IOException;
	void exit();
	void exit(String msg);
	int getSize();
	Employee[] getEmployees();
}
