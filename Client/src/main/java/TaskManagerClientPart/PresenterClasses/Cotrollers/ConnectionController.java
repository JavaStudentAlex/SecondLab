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

public class ConnectionController extends AbstractController {
    private static Logger logger = Logger.getLogger(ConnectionController.class);

    private static final int MINPORT=1;
    private static final int MAXPORT=65535;
    private static final String oktatPattern = "(\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5])";
    private static final String ipv4Pattern = new StringBuilder("^").append("(").append(oktatPattern).append("\\.")
            .append(")").append("{3}").append(oktatPattern).append("$").toString();
    private String txtAddress;
    private String txtPort;

    @Override
    protected void init() {
        gui.setServerButtonListener(new ConnectionHandler(), ServerContent.CONNECTNAME);
    }

    public ConnectionController(IView gui, WebClient client){
        super(gui,client);
    }
    private void connect(){
        Socket connection = getConnection();
        if(connection==null){
            return;
        }
        client.setConnection(connection,new StringBuilder("Connected on : ").append(txtAddress).append(":")
                .append(txtPort).toString());
        gui.clearServer();
    }

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

    private class ConnectionHandler implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            connect();
        }
    }
}
