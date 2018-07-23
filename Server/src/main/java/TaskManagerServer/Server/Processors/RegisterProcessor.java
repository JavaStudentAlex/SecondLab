package TaskManagerServer.Server.Processors;

import TaskManagerServer.Server.ConnectionHandler;
import CommonClasses.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * The processor controls the register process.
 */
public class RegisterProcessor extends AbstractProcessor {

    /**
     * the constructor gets the link to the connection thread.
     * @param handler
     */
    public RegisterProcessor(ConnectionHandler handler) {
        super(handler);
        process = Protocol.REGISTER;
    }

    /**
     * The method gets the user name and password,assert the logging in, and if yes - log out, assert is there the
     * user with same name, and if not - the user is registering and logging in. If something wrong  - the message of
     * error is sent by the output stream.
     * @param reader the input stream.
     * @param writer the output stream.
     * @throws IOException the error is thrown where the stream breaks out.
     */
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
