
package ru.ifmo.cs.programming.lab8;

import javax.sql.PooledConnection;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.Formatter;
import java.util.Objects;

public class ORMClass {

    private Connection con = null;
    private PreparedStatement preparedStatement;
    private Formatter formatter;
    PooledConnection pooledConnection;

    public ORMClass(Connection con, PooledConnection pooledConnection){
        this.con = con;
        this.pooledConnection = pooledConnection;
    }

    public ORMClass(PooledConnection pooledConnection){
        this.pooledConnection = pooledConnection;
    }

    public void insert(Object obj) {
        formatter = new Formatter();
        String query = formatter.format("INSERT INTO \"%s\" %s VALUES (%s)",
                tableName(obj),
                fieldsNamesToString(obj.getClass()),
                fieldsValuesToString(obj))
                .toString();
        System.out.println(query);
        try {
            preparedStatement = con.prepareStatement(query);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                con.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }

    public void update(Object obj) {
        formatter = new Formatter();
        String query = formatter.format("UPDATE \"%s\" SET %s %s ",
                tableName(obj),
                valuesToSET(obj),
                getWhereId(obj))
                .toString();
        try {
            System.out.println(query);
            preparedStatement = con.prepareStatement(query);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                con.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }

    public void removeEmployee(Object object) {
        formatter = new Formatter();
        String query = formatter.format("DELETE FROM \"%s\" WHERE %s",
                tableName(object), valuesToRemoveSET(object)).toString();
        System.out.println(query);
        try {
            preparedStatement = con.prepareStatement(query);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                con.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }

    public ResultSet readTable(Class _class) {
        Statement stat = null;
        formatter = new Formatter();
        try {
            stat = pooledConnection.getConnection().createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ResultSet res = null;
        try {
            String q = formatter.format("SELECT * FROM \"%s\"",
                    _class.getSimpleName().toUpperCase())
                    .toString();
            res = stat.executeQuery(q);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    public ResultSet selectById(Class _class, long id) {
        Statement stat = null;
        ResultSet resultSet = null;
        formatter = new Formatter();
        try {
            stat = pooledConnection.getConnection().createStatement();
        } catch (SQLException e) {

            e.printStackTrace();
        }
        try {
            String query = formatter.format("SELECT * FROM \"%s\" %s",
                    _class.getSimpleName().toUpperCase(), getWhereId(_class, id)).toString();
            System.out.println(query);
            resultSet = stat.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public void clearTable(Class _class){
        formatter = new Formatter();
        String query = formatter.format("DELETE FROM \"%s\"", _class.getSimpleName().toUpperCase()).toString();
        System.out.println(query);
        try {
            preparedStatement = con.prepareStatement(query);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String fieldsValuesToString(Object obj) {
        Field[] fields;
        Class currentClass = obj.getClass();
        String fieldsString = "";
        String fieldValue = "";
        Objects[] args = null;
        for (; !currentClass.getSimpleName().equals("Object"); currentClass = currentClass.getSuperclass()){
            fields = currentClass.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                try {
                    try {
                        fieldValue = obj.getClass().getMethod("get" +
                                field.getName().substring(0, 1).toUpperCase() +
                                field.getName().substring(1)).invoke(obj, args).toString();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
                if (typeIsNeedComma(field.getType().getSimpleName().toString())){
                    if (field.getType().getSimpleName().toString().equals("ZonedDateTime")){
                        fieldsString += ("\'" + fieldValue.substring(0, fieldValue.indexOf("+") + 3) + "\', ");
                    } else {
                        fieldsString += ("\'" + fieldValue + "\', ");
                    }
                } else {
                    if (field.isAnnotationPresent(MyAnnotation.class)) {
                        if (field.getAnnotation(MyAnnotation.class).name().equalsIgnoreCase("id")){
                            fieldsString += ("DEFAULT, ");
                        }
                    } else {
                        fieldsString += (fieldValue + ", ");
                    }
                }
            }
        }
        return fieldsString.substring(0, fieldsString.length() - 2);
    }

    private String fieldsNamesToString(Class _class) {
        Class currentClass = _class;
        Field[] fields;
        String fieldsString = "";
        String fieldName;
        for (; !currentClass.getSimpleName().equals("Object"); currentClass = currentClass.getSuperclass()){
            fields = currentClass.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                fieldName = field.getName();
                if (typeIsNeedComma(field.getType().getSimpleName().toString())){
                    fieldsString += (fieldName + ", ");
                } else fieldsString += (fieldName + ", ");
            }
        }
        return ("(" + fieldsString.substring(0, fieldsString.length() - 2) + ")");
    }

    private String getWhereId(Object object){
        Class currentClass = object.getClass();
        Field[] fields;
        for (; !currentClass.getSimpleName().equals("Object"); currentClass = currentClass.getSuperclass()) {
            fields = currentClass.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                if (field.isAnnotationPresent(MyAnnotation.class))
                    if (field.getAnnotation(MyAnnotation.class).name().equalsIgnoreCase("id")) {
                        field.setAccessible(true);
                        try {
                            return "WHERE " + field.getName() + " = " + field.get(object);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
            }
        }
        System.out.println("В классе нет поля со значением id");
        return null;
    }

    private String getWhereId(Class _class, long id){
        Class currentClass = _class;
        Field[] fields;
        for (; !currentClass.getSimpleName().equals("Object"); currentClass = currentClass.getSuperclass()) {
            fields = currentClass.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(MyAnnotation.class))
                    if (field.getAnnotation(MyAnnotation.class).name().equalsIgnoreCase("id")) {
                        field.setAccessible(true);
                        return "WHERE " + field.getName() + " = " + id;
                    }
            }
        }
        System.out.println("В классе нет поля со значением id");
        return null;
    }

    private boolean typeIsNeedComma(String javaType){

        switch (javaType) {
            case "String":
                return true;
            case "ZonedDateTime":
                return true;
        }
        return false;
    }

    public void closeORM(){
        try {
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private String valuesToSET(Object obj){
        formatter = new Formatter();
        String valuesSET = "";
        Field[] fields;
        Objects[] args = null;
        Class currentClass = obj.getClass();
        String fieldString = "";
        String fieldValue = "";
        for (; !currentClass.getSimpleName().equals("Object"); currentClass = currentClass.getSuperclass()) {
            fields = currentClass.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                try {
                    fieldValue = currentClass.getMethod("get" +
                            field.getName().substring(0, 1).toUpperCase() +
                            field.getName().substring(1)).invoke(obj, args).toString();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
                if (typeIsNeedComma(field.getType().getSimpleName())) {
                    if (field.getType().getSimpleName().toString().equals("ZonedDateTime")) {
                        fieldString += ("\'" + fieldValue.substring(0, fieldValue.indexOf("+") + 3) + "\', ");
                    } else {
                        valuesSET = formatter.format("%s = \'%s\', ", field.getName(), fieldValue).toString();
                    }
                } else valuesSET += formatter.format("%s = %s, ", field.getName(), fieldValue).toString();
            }
        }
        return valuesSET.substring(0, valuesSET.length() - 2);
    }

    private String valuesToRemoveSET(Object obj){
        formatter = new Formatter();
        String valuesSET = "";
        Field[] fields;
        Objects[] args = null;
        Class currentClass = obj.getClass();
        String fieldString = "";
        String fieldValue = "";
        for (; !currentClass.getSimpleName().equals("Object"); currentClass = currentClass.getSuperclass()) {
            fields = currentClass.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                try {
                    fieldValue = currentClass.getMethod("get" +
                            field.getName().substring(0, 1).toUpperCase() +
                            field.getName().substring(1)).invoke(obj, args).toString();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
                if (typeIsNeedComma(field.getType().getSimpleName())) {
                    if (field.getType().getSimpleName().toString().equals("ZonedDateTime")) {
                        fieldString += ("\'" + fieldValue.substring(0, fieldValue.indexOf("+") + 3) + "\', ");
                    } else {
                        valuesSET = formatter.format("%s = \'%s\' AND ", field.getName(), fieldValue).toString();
                    }
                } else valuesSET += formatter.format("%s = %s AND ", field.getName(), fieldValue).toString();
            }
        }
        return valuesSET.substring(0, valuesSET.length() - 5);
    }

    private String tableName(Object obj) {
        Class currentClass = obj.getClass();
        String tableName = obj.getClass().getSimpleName();
        for (; !currentClass.getSimpleName().equals("Object"); currentClass = currentClass.getSuperclass()){
            if (currentClass.isAnnotationPresent(MyAnnotation.class)){
                if (currentClass.getAnnotation(MyAnnotation.class)
                        .toString()
                        .substring(
                                currentClass.getAnnotation(MyAnnotation.class).toString().indexOf("name=") + 5,
                                currentClass.getAnnotation(MyAnnotation.class).toString().length() - 1)
                        .equalsIgnoreCase("table_name")) {
                    tableName = currentClass.getSimpleName();
                }
            }
        }
        return tableName.toUpperCase();
    }
}