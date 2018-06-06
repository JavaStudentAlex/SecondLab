package TaskManagerClientPart.PresenterClasses.Cotrollers;

import TaskManagerClientPart.MenuClasses.AccountContent;
import TaskManagerClientPart.MenuClasses.IView;
import TaskManagerClientPart.PresenterClasses.WebClient;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginController extends AbstractController {

    public LoginController(IView gui, WebClient client) {
        super(gui, client);
    }

    @Override
    protected void init() {
        gui.setAccountButtonListener(new LoginHandler(), AccountContent.LOGINNAME);
    }

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

    private class LoginHandler implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            login();
        }
    }
}
