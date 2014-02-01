package com.landanurm.progress_tracker.test.ui.fragment.task_list;

import android.app.Fragment;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.landanurm.progress_tracker.R;
import com.landanurm.progress_tracker.data.ProgressiveTask;
import com.landanurm.progress_tracker.ui.fragment.task_list.TaskListFragment;
import com.landanurm.progress_tracker.ui.helpers.helpers_to_test_fragments.CurrentTestedFragmentBuilder;
import com.landanurm.progress_tracker.ui.helpers.helpers_to_test_fragments.FragmentBuilder;
import com.landanurm.progress_tracker.ui.helpers.helpers_to_test_fragments.helper_to_test_tasklist_fragment.ActivityHelperToTestTaskListFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Leonid on 26.01.14.
 */
public class TestTaskListFragment extends ActivityInstrumentationTestCase2<ActivityHelperToTestTaskListFragment> {

    private ActivityHelperToTestTaskListFragment activity;
    private TaskListFragment testedFragment;

    private ListView listView;
    private ImageButton addButton;


    public TestTaskListFragment() {
        super(ActivityHelperToTestTaskListFragment.class);
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
        addButton = (ImageButton) rootView.findViewById(R.id.addButton);
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

    private static class CallbacksImpl implements TaskListFragment.Callbacks, Serializable {
        public final List<ProgressiveTask> clickedTasks;
        public boolean addNewTaskButtonWasClicked;

        private final List<ProgressiveTask> tasks;


        static CallbacksImpl withoutTasks() {
            List<ProgressiveTask> noTasks = new ArrayList<ProgressiveTask>();
            return new CallbacksImpl(noTasks);
        }

        CallbacksImpl(List<ProgressiveTask> tasks) {
            this.tasks = tasks;
            clickedTasks = new ArrayList<ProgressiveTask>();
            addNewTaskButtonWasClicked = false;
        }

        @Override
        public List<ProgressiveTask> getTasks() {
            return tasks;
        }

        @Override
        public void onNeedToAddNewTask() {
            addNewTaskButtonWasClicked = true;
        }

        @Override
        public void onTaskClick(ProgressiveTask clickedTask) {
            clickedTasks.add(clickedTask);
        }
    }

    @UiThreadTest
    public void testClickOnAddButtonTellsHostActivityThatNeedToAddNewTask() {
        CallbacksImpl callbacks = CallbacksImpl.withoutTasks();
        activity.setCallbacks(callbacks);
        assertFalse(callbacks.addNewTaskButtonWasClicked);
        addButton.performClick();
        assertTrue(callbacks.addNewTaskButtonWasClicked);
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

        activity.setCallbacks(CallbacksImpl.withoutTasks());
    }

    private void addNewTaskToAppData(String nameOfTask) {
        final List<ProgressiveTask> currentTasks = activity.getTasks();
        currentTasks.add(prepareTask(nameOfTask));
        CallbacksImpl callbacks = new CallbacksImpl(currentTasks);
        activity.setCallbacks(callbacks);
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


    @UiThreadTest
    public void testClickOnTaskListItemNotifyHostActivity() {
        List<ProgressiveTask> tasks = Arrays.asList(new ProgressiveTask[] {
                prepareTask("Task 1"),
                prepareTask("Task 2"),
                prepareTask("Task 3"),
                prepareTask("Task 4"),
                prepareTask("Task 5")
        });
        CallbacksImpl callbacks = new CallbacksImpl(tasks);
        activity.setCallbacks(callbacks);
        assertTrue(callbacks.clickedTasks.isEmpty());

        int clickedTasksCount = 0;
        for (int index = 0; index < tasks.size(); ++index) {
            clickOnTaskByIndex(index);
            ++clickedTasksCount;
            assertTrue((callbacks.clickedTasks.size() == clickedTasksCount));
            assertTrue(areBeginningOfListsEqual(callbacks.clickedTasks, tasks, index + 1));
        }
    }

    private static <T> boolean areBeginningOfListsEqual(List<T> list1, List<T> list2, int beginningLength) {
        for (int i = 0; i < beginningLength; ++i) {
            T elem1 = list1.get(i);
            T elem2 = list2.get(i);
            if (!elem1.equals(elem2)) {
                return false;
            }
        }
        return true;
    }

    private void clickOnTaskByIndex(int indexOfTask) {
        listView.performItemClick(
                listView.getChildAt(indexOfTask),
                indexOfTask,
                listView.getAdapter().getItemId(indexOfTask));
    }
}
