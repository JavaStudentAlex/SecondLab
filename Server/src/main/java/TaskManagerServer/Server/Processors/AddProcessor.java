package TaskManagerServer.Server.Processors;

import CommonClasses.*;
import Exceptions.*;
import TaskManagerServer.Server.ConnectionHandler;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * The process controls the adding a new task.
 */
public class AddProcessor extends AbstractProcessor {

    /**
     * The logger of the class.
     */
    private static Logger logger = Logger.getLogger(AddProcessor.class);

    /**
     * The constructor get the connection link, save it and also set the name of processor.
     * @param handler the link tc connection.
     * @see Protocol
     */
    public AddProcessor(ConnectionHandler handler) {
        super(handler);
        process = Protocol.ADD;
    }

    /**
     * The main method that assert the right login, get the sent task and parse it with the xml parser to string form,
     * and than to real task class . In conclusion the task is saved to the local repository and the OK status is sent
     * by the output message. if something goes wrong the message is sent by the output stream.
     * @param reader the input stream.
     * @param writer the output stream.
     * @throws IOException the error is thrown where the stream is broken.
     */
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
                logger.info("The same task");
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
