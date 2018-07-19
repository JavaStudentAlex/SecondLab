package Exceptions;

/**
 * The special exception with mining, that user have done invalid input into time field.
 */
public class TimeInvalidException extends MyOwnException {

    /**
     * The constructor that create a super class with special for this exception message about time field.
     */
    public TimeInvalidException(){
        super("Invalid time input");
    }
}
