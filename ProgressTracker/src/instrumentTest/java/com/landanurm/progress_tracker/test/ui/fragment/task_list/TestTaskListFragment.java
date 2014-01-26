package com.landanurm.progress_tracker.test.ui.fragment.task_list;

import android.app.Fragment;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.landanurm.progress_tracker.R;
import com.landanurm.progress_tracker.data.ProgressiveTask;
import com.landanurm.progress_tracker.ui.fragment.task_list.TaskListFragment;
import com.landanurm.progress_tracker.ui.helpers.helper_to_test_fragments.ActivityHelperToTestFragments;
import com.landanurm.progress_tracker.ui.helpers.helper_to_test_fragments.CurrentTestedFragmentBuilder;
import com.landanurm.progress_tracker.ui.helpers.helper_to_test_fragments.FragmentBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leonid on 26.01.14.
 */
public class TestTaskListFragment extends ActivityInstrumentationTestCase2<ActivityHelperToTestFragments> {

    private ActivityHelperToTestFragments activity;
    private TaskListFragment testedFragment;

    private ListView listView;
    private View addButton;


    public TestTaskListFragment() {
        super(ActivityHelperToTestFragments.class);
    }


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        CurrentTestedFragmentBuilder.setCurrentBuilder(new FragmentBuilder() {
            @Override
            public Fragment build() {
                return new TaskListFragment();
            }
        });
        activity = getActivity();
        getInstrumentation().waitForIdleSync();
        testedFragment = (TaskListFragment) activity.getTestedFragment();
        findViews(testedFragment.getView());
    }

    private void findViews(View rootView) {
        listView = (ListView) rootView.findViewById(R.id.taskList);
        addButton = rootView.findViewById(R.id.addButton);
    }

    public void testPreconditions() {
        assertNotNull(activity);
        assertNotNull(testedFragment);
        assertNotNull(listView);
        assertNotNull(addButton);
    }

    public void testStartState() {
        assertTrue(addButton.isEnabled());
        assertTrue(addButton.isClickable());
        assertTrue(listView.getCount() == 0);
    }

    private static class OnNeedToAddNewTaskListenerHelperToTest implements TaskListFragment.OnNeedToAddNewTaskListener {
        public boolean clicked;

        public OnNeedToAddNewTaskListenerHelperToTest() {
            clicked = false;
        }

        @Override
        public void onNeedToAddNewTask() {
            clicked = true;
        }
    }

    @UiThreadTest
    public void testClickOnAddButtonTellsHostActivityThatNeedToAddNewTask() {
        OnNeedToAddNewTaskListenerHelperToTest helper = new OnNeedToAddNewTaskListenerHelperToTest();
        activity.setOnNeedToAddNewTaskListener(helper);
        assertFalse(helper.clicked);
        addButton.performClick();
        assertTrue(helper.clicked);
    }


    @UiThreadTest
    public void testUpdateTaskList() {
        final String nameOfTask = "Task 1";

        addNewTaskToAppData(nameOfTask);
        testedFragment.updateTaskList();

        List<String> taskNames = getTaskNames();
        assertEquals(1, taskNames.size());

        String actualNameOfTask = taskNames.get(0);
        assertEquals(nameOfTask, actualNameOfTask);

        activity.setTasksProvider(new TaskListFragment.TasksProvider() {
            @Override
            public List<ProgressiveTask> getTasks() {
                return new ArrayList<ProgressiveTask>();
            }
        });
    }


    private static class TasksProviderHelperToTest implements TaskListFragment.TasksProvider, Serializable {
        private final List<ProgressiveTask> tasks;

        public TasksProviderHelperToTest(List<ProgressiveTask> tasks) {
            this.tasks = tasks;
        }

        @Override
        public List<ProgressiveTask> getTasks() {
            return tasks;
        }
    }

    private void addNewTaskToAppData(String nameOfTask) {
        final List<ProgressiveTask> currentTasks = activity.getTasks();
        currentTasks.add(prepareTask(nameOfTask));
        activity.setTasksProvider(new TasksProviderHelperToTest(currentTasks));
    }

    private ProgressiveTask prepareTask(String nameOfTask) {
        ProgressiveTask task = new ProgressiveTask();
        task.name = nameOfTask;
        return task;
    }

    private List<String> getTaskNames() {
        ListAdapter adapter = listView.getAdapter();
        final int numberOfTasks = adapter.getCount();
        List<String> names = new ArrayList<String>(numberOfTasks);
        for (int i = 0; i < numberOfTasks; ++i) {
            names.add(getTaskNameByIndex(i));
        }
        return names;
    }

    private String getTaskNameByIndex(int index) {
        ListAdapter adapter = listView.getAdapter();
        ProgressiveTask task = (ProgressiveTask) adapter.getItem(index);
        return task.name;
    }
}
