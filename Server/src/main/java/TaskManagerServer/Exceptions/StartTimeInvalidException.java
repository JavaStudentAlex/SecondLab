package TaskManagerServer.Exceptions;

/**
 * The special exception with mining, that user have done invalid input into start time field.
 */
public class StartTimeInvalidException extends MyOwnException {
    /**
     * The constructor that create a super class with special for this exception message about start time field.
     */
    public StartTimeInvalidException(){
        super("Invalid start time input");
    }
}
