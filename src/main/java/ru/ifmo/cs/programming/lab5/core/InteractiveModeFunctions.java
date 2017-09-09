package ru.ifmo.cs.programming.lab5.core;

import ru.ifmo.cs.programming.lab5.domain.Employee;

public interface InteractiveModeFunctions {
	void add(Employee employee);
	void update(Employee oldEmployee, Employee newEmployee);
	void remove(Employee employee);
	void clear();
	void save();
	void exit();
	void exit(String msg);
	int getSize();
	Employee[] getEmployees();
}
