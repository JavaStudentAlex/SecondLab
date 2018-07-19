package TaskManagerServer.Server.Processors;

import TaskManagerServer.Server.ConnectionHandler;
import CommonClasses.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class RemoveProcessor extends AbstractProcessor {

    public RemoveProcessor(ConnectionHandler handler) {
        super(handler);
        process= Protocol.REMOVE;
    }

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
