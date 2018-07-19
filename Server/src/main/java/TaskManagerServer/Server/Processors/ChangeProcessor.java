package TaskManagerServer.Server.Processors;

import Exceptions.*;
import TaskManagerServer.Server.ConnectionHandler;
import CommonClasses.*;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ChangeProcessor extends AbstractProcessor {

    private static Logger logger = Logger.getLogger(ChangeProcessor.class);

    public ChangeProcessor(ConnectionHandler handler){
        super(handler);
        process= Protocol.CHANGE;
    }

    @Override
    public void process(DataInputStream reader, DataOutputStream writer) throws IOException {
        String oldNewTaskXML = reader.readUTF();
        System.out.println(oldNewTaskXML);
        if(handler.getUser()==null){
            SocketWorker.writeTheErrorByWriter(writer,"Not logged - Desynchronization");
            handler.stopThis();
            return;
        }
        String oldTask;
        TaskStringForm newStringTask;
        Task newTask;
        try {
            TasksXMLParser tempParser = new TasksXMLParser(oldNewTaskXML);
            oldTask = tempParser.getOldTask();
            newStringTask=tempParser.getStringFormTask();
            if(!handler.containsTask(oldTask)){
                SocketWorker.writeTheErrorByWriter(writer,"Task not found on server - Desynchronization");
                handler.stopThis();
                return;
            }
            newTask=newStringTask.parseTask();
            if(handler.containsTask(newTask.getTitle())&&!oldTask.equals(newTask.getTitle())){
                throw new SameTaskException();
            }

        } catch (SAXException | ParserConfigurationException e) {
            SocketWorker.writeTheErrorByWriter(writer,"Server XMl error");
            logger.warn("Server XMl error");
            return;
        }
        catch (MyOwnException e){
            SocketWorker.writeTheErrorByWriter(writer,e.getMessage());
            return;
        }
        handler.removeOldTask(oldTask);
        handler.addNewTask(newTask);
        writer.writeBoolean(true);
        writer.flush();
    }
}
