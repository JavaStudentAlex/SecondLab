package TaskManagerClientPart.PresenterClasses;

import TaskManagerClientPart.Cheker;
import CommonClasses.TaskStringForm;
import CommonClasses.TasksXMLParser;
import TaskManagerClientPart.MenuClasses.IView;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The class that represent the data base of the tasks.
 */
public class TaskDataSource{

    /**
     * The logger of the class.
     */
    private static Logger logger = Logger.getLogger(TaskDataSource.class);

    /**
     * The map that save the tasks by it`s name.
     */
    private Map<String,TaskStringForm> tasks;

    /**
     * The UI.
     */
    private IView gui;

    /**
     * The checker when the active tasks happen.
     */
    private Cheker cheker;

    /**
     * The constructor that get as argument the UI.
     * @param gui the UI.
     */
    public TaskDataSource(IView gui) {
        this.gui=gui;
    }

    /**
     * Creating the map for saving tasks.
     */
    public void initTasks(){
        tasks=new HashMap<>();
    }

    /**
     * The method get the xml tasks info, parse it and save to map.
     * @param xmlTasks
     */
    public void tasksSaveAndSet(String xmlTasks){
        initTasks();
        List<TaskStringForm> stringTempTasks;
        try {
            stringTempTasks = new TasksXMLParser(xmlTasks).getStringFormTasks();
        } catch (SAXException | ParserConfigurationException | IOException e) {
            logger.info("Error with loaded user tasks");
            gui.showErrorMessage("Error with loaded user tasks");
            return;
        }

        for(TaskStringForm tempTask : stringTempTasks){
            tasks.put(tempTask.getTitle(),tempTask);
        }

        setTasksTitles();
        initCheker();
    }

    /**
     * The method create the checker and add all active tasks in.
     */
    public void initCheker(){
        cheker = new Cheker(gui);
        for (Map.Entry<String,TaskStringForm> tasksEntry : tasks.entrySet()){
            TaskStringForm tempTask = tasksEntry.getValue();
            if(tempTask.isActive()){
                cheker.addActiveTask(tempTask);
            }
        }
    }

    /**
     * The method get titles of all tasks and set it to UI.
     */
    public void setTasksTitles(){
        List<String> result = new ArrayList<>();
        for(Map.Entry<String,TaskStringForm> temp : tasks.entrySet()){
            result.add(temp.getKey());
        }

        gui.renewTasks(result);
    }

    /**
     * The method add the new task to data source.
     * @param task the task in string form.
     */
    public void addNewTask(TaskStringForm task){
        tasks.put(task.getTitle(),task);
        setTasksTitles();
        if(task.isActive()){
            cheker.addActiveTask(task);
        }
    }

    /**
     * The method set the detail info about the task with the title like the argument.
     * @param task the title of chosen task.
     */
    public void setTaskToUserByName(String task){
        if (!tasks.containsKey(task)){
            return;
        }
        TaskStringForm temp = tasks.get(task);
        gui.setTaskToFields(temp);
    }

    /**
     * The method remove from the data source the task with the title like in the argument.
     * @param title the title of the chosen task.
     */
    public void removeTaskByTitle(String title){
        if (!tasks.containsKey(title)){
            return;
        }
        tasks.remove(title);
        setTasksTitles();
        cheker.deleteTask(title);
    }

    /**
     * The method remove the task with the title like in oldTaskTitle argument and add new task like newTask argument.
     * @param oldTaskTitle the title of old task.
     * @param newTask the string form of the new task.
     */
    public void changeTaskByTitle(String oldTaskTitle,TaskStringForm newTask){
        if (!tasks.containsKey(oldTaskTitle)){
            return;
        }
        tasks.remove(oldTaskTitle);
        tasks.put(newTask.getTitle(),newTask);
        setTasksTitles();

        cheker.deleteTask(oldTaskTitle);
        if(newTask.isActive()){
            cheker.addActiveTask(newTask);
        }
    }

    /**
     * The method remove all tasks from data source and checker and set it double null.
     */
    public void removeAllTasks(){
        if(cheker!=null){
            cheker.deleteAll();
            cheker=null;
        }
        if(tasks!=null) {
            tasks.clear();
        }
    }
}
