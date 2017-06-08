package ru.ifmo.cs.programming.lab7.utils;

import ru.ifmo.cs.programming.lab5.domain.Employee;

import java.util.LinkedList;
import java.util.List;

public class EmployeeController extends AbstractController<Employee, Integer> {
    private static final String SELECT_ALL_EMPLOYEES = "SELECT * FROM postgres.EMPLOYEE";

    @Override
    public List<Employee> getAll() {
        List<Employee> lst = new LinkedList<>();
        //PreparedStatement ps = pc.getConnection().createStatement();
        try {
           // ResultSet rs = ps.executeQuery(SELECT_ALL_EMPLOYEES);
            //while (rs.next()) {
                Employee employee = new Employee();
                //employee.setId(rs.getInt(1));
                //employee.setName(rs.getString(2));
                lst.add(employee);
           // }
        //} catch (SQLException e) {
        //    e.printStackTrace();
        } finally {
            //closePreparedStatement(ps);
        }

        return lst;
    }

    @Override
    public Employee getEntityById(Integer id) {
        return null;
    }

    @Override
    public Employee update(Employee entity) {
        return null;
    }

    @Override
    public boolean delete(Integer id) {
        return false;
    }

    @Override
    public boolean create(Employee entity) {
        return false;
    }
}
