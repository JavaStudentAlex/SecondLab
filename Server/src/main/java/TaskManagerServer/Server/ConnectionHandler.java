package TaskManagerServer.Server;

import Exceptions.MyOwnException;
import CommonClasses.*;
import TaskManagerServer.Server.Processors.*;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The class that realize the separated thread of connection.
 */
public class ConnectionHandler extends Thread {

    /**
     * The logger of the class.
     */
    private static Logger logger = Logger.getLogger(ConnectionHandler.class);

    /**
     * The link to the server.
     */
    private Server server;

    /**
     * The connection socket.
     */
    private Socket connection;

    /**
     * The list of the processors that can work.
     */
    private List<AbstractProcessor> processors;

    /**
     * The name of the current logged user.
     */
    private String currrentUser;

    /**
     * The path to current user directory.
     */
    private String currentDir;

    /**
     * The local repository of tasks.
     */
    private Map<String,Task> tasks;

    /**
     * The constructor that gets the server link and save it. But it does not start the connection`s thread.
     * @param server the server link.
     */
    public ConnectionHandler(Server server){
        super();
        this.server = server;
    }

    /**
     * The method set the connection socket and start the connection thread.
     * @param connection the link to connection socket.
     */
    public void setConnection(Socket connection) {
        this.connection = connection;
        initProcessors();
        start();
    }

    /**
     * The method that overrides the behaviour of the thread from the parent class. The method gets the int/out streams
     * and call the main method for treatment work with user.
     */
    @Override
    public void run() {
        try(
            DataOutputStream writer = SocketWorker.getWriter(connection);
            DataInputStream reader = SocketWorker.getReader(connection);
        )
        {
            process(reader,writer);
            stopThis();
        }catch (IOException e) {
            logger.warn("Error in server work");
            stopThis();
            return;
        }
    }

    /**
     * The main method that wait for the instruction from the user, than compare it to the processor`s names and find
     * right one. After that the processor is running for doing it`s function.
     * @param reader the in stream.
     * @param writer the out stream.
     * @throws IOException the error class that is thrown when the stream is broken.
     */
    public void process(DataInputStream reader, DataOutputStream writer) throws IOException{
        String instruction;
        while (true){
            instruction = reader.readUTF();
            if (instruction.equals(Protocol.DISCONNECT)){
                if(!connection.isClosed()){
                    connection.close();
                }
                return;
            }

            for(AbstractProcessor proc : processors){
                if(proc.canProcess(instruction)){
                    proc.process(reader,writer);
                    break;
                }
            }
        }
    }

    /**
     * The method stop this own thread and close the connection.
     */
    public synchronized void stopThis(){
        server.removeThread(this);
        clearThisThread();
    }

    /**
     * The method delegates the calling to server link.
     * @param user the user name.
     * @param password the password.
     * @param authorization the identifier of the authorization operation.
     * @return the calling of the method containsUser() from the server link.
     */
    public boolean containsUser(String user, String password, boolean authorization){
        return server.containsUser(user, password,authorization);
    }

    /**
     * The method that write a new user with said user name and password, create it`s own directory and log in.
     * @param user the user name.
     * @param pass the password.
     * @throws IOException the error class that is thrown when the error in io.
     */
    public void writeUser(String user, String pass) throws IOException{
        server.writeUser(user,pass);
        currrentUser = user;
        currentDir = getCurrentDir(currrentUser);
        new File(currentDir).mkdirs();
        new File(currentDir + File.separator + Server.USER_FILE).createNewFile();
        initTasks();
        server.addActiveUser(user);
    }

    /**
     * The method that set the user logged in, assert his directory.
     * @param user the user name
     * @throws IOException the error class that is thrown when the error in io.
     */
    public void setUser(String user) throws IOException{
        currrentUser=user;
        currentDir = getCurrentDir(currrentUser);
        new File(currentDir).mkdirs();
        File temp = new File(currentDir+File.separator+Server.USER_FILE);
        if (!temp.exists()){
            temp.createNewFile();
        }
        initTasks();
        loadTasks();
        server.addActiveUser(user);
    }

    /**
     * The method that init the processors that will all the time handle the user`s requests.
     */
    private void initProcessors(){
        processors = new ArrayList<AbstractProcessor>();
        processors.add(new RegisterProcessor(this));
        processors.add(new LoginProcessor(this));
        processors.add(new LogoutProcessor(this));
        processors.add(new AddProcessor(this));
        processors.add(new RemoveProcessor(this));
        processors.add(new ChangeProcessor(this));
    }

    /**
     * The static get method for the field of current dir.
     * @param user the name of the user.
     * @return the string with user dir path.
     */
    private static String getCurrentDir(String user){
        return Server.MAIN_DIR+File.separator+Server.USER_DIR+File.separator+user;
    }

