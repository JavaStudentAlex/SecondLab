package TaskManagerClientPart.PresenterClasses.Cotrollers;

import CommonClasses.TaskStringForm;
import CommonClasses.TasksXMLParser;
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
 *The element, that control adding a new task in the model repository
 */
public class AddController extends AbstractController {
    private static Logger logger = Logger.getLogger(AddController.class);
    public AddController(IView gui, WebClient client) {
        super(gui, client);
    }

    @Override
    protected void init() {
        gui.setAddListener(new AddListener());
    }


    private void adding(){
        TaskStringForm result = gui.getNewTask();
        if(result==null){
            gui.showErrorMessage("Error in task reading");
            return;
        }
        StringWriter outString = new StringWriter();
        try {
            XMLStreamWriter writer = XMLOutputFactory.newFactory().createXMLStreamWriter(outString);
            TasksXMLParser.writeTaskByXML(writer,result);
        } catch (XMLStreamException e) {
            logger.info("XML writing problems in adding");
            gui.showErrorMessage("XML writing problems");
            return;
        }

        String taskXML = outString.toString();
        if(client.addNewTask(taskXML,result)){
            gui.clearTaskFields();
        }
    }
    /**
     * The event listener, that react on adding a new task.
     */
    private class AddListener implements ActionListener {

        /**
         * The method that handle the event
         * @param e The object of event
         * @see AddController#adding()
         */
        @Override
        public void actionPerformed(ActionEvent e) {  // switch buttons
            adding();
        }
    }
}
