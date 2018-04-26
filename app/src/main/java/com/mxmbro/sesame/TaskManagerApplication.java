package com.mxmbro.sesame;

import static com.mxmbro.sesame.tasks.TasksSQLiteOpenHelper.TASKS_TABLE;
import static com.mxmbro.sesame.tasks.TasksSQLiteOpenHelper.TASK_COMPLETE;
import static com.mxmbro.sesame.tasks.TasksSQLiteOpenHelper.TASK_DATE;
import static com.mxmbro.sesame.tasks.TasksSQLiteOpenHelper.TASK_ID;
import static com.mxmbro.sesame.tasks.TasksSQLiteOpenHelper.TASK_WHAT;
import static com.mxmbro.sesame.tasks.TasksSQLiteOpenHelper.TASK_WHERE;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import  com.mxmbro.sesame.tasks.Task;
import  com.mxmbro.sesame.tasks.TasksSQLiteOpenHelper;

public class TaskManagerApplication extends Application {

    private ArrayList<Task> currentTasks;
    private SQLiteDatabase database;

    public void onCreate() {
        super.onCreate();
        TasksSQLiteOpenHelper helper = new TasksSQLiteOpenHelper(this);
        database = helper.getWritableDatabase();


        if (null == currentTasks) {
            loadTasks();
        }else {
            viewToday();
        }
    }

    private void loadTasks() {
        currentTasks = new ArrayList<>();
        Cursor tasksCursor = database.query(TASKS_TABLE,
                new String[] {TASK_ID, TASK_DATE, TASK_WHAT, TASK_WHERE, TASK_COMPLETE},
                null, null, null, null, String.format("%s,%s,%s,%s", TASK_COMPLETE, TASK_DATE, TASK_WHAT, TASK_WHERE));

        tasksCursor.moveToFirst();
        Task t;
        if (! tasksCursor.isAfterLast()) {
            do {
                int id = tasksCursor.getInt(0);
                Long date = tasksCursor.getLong(1);
                String what = tasksCursor.getString(2);
                String where = tasksCursor.getString(3);
                String boolValue = tasksCursor.getString(4);
                boolean complete = Boolean.parseBoolean(boolValue);
                t = new Task(what, where, new Date(date));
                t.setId(id);
                t.setComplete(complete);
                currentTasks.add(t);
            } while(tasksCursor.moveToNext());
        }
    }

    void viewToday() {
        currentTasks = new ArrayList<>();
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY,0);
        c.set(Calendar.MINUTE,0);
        c.set(Calendar.SECOND,0);
        c.set(Calendar.MILLISECOND,0);
        System.out.println(c.getTimeInMillis());
        @SuppressLint("Recycle") Cursor tasksCursor = database.rawQuery("select * from "+TASKS_TABLE+" where "+TASK_DATE+" = "+ c.getTimeInMillis(),null);
        tasksCursor.moveToFirst();
        Task t;
        if (! tasksCursor.isAfterLast()) {
            do {
                int id = tasksCursor.getInt(0);
                Long date = tasksCursor.getLong(1);
                String what = tasksCursor.getString(2);
                String where = tasksCursor.getString(3);
                String boolValue = tasksCursor.getString(4);
                boolean complete = Boolean.parseBoolean(boolValue);
                t = new Task(what, where, new Date(date));
                t.setId(id);
                t.setComplete(complete);
                currentTasks.add(t);
            } while(tasksCursor.moveToNext());
        }
    }

    public void setCurrentTasks(ArrayList<Task> currentTasks) {
        this.currentTasks  = currentTasks;
    }

    public ArrayList<Task> getCurrentTasks() {
        return currentTasks;
    }

    public void addTask(Task t) {
        assert(null != t);

        ContentValues values = new ContentValues();
        values.put(TASK_DATE, t.getDate().getTime());
        values.put(TASK_WHAT, t.getWhat());
        values.put(TASK_WHERE, t.getWhere());
        values.put(TASK_COMPLETE, Boolean.toString(t.isComplete()));

        t.setId(database.insert(TASKS_TABLE, null, values));
        currentTasks.add(t);

    }

    public void saveTask(Task t) {
        assert (null != t);

        ContentValues values = new ContentValues();
        values.put(TASK_DATE, t.getDate().getTime());
        values.put(TASK_WHAT, t.getWhat());
        values.put(TASK_WHERE, t.getWhere());
        values.put(TASK_COMPLETE, Boolean.toString(t.isComplete()));

        long id = t.getId();
        String where = String.format("%s = %d", TASK_ID, id);
        database.update(TASKS_TABLE, values, where, null);

    }

    public void deleteTasks(Long[] ids) {
        StringBuffer idList = new StringBuffer();
        for (int i = 0; i < ids.length; i++) {
            idList.append(ids[i]);
            if (i < ids.length -1 ) {
                idList.append(",");
            }
        }

        String where = String.format("%s in (%s)", TASK_ID, idList);
        database.delete(TASKS_TABLE, where, null);
    }
}