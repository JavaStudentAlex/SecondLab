package TaskManagerClientPart.MenuClasses;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AccountContent extends AbstractContent {
    public static final int MAXNAME=64;
    public static final int MAXPASS=32;
    public static final String REGISTERNAME="Register";
    public static final String LOGOUTNAME="Logout";
    public static final String LOGINNAME="Login";
    public static final String UNMASK="Unmask";
    public static final String EMPTYSTRING="";
    public static final String NOTLOGGED="Not logged";
    private JTextField nameField;
    private JPasswordField passwordField;
    private JButton login, logout, register, clear, unmask;
    private JLabel state;

    public AccountContent(){
        super();
    }

    @Override
    protected void initElems() {
        rootPanel = PanelFactory.createPanel("Account");
        rootPanel.setLayout(new GridBagLayout());
        placeLabels();
        placeFields();
        placeButtons();
    }

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

    private void setClearHandler(){
        clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clear();
            }
        });
    }

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

    private void setMask(){
        passwordField.setEchoChar('*');
        unmask.setText(UNMASK);
    }

    private void setUnmask(){
        passwordField.setEchoChar((char)0);
        unmask.setText("Mask");
    }

    public void clear(){
        nameField.setText(EMPTYSTRING);
        passwordField.setText(EMPTYSTRING);
        setMask();
        rootPanel.revalidate();
    }

    public String getUserName(){
        return nameField.getText();
    }

    public String getPass(){
        return new String(passwordField.getPassword());
    }

    public void setState(String message){
        state.setText(message);
        state.revalidate();
    }

    public void setAccountButtonListener(ActionListener listener, String buttonName){
        switch (buttonName){
            case REGISTERNAME : View.setButtonListener(register,listener); return;
            case LOGOUTNAME : View.setButtonListener(logout,listener); return;
            case LOGINNAME : View.setButtonListener(login,listener); return;
        }
    }

}
