package TaskManagerServer.Server.XMLParsers;

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

public class UsersXMLParser extends DefaultHandler {
    private static final String USERS_TAG_NAME = "users";
    private static final String USER_TAG_NAME = "user";
    private static final String PASSWORD_TAG_NAME = "password";
    private String curUser;
    private String curPass;
    private String curTag;

    private File usersFile;
    private Map<String, String> users;

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
                System.out.println("Error in xml parsing");
                file.delete();
                file.createNewFile();
                return;
            }
        }
    }

    public Map<String, String> getAllUsers(){
        return users;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        curTag=qName;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        switch (curTag){
            case USER_TAG_NAME : curUser = new String(ch,start,length); break;
            case PASSWORD_TAG_NAME : curPass = new String(ch,start,length); break;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if(qName.equals(PASSWORD_TAG_NAME)){
            users.put(curUser,curPass);
            curUser=null;
            curPass=null;
            curTag="";
        }
    }

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
            throw new IOException();
        } catch (FileNotFoundException e) {
            usersFile.delete();
            usersFile.createNewFile();
            return;
        }
    }
}
