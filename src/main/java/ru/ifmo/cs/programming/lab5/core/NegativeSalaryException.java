package ru.ifmo.cs.programming.lab5.core;

/**
 * todo Исклюючение не содержит ничего нового. Зачем его добавлять?
 */
public class NegativeSalaryException extends RuntimeException {

    /**
     * Creates a new instance of <code>NegativeSalaryException</code> without
     * detail message.
     */
    public NegativeSalaryException() {
    }

    /**
     * Constructs an instance of <code>NegativeSalaryException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public NegativeSalaryException(String msg) {
        super(msg);
    }
}