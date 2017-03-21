package laba2;

/**
 * Created by skychik on 21.03.2017.
 */

class ByteOverflowException extends ArithmeticException{

    ByteOverflowException(){}

    ByteOverflowException(String msg){
        super(msg);
    }
}
