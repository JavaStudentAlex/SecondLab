package TaskManagerClientPart;

import java.io.Serializable;
import java.util.Date;

/**
 * The class that keep info about the task
 */
public class Task implements Cloneable, Comparable<Task>, Serializable{
    /**
     * The name
     */
    private String title;
    /**
     * Is the task active
     */
    private boolean active = false;
    /**
     * The time controller instance
     */
    private TimeControll controller;

    /**
     * The class that controll the time part of task
     */
    private class TimeControll {
        /**
         * var shows is the task repeated
         */
        boolean repeat = false;

        /**
         * var of start task time
         */
        Date startTime = null;

        /**
         * Var not null if the task repeated
         */
        Date endTime = null;

        /**
         * Var not equal 0 if the task repeated
         */
        long interval = 0;

        /**
         * Constructor for single task
         * @param time - start task's time
         */
        TimeControll(Date time){
            endTime = startTime = (Date)time.clone();
        }

        /**
         * Constructor for multiple task
         * @param start - start time
         * @param finish - end time
         * @param interval - repeated interval
         */
        TimeControll(Date start, Date finish, int interval){
            repeat = true;
            startTime = (Date)start.clone();
            endTime = (Date)finish.clone();
            this.interval = interval*1000;
        }

        /**
         * Constructor of copies
         * @param temp - copy of {@code TimeControll}
         */
        TimeControll(TimeControll temp){
            repeat = temp.repeat;
            interval = temp.interval;
            startTime = (Date)temp.startTime.clone();
            endTime = (Date)temp.endTime.clone();
        }

