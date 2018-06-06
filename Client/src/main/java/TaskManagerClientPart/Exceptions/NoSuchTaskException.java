package TaskManagerClientPart.Exceptions;

/**
 * The special exception with mining, that container din not find exception, you want to see on some position.
 */
public class NoSuchTaskException extends IllegalStateException{

    /**
     * The constructor that create an exception object and also initialize it with super class(empty constructor).
     */
    public NoSuchTaskException(){super();}

    /**
     * The constructor that create a new exception instance and also give in params a special message, that also go to
     * super class constructor
     * @param msg - a special message
     */
    public NoSuchTaskException(String msg){
        super(msg);
    }
}
