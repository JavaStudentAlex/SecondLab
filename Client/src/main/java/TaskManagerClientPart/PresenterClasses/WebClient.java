package TaskManagerClientPart.PresenterClasses;

import CommonClasses.Protocol;
import TaskManagerClientPart.MenuClasses.AccountContent;
import TaskManagerClientPart.MenuClasses.IView;
import TaskManagerClientPart.MenuClasses.ServerContent;
import CommonClasses.SocketWorker;
import CommonClasses.TaskStringForm;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.Socket;

public class WebClient {

    private static Logger logger = Logger.getLogger(WebClient.class);

    private IView gui;

    private DataInputStream in;

    private DataOutputStream out;

    private boolean logged=false;

    private String currUser;

    private Socket connection;

    private TaskDataSource taskSource;

    public WebClient(IView gui, TaskDataSource taskSource){
        this.gui=gui;
        this.taskSource=taskSource;
    }

    public void setConnection(Socket socket,String messageState){
        closeConnection();
        try {
            connection = socket;
            in = SocketWorker.getReader(connection);
            out = SocketWorker.getWriter(connection);
            gui.setState(messageState);
        } catch(IOException e) {
            logger.warn("Error in server connection");
            gui.showErrorMessage("Error server connection");
            setNULLServer();
        }
    }
    public boolean isConnected(){
        return connection!=null?connection.isConnected():false;
    }

    public void closeConnection(){
        if(connection==null){
            return;
        }
        if(connection.isClosed()){
            setNULLServer();
            return;}
        try {
            if(logged){
                closeAccount();
            }
            out.writeUTF(Protocol.DISCONNECT);
            out.flush();
            connection.close();
            setNULLServer();
        }catch (IOException e){
            logger.warn("Failed connection");
            setAllNULL();
        }
    }

    public void setNULLServer(){
        in=null;
        out=null;
        connection=null;
        gui.setState(ServerContent.NOTCONNECTED);
    }

    public void setRegister(String name, String password){
        if (!isConnected()){
            gui.showErrorMessage("Not connected to server");
            return;
        }

        try{
            if (logged){
                closeAccount();
            }
            out.writeUTF(Protocol.REGISTER);
            pushNamePass(name,password);
            boolean result = in.readBoolean();
            if(!result){
                gui.showErrorMessage("Name already in use");
                return;
            }
            successLogin(name);
            taskSource.initTasks();
            taskSource.initCheker();
        }catch (IOException e){
            logger.warn("Failed connection thrue the registering");
            connectionFailed();
            return;
        }
    }

    private void successLogin(String name){
        logged=true;
        currUser=name;
        gui.clearAccount();
        gui.setAccountState("Logged : " + name);
    }

    public void pushNamePass(String name, String pass) throws IOException{
        out.writeUTF(name);
        out.writeUTF(pass);
        out.flush();
    }

    public void closeAccount() throws IOException{
        out.writeUTF(Protocol.LOGOUT);
        out.flush();
        setNULLAccount();
    }

    public void setNULLAccount(){
        logged=false;
        currUser=null;
        taskSource.removeAllTasks();
        gui.setAccountState(AccountContent.NOTLOGGED);
        gui.clearAllTasks();
    }

    private void connectionFailed(){
        setAllNULL();
        gui.showErrorMessage("Connection failed");
    }

    private void setAllNULL(){
        setNULLAccount();
        setNULLServer();
    }

    public void logout(){
        try {
            if(!logged){
                gui.showErrorMessage("Not already logged in");
                return;
            }
            closeAccount();
        } catch (IOException e) {
            logger.warn("Failed connection with the logging out");
            connectionFailed();
            return;
        }
    }

    public void login(String userName, String password){
        if(!isConnected()){
            gui.showErrorMessage("Not connected to server");
            return;
        }
        try{
            if(logged){
                closeAccount();
            }
            out.writeUTF(Protocol.LOGIN);
            pushNamePass(userName,password);
            boolean success = in.readBoolean();
            if(!success){
                gui.showErrorMessage("Wrong username/password");
                return;
            }
            successLogin(userName);
            String xmlAllTasks = loadAllTasks();
            if (xmlAllTasks==null){
                connectionFailed();
                return;
            }
            taskSource.tasksSaveAndSet(xmlAllTasks);
        }catch (IOException e){
            logger.warn("Failed connection with the logging in");
            connectionFailed();
            return;
        }
    }

    public boolean addNewTask(String xmlTask, TaskStringForm originalTask){
        if (!validateState()){return false;}
        try {
            out.writeUTF(Protocol.ADD);
            out.writeUTF(xmlTask);
            out.flush();
            boolean result = in.readBoolean();
            if(result){
                taskSource.addNewTask(originalTask);
                return true;
            }
            else{
                String message = in.readUTF();
                gui.showErrorMessage(message);
                return false;
            }
        } catch (IOException e) {
            logger.warn("Failed connection with adding new task");
            connectionFailed();
            return false;
        }
    }

    private boolean validateState(){
        if(connection==null || connection.isClosed()){
            gui.showErrorMessage("Not connected");
            return false;
        }
        if(!logged){
            gui.showErrorMessage("Not logged");
            return false;
        }
        return true;
    }

    public boolean isLogged(){
        return logged;
    }

    public String loadAllTasks(){
        try {
            String result = in.readUTF();
            return result;
        } catch (IOException e) {
            logger.warn("Error in connection by loading tasks");
            connectionFailed();
            return null;
        }
    }

    public boolean removeTaskByTitle(String xmlOldTask, String originalTitle){
        if(!validateState()){
            return false;
        }
        try {
            out.writeUTF(Protocol.REMOVE);
            out.writeUTF(xmlOldTask);
            out.flush();
            boolean result = in.readBoolean();
            if(!result){
                gui.showErrorMessage(in.readUTF());
                connectionFailed();
                return false;
            }
            taskSource.removeTaskByTitle(originalTitle);
            return true;
        } catch (IOException e) {
            logger.warn("Failed connection by removing tasks");
            connectionFailed();
            return false;
        }
    }

    public boolean chnageTaskByTitle(String xmlOldNewTask, String oldOriginalTitle,TaskStringForm newOriginalTask ){
        if(!validateState()){
            return false;
        }
        try {
            out.writeUTF(Protocol.CHANGE);
            out.writeUTF(xmlOldNewTask);
            out.flush();
            boolean result = in.readBoolean();
            if(!result){
                gui.showErrorMessage(in.readUTF());
                connectionFailed();
                return false;
            }
            taskSource.changeTaskByTitle(oldOriginalTitle,newOriginalTask);
            return true;
        } catch (IOException e) {
            logger.warn("Failed connection by changing task");
            connectionFailed();
            return false;
        }
    }
}
