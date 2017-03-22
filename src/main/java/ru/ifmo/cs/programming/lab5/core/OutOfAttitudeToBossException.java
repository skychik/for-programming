package ru.ifmo.cs.programming.lab5.core;

/**
 * Created by admin on 21.03.2017.
 */
public class OutOfAttitudeToBossException extends ArithmeticException {

    public OutOfAttitudeToBossException(){
    }

    public OutOfAttitudeToBossException(String msg){
        super(msg);
    }
}
