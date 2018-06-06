package TaskManagerClientPart.MenuClasses;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

/**
 * The factory class that create new panels with the titled border
 */
public class PanelFactory {

    /**
     * The method returns the new panel with titled border. The title get text from the param {@code name}
     * @param name - the text for title of border
     * @return the new {@code JPanel} instance
     */
    public static JPanel createPanel(String name){
        JPanel result = new JPanel();
        Border border = BorderFactory.createEtchedBorder();
        TitledBorder borderTitle = BorderFactory.createTitledBorder(border,name);
        result.setBorder(borderTitle);
        return result;
    }
}
