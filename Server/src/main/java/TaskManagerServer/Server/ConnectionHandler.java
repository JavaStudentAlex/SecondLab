package TaskManagerServer.Server;

import TaskManagerServer.Exceptions.MyOwnException;
import TaskManagerServer.ModelClasses.Task;
import TaskManagerServer.CommonClasses.*;
import TaskManagerServer.Server.Processors.*;
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

public class ConnectionHandler extends Thread {
    private Server server;
    private Socket connection;
    private List<AbstractProcessor> processors;
    private String currrentUser;
    private String currentDir;
    private Map<String,Task> tasks;

    public ConnectionHandler(Server server){
        super();
        this.server = server;
    }

    public void setConnection(Socket connection) {
        this.connection = connection;
        initProcessors();
        start();
    }

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
            System.out.println("Error in server work");
            stopThis();
            return;
        }
    }

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

    public void stopThis(){
        server.removeThread(this);
        try {
            if (connection!=null){
                connection.close();
            }
        } catch (IOException e) {}
        this.interrupt();
    }

    public boolean containsUser(String user, String password, boolean authorization){
        return server.containsUser(user, password,authorization);
    }

    public void writeUser(String user, String pass) throws IOException{
        server.writeUser(user,pass);
        currrentUser = user;
        currentDir = getCurrentDir(currrentUser);
        new File(currentDir).mkdirs();
        new File(currentDir + File.separator + Server.USER_FILE).createNewFile();
        initTasks();
        server.addActiveUser(user);
    }

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

    private void initProcessors(){
        processors = new ArrayList<AbstractProcessor>();
        processors.add(new RegisterProcessor(this));
        processors.add(new LoginProcessor(this));
        processors.add(new LogoutProcessor(this));
        processors.add(new AddProcessor(this));
        processors.add(new RemoveProcessor(this));
        processors.add(new ChangeProcessor(this));
    }

    private static String getCurrentDir(String user){
        return Server.MAIN_DIR+File.separator+Server.USER_DIR+File.separator+user;
    }

    public void logout(){
        if(currrentUser!=null){
            writeTasksToCurFile(getCurrentTasksStringList());
            server.removeActiveUser(currrentUser);
        }
        currrentUser = null;
        currentDir = null;
        destroyTasks();
    }

    public String getUser(){
        return currrentUser;
    }

    public boolean containsTask(String task){
        for (Map.Entry<String,Task> curTask:tasks.entrySet()){
            if (curTask.getKey().equals(task)){
                return true;
            }
        }
        return false;
    }

    private void initTasks(){
        tasks = new HashMap<>();
    }

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
            e.printStackTrace();
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
            System.out.println("Error in user tasks parsing");
            return;
        }

        for(TaskStringForm stringTask : stringTasks){
            try {
                Task temp = stringTask.parseTask();
                tasks.put(temp.getTitle(),temp);
            } catch (MyOwnException e) {
                continue;
            }
        }
    }

    private void destroyTasks(){
        tasks = null;
    }

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

    private void writeTasksToCurFile(List<TaskStringForm> tasksStringList){
        try {
            XMLStreamWriter writer = XMLOutputFactory.newFactory().
                    createXMLStreamWriter(new FileOutputStream(currentDir+File.separator+Server.USER_FILE));
            TasksXMLParser.writeTasksByXML(writer,tasksStringList);
        } catch (XMLStreamException | FileNotFoundException e) {
            System.out.println("Should not be because directories asserted");
        }
    }

    public void addNewTask(Task task){
        tasks.put(task.getTitle(),task);
    }

    public void removeOldTask(String oldTaskTitle){
        tasks.remove(oldTaskTitle);
    }

    public boolean isLogged(String user){
        return server.isLogged(user);
    }
}
