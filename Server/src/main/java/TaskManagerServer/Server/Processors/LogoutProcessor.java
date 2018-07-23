package TaskManagerServer.Server.Processors;

import TaskManagerServer.Server.ConnectionHandler;
import CommonClasses.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * The processor controls the logging out.
 */
public class LogoutProcessor extends AbstractProcessor {

    /**
     * The constructor gets the link to connection thread and set the name of the processor.
     * @param handler the link to connection thread.
     * @see Protocol
     */
    public LogoutProcessor(ConnectionHandler handler) {
        super(handler);
        process = Protocol.LOGOUT;
    }

    /**
     * The method set the logging out operation to the connection thread by the link.
     * @param reader the input stream.
     * @param writer the output stream.
     * @see ConnectionHandler#logout()
     */
    @Override
    public void process(DataInputStream reader, DataOutputStream writer){
        handler.logout();
    }
}
