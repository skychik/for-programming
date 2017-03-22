package ru.ifmo.cs.programming.lab5.core;

/**
 * Created by skychik on 21.03.2017.
 */

public class ByteOverflowException extends ArithmeticException{

    public ByteOverflowException(){}

    public ByteOverflowException(String msg){
        super(msg);
    }
}
