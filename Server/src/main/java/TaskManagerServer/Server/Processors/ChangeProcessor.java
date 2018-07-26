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

/**
 * The processor controls the change of the task.
 */
public class ChangeProcessor extends AbstractProcessor {

    /**
     * The logger of the class.
     */
    private static Logger logger = Logger.getLogger(ChangeProcessor.class);

    /**
     * The constructor get the link on the connection and set the name of the processor.
     * @param handler the link to connection.
     * @see Protocol
     */
    public ChangeProcessor(ConnectionHandler handler){
        super(handler);
        process= Protocol.CHANGE;
    }

    /**
     * The method assert user name and login, get the xml pair old - new task, parse it to string title old task and
     * string formatted task and than get the real new task object. In conclusion the task with the title of old task
     * is removed from the local repository and the new task add and the OK status is sent by the output message. If
     * something wrong  - the error message is sent by the output stream.
     * @param reader the input stream.
     * @param writer the output stream.
     * @throws IOException the error is thrown where the stream is broken.
     */
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
        handler.removeOldTask(oldTask, true);
        handler.addNewTask(newTask);
        writer.writeBoolean(true);
        writer.flush();
    }
}
