package TaskManagerClientPart.MenuClasses;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import CommonClasses.Constrains;
import CommonClasses.TaskStringForm;

/**
 * The class that keep together all elements for working with tasks and it's info
 */
public class TasksContent{

    /**
     * The element of control list of tasks
     */
    private ListContent listContent;

    /**
     * The element of control elements for edition tasks info
     */
    private EditionContent editionContent;
    /**
     * The main container panel for all other elements
     */
    public JPanel tasksMainRootPanel;

    /**
     * The constructor that create,initialize and place elements
     * @see TasksContent#inializeElems()
     */
    public TasksContent(){
        inializeElems();
    }

    /**
     * The method initialize create and place elements on the main container {@code tasksMainRootPanel}
     * @see PanelFactory
     * @see Constrains
     */
    private void inializeElems(){
        tasksMainRootPanel = PanelFactory.createPanel(View.menuTasks);
        listContent = new ListContent(new ResetListener());
        editionContent = new EditionContent();
        tasksMainRootPanel.setLayout(new GridBagLayout());
        GridBagConstraints loc = Constrains.getLocator();
        Constrains.setLocation(loc,0,0);
        loc.weighty = 0.28;
        tasksMainRootPanel.add(listContent.tasksRootPanel,loc);
        Constrains.setLocation(loc,0,1);
        loc.weighty = 0.72;
        tasksMainRootPanel.add(editionContent.editionRootPanel,loc);
    }

    /**
     * The handler class for {@code reset} button pressing
     */
    private class ResetListener implements ActionListener{
        /**
         * The method that handle the event
         * @param e - the event object
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            editionContent.clearFields();
        }
    }

    /**
     * The method control clearing fields in his content parts
     * @see EditionContent#clearFields()
     * @see ListContent#setClearSelection()
     */
    public void clear(){
        editionContent.clearFields();
        listContent.setClearSelection();
    }

    public void clearTaskFields(){
        editionContent.clearFields();
    }

    /**
     * The method control getting new task performance and delegate this function to relevant functions of it's
     * content containers
     * @return the task formatted to string
     */
    public TaskStringForm getNewTask(){
        return editionContent.getNewTask();
    }

    /**
     * The method controls the renewing of tasks list and delegate it to list content container
     * @param tasks - array of task's titles
     */
    public void renewTasks(List<String> tasks){
        listContent.renewTasks(tasks);
    }

    /**
     * The method delegates setting handler to relevant list content container
     * @param listener - the handler
     */
    public void setSelectionListener(ListSelectionListener listener){
        listContent.setSelectionListener(listener);
    }

    /**
     * The method delegate setting task's info to fields to relevant edition content container
     * @param task - the task formatted to string
     */
    public void setTaskToFields(TaskStringForm task){
        editionContent.setTaskToFields(task);
    }

    /**
     * The method delegate getting selected task to relevant list content container
     * @return the task's title string
     */
    public String getSelectedTask(){
        return listContent.getSelectedTask();
    }

    /**
     * The method delegate setting handler for adding to relevant list content container
     * @param listener - the handler
     */
    public void setAddListener(ActionListener listener){
        listContent.setAddListener(listener);
    }

    /**
     * The method delegate setting handler for changing to relevant list content container
     * @param listener - the handler
     */
    public void setChangeListener(ActionListener listener){
        listContent.setChangeListener(listener);
    }

    /**
     * The method delegate setting handler for removing to relevant list content container
     * @param listener - the handler
     */
    public void setDeleteListener(ActionListener listener){
        listContent.setDeleteListener(listener);
    }
}
