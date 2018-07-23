package TaskManagerClientPart.PresenterClasses.Cotrollers;

import TaskManagerClientPart.MenuClasses.IView;
import TaskManagerClientPart.PresenterClasses.WebClient;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * The class, that control saving tasks on closing.
 */
public class SavingClosingController extends AbstractController {

    /**
     * The constructor that delegate object`s creating to parent class.
     * @param localView the UI.
     * @param client the web part.
     */
    public SavingClosingController(IView localView, WebClient client){
        super(localView,client);
    }

    /**
     * The method add the handler to UI.
     */
    @Override
    protected void init() {
        gui.setWindowCLosing(new SavingOnClosing());
    }

    /**
     * The saving on close handler class.
     */
    private class SavingOnClosing extends WindowAdapter {

        /**
         * The method catch the event and call the external method closeTheWindow()
         * @param e the event object.
         */
        @Override
        public void windowClosing(WindowEvent e) {
            closeTheWindow();
        }
    }

    /**
     * The class that assert logging and connection and if yes - fix it.
     */
    private void closeTheWindow(){
        if(client.isLogged()){
            client.logout();
        }
        if(client.isConnected()){
            client.closeConnection();
        }
    }
}
