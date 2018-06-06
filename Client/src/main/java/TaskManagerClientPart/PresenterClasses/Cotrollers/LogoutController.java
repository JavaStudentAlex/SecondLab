package TaskManagerClientPart.PresenterClasses.Cotrollers;

import TaskManagerClientPart.MenuClasses.AccountContent;
import TaskManagerClientPart.MenuClasses.IView;
import TaskManagerClientPart.PresenterClasses.WebClient;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LogoutController extends AbstractController {

    public LogoutController(IView gui, WebClient client) {
        super(gui, client);
    }

    @Override
    protected void init() {
        gui.setAccountButtonListener(new LogoutHandler(), AccountContent.LOGOUTNAME);
    }

    private class LogoutHandler implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            client.logout();
        }
    }
}
