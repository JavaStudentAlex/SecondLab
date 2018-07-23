package CommonClasses;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * The class for parsing tasks from xml format using SAX parser.
 */
public class TasksXMLParser extends DefaultHandler {
    /**
     * The name for tasks group tag.
     */
    public static final String TASKS_GROUP_TAG_NAME = "tasks";

    /**
     * The name for old task tag.
     */
    public static final String OLD_TASK_STRING = "old_task";

    /**
     * The name for task tag.
     */
    public static final String TASK_TAG_NAME = "task";

    /**
     * The name for title field tag.
     */
    public static final String TITLE_TAG_NAME="title";

    /**
     * The name for active field tag.
     */
    public static final String ACTIVE_TAG_NAME="active";

    /**
     * The name for repeat field tag.
     */
    public static final String REPEAT_TAG_NAME="repeat";

    /**
     * The name for start field tag.
     */
    public static final String START_DATE_TAG_NAME = "start";

    /**
     * The name for end field tag.
     */
    public static final String END_DATE_TAG_NAME = "end";

    /**
     * The name for interval field tag.
     */
    public static final String INTERVAL_TAG_NAME= "interval";
    private String sourceXML;
    private String oldTask;
    private TaskStringForm curTask;
    private List<TaskStringForm> tasks;
    private boolean groupTask;
    private String curTag;

    /**
     * The method that create new object of the class with the xml source string and start init the parsing.
     * @param sourceXML the xml string.
     * @throws SAXException the error if the document not correct.
     * @throws ParserConfigurationException if the configuration of parser incorrect.
     * @throws IOException if the error in input stream.
     */
    public TasksXMLParser(String sourceXML) throws SAXException, ParserConfigurationException,IOException{
        this.sourceXML = sourceXML;
        initParse();
    }

    /**
     * The method that parse.
     * @throws SAXException the error if the document not correct.
     * @throws ParserConfigurationException if the configuration of parser incorrect.
     * @throws IOException if the error in input stream.
     */
    private void initParse() throws SAXException, ParserConfigurationException, IOException{
        SAXParserFactory.newInstance().newSAXParser().parse(new InputSource(new StringReader(sourceXML)),this);
    }

    /**
     * The method that write the tasks in special format to writer.
     * @param writer - the stream writer.
     * @param tasks the list of the tasks in string form for writing.
     * @throws XMLStreamException the error of writing in xml stream.
     */
    public static void writeTasksByXML(XMLStreamWriter writer, List<TaskStringForm> tasks) throws XMLStreamException {
        writer.writeStartElement(TASKS_GROUP_TAG_NAME);
            for (TaskStringForm task : tasks){
                writeTaskByXML(writer,task);
            }
        writer.writeEndElement();
    }

    /**
     * The method that write the pair of tasks in string form - the old one and new one.
     * @param writer the xml stream for writing.
     * @param oldTask the old one.
     * @param newTask the new one.
     * @throws XMLStreamException the error of writing in xml stream.
     */
    public static void writeOldNewTaskByXML(XMLStreamWriter writer, String oldTask,TaskStringForm newTask)
            throws XMLStreamException{
            writer.writeStartElement(TASKS_GROUP_TAG_NAME);
                writer.writeStartElement(OLD_TASK_STRING);
                    writer.writeCharacters(oldTask);
                writer.writeEndElement();
                writeTaskByXML(writer,newTask);
            writer.writeEndElement();
    }

    /**
     * The method that write the old one task in string form.
     * @param writer the xml stream for writing.
     * @param oldTask the old one.
     * @throws XMLStreamException the error of writing in xml stream.
     */
    public static void writeOldTaskByXML(XMLStreamWriter writer, String oldTask)
            throws XMLStreamException{
        writer.writeStartElement(OLD_TASK_STRING);
            writer.writeCharacters(oldTask);
        writer.writeEndElement();
    }

