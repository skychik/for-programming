package laba2;

public class NegativeSalaryException extends Exception {

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