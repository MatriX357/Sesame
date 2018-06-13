package com.mxmbro.sesame.views;

import com.mxmbro.sesame.R;
import com.mxmbro.sesame.tasks.Task;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TaskListItem extends LinearLayout {

    private Task task;
    private TextView complete;
    private TextView title;
    private TextView priority;
    private TextView date;

    public TaskListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        complete = findViewById(R.id.tli_finished);
        title = findViewById(R.id.tli_title);
        priority = findViewById(R.id.tli_priority);
        date = findViewById(R.id.tli_date);
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
        if (task.isComplete()){
            complete.setText(R.string.finished);
        }else{
            complete.setText(R.string.not_finished);
        }

        title.setText(task.getTask());
        priority.setText(task.getPriority());
        date.setText(task.getDateString());
    }
}