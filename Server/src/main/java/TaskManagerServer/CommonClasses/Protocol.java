package TaskManagerServer.CommonClasses;

/**
 * The class that keep the basic constant commands of messaging by socket
 */
public class Protocol {

    /**
     * The var for log out the user
     */
    public static final String LOGOUT = "LOGOUT";

    /**
     * The var for disconnect
     */
    public static final String DISCONNECT="DISCONNECT";

    /**
     * The var for deleting task
     */
    public static final String REMOVE="DELETE";

    /**
     * The var for changing task
     */
    public static final String CHANGE="CHANGE";

    /**
     * The var for adding task
     */
    public static final String ADD="ADD";

    /**
     * The var for register the user
     */
    public static final String REGISTER="REGISTER";

    /**
     * The var for log in the user
     */
    public static final String LOGIN="LOGIN";
}
