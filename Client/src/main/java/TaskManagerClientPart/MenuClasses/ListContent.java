package TaskManagerClientPart.MenuClasses;

import CommonClasses.Constrains;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * The class that keep together elements of outputting list of tasks and buttons to give user possibilities to control
 * them
 */
public class ListContent{

    /**
     * The main container panel for all other elements
     */
    public JPanel tasksRootPanel;

    /**
     * The model for {@code listTasks}
     */
    private DefaultListModel<String> listModel;

    /**
     * The list for outputting tasks
     */
    private JList<String> listTasks;

    /**
     * The buttons to give the user an ability to make actions with tasks
     */
    private JButton add, change, delete,reset;

    /**
     * The constructor that create a new instance and initialize all elements, also place them for showing
     * @param resetListener - the handler for {@code reset} button
     * @see ListContent#setResetListener(ActionListener)
     * @see ListContent#initializeElems()
     */
    public ListContent( ActionListener resetListener){
        initializeElems();
        setResetListener(resetListener);
        tasksRootPanel.revalidate();
    }

    /**
     * The method initialize all elements and place them on the main panel
     * @see PanelFactory
     * @see Constrains
     * @see ListFactory
     */
    private void initializeElems(){
        tasksRootPanel = PanelFactory.createPanel("Tasks");
        tasksRootPanel.setLayout(new GridBagLayout());
        GridBagConstraints locator = Constrains.getLocator();

        listModel = new DefaultListModel<String>();
        listTasks = ListFactory.createList(listModel);
        add = new JButton("Add");
        change = new JButton("Change");
        delete = new JButton("Remove");
        reset = new JButton("Reset");

        Constrains.setLocation(locator,0,0);
        Constrains.setSize(locator,7,5);
        tasksRootPanel.add(listTasks,locator);
        tasksRootPanel.add(new JScrollPane(listTasks),locator);

        locator.weighty = 0.0005;
        Constrains.setLocation(locator,0,5);
        Constrains.setSize(locator,1,1);
        tasksRootPanel.add(add,locator);
        Constrains.setLocation(locator,2,5);
        tasksRootPanel.add(change,locator);
        Constrains.setLocation(locator,4,5);
        tasksRootPanel.add(delete,locator);
        Constrains.setLocation(locator,6,5);
        tasksRootPanel.add(reset,locator);
    }

    /**
     * the method set handler for pressing button reset
     * @param resetListener - the handler
     */
    private void setResetListener(ActionListener resetListener){
        reset.addActionListener(resetListener);
    }

    /**
     * The method remove all old tasks and add all new tasks from the {@code tasks} array
     * @param tasks - the array of new tasks
     */
    public void renewTasks(List<String> tasks){
        listModel.removeAllElements();
        for(String buf : tasks){
            listModel.addElement(buf);
        }
        tasksRootPanel.revalidate();
    }

    /**
     * The method set handler of selection task action
     * @param listener - the handler
     */
    public void setSelectionListener(ListSelectionListener listener){
        listTasks.addListSelectionListener(listener);
    }

    public void clearList(){
        listModel.removeAllElements();
        listTasks.clearSelection();
        listTasks.setSelectedIndex(-1);
    }

    /**
     * The method set selection model uncertain, without chosen element
     */
    public void setClearSelection(){
        clearList();
        tasksRootPanel.revalidate();
    }

    /**
     * The method returns the string title of chosen task
     * @return - the title formatted in string
     */
    public String getSelectedTask(){
        return listTasks.getSelectedValue();
    }

    /**
     * The method set handler for adding action
     * @param listener - the handler
     */
    public void setAddListener(ActionListener listener){
        add.addActionListener(listener);
    }

    /**
     * The method set handler for changing action
     * @param listener - the handler
     */
    public void setChangeListener(ActionListener listener){
        change.addActionListener(listener);
    }

    /**
     * The method set handler for removing action
     * @param listener - the handler
     */
    public void setDeleteListener(ActionListener listener){
        delete.addActionListener(listener);
    }
}
