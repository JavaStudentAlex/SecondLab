package TaskManagerServer.Server.Processors;

import TaskManagerServer.Server.ConnectionHandler;
import CommonClasses.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * The processor controls the remove process.
 */
public class RemoveProcessor extends AbstractProcessor {

    /**
     * The method gets the link to connection thread and set the name of the process.
     * @param handler the link to the connection thread.
     * @see Protocol
     */
    public RemoveProcessor(ConnectionHandler handler) {
        super(handler);
        process= Protocol.REMOVE;
    }

    /**
     * The method asserts the logging in, gets the title of the task, find it in the local repository and remove. After
     * this the message is sent to the user. But if something is wrong - teh error message is sent by the output stream.
     * @param reader the input stream.
     * @param writer the output stream.
     * @throws IOException the error is thrown where the stream breaks out.
     */
    @Override
    public void process(DataInputStream reader, DataOutputStream writer) throws IOException {
        String oldTaskXML = reader.readUTF();
        if(handler.getUser()==null){
            SocketWorker.writeTheErrorByWriter(writer,"Not logged - Desynchronization");
            handler.stopThis();
            return;
        }
        String oldTask;
        try {
            oldTask = new TasksXMLParser(oldTaskXML).getOldTask();
            if(!handler.containsTask(oldTask)){
                SocketWorker.writeTheErrorByWriter(writer,"Task not found on server - Desynchronization");
                handler.stopThis();
                return;
            }
        } catch (SAXException | ParserConfigurationException e) {
            SocketWorker.writeTheErrorByWriter(writer,"Server XMl error");
            return;
        }
        handler.removeOldTask(oldTask);
        writer.writeBoolean(true);
        writer.flush();
    }
}
