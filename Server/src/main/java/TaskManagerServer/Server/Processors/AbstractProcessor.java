package TaskManagerServer.Server.Processors;

import TaskManagerServer.Server.ConnectionHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * The parent class for all processors.
 */
public abstract class AbstractProcessor {

    /**
     * The identify name of the processor.
     */
    protected String process;

    /**
     * The link to the single connection (client - server) in one thread.
     */
    protected ConnectionHandler handler;

    /**
     * The constructor of the class that get the connection and save it link in the object.
     * @param handler the connection link.
     */
    public AbstractProcessor(ConnectionHandler handler) {
        this.handler = handler;
    }

    /**
     * The method get the string argument and say is it equals with the name of processor. This method is called to
     * find the processor we need to use.
     * @param example the name of the needy processor.
     * @return true if the argument and the local name equals and false if not.
     */
    public boolean canProcess(String example){
        return process.equals(example);
    }

    /**
     * The main method of the class that will be overridden in child classes.
     * @param reader the input stream.
     * @param writer the output stream.
     * @throws IOException the error class.
     */
    public abstract void process(DataInputStream reader, DataOutputStream writer) throws IOException;


}
