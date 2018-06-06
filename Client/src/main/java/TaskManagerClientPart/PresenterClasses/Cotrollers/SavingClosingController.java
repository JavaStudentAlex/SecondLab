package TaskManagerClientPart.PresenterClasses.Cotrollers;

import TaskManagerClientPart.PresenterClasses.Cotrollers.AbstractController;
import TaskManagerClientPart.MenuClasses.IView;
import TaskManagerClientPart.PresenterClasses.WebClient;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * The class, that controll saving tasks on closing
 */
public class SavingClosingController extends AbstractController {

    public SavingClosingController(IView localView, WebClient client){
        super(localView,client);
    }

    @Override
    protected void init() {
        gui.setWindowCLosing(new SavingOnClosing());
    }

    private class SavingOnClosing extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            closeTheWindow();
        }
    }

    private void closeTheWindow(){
        if(client.isLogged()){
            client.logout();
        }
        if(client.isConnected()){
            client.closeConnection();
        }
    }
}
