package TaskManagerClientPart.PresenterClasses.Cotrollers;

import TaskManagerClientPart.MenuClasses.IView;
import TaskManagerClientPart.MenuClasses.ServerContent;
import TaskManagerClientPart.PresenterClasses.WebClient;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The class that control the disconnect from server.
 */
public class DisconnectController  extends AbstractController {

    /**
     * The constructor delegate object`s creating to parent class.
     * @param gui the UI.
     * @param client the web part.
     */
    public DisconnectController(IView gui, WebClient client){
        super(gui,client);
    }

    /**
     * The method add the disconnect handler to UI.
     */
    @Override
    protected void init() {
       gui.setServerButtonListener(new DisconnectionHandler(), ServerContent.DISCONNECTNAME);
    }

    /**
     * The class that is disconnect handler.
     */
    private class DisconnectionHandler implements ActionListener{

        /**
         * The method that catch the event and close connection the web part.
         * @param e the event class.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            if(!client.isConnected()){
                gui.showErrorMessage("Not yet connected");
                return;
            }
            client.closeConnection();
        }
    }
}
