package TaskManagerServer.ModelClasses;

import java.io.Serializable;
import java.util.Iterator;

import CommonClasses.Task;

/**
 * The abstract class of collection for {@code Task}
 */
public abstract class TaskList implements Iterable<Task>,Cloneable, Serializable{
    /**
     * The size of collection
     */
    protected int size = 0;

    /**
     * The changing counter
     */
    protected int countChanging = 0;

    /**
     * The method returns the task in data position {@code index}.
     * @param index - the position
     * @return task int pos {@code index}
     */
    public abstract Task getTask(int index);

    /**
     * The method adds the {@code ex}
     * @param ex - the {@code Task} instance for adding
     */
    public abstract void add(Task ex);

    /**
     * The method remove the element that equal to {@code ex}.
     * @param ex - the task that should be removing
     */
    public abstract void remove(Task ex);

    /**
     * The method return the size of collection
     * @return the int number from 0 to max int
     */
    public int size(){
        return size;
    }

    /**
     * Abstract iterator class for abstract collection
     */
    protected abstract class TaskListIterator implements Iterator<Task> {

        /**
         * The local change counter
         */
        protected int localCountChanging = countChanging;

        /**
         * The index of the next elem
         */
        protected  int nextIndex=0;

        /**
         * Looking up the desynchronization. If counters not equal return false. Else - true
         * @return true or false
         */
        protected boolean assertChangingCount(){
            return localCountChanging != countChanging;
        }

        /**
         * The method returns the iterator to begin of collection and synchronize changing counters
         */
        protected abstract void setItreratorToBegin();

        /**
         * The method assert is there the desynchronization. If it is occurs - return iterator to begin of collection
         * @see TaskListIterator#assertChangingCount()
         * @see TaskListIterator#setItreratorToBegin()
         */
        protected void secureOfChangigng(){
            if(assertChangingCount()){
                setItreratorToBegin();
            }
        }

        /**
         * The method show is there the next element in collection
         * @return true if the next element exists, else - false
         */
        @Override
        public boolean hasNext(){
            return nextIndex<size;
        }

        /**
         * The method returns the next task.
         * @return the element in {@code nextIndex}
         */
        @Override
        public abstract Task next();

        /**
         * The method remove the elem that is current on in default
         */
        @Override
        public abstract void remove();
    }
    /**
     * The method returns the special iterator for {@code TaskList}
     * @return the {@code TaskListIterator} instance
     */
    @Override
    public abstract TaskListIterator iterator();

    @Override
    public String toString() {
        Iterator<Task> tempItr= this.iterator();
        String result="";
        while(tempItr.hasNext()){
            result += tempItr.next().toString()+"\n";
        }
        return result;
    }

    /**
     * The method assert equaling of two collections first by classes, than by size, than by elements. If one condition
     * do not occurs - the method returns false. Else - true;
     * @param o - the collection object
     * @return true if all conditions execute , else - false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TaskList)) return false;

        TaskList tasks = (TaskList) o;

        if(tasks.size!=size){return false;}

        Iterator<Task> iterThose = tasks.iterator();
        while(iterThose.hasNext()){
            Task temp = iterThose.next();
            if(!this.contains(temp)){
                return false;
            }
        }
        return true;
    }

    /**
     * The method get the hashCode of the collection
     * @return the int hashCode value
     */
    @Override
    public int hashCode() {
        int result  = 0;
        Iterator<Task> itr = this.iterator();
        while(itr.hasNext()){
            Task buffer = itr.next();
            result+=buffer==null?0:buffer.hashCode();
        }
        return result;
    }

    /**
     * The method assert is there in the collection element that equals to {@code e}
     * @param e - the asserting element
     * @return true if element equals at last one of the element, else - true
     */
    private boolean contains(Task e){
        Iterator<Task> itr = this.iterator();
        while(itr.hasNext()){
            if(e.equals(itr.next())){
                return true;
            }
        }
        return false;
    }


}
