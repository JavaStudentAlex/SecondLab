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

public class LoginProcessor extends AbstractProcessor {

    private static Logger logger = Logger.getLogger(LoginProcessor.class);

    public LoginProcessor(ConnectionHandler handler) {
        super(handler);
        process = Protocol.LOGIN;
    }

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
