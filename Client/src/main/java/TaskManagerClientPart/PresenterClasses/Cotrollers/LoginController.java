package TaskManagerClientPart.PresenterClasses.Cotrollers;

import TaskManagerClientPart.MenuClasses.AccountContent;
import TaskManagerClientPart.MenuClasses.IView;
import TaskManagerClientPart.PresenterClasses.WebClient;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The class that control the log in.
 */
public class LoginController extends AbstractController {

    /**
     * The constructor that delegates object`s creating to parent class.
     * @param gui the UI.
     * @param client the web part.
     */
    public LoginController(IView gui, WebClient client) {
        super(gui, client);
    }

    /**
     * The method add the handler for log in.
     */
    @Override
    protected void init() {
        gui.setAccountButtonListener(new LoginHandler(), AccountContent.LOGINNAME);
    }

    /**
     * The method get the user name and the password and use the web part to log in.
     */
    protected void login(){
        String user = getName(gui);
        if(user==null){
            return;
        }
        String pass = getPass(gui);
        if(pass==null){
            return;
        }

        client.login(user,pass);
    }

    /**
     * The method validate and return the user name.
     * @param gui the UI.
     * @return the string if validation successful, null if not.
     */
    public static String getName(IView gui){
        String result = gui.getUserName().trim();
        if(result.isEmpty()){
            gui.showErrorMessage("User name is empty");
            return null;
        }
        if(result.length()>AccountContent.MAXNAME){
            gui.showErrorMessage("User name is greater than "+ AccountContent.MAXNAME);
            return null;
        }
        return result;
    }

    /**
     * The method validate and return the password.
     * @param gui the UI.
     * @return the string if validation successful, null if not.
     */
    public static String getPass(IView gui){
        String result = gui.getPass().trim();
        if(result.isEmpty()){
            gui.showErrorMessage("Password is empty");
            return null;
        }
        if(result.length()>AccountContent.MAXPASS){
            gui.showErrorMessage("Password is greater than "+ AccountContent.MAXPASS);
            return null;
        }
        return result;
    }

    /**
     * The class that is the handler for log in.
     */
    private class LoginHandler implements ActionListener{

        /**
         * The method catch the event and react by calling login() method.
         * @param e the event object.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            login();
        }
    }
}
