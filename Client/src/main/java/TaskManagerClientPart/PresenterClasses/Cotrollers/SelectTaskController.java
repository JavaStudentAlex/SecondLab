package TaskManagerClientPart.PresenterClasses.Cotrollers;

import TaskManagerClientPart.MenuClasses.IView;
import TaskManagerClientPart.PresenterClasses.TaskDataSource;
import TaskManagerClientPart.PresenterClasses.WebClient;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class SelectTaskController extends AbstractController {
    private int raz;
    private TaskDataSource taskSource;

    public SelectTaskController(IView gui, WebClient client, TaskDataSource dataSource){
        super(gui,client);
        this.taskSource=dataSource;
        raz=0;
    }

    @Override
    protected void init() {
        gui.setSelectionListener(new SelectionListener());
    }

    private class SelectionListener implements ListSelectionListener { // for JList
        @Override
        public void valueChanged(ListSelectionEvent e) {
            select();
        }
    }

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
