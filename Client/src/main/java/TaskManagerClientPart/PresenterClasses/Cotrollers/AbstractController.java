package TaskManagerClientPart.PresenterClasses.Cotrollers;

import TaskManagerClientPart.MenuClasses.IView;
import TaskManagerClientPart.PresenterClasses.WebClient;

/**
 * The parent class for all controllers.
 */
public abstract class AbstractController {

    /**
     * The link on the user interface.
     */
    protected IView gui;

    /**
     * The link to web part.
     */
    protected WebClient client;

    /**
     * The constructor of the class that get UI and web part params.
     * @param gui the user interface
     * @param client the web part
     */
    AbstractController(IView gui, WebClient client){
        this.gui=gui;
        this.client=client;
        init();
    }

    /**
     * The method for init of the handlers of the buttons on the user interface.
     */
    protected abstract void init();
}
