package ru.ifmo.cs.programming.lab5.core;

import ru.ifmo.cs.programming.lab5.domain.Employee;

public interface InteractiveModeFunctions {
	void add(Employee employee);
	void remove(Employee employee);
	void clear();
	void save();
	int getSize();
	Employee[] getEmployees();
}
