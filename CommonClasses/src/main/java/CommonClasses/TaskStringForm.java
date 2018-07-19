package CommonClasses;


import Exceptions.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TaskStringForm {
    private String title;
    private boolean active;
    private boolean repeat;
    private String startDate;
    private String endDate;
    private String interval;
    public static final String FORMAT_TIME = "yyyy-mm-dd hh:mm:ss.msmsms",
            FORMAT_INTERVAL = "0 days 0 hours 0 minutes 0 seconds";

    public TaskStringForm(){
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isRepeat() {
        return repeat;
    }

    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    @Override
    public String toString() {
        return title + " " +
                active + " " +
                repeat + " " +
                (!repeat?startDate:startDate+" " + endDate + " " + interval);
    }

    public Task parseTask() throws MyOwnException{
        Task result = null;
        if(!repeat){
            try {
                Date date = parseDate(startDate);
                result=new Task(title,date);
                result.setActive(active);
                return result;
            } catch (Exception e) {
                throw new TimeInvalidException();
            }
        }

        Date start;
        try {
            start = parseDate(startDate);
        } catch (Exception e) {
            throw new StartTimeInvalidException();
        }

        Date end;
        try {
            end = parseDate(endDate);
        } catch (Exception e) {
            throw new EndTimeInvalidException();
        }
        if(start.after(end)){
            throw new EndTimeInvalidException();
        }

        int interv;
        try {
            interv = getInterval(interval);
        } catch (Exception e) {
            throw new IntervalInvalidException();
        }
        result = new Task(title,start,end,interv);
        result.setActive(active);
        return result;
    }

    /**
     * The method parse date from the source string {@code source}
     * @param source - the string with {@code Date} info
     * @return the Date structure
     * @throws ParseDateException - if the source has invalid format
     */
    private Date parseDate(String source) throws ParseDateException{
        String tempStr = source;
        int year = parseInt(tempStr.substring(0,4));
        int mounth = parseInt(tempStr.substring(5,7))-1;
        if(mounth<0 || mounth>12){throw new ParseDateException();}
        int day = parseInt(tempStr.substring(8,10));
        if(day>31 || day <0){throw new ParseDateException();}
        int hours = parseInt(tempStr.substring(11,13));
        if(hours<0 || hours >=24){throw new ParseDateException();}
        int minutes = parseInt(tempStr.substring(14,16));
        if(minutes<0 || minutes>=60){throw new ParseDateException();}
        int seconds = parseInt(tempStr.substring(17,19));
        if(seconds<0 || seconds>=60){throw new ParseDateException();}
        int miliseconds = parseInt(tempStr.substring(20,23));
        if(miliseconds<0 || miliseconds >=1000){throw new ParseDateException();}
        GregorianCalendar date = new GregorianCalendar(year,mounth,day,hours,minutes,seconds);
        Date current = new Date(date.getTimeInMillis()+miliseconds);
        return current;
    }

    /**
     * The method returns the int valus of the string {@code znach}
     * @param znach - the source string with int value into
     * @return the itn value
     */
    private int parseInt(String znach){
        return Integer.parseInt(znach);
    }
    /**
     * The method parse interval from the string {@code source}
     * @param source - the source string with interval info into
     * @return the int value of task's interval
     */
    private int getInterval(String source){
        String temp = source;
        String[] measures = new String[] {"day","hour","minute","second"};
        int[] measuresValue = new int[4];
        for(int i=0;i<measures.length;++i){
            measuresValue[i]=getInteger(measures[i],temp);
            temp=getStringAfter(measures[i],"s",temp);
        }
        return measuresValue[0]*24*60*60+measuresValue[1]*60*60+measuresValue[2]*60+measuresValue[3];
    }

    /**
     * The method returns the int number before the string {@code word}
     * @param word - the id string
     * @param source - the string with word and id value
     * @return the int value from {@code source}
     */
    private int getInteger(String word, String source){
        if(!source.contains(word)){
            return 0;
        }
        String number = source.substring(0,source.indexOf(word)-1);
        if(number.charAt(0)==' '){
            number=number.substring(1,number.length());
        }
        if(number.charAt(number.length()-1)==' '){
            number=number.substring(0,number.length()-1);
        }
        return Integer.parseInt(number);
    }

    /**
     * The method returns the substring of {@code source} after string {@code word}
     * @param word - the id string
     * @param adds - the suffix can be after the word
     * @param source - the source string that should be divided
     * @return the substring of source after {@code work} + may be {@code adds}
     */
    private String getStringAfter(String word, String adds, String source){
        if(!source.contains(word)){
            return source;
        }
        int index = source.indexOf(word)+word.length();
        if(source.contains(word+adds)){
            index+= adds.length();
        }
        if(source.length()!=index){
            ++index;
        }
        return source.substring(index,source.length());
    }

    /**
     * the method transform the number to the number with {@code schemeLength} count of symbols
     * @param number - string with number
     * @param shemeLength - the number of symbols
     * @return the time formatted to string
     */
    private static String formDate(String number, int shemeLength){
        StringBuilder temp = new StringBuilder(number);
        if(temp.length()<shemeLength){
            while(temp.length()<shemeLength){
                temp.insert(0,"0");
            }
        }
        return temp.toString();
    }

    /**
     * The method that form interval to string value and return it in format :
     * [0 day(s) 0 hour(s) 0 minute(s) 0 second(s)]
     * @param intervalArg - the number of interval's seconds count
     * @return the interval formatted to string
     */
    private static String formInterval(int intervalArg){
        String result="";
        ArrayList<Integer> capacity = new ArrayList<Integer>();
        capacity.add(60*60*24);
        ArrayList<Integer> interval = new ArrayList<Integer>();
        interval.add(intervalArg);
        String[] nameValue = new String[]{"day","hour","minute","second"};
        int[] res = new int[4];
        res[0] = getDown(interval,capacity,24);
        res[1] = getDown(interval,capacity,60);
        res[2] = getDown(interval,capacity,60);
        res[3] = interval.get(interval.size()-1).intValue();
        for(int i=0;i<res.length;++i){
            String nameVls = nameValue[i];
            if(res[i]==0){
                continue;
            }
            if(res[i]>1){
                nameVls+="s";
            }
            String separator="";
            if(i!=3){
                separator=" ";
            }
            result+=res[i]+" "+nameVls+separator;
        }
        return result;
    }


    /**
     * The method transform the {@code Date} structure to string in form {@code [yyyy-mm-dd hh:mm:ss.msmsms]}
     * @param dateTime - the control time
     * @return the time formatted to string
     */
    private static String formDateForWriting(Date dateTime){
        GregorianCalendar date = new GregorianCalendar();
        date.setTime(dateTime);
        int year = date.get(Calendar.YEAR), month = date.get(Calendar.MONTH)+1,day = date.get(Calendar.DAY_OF_MONTH), hours = date.get(Calendar.HOUR_OF_DAY), minutes = date.get(Calendar.MINUTE), seconds = date.get(Calendar.SECOND);
        StringBuilder result=new StringBuilder(formDate(""+year,4)+
                ":"+formDate(""+month,2)+
                ":"+formDate(""+day,2)+
                " "+formDate(""+hours,2)+
                ":"+formDate(""+minutes,2)+
                ":"+formDate(""+seconds,2)+
                "."+formDate(""+date.get(Calendar.MILLISECOND),3));
        return result.toString();
    }


    /**
     * The single descent with noting result of div, mode operations and the {@code downArg}
     * @param interval - array of residues of seconds
     * @param capacity - the capacity of each down measure level
     * @param downArg - the divider
     * @return the result of div operation
     */
    private static int getDown(ArrayList<Integer> interval,ArrayList<Integer> capacity, int downArg){
        int elemArg = interval.get(interval.size()-1).intValue(),elemCap =  capacity.get(capacity.size()-1).intValue();
        int temp = elemArg/elemCap;
        if(temp>0){
            interval.add(elemArg%elemCap);
        }
        capacity.add(elemCap/downArg);
        return temp;
    }

    public void setStartDate(Date date){
        startDate = formDateForWriting(date);
    }
    public void setEndDate(Date date){
        endDate=formDateForWriting(date);
    }

    public void setInterval(int intervalInt){
        interval=formInterval(intervalInt);
    }
}
