package TaskManagerServer.ModelClasses;


import CommonClasses.Task;
import org.apache.log4j.Logger;
import java.util.*;

/**
 * The class for working with write/parse strings and tasks
 */
public class Tasks {

    private static Logger logger = Logger.getLogger(Tasks.class);

    /**
     * The method returns the collection of tasks that happens between {@code start} and {@code finish}
     * @param tasks - iterator on collection of all tasks
     * @param start - start time in interval
     * @param end - end time in interval
     * @return the iterator on collection with all active tasks that happens in those period
     * @see Task#nextTimeAfter(Date)
     * @see Tasks#isNotGreaterThanFinish(Date, Date)
     */
    public static Iterable<Task> incoming(Iterable<Task> tasks, Date start, Date end){
        ArrayList<Task> result=new ArrayList<Task>();
        Iterator<Task> iterOnColl = tasks.iterator();
        while(iterOnColl.hasNext()){
            Task temp = iterOnColl.next();
            Date next = temp.nextTimeAfter(start);
            if(isNotGreaterThanFinish(next,end)){
                result.add(temp);
            }
        }
        return result;
    }

    /**
     * the method assert that {@code current } is not null and before of equal {}@code end
     * @param current - current time
     * @param end - the upper border
     * @return the true if the conditions happen, else - false
     */
    private static boolean isNotGreaterThanFinish(Date current,Date end){
        return current!=null && (end.equals(current) || end.after(current));
    }

    /**
     * The Method returns the map of format : 'date, Set(task)'. It is the schedule of dates and we identify by the date
     * the set of tasks that happend in those time
     * @param tasks - iterator on all task's collection
     * @param start - start time of period
     * @param end - enmd time of period
     * @return the {@code Map<Date,Set<Task>>} instance
     */
    public static SortedMap<Date, Set<Task>> calendar(Iterable<Task> tasks,Date start, Date end){
        SortedMap <Date, Set<Task>> result = new TreeMap<Date,Set<Task>>();
        Iterator<Task> chooseTasks = incoming(tasks,start,end).iterator();
        while(chooseTasks.hasNext()){
            Task tempTask = chooseTasks.next();
            for(Date curTime = tempTask.nextTimeAfter(start);       isNotGreaterThanFinish(curTime,end);     curTime = tempTask.nextTimeAfter(curTime)){
               if(result.containsKey(curTime)){
                   Set<Task> tempOldSet = result.get(curTime);
                   tempOldSet.add(tempTask);
               }
               else{
                   Set<Task> tempNewSet =new HashSet<Task>();
                   tempNewSet.add(tempTask);
                   result.put(curTime,tempNewSet);
               }
            }

        }
        return result;
    }
}
