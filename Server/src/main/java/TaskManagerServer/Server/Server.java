package TaskManagerServer.Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import TaskManagerServer.Server.XMLParsers.UsersXMLParser;
import org.apache.log4j.Logger;

/**
 * The class that represent the server work, save the user info, it`s directories , connection threads.
 */
public class Server {

    /**
     * The logger of the class.
     */
    private static Logger logger = Logger.getLogger(Server.class);

    /**
     * The main dir.
     */
    public static final String MAIN_DIR = "Server";

    /**
     * The dir for the users info.
     */
    public static final String USER_DIR = "Users";

    /**
     * The name of the tasks xml file in every user directory.
     */
    public static final String USER_FILE = "Tasks.xml";

    /**
     * The name of the users info file.
     */
    public static final String ALL_USERS_FILE = "Users.xml";

    /**
     * The number of active threads in the pool.
     */
    public static final int STATISTIC_CONNECTIONS=10;

    /**
     * The server socket that listen the specific port and gets the requests.
     */
    private ServerSocket listener;

    /**
     * The pool of the  not active connection threads.
     */
    private Queue<ConnectionHandler> pool;

    /**
     * The connection threads that already in use.
     */
    private List<ConnectionHandler> inUse;

    /**
     * The list of users with their passwords.
     */
    private Map<String, String> users;

    /**
     * The list of the active users.
     */
    private List<String> activeUsers;

    /**
     * The parser of the user`s info from the xml file.
     */
    private UsersXMLParser usersParser;

    /**
     * The constructor start the server. The server create create lists for pool and used connection threads and
     * also list of active users. Than assert directories, get the users info, assert personal directories and if there
     * are no - create. Than create the server socket, set the port and set it to listening. By the cycle, the server
     * gets the requests and push it to nearest connection thread and start it`s work. If the threads in tho pool are
     * less than 2 the pool init one more time.
     */
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

    /**
     * The method for init pool of the default number of the threads.
     */
    private void initPool(){
        for (int i=0;i<STATISTIC_CONNECTIONS;++i){
            pool.add(new ConnectionHandler(this));
        }
    }

    /**
     * The method indicate is the user with the name like string in argument logged into the system.
     * @param user the string name of user.
     * @return the true if it is logged and false if not.
     */
    public synchronized boolean isLogged(String user){
        return activeUsers.contains(user);
    }

    /**
     * The method add the name of the logged user to logged users list.
     * @param user the string name of user.
     */
    public synchronized void addActiveUser(String user){
        activeUsers.add(user);
    }

    /**
     * The method remove the user with the name like string in arguments from the logged users list.
     * @param user the string name of user.
     */
    public synchronized void removeActiveUser(String user){
        activeUsers.remove(user);
    }

    /**
     * The method remove the connection thread from the lists of threads that in use.
     * @param thread the thread object.
     */
    public synchronized void removeThread(ConnectionHandler thread){
        inUse.remove(thread);
    }

    /**
     * The method assert is there the user with the same name. If the identifier of authorization is true, the method
     * assert name and password, if not - only the name.
     * @param user the user name.
     * @param password the password.
     * @param authorization the identifier is this the authorization.
     * @return the true if all is OK and false if not.
     */
    public synchronized boolean containsUser(String user, String password, boolean authorization){
        if(!users.containsKey(user)){
            return false;
        }
        if(users.containsKey(user) && !authorization){
            return true;
        }
        return users.get(user).equals(password);
    }

    /**
     * The method write the user to the list of all users and also write it to the xml file with users info.
     * @param user the user name.
     * @param password the password.
     * @throws IOException the error object is thrown when the problems with writing appear.
     */
    public synchronized void writeUser(String user, String password) throws IOException{
        users.put(user,password);
        usersParser.writeUsers(users);
    }

    /**
     * The method calls the parsing of users from the xml file and write it to the all users list.
     * @throws IOException the error object is thrown when the problems with reading appear.
     */
    private void initUsers() throws IOException{
        usersParser = new UsersXMLParser(new File(MAIN_DIR+File.separator+
                                            USER_DIR+File.separator+ALL_USERS_FILE));
        users = usersParser.getAllUsers();
    }

    /**
     * The method assert are the main dirs of the server here. If not - create it.
     * @throws IOException the error object is thrown when the problems with reading/writing appear.
     */
    private void assertMainDirectories() throws IOException{
        File file = new File(MAIN_DIR+File.separator+USER_DIR);
        if(!file.exists()){
            file.mkdirs();
        }
    }

    /**
     * The method assert are the user`s dirs of the server here. If not - create it.
     * @throws IOException the error object is thrown when the problems with reading/writing appear.
     */
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
