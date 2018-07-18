package TaskManagerClientPart.PresenterClasses.Cotrollers;

import TaskManagerClientPart.CommonClasses.TaskStringForm;
import TaskManagerClientPart.CommonClasses.TasksXMLParser;
import TaskManagerClientPart.MenuClasses.IView;
import TaskManagerClientPart.PresenterClasses.WebClient;
import org.apache.log4j.Logger;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.StringWriter;

/**
 * The class, that control changing the tasks by user
 */
public class ChangeController extends AbstractController{
    private static Logger logger = Logger.getLogger(ChangeController.class);

    public ChangeController(IView gui, WebClient client){
        super(gui,client);
    }

    @Override
    protected void init() {
        gui.setChangeListener(new ChangeListener());
    }

    private void changing(){
        String chnaged = gui.getSelectedTask();
        TaskStringForm newTask = gui.getNewTask();
        if (chnaged==null){
            gui.showErrorMessage("No marked to delete");
            return;
        }
        if(newTask==null){return;}

        StringWriter outString = new StringWriter();
        try {
            XMLStreamWriter writer = XMLOutputFactory.newFactory().createXMLStreamWriter(outString);
            TasksXMLParser.writeOldNewTaskByXML(writer,chnaged,newTask);
        } catch (XMLStreamException e) {
            logger.info("XML writing problems in changing");
            gui.showErrorMessage("XML writing problems");
            return;
        }
        String xmlChanged = outString.toString();

        if(client.chnageTaskByTitle(xmlChanged,chnaged,newTask)){
            gui.clearTaskFields();
        }
    }

    /**
     * The event listener, that react on changing task.
     */
    private class ChangeListener implements ActionListener {

        /**
         * The method that handle the event
         * @param e The object of event
         * @see ChangeController#changing()
         */
        @Override
        public void actionPerformed(ActionEvent e) {  // switch buttons
            changing();
        }
    }
}
