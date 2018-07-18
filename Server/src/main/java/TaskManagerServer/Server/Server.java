package TaskManagerServer.Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import TaskManagerServer.Server.XMLParsers.UsersXMLParser;
import org.apache.log4j.Logger;

public class Server {
    private static Logger logger = Logger.getLogger(Server.class);
    public static final String MAIN_DIR = "Server";
    public static final String USER_DIR = "Users";
    public static final String USER_FILE = "Tasks.xml";
    public static final String ALL_USERS_FILE = "Users.xml";
    public static final int STATISTIC_CONNECTIONS=10;
    private ServerSocket listener;
    private Queue<ConnectionHandler> pool;
    private List<ConnectionHandler> inUse;
    private Map<String, String> users;
    private List<String> activeUsers;
    private UsersXMLParser usersParser;

    public Server(){
        activeUsers=new ArrayList<>();
        pool = new LinkedList<>();
        inUse=new ArrayList<>();
        initPool();
        try {
            assertMainDirectories();
            initUsers();
            assertUsersDirectories();
        } catch (IOException e) {
            logger.warn("Can not write/read directories");
            return;
        }

        try {
            listener = new ServerSocket(2000);
            logger.info("Server start working at " + listener.getLocalSocketAddress());
            while (true) {
                Socket socket = listener.accept();
                ConnectionHandler handler = pool.remove();
                if(pool.size()<2){
                    initPool();
                }
                handler.setConnection(socket);
                inUse.add(handler);
            }
        } catch (IOException e) {
            logger.warn("Can't run on those port");
        }
    }

    private void initPool(){
        for (int i=0;i<STATISTIC_CONNECTIONS;++i){
            pool.add(new ConnectionHandler(this));
        }
    }

    public synchronized boolean isLogged(String user){
        return activeUsers.contains(user);
    }

    public synchronized void addActiveUser(String user){
        activeUsers.add(user);
    }

    public synchronized void removeActiveUser(String user){
        activeUsers.remove(user);
    }

    public synchronized void removeThread(ConnectionHandler thread){
        inUse.remove(thread);
    }

    public synchronized boolean containsUser(String user, String password, boolean authorization){
        if(!users.containsKey(user)){
            return false;
        }
        if(users.containsKey(user) && !authorization){
            return true;
        }
        return users.get(user).equals(password);
    }

    public synchronized void writeUser(String user, String password) throws IOException{
        users.put(user,password);
        usersParser.writeUsers(users);
    }

    private void initUsers() throws IOException{
        usersParser = new UsersXMLParser(new File(MAIN_DIR+File.separator+
                                            USER_DIR+File.separator+ALL_USERS_FILE));
        users = usersParser.getAllUsers();
    }

    private void assertMainDirectories() throws IOException{
        File file = new File(MAIN_DIR+File.separator+USER_DIR);
        if(!file.exists()){
            file.mkdirs();
        }
    }

    private void assertUsersDirectories() throws IOException{
        File file = new File(MAIN_DIR+File.separator+USER_DIR);
        for(Map.Entry<String,String> user : users.entrySet()){
            File temp = new File(file,user.getKey());
            if (!temp.exists()){
                temp.mkdir();
            }

            File userTasksTemp = new File(temp,USER_FILE);
            if(!userTasksTemp.exists()){
                userTasksTemp.createNewFile();
            }
        }
    }
}
