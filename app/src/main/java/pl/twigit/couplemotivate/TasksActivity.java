package pl.twigit.couplemotivate;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ListIterator;

import pl.twigit.couplemotivate.descriptorAdapters.TaskAdapter;
import pl.twigit.couplemotivate.descriptors.Task;
import pl.twigit.couplemotivate.descriptors.TaskGroup;
import pl.twigit.couplemotivate.utils.utils;

public class TasksActivity extends Activity {

    //  Model
    private TaskGroup   taskGroup;


    // Views
    private TextView    soonestTimerTextView;
    private TextView    titleTextView;
    private ListView    taskListView;

    // Model
    private TaskAdapter adapter;

    // Builded task, this is available from Time/Date Picker as well as inputs
    private Task        buildedTaskInFooter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        this.taskGroup = ((TaskGroup) this.getIntent().getSerializableExtra("TAG"));

        this.identifyViews();
        this.setUpTitle();
        this.updateSoonestTimer();
        this.setAdapterForListview();
        this.createNewBuildedTaskForFooter();
        this.addFooterToTaskGroupListView();
        this.scheduleUpdatesOfListView();
    }

    private void identifyViews () {
        this.titleTextView          = (TextView) this.findViewById(R.id.titleTextView);
        this.soonestTimerTextView   = (TextView) this.findViewById(R.id.soonestTimerTextView);
        this.taskListView           = (ListView) this.findViewById(R.id.taskListView);
    }

    private void setUpTitle () {
        this.titleTextView.setText(this.taskGroup.title);
    }
    private void updateSoonestTimer () {

        Calendar soonest    = this.taskGroup.getSoonestDueDate();
        Calendar now        = Calendar.getInstance();
        String soonestTimerString;


        if(soonest == null) {
            soonestTimerString = utils.representationOfInterval(now,now);
        } else {
            soonestTimerString = utils.representationOfInterval(this.taskGroup.getSoonestDueDate(), now);
        }

        this.soonestTimerTextView.setText(soonestTimerString);

    }


    private void setAdapterForListview() {
        this.adapter = new TaskAdapter(this, R.layout.activity_tasks, this.taskGroup.taskList);
        this.taskListView.setAdapter(this.adapter);
    }

    //private


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.tasks, menu);
        return true;
    }
    private void addFooterToTaskGroupListView() {
        //this.buildedTaskInFooter = new Task();

        LayoutInflater inflater = this.getLayoutInflater();
        View inputFooter = inflater.inflate(R.layout.task_input, null, false);
        this.taskListView.addFooterView(inputFooter);

        Button addGroupButton               =   (Button)    inputFooter.findViewById(R.id.addTaskButton);
        final EditText titleEditText        =   (EditText)  inputFooter.findViewById(R.id.titleEditText);
        final EditText descriptionEditText  =   (EditText)  inputFooter.findViewById(R.id.descriptionEditText);
        final TextView setDueTimeTextView   =   (TextView)  inputFooter.findViewById(R.id.setDueTimeTextView);


        final TasksActivity self = this;

        setDueTimeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(self);

                LayoutInflater inflater = self.getLayoutInflater();
                builder.setView(inflater.inflate(R.layout.date_time_picker, null));
                builder.setTitle(R.string.set_due_time);

                // Add the buttons
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Dialog d = (Dialog) dialog;
                        DatePicker datePicker = (DatePicker) d.findViewById(R.id.datePicker);
                        TimePicker timePicker = (TimePicker) d.findViewById(R.id.timePicker);


                        int day = datePicker.getDayOfMonth();
                        int month = datePicker.getMonth();
                        int year = datePicker.getYear();
                        int hour = timePicker.getCurrentHour();
                        int minute = timePicker.getCurrentMinute();

                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, month, day, hour, minute, 0);

                        self.buildedTaskInFooter.dueDate = calendar;

                        String dateString = utils.representReadablyCalendar(calendar);
                        setDueTimeTextView.setText(dateString);
                        // User clicked OK button
                    }
                });

                builder.show();
            }
        });


        addGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title        = titleEditText.getText().toString();
                String description  = descriptionEditText.getText().toString();

                self.buildedTaskInFooter.title = title;
                self.buildedTaskInFooter.description = description;



                self.adapter.add(self.buildedTaskInFooter);
                self.adapter.notifyDataSetChanged();

                self.createNewBuildedTaskForFooter();

            }
        });
    }

    private void createNewBuildedTaskForFooter () {
        this.buildedTaskInFooter = new Task();
        this.buildedTaskInFooter.dueDate = Calendar.getInstance();
    }

    private void updateDueTimeInInputFooter () {
        //String dateString = utils.representReadablyCalendar(calendar);
        //setDueTimeTextView.setText(dateString);
    }

    private void scheduleUpdatesOfListView () {

        final TasksActivity self = this;
        CountDownTimer countDownTimer = new CountDownTimer(Integer.MAX_VALUE, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                self.tickForDueTimerUpdate();
            }

            @Override
            public void onFinish() {

            }
        };

        countDownTimer.start();

    }

    private void tickForDueTimerUpdate () {



        Calendar now = Calendar.getInstance();

        int firstVisible = this.taskListView.getFirstVisiblePosition();
        for (int i = 0; i < this.taskListView.getLastVisiblePosition() - this.taskListView.getFirstVisiblePosition(); i++) {
            Task                  task         = (Task) this.taskListView.getItemAtPosition(firstVisible + i);
            TaskAdapter.TaskViews taskRowViews = (TaskAdapter.TaskViews) this.taskListView.getChildAt(i).getTag();

            // GET

            Log.e("TAG", " taskTimerTextview" + taskRowViews + " --- "  );

            TextView tw;

            if(taskRowViews == null) {
                tw =  (TextView) this.taskListView.getChildAt(i).findViewById(R.id.taskTimerTextView);
            } else {
                tw = taskRowViews.taskTimerTextView;
            }

            Log.e("TAG", " taskTimerTextview" + tw + " --- "  );
            tw.setText(utils.representationOfInterval(task.dueDate, now));
        }

        this.updateSoonestTimer();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        Log.v("TAG","HOLO");

        Intent resultIntent = new Intent();
        resultIntent.putExtra("TAG", this.taskGroup);

        setResult(Activity.RESULT_OK, resultIntent);
        finish();
        // super.onBackPressed();

        //Intent intent = new Intent();
        //intent.putIntegerArrayListExtra(SELECTION_LIST, selected);
        //setResult(RESULT_OK, intent);
        //finish();
    }

}
