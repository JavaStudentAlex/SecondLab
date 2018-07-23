package TaskManagerClientPart.PresenterClasses.Cotrollers;

import TaskManagerClientPart.MenuClasses.AccountContent;
import TaskManagerClientPart.MenuClasses.IView;
import TaskManagerClientPart.PresenterClasses.WebClient;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The class that control log out.
 */
public class LogoutController extends AbstractController {

    /**
     * The constructor that delegate object`s creating to parent class.
     * @param gui the UI.
     * @param client the web part.
     */
    public LogoutController(IView gui, WebClient client) {
        super(gui, client);
    }

    /**
     * The method add the log out handler to UI.
     */
    @Override
    protected void init() {
        gui.setAccountButtonListener(new LogoutHandler(), AccountContent.LOGOUTNAME);
    }

    /**
     * The handler class of log out.
     */
    private class LogoutHandler implements ActionListener{

        /**
         * The method catch the event and log out with the web part.
         * @param e the event object.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            client.logout();
        }
    }
}
