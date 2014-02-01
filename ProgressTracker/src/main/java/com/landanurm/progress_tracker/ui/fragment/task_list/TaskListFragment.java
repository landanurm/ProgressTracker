package com.landanurm.progress_tracker.ui.fragment.task_list;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.landanurm.progress_tracker.R;
import com.landanurm.progress_tracker.data.ProgressiveTask;
import com.landanurm.progress_tracker.ui.helpers.SimpleArrayAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leonid on 26.01.14.
 */
public class TaskListFragment extends Fragment implements AdapterView.OnItemClickListener {

    public static interface Callbacks {
        List<ProgressiveTask> getTasks();
        void onNeedToAddNewTask();
        void onTaskClick(ProgressiveTask clickedTask);
    }

    private Callbacks callbacks;
    private SimpleArrayAdapter<ProgressiveTask> adapter;
    private List<ProgressiveTask> tasks;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callbacks = (Callbacks) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_task_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initAddButton();
        initList();
    }

    private void initAddButton() {
        findViewById(R.id.addButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callbacks.onNeedToAddNewTask();
            }
        });
    }

    private View findViewById(int id) {
        return getView().findViewById(id);
    }

    private void initList() {
        SimpleArrayAdapter.ItemToTitleConvertor<ProgressiveTask> convertor =
                new SimpleArrayAdapter.ItemToTitleConvertor<ProgressiveTask>() {
                    @Override
                    public String getTitle(ProgressiveTask item) {
                        return item.name;
                    }
                };
        tasks = new ArrayList<ProgressiveTask>();
        adapter = new SimpleArrayAdapter<ProgressiveTask>(getActivity(), tasks, convertor);
        ListView listView = (ListView) findViewById(R.id.taskList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }


    @Override
    public void onResume() {
        super.onResume();
        updateTaskList();
    }

    public void updateTaskList() {
        List<ProgressiveTask> actualTasks = callbacks.getTasks();
        if (!tasks.equals(actualTasks)) {
            tasks.clear();
            tasks.addAll(actualTasks);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ProgressiveTask task = adapter.getItem(position);
        callbacks.onTaskClick(task);
    }
}
