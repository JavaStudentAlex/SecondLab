package CommonClasses;

import javax.swing.*;
import java.awt.*;

public class FramesFactory {
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
}
