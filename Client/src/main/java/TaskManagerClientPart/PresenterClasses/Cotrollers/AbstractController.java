package TaskManagerClientPart.PresenterClasses.Cotrollers;

import TaskManagerClientPart.MenuClasses.IView;
import TaskManagerClientPart.PresenterClasses.WebClient;

public abstract class AbstractController {
    protected IView gui;
    protected WebClient client;

    AbstractController(IView gui, WebClient client){
        this.gui=gui;
        this.client=client;
        init();
    }

    protected abstract void init();
}
