package TaskManagerClientPart.MenuClasses;

import CommonClasses.Constrains;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * The child class of AbstractContent that includes the content for server connection and log in.
 */
public class ConnectionContent extends AbstractContent {

    /**
     * The server content.
     */
    private ServerContent server;

    /**
     * The account content.
     */
    private AccountContent account;

    /**
     * The default constructor of the class that delegate to constructor of parent class.
     */
    public ConnectionContent(){
        super();
    }

    /**
     * The method overrides from the parent class the behaviour of init elements.
     */
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

    /**
     * The get method delegate to server content for address.
     * @return the string if the field is filled and empty string if not.
     * @see ServerContent#getServerAddress()
     */
    public String getServerAddress(){
        return server.getServerAddress();
    }

    /**
     * The get method delegate to server content for port.
     * @return the string if the field is filled and empty iof not.
     * @see ServerContent#getPort()
     */
    public String getPort(){
        return server.getPort();
    }

    /**
     * The method delegate to clear method to server content.
     * @see ServerContent#clear()
     */
    public void clearServerPart(){
        server.clear();
    }

    /**
     * The method delegate to server content.
     * @param message - the string argument.
     * @see ServerContent#setState(String)
     */
    public void setState(String message){
        server.setState(message);
    }

    /**
     * The get method delegate to account content.
     * @return the calling of the method of account content.
     * @see AccountContent#getUserName()
     */
    public String getUserName(){
        return account.getUserName();
    }

    /**
     * The get method delegate to account content
     * @return the calling of the method of account content.
     * @see AccountContent#getPass()
     */
    public String getPass(){
        return account.getPass();
    }

    /**
     * The method delegate to account content.
     * @see AccountContent#clear()
     */
    public void clearAccountPart(){
        account.clear();
    }

    /**
     * The set method delegate to account content.
     * @param message the string message.
     * @see AccountContent#setState(String)
     */
    public void setAccountState(String message){
        account.setState(message);
    }

    /**
     * The set method delegate to account content.
     * @param listener the handler argument.
     * @param buttonName the string argument.
     * @see AccountContent#setAccountButtonListener(ActionListener, String)
     */
    public void setAccountButtonListener(ActionListener listener, String buttonName){
        account.setAccountButtonListener(listener,buttonName);
    }

    /**
     * The set method delegate to server content.
     * @param listener the handler argument.
     * @param buttonName the string argument.
     * @see ServerContent#setServerButtonListener(ActionListener, String)
     */
    public void setServerButtonListener(ActionListener listener, String buttonName){
        server.setServerButtonListener(listener,buttonName);
    }
}
