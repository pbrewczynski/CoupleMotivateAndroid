package pl.twigit.couplemotivate;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.content.Intent;

import com.parse.Parse;
import com.parse.ParseObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.zip.Inflater;

import pl.twigit.couplemotivate.descriptorAdapters.TaskGroupAdapter;
import pl.twigit.couplemotivate.descriptors.Task;
import pl.twigit.couplemotivate.descriptors.TaskGroup;


public class MainActivity extends Activity {



    private ListView                taskGroupListView;
    private ArrayList<TaskGroup>    taskGroupModel;
    private TaskGroupAdapter        adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.initializeParseBackend();

        this.taskGroupModel = new ArrayList<TaskGroup>() {{ }};

        ArrayList<Task> t = new ArrayList<Task>();
        t.add(new Task("Title", "description", Calendar.getInstance(), 1));

        taskGroupModel.add(new TaskGroup("Title", t, 2));

        this.taskGroupListView = (ListView) this.findViewById(R.id.taskGroupListView);

        // Setting button footer that allow to add rows

        this.addFooterToTaskGroupListView();

        this.adapter = new TaskGroupAdapter(this, R.layout.task_group, this.taskGroupModel);
        this.taskGroupListView.setAdapter(adapter);

        final MainActivity self = this;
        this.taskGroupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(self, TasksActivity.class);
                //intent.putExtra("")

                intent.putExtra("TAG", self.taskGroupModel.get(position));
                //self.startActivityForResult();
                self.startActivityForResult(intent,1);
                //self.startActivity(intent);

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e("TAG", "onActivityResult");

    }

    private void initializeParseBackend() {
        Parse.initialize(this, "odtKXMch88tbk46Ik9vvra7Qvvyyuwk6Gttd3Uwe", "5HAttImdrjqXHCV0LgcbBdiXqioh3emg8XrEAuM8");
    }

    private void addFooterToTaskGroupListView() {
        LayoutInflater inflater = this.getLayoutInflater();
        View inputFooter = inflater.inflate(R.layout.task_group_input, null, false);
        this.taskGroupListView.addFooterView(inputFooter);

        Button addGroupButton           =   (Button)    inputFooter.findViewById(R.id.addGroupButton);
        final EditText titleEditText    =   (EditText)  inputFooter.findViewById(R.id.titleEditText);

        final MainActivity self = this;
        addGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titleEditText.getText().toString();
                TaskGroup newGroup = new TaskGroup(title);
                self.adapter.add(newGroup);
                self.adapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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
}
