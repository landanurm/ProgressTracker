package com.landanurm.progress_tracker.ui.helpers.helpers_to_test_fragments.helper_to_test_tasklist_fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

import com.landanurm.progress_tracker.R;
import com.landanurm.progress_tracker.data.ProgressiveTask;
import com.landanurm.progress_tracker.ui.fragment.task_list.TaskListFragment;
import com.landanurm.progress_tracker.ui.helpers.helpers_to_test_fragments.CurrentTestedFragmentBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leonid on 25.01.14.
 */
public class ActivityHelperToTestTaskListFragment extends Activity implements TaskListFragment.Callbacks {

    private static class CallbacksDummyImpl implements TaskListFragment.Callbacks, Serializable {
        @Override
        public List<ProgressiveTask> getTasks() {
            return new ArrayList<ProgressiveTask>();
        }

        @Override
        public void onNeedToAddNewTask() {
            // do nothing
        }
    }

    private static final String KEY_OF_CALLBACKS = CallbacksDummyImpl.class.getName();

    private TaskListFragment.Callbacks callbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helper_to_test_fragments);

        if (savedInstanceState == null) {
            callbacks = new CallbacksDummyImpl();
        } else {
            callbacks = (TaskListFragment.Callbacks) savedInstanceState.getSerializable(KEY_OF_CALLBACKS);
        }

        if (savedInstanceState == null) {
            Fragment currentTestedFragment = CurrentTestedFragmentBuilder.build();
            getFragmentManager()
                .beginTransaction()
                .add(R.id.containerOfCurrentTestedFragment, currentTestedFragment)
                .commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_OF_CALLBACKS, (Serializable) callbacks);
    }

    public Fragment getTestedFragment() {
        return getFragmentManager().findFragmentById(R.id.containerOfCurrentTestedFragment);
    }

    @Override
    public void onNeedToAddNewTask() {
        callbacks.onNeedToAddNewTask();
    }

    public void setCallbacks(TaskListFragment.Callbacks callbacks) {
        if (!(callbacks instanceof Serializable)) {
            throw new IllegalArgumentException("callbacks must implement Serializable");
        }
        this.callbacks = callbacks;
    }

    @Override
    public List<ProgressiveTask> getTasks() {
        return callbacks.getTasks();
    }
}
