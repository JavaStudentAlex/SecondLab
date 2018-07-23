package Exceptions;

/**
 * This class is used to report about the error in parsing to Date class.
 */
public class ParseDateException extends Exception {

    /**
     * The empty constructor of the class.
     */
    public ParseDateException(){
        super("User exception for date parsing");
    }
}
