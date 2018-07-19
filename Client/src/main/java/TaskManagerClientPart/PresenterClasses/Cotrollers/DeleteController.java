package TaskManagerClientPart.PresenterClasses.Cotrollers;

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
 * The class, that control removing task by user.
 */
public class DeleteController extends AbstractController{
    private static Logger logger = Logger.getLogger(DeleteController.class);

    public DeleteController(IView gui, WebClient client){
        super(gui,client);
    }

    @Override
    protected void init() {
        gui.setDeleteListener(new DeleteListener());
    }

    /**
     * The event listener, that react on deleting task.
     */
    private class DeleteListener implements ActionListener { // for buttons
        /**
         * The method that handle the event
         * @param e The object of event
         * @see DeleteController#deleting()
         */
        @Override
        public void actionPerformed(ActionEvent e) {  // switch buttons
            deleting();
        }
    }

    private void deleting(){
        String deleted = gui.getSelectedTask();
        if (deleted==null){
            gui.showErrorMessage("No marked to delete");
            return;
        }

        StringWriter outString = new StringWriter();
        try {
            XMLStreamWriter writer = XMLOutputFactory.newFactory().createXMLStreamWriter(outString);
            TasksXMLParser.writeOldTaskByXML(writer,deleted);
        } catch (XMLStreamException e) {
            logger.warn("XML writing problems in deleting");
            gui.showErrorMessage("XML writing problems");
            return;
        }
        String xmlDeleted = outString.toString();

        if(client.removeTaskByTitle(xmlDeleted,deleted)){
            gui.clearTaskFields();
        }
    }
}
