package TaskManagerClientPart;

import CommonClasses.TaskStringForm;
import Exceptions.*;
import TaskManagerClientPart.MenuClasses.IView;
import org.apache.log4j.Logger;
import javax.swing.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import CommonClasses.Task;

/**
 * The class that manage task's happening control
 */
public class Cheker{
    private static Logger logger = Logger.getLogger(Cheker.class);

    /**
     * The class that control task's happening
     */
    private class TaskChekThread extends Thread{
        /**
         * The current task
         */
        private Task task;

        /**
         * The time she should happen
         */
        private Date nextTime;

        /**
         * The manager of this controller
         */
        private Cheker own;

        /**
         * The method that start running with starting new thread of this class. Here happen checking current time and
         * {@code nextTime}. If the task happens, we got new {@code nextTime}. If it is null the checker is interrupted
         * and remove, else - we push a new time to {@code nextTime}
         */
        @Override
        public void run() {
            while(true){
                Date cur = new Date();
                if(cur.equals(nextTime) || cur.after(nextTime)){
                    gui.showInformMessage(cur+" - "+task.getTitle());
                    nextTime = task.nextTimeAfter(cur);
                    if(nextTime==null){
                        own.deleteTask(task.getTitle());
                        return;
                    }
                }
            }
        }

        /**
         * The constructor of new instance that also initialize elements
         * @param task - the current task
         * @param cheker - the manager of the checker
         */
        public TaskChekThread(Task task,Cheker cheker){
            this.own = cheker;
            this.task = task;
            nextTime = task.nextTimeAfter(new Date());


        }


    }

    private IView gui;

    /**
     * The container of all checkers
     */
    HashMap<String,TaskChekThread> threads;

    /**
     * The user panel for messages pushing
     */
    private JPanel root;

    /**
     * The manager constructor that create main container
     */
    public Cheker(IView gui){
        this.gui=gui;
        threads = new HashMap<String ,TaskChekThread>();
    }

    /**
     * The method add new active tasks, by creating new checker in manager's container and run it like a new thread
     */
    public void addActiveTask(TaskStringForm taskString){
        Task task = null;
        try {
            task = taskString.parseTask();
        } catch (MyOwnException e) {
            logger.info("After asserting on the server can not be");
            deleteAll();
        }
        if(task.nextTimeAfter(new Date())==null){
            return;
        }
        String title = task.getTitle();

        TaskChekThread potok = new TaskChekThread(task,this);
        potok.start();

        threads.put(title,potok);

    }

    /**
     * The method find and interrupt and remove the thread wit task that have title equal to {@code taskTitle}
     * @param taskTitle - the title of controlled task
     */
    public void deleteTask(String taskTitle){
        if(threads.containsKey(taskTitle)){
            TaskChekThread thread = threads.get(taskTitle);
            thread.interrupt();
            threads.remove(taskTitle);
        }
    }

    /**
     * The method clear the main manager's container and interrupt all threads
     */
    public void deleteAll(){
        for(Map.Entry<String,TaskChekThread> temp : threads.entrySet()){
            temp.getValue().interrupt();
        }
        threads.clear();
    }
}
