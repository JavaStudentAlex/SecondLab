package TaskManagerClientPart.PresenterClasses;

import TaskManagerClientPart.MenuClasses.IView;
import TaskManagerClientPart.PresenterClasses.Cotrollers.*;

/**
 * The class that control the behaviour of user interface and have some handlers of user's actions
 */
public class Presenter {

    public Presenter(IView gui){
        TaskDataSource dataSource = new TaskDataSource(gui);
        WebClient webClient = new WebClient(gui,dataSource);
        ConnectionController connecter = new ConnectionController(gui,webClient);
        DisconnectController disconnector = new DisconnectController(gui,webClient);
        RegisterController registerer = new RegisterController(gui,webClient);
        LogoutController logouter = new LogoutController(gui,webClient);
        LoginController loginer = new LoginController(gui,webClient);
        AddController adder = new AddController(gui,webClient);
        SelectTaskController selector = new SelectTaskController(gui,webClient,dataSource);
        DeleteController deleter = new DeleteController(gui,webClient);
        ChangeController changer = new ChangeController(gui,webClient);
        SavingClosingController saver = new SavingClosingController(gui,webClient);
    }
}
