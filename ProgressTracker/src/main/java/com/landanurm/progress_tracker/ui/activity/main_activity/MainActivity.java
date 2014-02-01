package com.landanurm.progress_tracker.ui.activity.main_activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.landanurm.progress_tracker.R;
import com.landanurm.progress_tracker.data.ProgressiveTask;
import com.landanurm.progress_tracker.ui.fragment.task_list.TaskListFragment;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends Activity implements TaskListFragment.Callbacks {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new TaskListFragment())
                    .commit();
        }
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

    @Override
    public List<ProgressiveTask> getTasks() {
        return Arrays.asList(new ProgressiveTask[] {
                taskWithName("Task 1"),
                taskWithName("Task 2"),
                taskWithName("Task 3"),
                taskWithName("Task 4"),
                taskWithName("Task 5"),
                taskWithName("Task 6"),
                taskWithName("Task 7"),
                taskWithName("Task 8"),
                taskWithName("Task 9"),
                taskWithName("Task 10"),
                taskWithName("Task 11"),
                taskWithName("Task 12"),
                taskWithName("Task 13"),
                taskWithName("Task 14"),
                taskWithName("Task 15"),
                taskWithName("Task 16"),
                taskWithName("Task 17"),
                taskWithName("Task 18"),
                taskWithName("Task 19"),
                taskWithName("Task 20")
        });
    }

    private ProgressiveTask taskWithName(String name) {
        ProgressiveTask task = new ProgressiveTask();
        task.name = name;
        return task;
    }

    @Override
    public void onNeedToAddNewTask() {
        Toast.makeText(this, "Add NEW Task", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTaskClick(ProgressiveTask clickedTask) {
        Toast.makeText(this, "Task <" + clickedTask.name + ">", Toast.LENGTH_SHORT).show();
    }
}
