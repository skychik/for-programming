package ru.ifmo.cs.programming.lab7.core;

import ru.ifmo.cs.programming.lab5.domain.Employee;

public class EmployeesResource {
    private EmployeeService employeesService = EmployeeService.getInstance();

    public Employee getEmployee() {
        return employeesService.getEmployee();
    }

/*    public Response addEmployee(Employee employee) {
        employeesService.addEmployee(employee);
        return Response.ok().build();
    }

    public Response updateEmployee(Employee employee) {
        employeesService.updateEmployee(employee);
        return Response.ok().build();
    }

    public Response deleteEmployee(Employee employee) {
        employeesService.deleteEmployee(employee);
        return Response.ok().build();
    }*/
}
