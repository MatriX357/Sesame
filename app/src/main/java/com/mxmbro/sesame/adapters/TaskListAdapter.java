package com.mxmbro.sesame.adapters;

import java.util.ArrayList;

import com.mxmbro.sesame.R;
import com.mxmbro.sesame.tasks.Task;
import com.mxmbro.sesame.views.TaskListItem;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class TaskListAdapter extends BaseAdapter {

    private final ArrayList<Task> tasks;
    private final Context context;

    public TaskListAdapter(Context context, ArrayList<Task> tasks) {
        super();
        this.tasks = tasks;
        this.context = context;
    }

    @Override
    public int getCount() {
        return tasks.size();
    }

    @Override
    public Task getItem(int position) {
        return (null == tasks) ? null : tasks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TaskListItem tli;
        if (null == convertView) {
            tli = (TaskListItem) View.inflate(context, R.layout.task_list_item, null);
        } else {
            tli = (TaskListItem) convertView;
        }
        tli.setTask(tasks.get(position));
        return tli;
    }

    public void forceReload() {
        notifyDataSetChanged();
    }

    public void toggleTaskCompleteAtPosition(int position) {
        Task task = getItem(position);
        task.toggleComplete();
        notifyDataSetChanged();
    }

    public Long[] removeCompletedTasks() {
        ArrayList<Task> completedTasks = new ArrayList<>();
        ArrayList<Long> completedIds = new ArrayList<>();
        for (Task task : tasks) {
            if (task.isComplete()) {
                completedTasks.add(task);
                completedIds.add(task.getId());
            }
        }
        tasks.removeAll(completedTasks);
        notifyDataSetChanged();
        return completedIds.toArray(new Long[]{});
    }
    public Task[] CompletedTasks() {
        ArrayList<Task> completedTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.isComplete()) {
                completedTasks.add(task);
            }
        }
        return completedTasks.toArray(new Task[]{});
    }

}