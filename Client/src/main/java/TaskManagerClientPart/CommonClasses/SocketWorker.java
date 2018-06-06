package TaskManagerClientPart.CommonClasses;

import java.io.*;
import java.net.Socket;

/**
 * The class that is used by sockets of server/client
 */
public class SocketWorker {

    /**
     * The method return the writer for socket in argument
     * @param socket - the source socket
     * @return the DataOutputStream object
     * @throws IOException if there is no connection on socket
     */
    public static DataOutputStream getWriter(Socket socket) throws IOException {
        return new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
    }

    /**
     * The method retrun the reader for the socket in argument
     * @param socket - the source socket
     * @return the DataInputStream object
     * @throws IOException if there is no connection on socket
     */
    public static DataInputStream getReader(Socket socket) throws IOException{
        return new DataInputStream(new BufferedInputStream(socket.getInputStream()));
    }

    /**
     * The method write the boolean tue and the text of the error on the server(answer for user activity)
     * @param writer - the writer for writing messages
     * @param message - the text of the error
     * @throws IOException if the connection end
     */
    public static void writeTheErrorByWriter(DataOutputStream writer, String message) throws IOException{
        writer.writeBoolean(false);
        writer.writeUTF(message);
        writer.flush();
    }
}
