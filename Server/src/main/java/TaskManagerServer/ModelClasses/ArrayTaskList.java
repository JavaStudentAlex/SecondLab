package TaskManagerServer.ModelClasses;

import Exceptions.*;
import CommonClasses.Task;
import org.apache.log4j.Logger;

import java.util.Arrays;

/**
 * The collection for {@code Task} instances
 */
public class ArrayTaskList extends TaskList {

    /**
     * The logger of the class
     */
    private static Logger logger = Logger.getLogger(ArrayTaskList.class);

    /**
     * The array where the tasks are saved
     */
    private Task[] data;

    /**
     * The start length of the {@code data}
     */
    private static final int startCapacity = 12;

    /**
     * The constructor of the class, create a {@code data} instance with {@code startCapacity} length
     */
    public ArrayTaskList() {
        data = new Task[startCapacity];
    }

    /**
     * The constructor of the class, create a {@code data} instance with {@code capasity} length
     * @param capasity - the capacity indicated by user
     */
    public ArrayTaskList(int capasity){
        if(capasity>0){
            data = new Task[capasity];
        }
    }

    /**
     * The method asserts is {@code localSize} bigger than current data length. If yes we recreate a new data with new
     * {@code localSize*3)/2+1} length
     * @param localSize - the new sixe
     * @see Arrays#copyOf(Object[], int)
     */
    private void secureCapacity(int localSize){
        if(localSize>data.length){
            data = Arrays.copyOf(data,(localSize*3)/2+1);
        }
    }

    /**
     * The method assert ex is not null. If yes it goes on but if no - return. If the {@code ex} is not null the method
     * assert size of {@code data}. If it is not enough - solve this problem and than add the {@code ex}
     * @param ex - the {@code Task} instance for adding
     */
    @Override
    public void add(Task ex){
        if(ex==null){
            logger.warn("Null pointer to the Object");
            throw new NullPointerException("Null pointer to the Object");
        }
        secureCapacity(size+1);
        data[size++] = ex;
    }

    /**
     * The method shifts the elements in data form index to (size-1) on one position down
     * @param index - the start shifting position
     * @see System#arraycopy(Object, int, Object, int, int)
     */
    protected void replaceElements(int index){
        int numberElems = size-index-1;
        if(numberElems>0){
            System.arraycopy(data,index+1,data,index,numberElems);
        }
        data[--size] = null;
    }
    /**
     * The method remove the element that equal to {@code ex}. If ex is null and in some cell of data there is the null
     * value - so the result is successful. if no - nothing occurs
     * @param ex - the task that should be removing
     * @see ArrayTaskList#replaceElements(int)
     */
    @Override
    public void remove( Task ex){
        if (ex != null) {
            for(int i = 0;i<size;++i){
                if(data[i].equals(ex)){
                    replaceElements(i);
                }
            }
        }
        else{
            for(int i = 0;i<size;++i){
                if(data[i]==null){
                    replaceElements(i);
                }
            }
        }
    }

    /**
     * The method removes the elem by the index {@code index} param if it is not less than 0 and not greater than size.
     * If no - throw IndexOutOfBoundsException
     * @param index - the index of element
     */
    public void remove(int index){
        if(index<0||index>=size){
            logger.warn("Try to remove Task in nonexistent index : " + index);
            throw new IndexOutOfBoundsException("Try to remove Task in nonexistent index : " + index);
        }
        remove(data[index]);
    }

    /**
     * The method asserts that param {@code index} is not less than 0 and not greater than size. If no - throws
     * IndexOutOfBoundsException
     * @param index - the asserting index
     */
    private void assertRange(int index){
        if(index<0 || index>=size ){
            logger.warn("Index : " + index + " Size : " + size);
            throw new IndexOutOfBoundsException("Index : " + index + " Size : " + size);
        }
    }

    /**
     * The method returns the task in data position {@code index}. If the position is not between 0 and size the method
     * throws IndexOutOfBoundsException
     * @param index - the position
     * @return task int pos {@code index}
     */
    @Override
    public  Task getTask(int index){
        assertRange(index);
        return data[index];
    }


    /**
     * The method returns the special {@code iterator} for {@code ArrayTaskList}
     * @return the {@code ArrayTaskListIterator} instance
     */
    @Override
    public ArrayTaskListIterator iterator() {
        return new ArrayTaskListIterator();
    }

    /**
     * The class realize the iterator for {@code ArrayTaskList}
     */
    private class ArrayTaskListIterator extends TaskListIterator{
        /**
         * Index of last returned element
         */
        private int lastReturnedIndex=-1;

        /**
         * The method returns the next task. If the iterator has no next index the method throws NoSuchTaskException. If
         * the desynchronization occurs we place the iterator to begin.
         * @return the element in {@code nextIndex}
         * @see ArrayTaskListIterator#secureOfChangigng()
         */
        @Override
        public Task next() {
            secureOfChangigng();
            int tempIndex = nextIndex;
            if(!hasNext()){
                logger.warn("You try to go to nonexistent Task!");
                throw new NoSuchTaskException("You try to go to nonexistent Task!");
            }
            ++nextIndex;
            lastReturnedIndex=tempIndex;
            return data[lastReturnedIndex];
        }

        /**
         * The method remove the elem that in {@code lastReturnedIndex} position. If the desynchronization occurs
         * we place the iterator to begin. If we haven't move yet the iterator and {@code lastReturnedIndex} less than
         * 0 the method throws NoSuchTaskException. If removing is successful we increase the changing counter
         * {@oode countChanging} and his local version
         */
        @Override
        public void remove() {
            secureOfChangigng();
            if(lastReturnedIndex<0){
                logger.warn("You try to remove nonexistent Task!");
                throw new NoSuchTaskException("You try to remove nonexistent Task!");
            }
            ArrayTaskList.this.remove(lastReturnedIndex);
            nextIndex = lastReturnedIndex;
            lastReturnedIndex = -1;
            localCountChanging = ++countChanging;
        }

        /**
         * The method returns the iterator to begin of collection and synchronize changing counters
         */
        @Override
        protected void setItreratorToBegin() {
            nextIndex = 0;
            lastReturnedIndex = -1;
            localCountChanging = countChanging;
        }
    }

    /**
     * The method returns the cloned {@code ArrayTaskList}.
     * @return {@code ArrayTaskList} instance
     * @see Arrays#copyOf(Object[], int)
     */
    @Override
    public ArrayTaskList clone(){
        try{
            ArrayTaskList result = (ArrayTaskList) super.clone();
            result.data = Arrays.copyOf(data,size);
            countChanging = 0;
            return result;
        }catch (CloneNotSupportedException e){
            logger.error("Error in cloning ArrayTaskList");
            throw new RuntimeException("Error in cloning ArrayTaskList");
        }
    }

}
