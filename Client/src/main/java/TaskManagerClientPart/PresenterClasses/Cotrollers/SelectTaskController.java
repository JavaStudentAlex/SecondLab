package TaskManagerClientPart.PresenterClasses.Cotrollers;

import TaskManagerClientPart.MenuClasses.IView;
import TaskManagerClientPart.PresenterClasses.TaskDataSource;
import TaskManagerClientPart.PresenterClasses.WebClient;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * The class that control selecting task.
 */
public class SelectTaskController extends AbstractController {

    /**
     * The counter of reacting.
     */
    private int raz;

    /**
     * The data base of tasks.
     */
    private TaskDataSource taskSource;

    /**
     * The constructor delegate object`s creating to parent class and also get the link to base of tasks and set
     * the counter to 0.
     * @param gui the UI
     * @param client the web part.
     * @param dataSource the link to task`s data base.
     */
    public SelectTaskController(IView gui, WebClient client, TaskDataSource dataSource){
        super(gui,client);
        this.taskSource=dataSource;
        raz=0;
    }

    /**
     * The method add the handler to UI.
     */
    @Override
    protected void init() {
        gui.setSelectionListener(new SelectionListener());
    }

    /**
     * The selection handler class.
     */
    private class SelectionListener implements ListSelectionListener { // for JList

        /**
         * The method catch the event and call the external method select().
         * @param e the event object.
         */
        @Override
        public void valueChanged(ListSelectionEvent e) {
            select();
        }
    }

    /**
     * The method that choose the selected task by name and set it`s info into the fields in UI.
     */
    private void select(){
        if(raz==1){
            raz=0;
            return;
        }
        raz=1;
        String name = gui.getSelectedTask();
        if(name==null){
            return;
        }
        gui.clearTaskFields();
        taskSource.setTaskToUserByName(name);
    }
}
