package TaskManagerClientPart.MenuClasses;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ServerContent extends AbstractContent {
    private JTextField addressField, portField;
    private JButton connect, disconnect, clear;
    public static final String CONNECTNAME="Connect";
    public static final String DISCONNECTNAME="Disconnect";
    public static final String ipv4Format = "xxx.xxx.xxx.xxx";
    public static final String defaultPort = "2000";
    public static final String NOTCONNECTED = "Not connected";
    private static JLabel state;

    public ServerContent(){
        super();
    }

    @Override
    protected void initElems() {
        rootPanel = PanelFactory.createPanel("Server");
        rootPanel.setLayout(new GridBagLayout());
        locateLabels();
        locateFields();
        locateButtons();
    }

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

    public String getServerAddress(){
        return addressField.getText();
    }

    public String getPort(){
        return portField.getText();
    }

    public void clear(){
        addressField.setText(ipv4Format);
        portField.setText(defaultPort);
        rootPanel.revalidate();
    }

    public void setState(String message){
        state.setText(message);
        state.revalidate();
    }

    private void setClearHandler(){
        clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clear();
            }
        });
    }

    public void setServerButtonListener(ActionListener listener, String buttonName){
        switch (buttonName){
            case CONNECTNAME : View.setButtonListener(connect,listener); return;
            case DISCONNECTNAME : View.setButtonListener(disconnect,listener); return;
        }
    }
}
