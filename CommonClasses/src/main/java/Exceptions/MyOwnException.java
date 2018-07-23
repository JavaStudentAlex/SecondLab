package Exceptions;

/**
 * This parent class will used to create other more strict classes of errors.
 */
public class MyOwnException extends Exception {

    /**
     * The constructor of the class that allow to create error class with message into it.
     * @param message - the text of the message
     */
    public MyOwnException(String message){
        super(message);
    }

    /**
     * The empty constructor of the error class that create the object by default.
     */
    public MyOwnException(){super();}
}
