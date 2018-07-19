package TaskManagerServer.ModelClasses;

import org.apache.log4j.Logger;
import CommonClasses.Task;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Date;

/**
 * The class is used to make IO operations with tasks
 */
public class TaskIO {

    /**
     * The logger of the class
     */
    private static Logger logger = Logger.getLogger(TaskIO.class);

    /**
     * The method write task {@code task} to stream {@code out} in so order : length of bytes array of name, bytes array
     * of name, boolean var is task active, repeat interval(int) in seconds, if task is repeated : start time(long), end
     * time(long), else - time(long)
     * @param out - the output stream
     * @param task - source info for writing
     */
    public static void writeTask(DataOutputStream out, Task task){
        try {
            out.writeInt(task.getTitle().getBytes().length);
            out.write(task.getTitle().getBytes());
            out.writeBoolean(task.isActive());
            out.writeInt(task.getRepeatInterval());
            if(task.isRepeated()){
                out.writeLong(task.getStartTime().getTime());
                out.writeLong(task.getEndTime().getTime());
            }
            else{
                out.writeLong(task.getTime().getTime());
            }
        } catch (IOException e) {
            logger.warn("Can not be TaskIO");
        }
    }

    /**
     * The method read from {@code in} and return the task. The info is read by order : length of bytes array of name,
     * bytes array of name, boolean var is task active, repeat interval(int) in seconds, if task interval>0 :
     * start time(long), end time(long), else - time(long)
     * @param in - input stream
     * @return the {@code Task} instance
     */
    public static Task readTask(DataInputStream in){
        Task result = null;
        try {
            int size = in.readInt();
            byte[] array = new byte[size];
            in.read(array);
            String title = new String(array);
            boolean isActive = in.readBoolean();
            int interval = in.readInt();
            if(interval>0){
                long startTime = in.readLong();
                long endTime = in.readLong();
                result = new Task(title,new Date(startTime),new Date(endTime),interval);
            }
            else{
                long time = in.readLong();
                result = new Task(title,new Date(time));
            }
            result.setActive(isActive);
        } catch (IOException e) {
            logger.warn("Can not be TaskIO");
        }
        return result;
    }
}
