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

/**
 * The class that realize the web part for the client side.
 */
public class WebClient {

    /**
     * The logger of the class.
     */
    private static Logger logger = Logger.getLogger(WebClient.class);

    /**
     * The UI.
     */
    private IView gui;

    /**
     * The input stream.
     */
    private DataInputStream in;

    /**
     * The output stream.
     */
    private DataOutputStream out;

    /**
     * The indicator of log in.
     */
    private boolean logged=false;

    /**
     * The name of current user.
     */
    private String currUser;

    /**
     * The socket od the current connection.
     */
    private Socket connection;

    /**
     * The task data base.
     */
    private TaskDataSource taskSource;

    /**
     * The constructor of the class that get UI and data source.
     * @param gui the UI.
     * @param taskSource the task data base.
     */
    public WebClient(IView gui, TaskDataSource taskSource){
        this.gui=gui;
        this.taskSource=taskSource;
    }

    /**
     * The method get the connection by the argument socket, get the in/out streams and set the
     * @param socket
     * @param messageState
     */
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

    /**
     * The method tell is the connection opened.
     * @return the true if the connection opened and false if not.
     */
    public boolean isConnected(){
        return connection!=null?connection.isConnected():false;
    }

    /**
     * The method close the connection(send the special word from protocol). If the user is logged in the web client
     * log it out. And set null all the components.
     */
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

    /**
     * The method set null all the components.
     */
    public void setNULLServer(){
        in=null;
        out=null;
        connection=null;
        gui.setState(ServerContent.NOTCONNECTED);
    }

    /**
     * The method assert connection, send the request to register the user. If there is the same user on the server,
     * register is failed and the error message is sent, but if the register successful - the account state changes
     * on the username that is registered and in that time logged.
     * @param name the user name value.
     * @param password the password value.
     */
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
            logger.warn("Failed connection with the registering");
            connectionFailed();
            return;
        }
    }

    /**
     * The method is called when the log in is successful to clear the fields in account and refresh the state field.
     * @param name the username.
     */
    private void successLogin(String name){
        logged=true;
        currUser=name;
        gui.clearAccount();
        gui.setAccountState("Logged : " + name);
    }

    /**
     * The method push the pair : user name and password to output stream.
     * @param name the user name.
     * @param pass the password.
     * @throws IOException the error class is thrown when the streams suddenly over.
     */
    public void pushNamePass(String name, String pass) throws IOException{
        out.writeUTF(name);
        out.writeUTF(pass);
        out.flush();
    }

    /**
     * The method that is called for log out and change state of account info. It send the special word from the
     * Protocol
     * @throws IOException the error class is thrown when the streams suddenly over.
     */
    public void closeAccount() throws IOException{
        out.writeUTF(Protocol.LOGOUT);
        out.flush();
        setNULLAccount();
    }

    /**
     * The method clear all account`s tasks, change the state of account info.
     */
    public void setNULLAccount(){
        logged=false;
        currUser=null;
        taskSource.removeAllTasks();
        gui.setAccountState(AccountContent.NOTLOGGED);
        gui.clearAllTasks();
    }

    /**
     * The method to remove the connection and send the message for user about the error.
     */
    private void connectionFailed(){
        setAllNULL();
        gui.showErrorMessage("Connection failed");
    }

    /**
     * The method that that remove all info about account and server.
     */
    private void setAllNULL(){
        setNULLAccount();
        setNULLServer();
    }

    /**
     * The official method for log out from the account. It assert is there the connection and log in for those
     * activity. If yes - the account is logging out.
     */
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

    /**
     * The official method for log in. It assert is there the connection and if yes - the client try to log in by
     * sending pair : user name and password.
     * @param userName the string user name
     * @param password the string password
     */
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

    /**
     * The method send the text string with xml new task to server and wait for the answer. If all right the new task
     * is adding to task data base, but if no - is sending the error message with downloaded text.
     * @param xmlTask the xml formatted task.
     * @param originalTask the string formatted task.
     * @return the true if all right, false - if not.
     */
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

    /**
     * The method that assert the state of the client for working with server. It is connection and login.
     * @return the true if the both of them here and no - if not.
     */
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

    /**
     * The get method for logged indicator.
     * @return the true if it is logged and false - if not.
     */
    public boolean isLogged(){
        return logged;
    }

    /**
     * The method download all the tasks in xml string form.
     * @return the xml string os tasks.
     */
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

    /**
     * The method send to server the message to remove the old task by title. If the answer is successful, it is
     * removed from the local repository.
     * @param xmlOldTask the old task formatted in xml.
     * @param originalTitle the string title of old task.
     * @return the true statement if the removing is successful and false if not.
     */
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

    /**
     * The method send to server the message to change the old task by title. If the answer is successful, it is
     * removed from the local repository.
     * @param xmlOldNewTask the old task formatted in xml.
     * @param oldOriginalTitle the string title.
     * @param newOriginalTask the string formatted new task.
     * @return the true if all is OK anf false if not.
     */
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
