package TaskManagerServer.Server.Processors;

import TaskManagerServer.CommonClasses.*;
import TaskManagerServer.Exceptions.*;
import TaskManagerServer.ModelClasses.Task;
import TaskManagerServer.Server.ConnectionHandler;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class AddProcessor extends AbstractProcessor {
    public AddProcessor(ConnectionHandler handler) {
        super(handler);
        process = Protocol.ADD;
    }

    @Override
    public void process(DataInputStream reader, DataOutputStream writer) throws IOException {
        String taskXML = reader.readUTF();
        if(handler.getUser()==null){
            SocketWorker.writeTheErrorByWriter(writer,"Not logged - Desynchronization");
            handler.stopThis();
            return;
        }
        TaskStringForm taskString;
        Task task;
        try {
            taskString = new TasksXMLParser(taskXML).getStringFormTask();
            task=taskString.parseTask();
            if(handler.containsTask(task.getTitle())){
                throw new SameTaskException();
            }
        } catch (SAXException | ParserConfigurationException e) {
            SocketWorker.writeTheErrorByWriter(writer,"Server XMl error");
            return;
        }
        catch (MyOwnException e){
            SocketWorker.writeTheErrorByWriter(writer,e.getMessage());
            return;
        }
        handler.addNewTask(task);
        writer.writeBoolean(true);
        writer.flush();
    }
}
