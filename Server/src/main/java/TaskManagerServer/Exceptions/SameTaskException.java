package TaskManagerServer.Exceptions;

/**
 * The special exception with mining, that user have added the task with title, that already used.
 */
public class SameTaskException extends MyOwnException {

    /**
     * The constructor that create a super class with special for this exception message about same task.
     */
    public SameTaskException(){
        super("There is the same task");
    }
}
