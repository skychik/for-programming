package ru.ifmo.cs.programming.lab5.core;

/**
 * todo Исклюючение не содержит ничего нового. Зачем его добавлять?
 */
public class NegativeSalaryException extends ArithmeticException {

    public NegativeSalaryException(){}

    public NegativeSalaryException(String msg) {
        super(msg);
    }
}