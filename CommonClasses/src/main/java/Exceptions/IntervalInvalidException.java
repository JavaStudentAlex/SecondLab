package Exceptions;

/**
 * The special exception with mining, that user have done invalid input into interval field.
 */
public class IntervalInvalidException extends MyOwnException {

    /**
     * The constructor that create a super class with special for this exception message about interval field.
     */
    public IntervalInvalidException(){
        super("Invalid interval input");
    }
}
