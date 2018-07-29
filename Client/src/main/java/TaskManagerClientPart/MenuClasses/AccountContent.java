package TaskManagerClientPart.MenuClasses;

import CommonClasses.Constrains;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The child class of AbstractContent for showing the account info
 */
public class AccountContent extends AbstractContent {

    /**
     * The max length of the name.
     */
    public static final int MAXNAME=64;

    /**
     * The max length of password.
     */
    public static final int MAXPASS=32;

    /**
     * The name of the buttons.
     */
    public static final String REGISTERNAME="Register",LOGOUTNAME="Logout",LOGINNAME="Login",UNMASK="Unmask";

    /**
     * The accounting statuses.
     */
    public static final String EMPTYSTRING="",NOTLOGGED="Not logged";

    /**
     * The filed for username.
     */
    private JTextField nameField;

    /**
     * The field for password.
     */
    private JPasswordField passwordField;

    /**
     * The buttons.
     */
    private JButton login, logout, register, clear, unmask;

    /**
     * Th field for account statuses.
     */
    private JLabel state;

    /**
     * The default constructor of the class.
     */
    public AccountContent(){
        super();
    }


    /**
     * The method overrides the behaviour of init local elements.
     */
    @Override
    protected void initElems() {
        rootPanel = PanelFactory.createPanel("Account");
        rootPanel.setLayout(new GridBagLayout());
        placeLabels();
        placeFields();
        placeButtons();
    }


    /**
     * The  method place the labels on the main panel.
     * @see AbstractContent
     */
    private void placeLabels(){
        state = new JLabel(NOTLOGGED);
        GridBagConstraints locator = Constrains.getLocator();
        locator.weightx=0.3;

        Constrains.setLocation(locator,1,0);
        rootPanel.add(new JLabel("HINT : "),locator);

        Constrains.setLocation(locator,1,1);
        rootPanel.add(new JLabel("USER : "),locator);

        Constrains.setLocation(locator,1,2);
        rootPanel.add(new JLabel("HINT : "),locator);

        Constrains.setLocation(locator,1,3);
        rootPanel.add(new JLabel("PASSWORD : "),locator);

        Constrains.setLocation(locator,1,4);
        rootPanel.add(new JLabel("STATE : "),locator);

        Constrains.setLocation(locator,2,4);
        rootPanel.add(state,locator);

        Constrains.setLocation(locator,2,0);
        rootPanel.add(new JLabel("MAX NAME : " + MAXNAME),locator);

        Constrains.setLocation(locator,2,2);
        rootPanel.add(new JLabel("MAX PASS : " + MAXPASS),locator);
    }

    /**
     * The  method place the text fields on the main panel.
     */
    private void placeFields(){
        nameField = new JTextField(35);
        passwordField = new JPasswordField(35);

        GridBagConstraints locator = Constrains.getLocator();
        locator.fill = GridBagConstraints.HORIZONTAL;
        locator.weightx=0.4;
        Constrains.setLocation(locator,2,1);
        Constrains.setSize(locator,2,1);
        rootPanel.add(nameField, locator);

        Constrains.setLocation(locator,2,3);
        rootPanel.add(passwordField,locator);
    }

    /**
     * The  method place the buttons on the main panel.
     */
    private void placeButtons(){
        login = new JButton(LOGINNAME);
        logout=new JButton(LOGOUTNAME);
        register = new JButton(REGISTERNAME);
        clear = new JButton("Clear");
        unmask = new JButton(UNMASK);
        setMask();
        setClearHandler();
        setUnmaskHandler();


        GridBagConstraints locator = Constrains.getLocator();
        locator.fill = GridBagConstraints.HORIZONTAL;
        locator.weightx=0.3;

        Constrains.setLocation(locator,0,3);
        rootPanel.add(clear,locator);

        locator.weightx=0.35;
        Constrains.setLocation(locator,0,0);
        rootPanel.add(login,locator);

        Constrains.setLocation(locator,0,1);
        rootPanel.add(register,locator);

        Constrains.setLocation(locator,0,2);
        rootPanel.add(logout,locator);

        Constrains.setLocation(locator,0,3);
        rootPanel.add(clear,locator);

        Constrains.setLocation(locator,0,4);
        rootPanel.add(unmask,locator);
    }

    /**
     * The method set for the button clear click handler.
     */
    private void setClearHandler(){
        clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clear();
            }
        });
    }

    /**
     * The method set for the button unmask click handler.
     */
    private void setUnmaskHandler(){
        unmask.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!unmask.getText().equals(UNMASK)){
                    setMask();
                    return;
                }
                setUnmask();
                rootPanel.revalidate();
            }
        });
    }

    /**
     * The method for set the mask of the password field.
     */
    private void setMask(){
        passwordField.setEchoChar('*');
        unmask.setText(UNMASK);
    }

    /**
     * The method for set unmask of the password field.
     */
    private void setUnmask(){
        passwordField.setEchoChar((char)0);
        unmask.setText("Mask");
    }

    /**
     * The method clear all the text fields on the panel.
     */
    public void clear(){
        nameField.setText(EMPTYSTRING);
        passwordField.setText(EMPTYSTRING);
        setMask();
        rootPanel.revalidate();
    }

    /**
     * The get method for user name field.
     * @return string if the field filled and empty if not.
     */
    public String getUserName(){
        return nameField.getText();
    }

    /**
     * The get method for password field
     * @return string if the field filled and empty if not.
     */
    public String getPass(){
        return new String(passwordField.getPassword());
    }

    /**
     * The method for set login state on the panel on field state.
     * @param message the message in the state field
     */
    public void setState(String message){
        state.setText(message);
        state.revalidate();
    }

    /**
     * The method for adding handlers on the buttons login, logout, register by the button name.
     * @param listener the handler.
     * @param buttonName the name of the target button.
     */
    public void setAccountButtonListener(ActionListener listener, String buttonName){
        switch (buttonName){
            case REGISTERNAME : View.setButtonListener(register,listener); return;
            case LOGOUTNAME : View.setButtonListener(logout,listener); return;
            case LOGINNAME : View.setButtonListener(login,listener); return;
        }
    }

}
