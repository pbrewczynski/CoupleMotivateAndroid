package pl.twigit.couplemotivate.descriptors;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by pbrewczynski on 11/10/14.
 */
public class Task implements Serializable {
    public String   title;
    public String   description;
    public Calendar dueDate;
    public int      priority;


    public Task(String title, String description, Calendar dueDate, int priority) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.priority = priority;
    }

    public Task() {

    }

}

