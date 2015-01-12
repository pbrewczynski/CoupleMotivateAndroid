package pl.twigit.couplemotivate.descriptorAdapters;

        import android.content.Context;
        import android.text.Layout;
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
        import pl.twigit.couplemotivate.utils.utils;


/**
 * Created by pbrewczynski on 11/10/14.
 */
public class TaskAdapter extends ArrayAdapter<Task> {


    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when
     *                 instantiating views.
     * @param objects  The objects to represent in the ListView.
     */

    public TaskAdapter(Context context, int resource, List<Task> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TaskViews viewsInRow = null;


        if(convertView != null) {
            viewsInRow = (TaskViews) convertView.getTag();
        } else {
            viewsInRow = new TaskViews();

            LayoutInflater inflater = LayoutInflater.from(this.getContext());
            convertView = inflater.inflate(R.layout.task, parent, false);

            viewsInRow.taskTimerTextView    = (TextView) convertView.findViewById(R.id.taskTimerTextView);
            viewsInRow.titleTextView        = (TextView) convertView.findViewById(R.id.titleTextView);
            viewsInRow.descriptionTextView  = (TextView) convertView.findViewById(R.id.descriptionTextView);

            convertView.setTag(viewsInRow);
        }

        Task taskDescriptor = this.getItem(position);
        viewsInRow.titleTextView.setText(taskDescriptor.title);

        //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        //String dateString = dateFormat.format(taskDescriptor.dueDate.getTime()).toString();

        String dueTimeString = utils.representationOfInterval(taskDescriptor.dueDate, Calendar.getInstance());
        viewsInRow.taskTimerTextView.setText(dueTimeString);
        //viewsInRow.taskTimerTextView.setText(dateString);

        viewsInRow.descriptionTextView.setText(taskDescriptor.description);

        return convertView;

        //return super.getView(position, convertView, parent);

    }

    public class TaskViews  {
        public TextView taskTimerTextView;
        public TextView titleTextView;
        public TextView descriptionTextView;
    }

}
