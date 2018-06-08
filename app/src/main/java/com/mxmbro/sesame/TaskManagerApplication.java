package com.mxmbro.sesame;

import static com.mxmbro.sesame.helper.SesameSQLiteOpenHelper.PASSWORD;
import static com.mxmbro.sesame.helper.SesameSQLiteOpenHelper.TASKS_TABLE;
import static com.mxmbro.sesame.helper.SesameSQLiteOpenHelper.TASKS_TABLE2;
import static com.mxmbro.sesame.helper.SesameSQLiteOpenHelper.TASK_COMPLETE;
import static com.mxmbro.sesame.helper.SesameSQLiteOpenHelper.TASK_DATE;
import static com.mxmbro.sesame.helper.SesameSQLiteOpenHelper.TASK_ID;
import static com.mxmbro.sesame.helper.SesameSQLiteOpenHelper.TASK_WHAT;
import static com.mxmbro.sesame.helper.SesameSQLiteOpenHelper.TASK_WHERE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import  com.mxmbro.sesame.tasks.Task;
import com.mxmbro.sesame.helper.SesameSQLiteOpenHelper;

public class TaskManagerApplication extends Application {

    public static String password;
    private ArrayList<Task> currentTasks;
    private SQLiteDatabase database;

    public void onCreate() {
        super.onCreate();
        SesameSQLiteOpenHelper helper = new SesameSQLiteOpenHelper(this);
        database = helper.getWritableDatabase();
    }

    @SuppressLint("Recycle")
    void loadTasks(String mode) {
        currentTasks = new ArrayList<>();
        Cursor tasksCursor;
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY,0);
        c.set(Calendar.MINUTE,0);
        c.set(Calendar.SECOND,0);
        c.set(Calendar.MILLISECOND,0);
        Calendar d = Calendar.getInstance();
        d.set(Calendar.HOUR_OF_DAY,0);
        d.set(Calendar.MINUTE,0);
        d.set(Calendar.SECOND,0);
        d.set(Calendar.MILLISECOND,0);
        switch (mode) {
            case "today":
                tasksCursor = database.rawQuery("select * from " + TASKS_TABLE + " where " + TASK_DATE + " = " + c.getTimeInMillis() + " order by " + TASK_DATE + ", " + TASK_WHAT, null);
                break;
            case "week":
                int dow = c.get(Calendar.DAY_OF_WEEK);
                c.set(Calendar.DAY_OF_YEAR, c.get(Calendar.DAY_OF_YEAR) - dow + 2);
                d.set(Calendar.DAY_OF_YEAR, d.get(Calendar.DAY_OF_YEAR) + (7 - dow + 2));
                tasksCursor = database.rawQuery("select * from " + TASKS_TABLE + " where " + TASK_DATE + " >= " + c.getTimeInMillis() + " and " + TASK_DATE + " < " + d.getTimeInMillis() + " order by " + TASK_DATE + ", " + TASK_WHAT, null);
                break;
            case "month":
                int moy = c.get(Calendar.MONTH);
                c.set(Calendar.DAY_OF_MONTH, 1);
                d.set(Calendar.DAY_OF_MONTH, 1);
                d.set(Calendar.MONTH, moy + 1);
                tasksCursor = database.rawQuery("select * from " + TASKS_TABLE + " where " + TASK_DATE + " >= " + c.getTimeInMillis() + " and " + TASK_DATE + " < " + d.getTimeInMillis() + " order by " + TASK_DATE + ", " + TASK_WHAT, null);
                break;
            default:
                tasksCursor = database.rawQuery("select * from " + TASKS_TABLE + " order by " + TASK_DATE + ", " + TASK_WHAT, null);
                break;
        }
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
        String where = String.format(getString(R.string.rsrc), TASK_ID, id);
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

    public char[] getPassword() {
        char[] password = {};
        try (Cursor tasksCursor = database.rawQuery(getString(R.string.SelectFrom) +" "+ TASKS_TABLE2, null)) {
            tasksCursor.moveToFirst();
            if (!tasksCursor.isAfterLast()) {
                password = tasksCursor.getString(0).toCharArray();
            }
        }
        System.out.println(password);
        return password;

    }
    public void setPassword(char[] pss0) {
        ContentValues values = new ContentValues();
        values.put(PASSWORD, Arrays.toString(pss0));
        database.update(TASKS_TABLE2, values, null, null);
    }
}