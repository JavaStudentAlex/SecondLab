package TaskManagerClientPart.MenuClasses;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.util.List;
import CommonClasses.TaskStringForm;

/**
 * The main container class for user interface
 */
public class View implements IView{

    /**
     * The connection content of the interface that inclused server and account parts.
     */
    private ConnectionContent connectionContent;

    /**
     * The class for keeping all elements for edition/removing tasks
     */
    private TasksContent tasksContent;

    /**
     * The manager for changing menus
     */
    private CardLayout panels;

    /**
     * The main panel container
     */
    private JPanel rootPanel;

    /**
     * The window of the interface for user
     */
    private JFrame window;

    /**
     * The ids for menus
     */
    public static final String menuTasks = "Tasks Controll", MENU_CONNECTION = "Connection";

    /**
     * The id of current menu
     */
    private String currentMenu;

    /**
     * The constructor that create a new instance and also start initializing elements
     * @see View#getWindow()
     */
    public View(){
        window = getWindow();
    }

    /**
     * The method create a visible but empty window with special attributes
     * @return the {@code JFrame} instance
     */
    public static JFrame getFrame(){ // creating frame method
        JFrame window = new JFrame(){};
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setTitle("Demo API");
        Toolkit thisToolkit = Toolkit.getDefaultToolkit();
        Dimension size = thisToolkit.getScreenSize();
        window.setBounds((int)(size.width*0.3),(int)(size.height*0.2),(int)(size.width*0.4),(int)(size.height*0.6));
        window.setVisible(true);
        return window;
    }

    /**
     * The method that creates and initializes all elements(also menus) in the class. This method places also
     * all elements on the main panel.
     * @return the initialized and filled {@code JFrame} instance
     * @see TasksContent
     */
    private JFrame getWindow() {
        JFrame result = getFrame();//create frame

        rootPanel = new JPanel();// navigation
        panels = new CardLayout();
        result.setContentPane(rootPanel);
        rootPanel.setLayout(panels);
        tasksContent = new TasksContent();
        connectionContent = new ConnectionContent();
        rootPanel.add(tasksContent.tasksMainRootPanel,menuTasks);
        rootPanel.add(connectionContent.getRootPanel(),MENU_CONNECTION);
        currentMenu=MENU_CONNECTION;
        panels.show(rootPanel,currentMenu);

        JMenuBar bar = new JMenuBar();
        JMenu tasksMenu = new JMenu("Tasks");
        JMenuItem tasksItem = getMenuItem(menuTasks);
        tasksMenu.add(tasksItem);
        JMenu connectionMenu = new JMenu("Connect");
        JMenuItem connectionItem = getMenuItem(MENU_CONNECTION);
        connectionMenu.add(connectionItem);
        bar.add(tasksMenu);
        bar.add(connectionMenu);
        result.setJMenuBar(bar);
        result.revalidate();
        return result;
    }

