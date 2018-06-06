package TaskManagerServer.Server.Processors;

import TaskManagerServer.Server.ConnectionHandler;
import TaskManagerServer.CommonClasses.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class RegisterProcessor extends AbstractProcessor {

    public RegisterProcessor(ConnectionHandler handler) {
        super(handler);
        process = Protocol.REGISTER;
    }

    @Override
    public void process(DataInputStream reader, DataOutputStream writer) throws IOException {
        String user = reader.readUTF();
        String pass = reader.readUTF();
        handler.logout();
        if(handler.containsUser(user,pass,false)){
            writer.writeBoolean(false);
            writer.flush();
            return;
        }
        handler.writeUser(user,pass);
        writer.writeBoolean(true);
        writer.flush();
    }
}
