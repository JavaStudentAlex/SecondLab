package TaskManagerServer.Server.XMLParsers;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * The class that represents the xml parser for users info.
 */
public class UsersXMLParser extends DefaultHandler {

    /**
     * The logger of the class.
     */
    private static Logger logger = Logger.getLogger(UsersXMLParser.class);

    /**
     * The tag names.
     */
    private static final String USERS_TAG_NAME = "users",USER_TAG_NAME = "user",PASSWORD_TAG_NAME = "password";

    /**
     * The current user name.
     */
    private String curUser;

    /**
     * The current password.
     */
    private String curPass;

    /**
     * The current tag.
     */
    private String curTag;

    /**
     * The name of parsing file.
     */
    private File usersFile;

    /**
     * The container for user info.
     */
    private Map<String, String> users;

    /**
     * The constructor gets the file path and save it to the field usersFile. Than create container for tasks, assert
     * the file/ dir and if not exists - create it. If something goes wrong the file is recreating.
     * @param file the file path.
     * @throws IOException the error class that is thrown when the file stream is broken.
     */
    public UsersXMLParser(File file) throws IOException{
        usersFile = file;
        users = new HashMap<String, String>();
        if (!usersFile.exists()){
            usersFile.createNewFile();
        }
        else {
            try {
                SAXParserFactory.newInstance().newSAXParser().parse(usersFile,this);
            } catch (SAXException | ParserConfigurationException e) {
                logger.warn("Error in xml parsing");
                file.delete();
                file.createNewFile();
                return;
            }
        }
    }

    /**
     * The get method for the users list.
     * @return
     */
    public Map<String, String> getAllUsers(){
        return users;
    }

    /**
     * The method is called when the tag start.
     * @param uri the uri of the namespace.
     * @param localName the name of the namespace.
     * @param qName the tag name.
     * @param attributes the list of attributes in the tag.
     * @throws SAXException the error of the parsing.
     */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        curTag=qName;
    }

    /**
     * The method is called when the symbols in tag meet.
     * @param ch the all symbols.
     * @param start the start pos of symbols.
     * @param length the length of symbols you need.
     * @throws SAXException the error of the parsing.
     */
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        switch (curTag){
            case USER_TAG_NAME : curUser = new String(ch,start,length); break;
            case PASSWORD_TAG_NAME : curPass = new String(ch,start,length); break;
        }
    }

    /**
     * The method is called when the tag finish.
     * @param uri the uri of the namespace.
     * @param localName the name of the namespace.
     * @param qName the tag name.
     * @throws SAXException the error of the parsing.
     */
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if(qName.equals(PASSWORD_TAG_NAME)){
            users.put(curUser,curPass);
            curUser=null;
            curPass=null;
            curTag="";
        }
    }

    /**
     * The method write the customers from the container to the current file.
     * @param customers the container link.
     * @throws IOException the error class that is thrown when the error in i/o.
     */
    public void writeUsers(Map<String,String> customers) throws IOException{
        try {
            XMLStreamWriter writer = XMLOutputFactory.newFactory().createXMLStreamWriter(new FileOutputStream(usersFile));
            writer.writeStartElement(USERS_TAG_NAME);
                for(Map.Entry<String,String> customer : users.entrySet()){
                    writer.writeStartElement(USER_TAG_NAME);
                        writer.writeCharacters(customer.getKey());
                    writer.writeEndElement();
                    writer.writeStartElement(PASSWORD_TAG_NAME);
                        writer.writeCharacters(customer.getValue());
                    writer.writeEndElement();
                }
            writer.writeEndElement();
        } catch (XMLStreamException e) {
            throw new IOException(e);
        } catch (FileNotFoundException e) {
            logger.warn("File not found, create new");
            usersFile.delete();
            usersFile.createNewFile();
            return;
        }
    }
}
