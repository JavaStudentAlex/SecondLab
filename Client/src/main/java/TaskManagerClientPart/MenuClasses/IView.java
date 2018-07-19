package TaskManagerClientPart.MenuClasses;

import CommonClasses.TaskStringForm;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.util.List;

/**
 * The interface of user view
 */
public interface IView {

    /**
     * The method that returns the string with new task
     * @return the string
     */
    public TaskStringForm getNewTask();

    /**
     * The method that renews tasks view for user
     * @param tasks - new tasks in string format
     */
    public void renewTasks(List<String> tasks);

    /**
     * The method sets handler for choosing task action
     * @param listener - the handler
     */
    public void setSelectionListener(ListSelectionListener listener);

    /**
     * Sets task info into the relevant fields
     * @param task - the task in string format
     */
    public void setTaskToFields(TaskStringForm task);

    /**
     * The method shows to user the error message with text {@code message}
     * @param message - the text message for user
     */
    public void showErrorMessage(String message);

    /**
     * The method clears the fields and transform it to the primary format
     */
    public void clearAllTasks();

    /**
     * The method sets handler of window_closing action
     * @param adapter - the handler
     */
    public void setWindowCLosing(WindowAdapter adapter);

    /**
     * The method that returns the task, selected by user
     * @return the string title of relevant task
     */
    public String getSelectedTask();

    /**
     * The method sets handler of adding new task
     * @param listener - the handler
     */
    public void setAddListener(ActionListener listener);

    /**
     * The method sets handler of changing new task
     * @param listener- the handler
     */
    public void setChangeListener(ActionListener listener);

    /**
     * The method sets handler of removing new task
     * @param listener - the handler
     */
    public void setDeleteListener(ActionListener listener);

    public String getServerAddress();

    public void clearServer();

    public String getPort();

    public void setState(String message);

    public String getUserName();

    public String getPass();

    public void clearAccount();

    public void setAccountState(String message);

    public void setAccountButtonListener(ActionListener listener, String buttonName);

    public void setServerButtonListener(ActionListener listener, String buttonName);

    public void clearTaskFields();

    public void showInformMessage(String message);

}