    /**
     * The method that write the one task in string form.
     * @param writer the xml stream for writing.
     * @param task the task in string form.
     * @throws XMLStreamException the error of writing in xml stream.
     */
    public static void writeTaskByXML(XMLStreamWriter writer, TaskStringForm task) throws XMLStreamException{
        writer.writeStartElement(TASK_TAG_NAME);
        writer.writeStartElement(TITLE_TAG_NAME);
            writer.writeCharacters(task.getTitle());
        writer.writeEndElement();
        writer.writeStartElement(ACTIVE_TAG_NAME);
            writer.writeCharacters(task.isActive()+"");
        writer.writeEndElement();
        writer.writeStartElement(REPEAT_TAG_NAME);
            writer.writeCharacters(task.isRepeat()+"");
        writer.writeEndElement();
        writer.writeStartElement(START_DATE_TAG_NAME);
            writer.writeCharacters(task.getStartDate());
        writer.writeEndElement();
        if(task.isRepeat()) {
            writer.writeStartElement(END_DATE_TAG_NAME);
                writer.writeCharacters(task.getEndDate());
            writer.writeEndElement();
            writer.writeStartElement(INTERVAL_TAG_NAME);
                writer.writeCharacters(task.getInterval());
            writer.writeEndElement();
        }
        writer.writeEndElement();
    }

    /**
     * The method overrides from the parent class the behaviour with the start of the tag.
     * @param uri The Namespace URI.
     * @param localName The local name of namespace.
     * @param qName The name of tag.
     * @param attributes The attributes attached to the element.
     * @throws SAXException the error of reading from xml stream.
     */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        switch (qName){
            case TASKS_GROUP_TAG_NAME : tasks = new ArrayList<TaskStringForm>();
                                        groupTask = true;
                                        break;
            case TASK_TAG_NAME : curTask = new TaskStringForm();
                                 break;
        }
        curTag=qName;
    }

    /**
     * The method overrides from the parent class the behaviour with the end of the tag.
     * @param uri The Namespace URI.
     * @param localName The local name of namespace.
     * @param qName The name of tag.
     * @throws SAXException the error of reading from xml stream.
     */
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if(qName.equals(TASK_TAG_NAME)&&groupTask&&oldTask==null){
            if(!curTask.isRepeat()){
                if(curTask.getTitle()==null || curTask.getStartDate()==null){
                    return;
                }
            }
            else{
                if(curTask.getTitle()==null||curTask.getStartDate()==null
                        ||curTask.getEndDate()==null||curTask.getInterval()==null){
                    return;
                }
            }
            tasks.add(curTask);
            curTask=null;
        }
        curTag="";
    }

    /**
     * The method overrides from the parent class the behaviour with the characters into the tag space.
     * @param ch - array of characters of the document.
     * @param start - start of the target.
     * @param length - end of the target , not included.
     * @throws SAXException the error of reading from xml stream.
     */
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String result = new String(ch,start,length);
        switch (curTag){
            case TITLE_TAG_NAME : curTask.setTitle(result);
                                  break;
            case ACTIVE_TAG_NAME : curTask.setActive(result.equals("true"));
                                    break;
            case REPEAT_TAG_NAME : curTask.setRepeat(result.equals("true"));
                                    break;
            case START_DATE_TAG_NAME : curTask.setStartDate(result);
                                        break;
            case END_DATE_TAG_NAME : curTask.setEndDate(result);
                                     break;
            case INTERVAL_TAG_NAME : curTask.setInterval(result);
                                    break;
            case OLD_TASK_STRING : oldTask = result;
                                    break;
        }
    }

    /**
     * The method is called after creating of object to get the old one task.
     * @return string if there was the old task(tag) and null if not.
     */
    public String getOldTask(){
        return oldTask;
    }

    /**
     * The method is called after creating of object to get the tasks.
     * @return the list of tasks in string form if there was a group tasks tag and null if not.
     */
    public List<TaskStringForm> getStringFormTasks(){
        return tasks==null?new ArrayList<TaskStringForm>():tasks;
    }

    /**
     * The method is called after creating of object to get the task.
     * @return the tasks in the string form if the task tag was and null if not.
     */
    public TaskStringForm getStringFormTask(){
        return curTask;
    }
}
