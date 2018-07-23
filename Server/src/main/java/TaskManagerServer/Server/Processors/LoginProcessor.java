package TaskManagerServer.Server.Processors;

import TaskManagerServer.Server.ConnectionHandler;
import CommonClasses.*;
import org.apache.log4j.Logger;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.StringWriter;

/**
 * The processor that controls the logging in.
 */
public class LoginProcessor extends AbstractProcessor {

    /**
     * The logger of the class.
     */
    private static Logger logger = Logger.getLogger(LoginProcessor.class);

    /**
     * The constructor that get the link to connection thread and set the name of processor.
     * @param handler the link to connection thread.
     * @see Protocol
     */
    public LoginProcessor(ConnectionHandler handler) {
        super(handler);
        process = Protocol.LOGIN;
    }

    /**
     * The method that get the user name and the password, compare it with the info on the server and if all is ok send
     * the message to the user. Also the all tasks of logged user is read from the file in it`s directory and all of
     * them are sent by the output stream in the xml format. If something goes wrong the error message is sent by the
     * output stream and the method breaks out.
     * @param reader the input stream.
     * @param writer the output stream.
     * @throws IOException the error is thrown where the stream breaks out.
     */
    @Override
    public void process(DataInputStream reader, DataOutputStream writer) throws IOException {
        String user = reader.readUTF();
        String pass = reader.readUTF();
        handler.logout();
        if(!handler.containsUser(user,pass,true) || handler.isLogged(user)){
            writer.writeBoolean(false);
            writer.flush();
            return;
        }
        handler.setUser(user);
        writer.writeBoolean(true);
        StringWriter outString=new StringWriter();
        try {
            XMLStreamWriter stringWriter = XMLOutputFactory.newFactory().createXMLStreamWriter(outString);

            TasksXMLParser.writeTasksByXML(stringWriter,handler.getCurrentTasksStringList());
        } catch (XMLStreamException e) {
            logger.warn("XML stream error");
            writer.writeUTF("");
            writer.flush();
            return;
        }
        String sourceXml = outString.toString();
        writer.writeUTF(sourceXml);
        writer.flush();
    }
}
