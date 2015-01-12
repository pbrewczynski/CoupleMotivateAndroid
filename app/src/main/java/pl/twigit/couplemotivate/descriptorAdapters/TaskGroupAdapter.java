package pl.twigit.couplemotivate.descriptorAdapters;

import android.content.Context;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import pl.twigit.couplemotivate.R;
import pl.twigit.couplemotivate.descriptors.Task;
import pl.twigit.couplemotivate.descriptors.TaskGroup;


/**
 * Created by pbrewczynski on 11/10/14.
 */
public class TaskGroupAdapter extends ArrayAdapter<TaskGroup> {


    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when
     *                 instantiating views.
     * @param objects  The objects to represent in the ListView.
     */

    public TaskGroupAdapter(Context context, int resource, List<TaskGroup> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TaskGroupViews viewsInRow = null;


        if(convertView != null) {
            viewsInRow = (TaskGroupViews) convertView.getTag();
        } else {
            viewsInRow = new TaskGroupViews();

            LayoutInflater inflater = LayoutInflater.from(this.getContext());
            convertView = inflater.inflate(R.layout.task_group, parent, false);

            viewsInRow.soonestTaskDueDateTextView  = (TextView) convertView.findViewById(R.id.soonestTaskDueDateTextView);
            viewsInRow.titleTextView               = (TextView) convertView.findViewById(R.id.titleTextView);
            viewsInRow.amountOfTasksTextView       = (TextView) convertView.findViewById(R.id.amountOfTasksTextView);

            convertView.setTag(viewsInRow);
        }

        TaskGroup taskGroupDescriptor = this.getItem(position);


        viewsInRow.titleTextView.setText(taskGroupDescriptor.title);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");


        Calendar soonestTaskCalendar = taskGroupDescriptor.getSoonestDueDate();

        String dateString;
        if(soonestTaskCalendar == null) {
            dateString = this.getContext().getResources().getString(R.string.empty);
        } else {
            dateString = dateFormat.format(taskGroupDescriptor.getSoonestDueDate().getTime()).toString();
        }

        viewsInRow.soonestTaskDueDateTextView.setText(dateString);
        viewsInRow.amountOfTasksTextView.setText(String.valueOf(taskGroupDescriptor.getAmountOfTask()));

        //Log.e("TAG", viewsInRow.amountOfTasksTextView);


        return convertView;

        //return super.getView(position, convertView, parent);

    }


    private class TaskGroupViews  {
        public TextView soonestTaskDueDateTextView;
        public TextView titleTextView;
        public TextView amountOfTasksTextView;
    }

}
