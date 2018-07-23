package TaskManagerClientPart.MenuClasses;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The child class of AbstractContent.
 */
public class ServerContent extends AbstractContent {

    /**
     * The address and port fields.
     */
    private JTextField addressField, portField;

    /**
     * The buttons.
     */
    private JButton connect, disconnect, clear;

    /**
     * The name of buttons.
     */
    public static final String CONNECTNAME="Connect",DISCONNECTNAME="Disconnect";

    /**
     * The IP format.
     */
    public static final String ipv4Format = "xxx.xxx.xxx.xxx";

    /**
     * The default port.
     */
    public static final String defaultPort = "2000";

    /**
     * The state of no connect.
     */
    public static final String NOTCONNECTED = "Not connected";

    /**
     * The state field.
     */
    private static JLabel state;

    /**
     * The default constructor delegate it to parent constructor.
     */
    public ServerContent(){
        super();
    }

    /**
     * The method overrides the behaviour of the parent class of init elements.
     */
    @Override
    protected void initElems() {
        rootPanel = PanelFactory.createPanel("Server");
        rootPanel.setLayout(new GridBagLayout());
        locateLabels();
        locateFields();
        locateButtons();
    }

    /**
     * The method init and place the labels.
     */
    private void locateLabels(){
        GridBagConstraints locator = Constrains.getLocator();
        locator.weighty = 0.25;
        Constrains.setLocation(locator,0,0);
        rootPanel.add(new JLabel("IPV4 ADDRESS : "),locator);
        Constrains.setLocation(locator,0,1);
        rootPanel.add(new JLabel("PORT : "),locator);
        Constrains.setLocation(locator,0,2);
        rootPanel.add(new JLabel("CONNECTION STATE : "),locator);
    }

    /**
     * The method init and place the text fields.
     */
    private void locateFields(){
        GridBagConstraints locator = Constrains.getLocator();
        locator.weighty=0.25;
        locator.fill = GridBagConstraints.HORIZONTAL;
        Constrains.setSize(locator,2,1);
        Constrains.setLocation(locator,1,0);
        addressField = new JTextField(ipv4Format,35);
        portField = new JTextField(defaultPort,35);
        rootPanel.add(addressField,locator);
        Constrains.setLocation(locator,1,1);
        rootPanel.add(portField,locator);
        Constrains.setLocation(locator,1,2);
        state= new JLabel(NOTCONNECTED);
        rootPanel.add(state,locator);
    }

    /**
     * The method init and place the buttons.
     */
    private void locateButtons(){
        GridBagConstraints locator = Constrains.getLocator();
        connect = new JButton("connect");
        disconnect = new JButton("disconnect");
        clear = new JButton("clear");
        setClearHandler();

        locator.weighty = 0.25;
        locator.fill = GridBagConstraints.HORIZONTAL;
        Constrains.setLocation(locator,0,3);
        rootPanel.add(clear,locator);

        Constrains.setLocation(locator,1,3);
        rootPanel.add(connect,locator);

        Constrains.setLocation(locator,2,3);
        rootPanel.add(disconnect,locator);
    }

    /**
     * The get method for server address field.
     * @return the string if the field id filled and the empty one if not.
     */
    public String getServerAddress(){
        return addressField.getText();
    }

    /**
     * The get method for server port field.
     * @return the string if the field id filled and the empty one if not.
     */
    public String getPort(){
        return portField.getText();
    }

    /**
     * The method clear all text fields.
     */
    public void clear(){
        addressField.setText(ipv4Format);
        portField.setText(defaultPort);
        rootPanel.revalidate();
    }

    /**
     * The method set the state of the connection to server.
     * @param message
     */
    public void setState(String message){
        state.setText(message);
        state.revalidate();
    }

    /**
     * The method set the handler for the button clear in this server part.
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
     * The method set the handler for the buttons with the button name.
     * @param listener the handler
     * @param buttonName the button name for handler.
     */
    public void setServerButtonListener(ActionListener listener, String buttonName){
        switch (buttonName){
            case CONNECTNAME : View.setButtonListener(connect,listener); return;
            case DISCONNECTNAME : View.setButtonListener(disconnect,listener); return;
        }
    }
}
