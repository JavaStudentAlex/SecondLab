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

public class TaskDataSource{
    private static Logger logger = Logger.getLogger(TaskDataSource.class);
    private Map<String,TaskStringForm> tasks;
    private IView gui;
    private Cheker cheker;

    public TaskDataSource(IView gui) {
        this.gui=gui;
    }

    public void initTasks(){
        tasks=new HashMap<>();
    }

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

    public void initCheker(){
        cheker = new Cheker(gui);
        for (Map.Entry<String,TaskStringForm> tasksEntry : tasks.entrySet()){
            TaskStringForm tempTask = tasksEntry.getValue();
            if(tempTask.isActive()){
                cheker.addActiveTask(tempTask);
            }
        }
    }

    public void setTasksTitles(){
        List<String> result = new ArrayList<>();
        for(Map.Entry<String,TaskStringForm> temp : tasks.entrySet()){
            result.add(temp.getKey());
        }

        gui.renewTasks(result);
    }

    public void addNewTask(TaskStringForm task){
        tasks.put(task.getTitle(),task);
        setTasksTitles();
        if(task.isActive()){
            cheker.addActiveTask(task);
        }
    }

    public void setTaskToUserByName(String task){
        if (!tasks.containsKey(task)){
            return;
        }
        TaskStringForm temp = tasks.get(task);
        gui.setTaskToFields(temp);
    }

    public void removeTaskByTitle(String title){
        if (!tasks.containsKey(title)){
            return;
        }
        tasks.remove(title);
        setTasksTitles();
        cheker.deleteTask(title);
    }

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
