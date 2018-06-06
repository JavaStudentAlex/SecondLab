package TaskManagerClientPart.PresenterClasses.Cotrollers;

import TaskManagerClientPart.MenuClasses.AccountContent;
import TaskManagerClientPart.MenuClasses.IView;
import TaskManagerClientPart.PresenterClasses.WebClient;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterController extends AbstractController {

    public RegisterController(IView gui, WebClient client){
        super(gui,client);
    }

    private void register(){
        String name = LoginController.getName(gui);
        if(name==null){
            return;
        }

        String pass = LoginController.getPass(gui);
        if (pass==null){
            return;
        }

        client.setRegister(name,pass);
    }

    public void init(){
        gui.setAccountButtonListener(new RegisterHandler(),AccountContent.REGISTERNAME);
    }

    private class RegisterHandler implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            register();
        }
    }
}