    /**
     * The method creates the {@code JMenuItem} instance, add him the anonymous event handler of changing menus. If the
     * name of user activated menu is mot equal to current menu so occurs the menu's change. With changing menus
     * occurs the clearing of previous menu.
     * @param name - name for menu item
     * @return the {@code JMenuItem} instance
     * @see View#menuTasks
     */
    private JMenuItem getMenuItem(String name){
        JMenuItem result = new JMenuItem(name);
        result.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(name!=currentMenu){
                    currentMenu=name;
                    panels.show(rootPanel,currentMenu);
                    rootPanel.revalidate();
                }
                rootPanel.revalidate();
            }
        });
        return result;
    }

    /**
     * The static method output the dialogue message with title "ERROR" and text from the param {@code message} string
     * on the parent panel from the {@code root} panel.
     * @param root - the panel on what the message wiil be outputted
     * @param message - the text for the message
     */
    public static void showErrorMessage(JPanel root, String message){
        JOptionPane.showMessageDialog(root,message,"ERROR",JOptionPane.ERROR_MESSAGE);
    }

    /**
     * The method controls the renewing of tasks list and delegate it to tasks content container
     * @param tasks - array of task's titles
     */
    public void renewTasks(List<String> tasks){
        tasksContent.renewTasks(tasks);
    }

    /**
     * The method delegates setting handler to relevant task content container
     * @param listener - the handler
     */
    public void setSelectionListener(ListSelectionListener listener){
        tasksContent.setSelectionListener(listener);
    }

    /**
     * The method delegate setting task's info to fields to relevant task content container
     * @param task - the task formatted to string
     */
    public void setTaskToFields(TaskStringForm task){
        tasksContent.setTaskToFields(task);
    }

    /**
     * The method adds the handler of closing window to {@code window}
     * @param adapter - the handler
     */
    public void setWindowCLosing(WindowAdapter adapter){
        window.addWindowListener(adapter);
    }

    /**
     * The method control getting new task performance and delegate this function to relevant functions of it's
     * task content container
     * @return the task formatted to string
     */
    public TaskStringForm getNewTask(){
        return tasksContent.getNewTask();
    }

    /**
     * The method output the dialogue message with title "ERROR" and text from the param {@code message} string
     * on the {@code rootPanel}
     * @param message - the text message for user
     */
    public void showErrorMessage(String message){
        JOptionPane.showMessageDialog(rootPanel,message,"ERROR",JOptionPane.ERROR_MESSAGE);
    }

    /**
     * The method clear all fields on the menu panel that now is shown
     */
    public void clearAllTasks(){
        tasksContent.clear();
    }

    /**
     * The method delegate getting selected task to relevant task content container
     * @return the task's title string
     */
    public String getSelectedTask(){
        return tasksContent.getSelectedTask();
    }

    /**
     * The method delegate setting handler for adding to relevant task content container
     * @param listener - the handler
     */
    public void setAddListener(ActionListener listener){
        tasksContent.setAddListener(listener);
    }

    /**
     * The method delegate setting handler for changing to relevant task content container
     * @param listener - the handler
     */
    public void setChangeListener(ActionListener listener){
        tasksContent.setChangeListener(listener);
    }

    /**
     * The method delegate setting handler for removing to relevant task content container
     * @param listener - the handler
     */
    public void setDeleteListener(ActionListener listener){
        tasksContent.setDeleteListener(listener);
    }

    /**
     * The get method that delegate to connection content.
     * @return the calling of the method in connection content.
     * @see ConnectionContent#getServerAddress()
     */
    public String getServerAddress(){
        return connectionContent.getServerAddress();
    }

    /**
     * The get method that delegate to connection content.
     * @return the calling of the method in connection content.
     * @see ConnectionContent#getPort()
     */
    public String getPort(){
        return connectionContent.getPort();
    }

    /**
     * The get method that delegate to connection content.
     * @see ConnectionContent#clearServerPart()
     */
    public void clearServer(){
        connectionContent.clearServerPart();
    }

    /**
     * The set method that delegate to connection content.
     * @param message the string message on state.
     * @see ConnectionContent#setState(String)
     */
    public void setState(String message){
        connectionContent.setState(message);
    }

    /**
     * The get method that delegate to connection content.
     * @return the calling of the method in connection content.
     * @see ConnectionContent#getUserName()
     */
    public String getUserName(){
        return connectionContent.getUserName();
    }

    /**
     * The get method that delegate to connection content.
     * @return the calling of the method in connection content.
     * @see ConnectionContent#getPass()
     */
    public String getPass(){
        return connectionContent.getPass();
    }

    /**
     * The get method that delegate to connection content.
     * @see ConnectionContent#clearAccountPart()
     */
    public void clearAccount(){
        connectionContent.clearAccountPart();
    }

    /**
     * The set method that delegate to connection content.
     * @param message the string message on state.
     * @see ConnectionContent#setAccountState(String)
     */
    public void setAccountState(String message){
        connectionContent.setAccountState(message);
    }

    /**
     * The set static method for setting button listener handler.
     * @param button the button
     * @param listener the handler
     */
    public static void setButtonListener(JButton button, ActionListener listener){
        button.addActionListener(listener);
    }

    /**
     * The set set method that delegate to ConnectionContent.
     * @param listener the handler.
     * @param buttonName the name of the button.
     * @see ConnectionContent#setAccountButtonListener(ActionListener, String)
     */
    public void setAccountButtonListener(ActionListener listener, String buttonName){
        connectionContent.setAccountButtonListener(listener,buttonName);
    }

    /**
     * The set method that delegate to ConnectionContent.
     * @param listener the handler.
     * @param buttonName the name of the button.
     * @see
     */
    public void setServerButtonListener(ActionListener listener, String buttonName){
        connectionContent.setServerButtonListener(listener,buttonName);
    }

    /**
     * The method assert is the value of string empty. If yes the error message is sent.
     * @param val - the asserting value.
     * @param nameInMessage The name of the filed for error message.
     * @param panel the panel that control the error message.
     * @return true if the value is not empty.
     */
    public static boolean validate(String val, String nameInMessage, JPanel panel){
        if(val.isEmpty()){
            showErrorMessage(panel,"Fill please the "+nameInMessage+" field");
            return false;
        }
        return true;
    }

    /**
     * The method delegate to task content.
     * @see TasksContent#clearTaskFields()
     */
    public void clearTaskFields(){
        tasksContent.clearTaskFields();
    }

    /**
     * The method to output the text messages about the error.
     * @param message the text message.
     * @see View#showErrorMessage(JPanel, String)
     */
    public void showInformMessage(String message){
        JOptionPane.showInternalMessageDialog(rootPanel,message);
    }
}
