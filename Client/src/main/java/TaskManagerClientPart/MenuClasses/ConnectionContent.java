package TaskManagerClientPart.MenuClasses;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ConnectionContent extends AbstractContent {
    private ServerContent server;
    private AccountContent account;

    public ConnectionContent(){
        super();
    }

    @Override
    protected void initElems() {
        rootPanel = new JPanel();
        server = new ServerContent();
        account = new AccountContent();

        rootPanel.setLayout(new GridBagLayout());
        GridBagConstraints locator = Constrains.getLocator();
        locator.weighty=0.3;
        Constrains.setLocation(locator,0,0);
        rootPanel.add(server.getRootPanel(), locator);

        locator.weighty=0.7;
        Constrains.setLocation(locator,0,1);
        rootPanel.add(account.getRootPanel(),locator);
    }

    public String getServerAddress(){
        return server.getServerAddress();
    }

    public String getPort(){
        return server.getPort();
    }

    public void clearServerPart(){
        server.clear();
    }

    public void setState(String message){
        server.setState(message);
    }

    public String getUserName(){
        return account.getUserName();
    }

    public String getPass(){
        return account.getPass();
    }

    public void clearAccountPart(){
        account.clear();
    }

    public void setAccountState(String message){
        account.setState(message);
    }

    public void setAccountButtonListener(ActionListener listener, String buttonName){
        account.setAccountButtonListener(listener,buttonName);
    }

    public void setServerButtonListener(ActionListener listener, String buttonName){
        server.setServerButtonListener(listener,buttonName);
    }
}
