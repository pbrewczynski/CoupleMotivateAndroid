package pl.twigit.couplemotivate.descriptors;

import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by pbrewczynski on 11/10/14.
 */
public class TaskGroup implements Serializable {

    public String title;
    public ArrayList<Task> taskList;
    public int priority;


    public TaskGroup(String title) {
        this.title = title;
        this.taskList = new ArrayList<Task>();
    }

    public TaskGroup(String title, ArrayList<Task> taskList, int priority) {
        this.title = title;
        this.taskList = taskList;
        this.priority = priority;
    }

    public Calendar getSoonestDueDate() {

        Calendar soonest = null;
        if(this.taskList.size() > 0 ) {
            soonest = this.taskList.get(0).dueDate;
            for(Task iteratedTask : this.taskList) {
                if(soonest.compareTo(iteratedTask.dueDate) > 0) {
                    soonest = iteratedTask.dueDate;
                }
            }
        }

        return soonest;

    }

    public int getAmountOfTask () {
        if(this.taskList == null) {
            return 0;
        } else {
            return this.taskList.size();  // Complexity O(1)
        }
        //return this.taskList.size();
    }
}
