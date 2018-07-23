package TaskManagerClientPart.MenuClasses;

import javax.swing.*;

/**
 * The parent class for all content classes of view part
 */
public abstract class AbstractContent {
    protected JPanel rootPanel;

    /**
     * The abstract method for overriding of init elements in content.
     */
    protected abstract void initElems();

    /**
     * The get method for the main panel of the content element.
     * @return the JPanel object.
     */
    public JPanel getRootPanel(){return rootPanel;}

    /**
     * The default constructor of class that will call init method and repaint after init of main elements.
     * @see AbstractContent#initElems()
     */
    public AbstractContent(){
        initElems();
        rootPanel.revalidate();
    }
}
