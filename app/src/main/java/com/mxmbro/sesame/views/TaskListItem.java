package com.mxmbro.sesame.views;

import com.mxmbro.sesame.R;
import com.mxmbro.sesame.tasks.Task;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TaskListItem extends LinearLayout {

    private Task task;
    private TextView checkbox;
    private TextView what;
    private TextView where;
    private TextView date;

    public TaskListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        checkbox = findViewById(R.id.finished);
        what = findViewById(R.id.what);
        where = findViewById(R.id.where);
        date = findViewById(R.id.date);
    }

    public void setTask(Task task) {
        this.task = task;
        if (task.isComplete()){
            checkbox.setText(R.string.finished);
        }else{
            checkbox.setText(R.string.not_finished);
        }

        what.setText(task.getWhat());
        where.setText(task.getWhere());
        date.setText(task.getDateString());
    }

    public Task getTask() {
        return task;
    }
}