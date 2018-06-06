package TaskManagerServer.Exceptions;

public class MyOwnException extends Exception {
    public MyOwnException(String message){
        super(message);
    }

    public MyOwnException(){super();}
}
