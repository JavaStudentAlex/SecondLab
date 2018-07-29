package TaskManagerServer.Server.MenuClasses;

import CommonClasses.Constrains;
import CommonClasses.FramesFactory;
import TaskManagerServer.Server.Server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * The class that represent the gui interface of the server.
 */
public class ServerMenu {

    /**
     * The window of the gui.
     */
    private JFrame window;

    /**
     * The main panel of it.
     */
    private JPanel mainPanel;

    /**
     * The object that is responsible.
     */
    private Server server;

    /**
     * The buttons of the gui.
     */
    private JButton start, stop, restart;

    /**
     * The state field of the gui.
     */
    private JLabel state;

    /**
     * The constructor that creates the elements of the gui and tie it to each other.
     * @see ServerMenu#initState()
     * @see ServerMenu#initButtons()
     * @see ServerMenu#initHandlers()
     */
    public ServerMenu(){
        window = FramesFactory.getFrame();
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        window.setContentPane(mainPanel);
        initState();
        initButtons();
        initHandlers();
        mainPanel.revalidate();
    }

    /**
     * The method init and place the state field on the main panel.
     */
    private void initState(){
        state = new JLabel("Do not work");
        GridBagConstraints locator = Constrains.getLocator();
        Constrains.setLocation(locator,0,0);
        locator.fill=GridBagConstraints.NONE;
        mainPanel.add(state,locator);
    }

    /**
     * The method init and place the buttons for the gui.
     */
    private void initButtons(){
        GridBagConstraints locator = Constrains.getLocator();
        start=new JButton("Start");
        stop = new JButton("Stop");
        restart = new JButton("Restart");
        Constrains.setLocation(locator,0,2);
        locator.fill=GridBagConstraints.NONE;
        mainPanel.add(start,locator);
        Constrains.setLocation(locator,0,4);
        locator.fill=GridBagConstraints.NONE;
        mainPanel.add(stop,locator);
        Constrains.setLocation(locator,0,6);
        locator.fill=GridBagConstraints.NONE;
        mainPanel.add(restart,locator);
    }

    /**
     * The method init and tie the handlers with the buttons.
     */
    private void initHandlers(){
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                start();
            }
        });

        stop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stop();
            }
        });

        restart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(server!=null){
                    stop();
                    start();
                }
            }
        });

        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                stop();
            }
        });
    }

    /**
     * The method to start the server.
     */
    private void start(){
        if(server!=null){return;}
        server = new Server();
        state.setText("Work on 2000 port");
        System.out.println("Worked start server");
        mainPanel.revalidate();
    }

    /**
     * The method to stop the server.
     */
    private void stop(){
        if (server==null){return;}
        server.clearServer();
        server=null;
        state.setText("Do not work");
        mainPanel.revalidate();
    }
}
