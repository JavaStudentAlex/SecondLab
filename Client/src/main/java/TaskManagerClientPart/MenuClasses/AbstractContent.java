package TaskManagerClientPart.MenuClasses;

import javax.swing.*;

public abstract class AbstractContent {
    protected JPanel rootPanel;

    protected abstract void initElems();
    public JPanel getRootPanel(){return rootPanel;}

    public AbstractContent(){
        initElems();
        rootPanel.revalidate();
    }
}
