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

public class TasksXMLParser extends DefaultHandler {
    public static final String TASKS_GROUP_TAG_NAME = "tasks";
    public static final String OLD_TASK_STRING = "old_task";
    public static final String TASK_TAG_NAME = "task";
    public static final String TITLE_TAG_NAME="title";
    public static final String ACTIVE_TAG_NAME="active";
    public static final String REPEAT_TAG_NAME="repeat";
    public static final String START_DATE_TAG_NAME = "start";
    public static final String END_DATE_TAG_NAME = "end";
    public static final String INTERVAL_TAG_NAME= "interval";
    private String sourceXML;
    private String oldTask;
    private TaskStringForm curTask;
    private List<TaskStringForm> tasks;
    private boolean groupTask;
    private String curTag;
    private boolean isOldTask;

    public TasksXMLParser(String sourceXML) throws SAXException, ParserConfigurationException,IOException{
        this.sourceXML = sourceXML;
        initParse();
    }

    private void initParse() throws SAXException, ParserConfigurationException, IOException{
        SAXParserFactory.newInstance().newSAXParser().parse(new InputSource(new StringReader(sourceXML)),this);
    }

    public static void writeTasksByXML(XMLStreamWriter writer, List<TaskStringForm> tasks) throws XMLStreamException {
        writer.writeStartElement(TASKS_GROUP_TAG_NAME);
            for (TaskStringForm task : tasks){
                writeTaskByXML(writer,task);
            }
        writer.writeEndElement();
    }

    public static void writeOldNewTaskByXML(XMLStreamWriter writer, String oldTask,TaskStringForm newTask)
            throws XMLStreamException{
            writer.writeStartElement(TASKS_GROUP_TAG_NAME);
                writer.writeStartElement(OLD_TASK_STRING);
                    writer.writeCharacters(oldTask);
                writer.writeEndElement();
                writeTaskByXML(writer,newTask);
            writer.writeEndElement();
    }

    public static void writeOldTaskByXML(XMLStreamWriter writer, String oldTask)
            throws XMLStreamException{
        writer.writeStartElement(OLD_TASK_STRING);
            writer.writeCharacters(oldTask);
        writer.writeEndElement();
    }

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

    public String getOldTask(){
        return oldTask;
    }

    public List<TaskStringForm> getStringFormTasks(){
        return tasks==null?new ArrayList<TaskStringForm>():tasks;
    }

    public TaskStringForm getStringFormTask(){
        return curTask;
    }
}
