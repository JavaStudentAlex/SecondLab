package TaskManagerClientPart.PresenterClasses.Cotrollers;

import TaskManagerClientPart.MenuClasses.IView;
import TaskManagerClientPart.MenuClasses.ServerContent;
import TaskManagerClientPart.PresenterClasses.WebClient;
import org.apache.log4j.Logger;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.regex.Pattern;

/**
 * The class that control connection to server.
 */
public class ConnectionController extends AbstractController {
    /**
     * The logger of this class.
     */
    private static Logger logger = Logger.getLogger(ConnectionController.class);

    /**
     * The port max and min scopes.
     */
    private static final int MINPORT=1,MAXPORT=65535;

    /**
     * The patterns for IP validation.
     */
    private static final String oktatPattern = "(\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5])",
            ipv4Pattern = new StringBuilder("^").append("(").append(oktatPattern).append("\\.")
            .append(")").append("{3}").append(oktatPattern).append("$").toString();

    /**
     * The value of current connection.
     */
    private String txtAddress,txtPort;

    /**
     * The method add the connection handler to UI.
     */
    @Override
    protected void init() {
        gui.setServerButtonListener(new ConnectionHandler(), ServerContent.CONNECTNAME);
    }

    /**
     * The constructor delegate object`s creating to parent class.
     * @param gui the UI.
     * @param client the web part.
     */
    public ConnectionController(IView gui, WebClient client){
        super(gui,client);
    }

    /**
     * The method try to get connection with address and port in fields and if successful - set the info state, if no -
     * send the error message to UI.
     */
    private void connect(){
        Socket connection = getConnection();
        if(connection==null){
            return;
        }
        client.setConnection(connection,new StringBuilder("Connected on : ").append(txtAddress).append(":")
                .append(txtPort).toString());
        gui.clearServer();
    }

    /**
     * The method validate the values of the IP and port. If it is valid - create the socket connection, if no - return
     * null and send error message to UI.
     * @return
     */
    private Socket getConnection(){
        String tempAddress = gui.getServerAddress().trim();
        String tempPort = gui.getPort().trim();

        if (tempAddress.isEmpty()){
            gui.showErrorMessage("Address field is empty");
            return null;
        }
        if (tempPort.isEmpty()){
            gui.showErrorMessage("Port field is empty");
            return null;
        }

        if(!Pattern.matches(ipv4Pattern,tempAddress)){
            gui.showErrorMessage("IP not valid");
            return null;
        }

        int port = parsePort(tempPort);
        if (port==0){return null;}

        if (port<MINPORT || port>MAXPORT){
            gui.showErrorMessage("Port must be in ["+MINPORT+","+MAXPORT+"]");
            return null;
        }

        if(client.isConnected() && tempPort==txtPort && txtAddress==tempAddress){
            gui.showErrorMessage("Already connected");
            return null;
        }

        try {
            InetAddress address = InetAddress.getByName(tempAddress);
            Socket result = new Socket(address,port);
            txtAddress=tempAddress;
            txtPort=tempPort;
            return result;
        } catch (UnknownHostException e) {
            logger.info("Unknown IP address");
            gui.showErrorMessage("Unknown IP address");
            return null;
        }catch (IOException e){
            logger.info("Server not found");
            gui.showErrorMessage("Server not found");
            return null;
        }
    }

    /**
     * The method parse the port value from string to integer.
     * @param port the port string value.
     * @return the port number or 0 if port not valid.
     */
    private int parsePort(String port){
        int result;
        try{
            result = Integer.parseInt(port);
            return result;
        }catch (NumberFormatException e){
            logger.info("Port not valid");
            gui.showErrorMessage("Port not valid");
            return 0;
        }
    }

    /**
     * The handler class for the connection activity.
     */
    private class ConnectionHandler implements ActionListener{
        /**
         * The method that catch the activity and delegate work to connect() method.
         * @param e the event class.
         * @see ConnectionController#connect()
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            connect();
        }
    }
}
