package com.landanurm.progress_tracker.ui.helpers.helper_to_test_fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

import com.landanurm.progress_tracker.R;
import com.landanurm.progress_tracker.data.ProgressiveTask;
import com.landanurm.progress_tracker.ui.fragment.task_list.TaskListFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leonid on 25.01.14.
 */
public class ActivityHelperToTestFragments extends Activity
        implements TaskListFragment.OnNeedToAddNewTaskListener, TaskListFragment.TasksProvider {

    private static class DummyTasksProvider implements TaskListFragment.TasksProvider, Serializable {
        @Override
        public List<ProgressiveTask> getTasks() {
            return new ArrayList<ProgressiveTask>();
        }
    }

    private TaskListFragment.OnNeedToAddNewTaskListener onNeedToAddNewTaskListener =
            new TaskListFragment.OnNeedToAddNewTaskListener() {
                @Override
                public void onNeedToAddNewTask() {
                    // do nothing
                }
            };

    private TaskListFragment.TasksProvider tasksProvider = new TaskListFragment.TasksProvider() {
        @Override
        public List<ProgressiveTask> getTasks() {
            return new ArrayList<ProgressiveTask>();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helper_to_test_fragments);

        if (savedInstanceState == null) {
            Fragment currentTestedFragment = CurrentTestedFragmentBuilder.build();
            getFragmentManager()
                .beginTransaction()
                .add(R.id.containerOfCurrentTestedFragment, currentTestedFragment)
                .commit();
        }

        tasksProvider = prepareTasksProvider(savedInstanceState);
    }

    private TaskListFragment.TasksProvider prepareTasksProvider(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            return new DummyTasksProvider();
        }  else {
            return (TaskListFragment.TasksProvider)
                    savedInstanceState.getSerializable(TaskListFragment.TasksProvider.class.getName());
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(TaskListFragment.TasksProvider.class.getName(), (Serializable) tasksProvider);
    }

    public Fragment getTestedFragment() {
        return getFragmentManager().findFragmentById(R.id.containerOfCurrentTestedFragment);
    }

    @Override
    public void onNeedToAddNewTask() {
        onNeedToAddNewTaskListener.onNeedToAddNewTask();
    }

    public void setOnNeedToAddNewTaskListener(TaskListFragment.OnNeedToAddNewTaskListener listener) {
        onNeedToAddNewTaskListener = listener;
    }

    @Override
    public List<ProgressiveTask> getTasks() {
        return tasksProvider.getTasks();
    }

    public void setTasksProvider(TaskListFragment.TasksProvider tasksProvider) {
        this.tasksProvider = tasksProvider;
    }
}
