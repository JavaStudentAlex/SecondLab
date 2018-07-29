package TaskManagerClientPart.MenuClasses;

import javax.swing.*;
import java.awt.*;

import CommonClasses.Constrains;
import CommonClasses.TaskStringForm;

/**
 * The class for control time info
 */
public class TimerContent {


    /**
     * Manager for changing two different timer menus
     */
    private CardLayout manager;
    /**
     * Panels with timer menus
     */
    private JPanel singlePanel, multiplePanel;

    /**
     * The name of current timer menu
     */
    private String currentTimer;

    /**
     * The fields for task's time info
     */
    private JTextField timeField,startTimeField, endTimeField, intervalField;

    /**
     * The names of timer menu and formats for fields
     */
    public static final String SINGLE="Single", MULTIPLE = "Multiple";

    /**
     * The main panel
     */
    public JPanel timerRootPanel;

    /**
     * The constructor that creates a new instance and initialize elems
     * @see TimerContent#initializeContent()
     */
    public TimerContent(){
        initializeContent();
        timerRootPanel.revalidate();
    }

    /**
     * Method create and place functional elements
     * @see PanelFactory
     * @see TimerContent#initializeSinglePanel()
     * @see TimerContent#initializeMultiplePanel()
     */
    private void initializeContent(){
        timerRootPanel = new JPanel();

        singlePanel = PanelFactory.createPanel(SINGLE);
        multiplePanel = PanelFactory.createPanel(MULTIPLE);

        initializeSinglePanel();
        initializeMultiplePanel();

        manager = new CardLayout();
        timerRootPanel.setLayout(manager);

        timerRootPanel.add(singlePanel,SINGLE);
        timerRootPanel.add(multiplePanel,MULTIPLE);

        currentTimer = SINGLE;

        manager.show(timerRootPanel,currentTimer);
    }

    /**
     * Them method creates and locates the single panel of timer
     * @see Constrains
     */
    private void initializeSinglePanel(){
        timeField= new JTextField(TaskStringForm.FORMAT_TIME, 40);
        singlePanel.setLayout(new GridBagLayout());
        GridBagConstraints loc = Constrains.getLocator();
        loc.fill = GridBagConstraints.NONE;
        loc.weightx = 0.25;
        Constrains.setLocation(loc,0,0);
        singlePanel.add(new Label("Time : "),loc);
        loc.weightx=0.75;
        Constrains.setLocation(loc,1,0);
        singlePanel.add(timeField,loc);
    }

    /**
     * Them method creates and locates the multiple panel of timer
     * @see Constrains
     */
    private void initializeMultiplePanel(){
        startTimeField = new JTextField(TaskStringForm.FORMAT_TIME,40);
        endTimeField = new JTextField(TaskStringForm.FORMAT_TIME,40);
        intervalField = new JTextField(TaskStringForm.FORMAT_INTERVAL,40);
        multiplePanel.setLayout(new GridBagLayout());
        GridBagConstraints loc = Constrains.getLocator();
        GridBagConstraints loc1 = Constrains.getLocator();
        loc.fill = loc1.fill = GridBagConstraints.NONE;
        loc.weightx = 0.25;
        loc1.weightx = 0.75;

        Constrains.setLocation(loc,0,0);
        multiplePanel.add(new JLabel("Start time : "),loc);

        Constrains.setLocation(loc1,1,0);
        multiplePanel.add(startTimeField,loc1);

        Constrains.setLocation(loc,0,1);
        multiplePanel.add(new JLabel("Interval : "),loc);

        Constrains.setLocation(loc1,1,1);
        multiplePanel.add(intervalField,loc1);

        Constrains.setLocation(loc,0,2);
        multiplePanel.add(new JLabel("End time : "),loc);

        Constrains.setLocation(loc1,1,2);
        multiplePanel.add(endTimeField,loc1);
    }

    /**
     * The method assert equaling current timer menu name to the name in param. And returns relevant true if it is equal
     * and false if not
     * @param name - the name of asserted menu's name
     * @return return true of false
     */
    public boolean assertCurrent(String name){
        return currentTimer.equals(name);
    }

    /**
     * The method show on the main panel the menu with name in param
     * @param name - the name of menu that should be shown
     */
    public void showCard(String name){
        currentTimer = name;
        manager.show(timerRootPanel,currentTimer);
    }

    /**
     * This method clear fields on the panel that now is shown on the main panel
     */
    public void clearFields(){
        timeField.setText(TaskStringForm.FORMAT_TIME);
        startTimeField.setText(TaskStringForm.FORMAT_TIME);
        endTimeField.setText(TaskStringForm.FORMAT_TIME);
        intervalField.setText(TaskStringForm.FORMAT_INTERVAL);
        showCard(SINGLE);
    }

    /**
     * The method validate, transform the time from the fields to special string and return in format
     * {@code at [yyyy-mm-dd hh:mm:ss.msmsms] (inactive)} or
     * {@code from [yyyy-mm-dd hh:mm:ss.msmsms] to [yyyy-mm-dd hh:mm:ss.msmsms]
     * every [0 day(s) 0 hour(s) 0 minute(s) 0 second(s)] (inactive)}. If the fields are not valid the method returns ""
     * @return the string with the time or empty string
     */
    public TaskStringForm getNewTask(){
        TaskStringForm result = new TaskStringForm();
        if(currentTimer.equals(SINGLE)){
            String time = timeField.getText().trim();
            if(!View.validate(time,"time", timerRootPanel)){
                return null;
            }
            result.setStartDate(time);
        }
        else {
            String startTime = startTimeField.getText().trim();
            if(!View.validate(startTime,"start time", timerRootPanel)){
                return null;
            }
            String endTime = endTimeField.getText().trim();
            if(!View.validate(endTime,"end time", timerRootPanel)){
                return null;
            }
            String interval = intervalField.getText().trim();
            if(!View.validate(interval,"interval", timerRootPanel)){
                return null;
            }
            result.setStartDate(startTime);
            result.setEndDate(endTime);
            result.setInterval(interval);
        }
        return result;
    }


    /**
     * The method parse string {@code time} from params and push info to relevant fields on the panel. Also this method
     * change menu timer panels according to value of {@code repeatTask} param. If param is true so the method show
     * panel with {@code MULTIPLE} name, if not  - {@code SINGLE} name.
     */
    public void setTimeToFields(TaskStringForm task){
        if(task.isRepeat()){
            startTimeField.setText(task.getStartDate());
            endTimeField.setText(task.getEndDate());
            intervalField.setText(task.getInterval());
            return;
        }
        timeField.setText(task.getStartDate());
    }
}
