package TaskManagerServer.Server.Processors;

import TaskManagerServer.Server.ConnectionHandler;
import CommonClasses.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class LogoutProcessor extends AbstractProcessor {
    public LogoutProcessor(ConnectionHandler handler) {
        super(handler);
        process = Protocol.LOGOUT;
    }

    @Override
    public void process(DataInputStream reader, DataOutputStream writer){
        handler.logout();
    }
}
