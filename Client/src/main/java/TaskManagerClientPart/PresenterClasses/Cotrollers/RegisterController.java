package TaskManagerClientPart.PresenterClasses.Cotrollers;

import TaskManagerClientPart.MenuClasses.AccountContent;
import TaskManagerClientPart.MenuClasses.IView;
import TaskManagerClientPart.PresenterClasses.WebClient;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The class that control the register.
 */
public class RegisterController extends AbstractController {

    /**
     * the constructor delegate the object`s creating to parent class.
     * @param gui the UI.
     * @param client the web part.
     */
    public RegisterController(IView gui, WebClient client){
        super(gui,client);
    }

    /**
     * The method that get user name and password using login controller class and register with the web part.
     */
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

    /**
     * The method add the handler of register to UI.
     */
    public void init(){
        gui.setAccountButtonListener(new RegisterHandler(),AccountContent.REGISTERNAME);
    }

    /**
     * The handler register class.
     */
    private class RegisterHandler implements ActionListener{

        /**
         * The method catch the event and call the method register() to external.
         * @param e the event class.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            register();
        }
    }
}