        /**
         * Method assert equaling {@code o} to current object
         * @param o - the check object
         * @return true if equal, if one of all conditions does not occur - false
         */
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof TimeControll)) return false;
            TimeControll that = (TimeControll) o;
            if (repeat != that.repeat) return false;
            if(isRepeated()){
                if(!startTime.equals(that.startTime)){return false;}
                if(!endTime.equals(that.endTime)){return false;}
                if(interval!=that.interval){return  false;}
            }
            else{
                if(!startTime.equals(that.startTime)){return false;}
            }
            return true;
        }

        /**
         * The method get the hash code of the objct
         * @return int hash code
         */
        @Override
        public int hashCode() {
            int result = (repeat ? 1 : 0);
            result = 31 * result + startTime.hashCode();
            result = 31 * result + endTime.hashCode();
            result = 31 * result + (int) (interval ^ (interval >>> 32));
            return result;
        }

        /**
         * The method transform task to string
         * @return task formatted to string
         */
        @Override
        public String toString() {
            String result ="  repeated : " + repeat +  (repeat ? "  start : " + startTime.toString() + "  end : " +
                    endTime.toString() + "  interval : " + interval +" ms":"   time : " + startTime.toString());
            return result;
        }
    }


    /**
     * Constructor for any task
     * @param title - the name
     * @param active - is active
     * @param controller - control time object
     */
    private Task(String title, boolean active, TimeControll controller ){
        this.title = title;
        this.active = active;
        this.controller = new TimeControll(controller);
    }

    /**
     * Constructor for single task
     * @param title - name
     * @param time - start time
     */
    public Task(String title, Date time){
        this.title = title;
        controller = new TimeControll(time);
    }

    /**
     * COnstructor for multiple task
     * @param title - name
     * @param start -start time
     * @param end - end time
     * @param interval - interval
     */
    public Task(String title, Date start, Date end, int interval){
        this.title = title;
        controller = new TimeControll(start,end,interval);
    }

    /**
     * Set for {@code title}
     * @param title - name
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Get {@code title} field
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * The method get the boolean meaning is task active
     * @return true if active task, else - false
     */
    public boolean isActive() {
        return active;
    }

    /**
     * The method set the variable {@code active} meaning {@code act}
     * @param act - the setting mining
     */
    public void setActive(boolean act) {
        active = act;
    }

    /**
     * Get start time in single task
     * @return
     */
    public Date getTime() {

        return (Date) controller.startTime.clone();
    }

    /**
     * The method set start time in single task
     * @param time - the setting time
     */
    public void setTime(Date time){
        controller=new TimeControll(time);
    }

    /**
     * The method set start, end and interval in multiple task
     * @param start - value for startTime field
     * @param end - value for endTime field
     * @param interval - value for interval field
     */
    public void setTime(Date start, Date end, int interval){
        controller = new TimeControll(start,end,interval);
    }

    /**
     * The method return for single - time, for multiple - startTime
     * @return Date structure from start time or time
     */
    public Date getStartTime() {
        return getTime();
    }

    /**
     * The method return for single - time, for multiple - endTime
     * @return Date structure from end time or time
     */
    public Date getEndTime() {
        return (Date)controller.endTime.clone();
    }

    /**
     * Method returns millis of the date
     * @param date - the control date
     * @return the long number of millis
     */
    private long getDateTime(Date date){
        return date.getTime();
    }

    /**
     * Repeat repeat interval in seconds if multiple, else(single) - 0
     * @return
     */
    public int getRepeatInterval() {
        return (int)controller.interval/1000;
    }

    /**
     * The method assert is task repeated
     * @return true if is repeated, else - false
     */
    public boolean isRepeated() {
        return controller.repeat;
    }

    /**
     * Return next time task happening for multiple  but without upper board
     * @param curTime - point of control time
     * @return the {@code Date structure}
     */
    private Date nextTimeCalc(Date curTime){
        long start = getDateTime(controller.startTime);
        long interval = controller.interval;
        long current = getDateTime(curTime);
        return new Date(start + ((current-start)/interval+1)*interval);
    }

    /**
     * The method calc the time of next task's happening
     * @param current - point of control time
     * @return date if the time exists in relevant border or null is there is any so time
     */
    public Date nextTimeAfter(Date current) {
        if(isActive()){
            Date result = controller.startTime;
            if(current.before(result)){
                return result;
            }
            else if(isRepeated()){
                result = nextTimeCalc(current);
            }
            if(result.after(current) && (result.before(controller.endTime) || result.equals(controller.endTime))){
                return result;
            }
        }
        return null;
    }

    /**
     * The method assert the object {@code o} on equaling.
     * @param o - the control object
     * @return true if all conditions are followed and false if one of them fails
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;

        Task task = (Task) o;

        if (active != task.active) return false;
        if (!title.equals(task.title)) return false;
        return controller.equals(task.controller);
    }

    /**
     * The method get the int hash code for task
     * @return int hash code number
     */
    @Override
    public int hashCode() {
        int result = title.hashCode();
        result = 31 * result + (active ? 1 : 0);
        result = 31 * result + controller.hashCode();
        return result;
    }

    /**
     * The method clone the current task and return it
     * @return the new {@code Task} instance with old attributes
     */
    @Override
    public Task clone(){
        try {
            Task result = (Task) super.clone();
            if(!isRepeated()){
                result.controller = new TimeControll(controller.startTime);
            }
            else{
                result.controller = new TimeControll(controller.startTime,controller.endTime,(int)controller.interval/1000);
            }
            result.controller.repeat = controller.repeat;
            return result;
        }
        catch (CloneNotSupportedException e){
            throw new RuntimeException("Error in cloning Task");
        }
    }

    /**
     * The method transform the current task to string and return it
     * @return the task formatted to string
     */
    @Override
    public String toString() {
        String result = "name : " + title + "  active : " + active + controller.toString();
        return result;
    }

    /**
     * The method try to compare current task with {@code temp}
     * @param temp- the compared task
     * @return int  number, 0 if equals tasks, less than 0 if current after {@code temp}, else bigger than 0
     */
    @Override
    public int compareTo(Task temp) {
        if(this.equals(temp)){
            return 0;
        }
        return controller.startTime.compareTo(temp.controller.startTime);
    }
}
