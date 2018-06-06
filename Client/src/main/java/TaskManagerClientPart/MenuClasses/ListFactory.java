package TaskManagerClientPart.MenuClasses;

import javax.swing.*;

/**
 * The factory class for creating list with special params
 */
public class ListFactory {

    /**
     * The method that create a new task with special attributes  by the model in params
     * @param model - the model for list
     * @return the new {@code Jlist} that based on model in params
     */
    public static JList<String> createList(DefaultListModel<String> model){
        JList<String> result = new JList(model);
        result.setLayoutOrientation(JList.VERTICAL);
        result.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        return result;
    }
}
