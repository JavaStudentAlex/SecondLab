package TaskManagerClientPart.PresenterClasses.Cotrollers;

import TaskManagerClientPart.MenuClasses.IView;
import TaskManagerClientPart.MenuClasses.ServerContent;
import TaskManagerClientPart.PresenterClasses.WebClient;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DisconnectController  extends AbstractController {

    public DisconnectController(IView gui, WebClient client){
        super(gui,client);
    }

    @Override
    protected void init() {
       gui.setServerButtonListener(new DisconnectionHandler(), ServerContent.DISCONNECTNAME);
    }

    private class DisconnectionHandler implements ActionListener{
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