    /**
     * The method that with the logging out save all tasks to file, remove the user name from the current active users
     * and clear the name of active user and identifier of the logged in.
     */
    public void logout(){
        if(currrentUser!=null){
            writeTasksToCurFile(getCurrentTasksStringList());
            server.removeActiveUser(currrentUser);
        }
        currrentUser = null;
        currentDir = null;
        destroyTasks();
    }

    /**
     * The get method for the current user field.
     * @return string if logged and null if not.
     */
    public String getUser(){
        return currrentUser;
    }

    /**
     * The method assert is there the task with the title.
     * @param task the asserting title.
     * @return the true if there is the task and not if there isn`t.
     */
    public boolean containsTask(String task){
        for (Map.Entry<String,Task> curTask:tasks.entrySet()){
            if (curTask.getKey().equals(task)){
                return true;
            }
        }
        return false;
    }

    /**
     * The method create the map container for tasks.
     */
    private void initTasks(){
        tasks = new HashMap<>();
    }

    /**
     * The method read the string in xml format from the file, parse it with the xml parser and get the list of string
     * formatted tasks.
     */
    private void loadTasks(){
        String fileName=currentDir+File.separator+Server.USER_FILE;
        File file = new File(fileName);
        if (file.length()==0){
            return;
        }
        StringBuilder gasterBeiter;
        try {
            gasterBeiter = new StringBuilder("");
            List<String> strings = Files.readAllLines(Paths.get(fileName));
            for (String tempStr : strings){
                gasterBeiter.append(tempStr);
            }
        } catch (IOException e) {
            System.out.println("Error from file system reading");
            stopThis();
            return;
        }

        String tasksStringXML = gasterBeiter.toString();
        if (tasksStringXML.trim().isEmpty()){
            return;
        }
        List<TaskStringForm> stringTasks=null;
        try {
            stringTasks = new TasksXMLParser(tasksStringXML).getStringFormTasks();
        } catch (SAXException | ParserConfigurationException | IOException e) {
            logger.warn("Error in user tasks parsing");
            return;
        }

        for(TaskStringForm stringTask : stringTasks){
            try {
                Task temp = stringTask.parseTask();
                tasks.put(temp.getTitle(),temp);
            } catch (MyOwnException e) {
                logger.info("Beaten task from file");
                continue;
            }
        }
    }

    /**
     * The method that null the tasks map container.
     */
    private void destroyTasks(){
        tasks = null;
    }

    /**
     * The method return the list of string formatted tasks.
     * @return the list.
     */
    public List<TaskStringForm> getCurrentTasksStringList(){
        List<TaskStringForm> result = new ArrayList<>();
        Task tempTask;
        TaskStringForm tempTaskString;
        for(Map.Entry<String,Task> temp : tasks.entrySet()){
            tempTask = temp.getValue();
            tempTaskString = new TaskStringForm();
            tempTaskString.setTitle(tempTask.getTitle());
            tempTaskString.setActive(tempTask.isActive());
            tempTaskString.setRepeat(tempTask.isRepeated());
            if(tempTask.isRepeated()){
                tempTaskString.setStartDate(tempTask.getStartTime());
                tempTaskString.setEndDate(tempTask.getEndTime());
                tempTaskString.setInterval(tempTask.getRepeatInterval());
            }
            else{
                tempTaskString.setStartDate(tempTask.getTime());
            }
            result.add(tempTaskString);
        }
        return result;
    }

    /**
     * The method write the list of string formatted tasks to file in xml format.
     * @param tasksStringList the list of string formatted tasks.
     */
    private void writeTasksToCurFile(List<TaskStringForm> tasksStringList){
        try {
            XMLStreamWriter writer = XMLOutputFactory.newFactory().
                    createXMLStreamWriter(new FileOutputStream(currentDir+File.separator+Server.USER_FILE));
            TasksXMLParser.writeTasksByXML(writer,tasksStringList);
            System.out.println("Writing new task to " + currentDir+File.separator+Server.USER_FILE);
        } catch (XMLStreamException | FileNotFoundException e) {
            logger.info("Should not be because directories asserted");
        }
    }

    /**
     * The method add a new task.
     * @param task the task object link.
     */
    public void addNewTask(Task task){
        tasks.put(task.getTitle(),task);
        writeTasksToCurFile(getCurrentTasksStringList());
    }

    /**
     * The method remove the task by the title.
     * @param oldTaskTitle the title of the chosen task.
     */
    public void removeOldTask(String oldTaskTitle, boolean isChange){
        tasks.remove(oldTaskTitle);
        if(!isChange){
            writeTasksToCurFile(getCurrentTasksStringList());
        }
    }

    /**
     * The get method that delegate the calling to server link.
     * @param user the user name.
     * @return calling the method  isLogged() in server
     */
    public boolean isLogged(String user){
        return server.isLogged(user);
    }

    /**
     * The method for clearing the threads of the user. It stops the connection to client.
     */
    public synchronized  void clearThisThread(){
        try {
            if (connection!=null){
                connection.close();
            }
        } catch (IOException e) {
            logger.info("Could not close connection by stopping");
        }
        this.interrupt();
    }
}
