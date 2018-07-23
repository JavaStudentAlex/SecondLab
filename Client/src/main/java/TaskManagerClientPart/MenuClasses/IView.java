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
    TaskStringForm getNewTask();

    /**
     * The method that renews tasks view for user
     * @param tasks - new tasks in string format
     */
    void renewTasks(List<String> tasks);

    /**
     * The method sets handler for choosing task action
     * @param listener - the handler
     */
    void setSelectionListener(ListSelectionListener listener);

    /**
     * Sets task info into the relevant fields
     * @param task - the task in string format
     */
    void setTaskToFields(TaskStringForm task);

    /**
     * The method shows to user the error message with text {@code message}
     * @param message - the text message for user
     */
    void showErrorMessage(String message);

    /**
     * The method clears the fields and transform it to the primary format
     */
    void clearAllTasks();

    /**
     * The method sets handler of window_closing action
     * @param adapter - the handler
     */
    void setWindowCLosing(WindowAdapter adapter);

    /**
     * The method that returns the task, selected by user
     * @return the string title of relevant task
     */
    String getSelectedTask();

    /**
     * The method sets handler of adding new task
     * @param listener - the handler
     */
    void setAddListener(ActionListener listener);

    /**
     * The method sets handler of changing new task
     * @param listener- the handler
     */
    void setChangeListener(ActionListener listener);

    /**
     * The method sets handler of removing new task
     * @param listener - the handler
     */
    void setDeleteListener(ActionListener listener);

    /**
     * The get method for server address field.
     * @return the string if filled and the empty string if not.
     */
    String getServerAddress();

    /**
     * The method for clearing server part of the interface.
     */
    void clearServer();

    /**
     * The get method for port field.
     * @return the string if filled and the empty string if not.
     */
    String getPort();

    /**
     * The set method for server state field.
     * @param message the string message on state.
     */
    void setState(String message);

    /**
     * The get method for user name field.
     * @return the string if filled and the empty string if not.
     */
    String getUserName();

    /**
     * The get method for password field.
     * @return the string if filled and the empty string if not.
     */
    String getPass();

    /**
     * The method for clearing account part of the interface.
     */
    void clearAccount();

    /**
     * The set method for account state field.
     * @param message the string message on state.
     */
    void setAccountState(String message);

    /**
     * The set method for handlers of account.
     * @param listener the handler.
     * @param buttonName the name of the button.
     */
    void setAccountButtonListener(ActionListener listener, String buttonName);

    /**
     * The set method for handlers of server.
     * @param listener the handler.
     * @param buttonName the name of the button.
     */
    void setServerButtonListener(ActionListener listener, String buttonName);

    /**
     * The clear method for task edition part.
     */
    void clearTaskFields();

    /**
     * The method for showing info method to user/
     * @param message the text message.
     */
    void showInformMessage(String message);

}
