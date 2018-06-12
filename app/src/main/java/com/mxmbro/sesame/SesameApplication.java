package com.mxmbro.sesame;

import android.app.Application;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import com.mxmbro.sesame.helper.SesameSQLiteOpenHelper;
import com.mxmbro.sesame.tasks.Task;
import com.mxmbro.sesame.user.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.mxmbro.sesame.helper.SesameSQLiteOpenHelper.COMPLETE;
import static com.mxmbro.sesame.helper.SesameSQLiteOpenHelper.EXTRA_INFO;
import static com.mxmbro.sesame.helper.SesameSQLiteOpenHelper.PRIORITY;
import static com.mxmbro.sesame.helper.SesameSQLiteOpenHelper.TASK;
import static com.mxmbro.sesame.helper.SesameSQLiteOpenHelper.TASKS_TABLE;
import static com.mxmbro.sesame.helper.SesameSQLiteOpenHelper.TASK_ID;
import static com.mxmbro.sesame.helper.SesameSQLiteOpenHelper.TASK_LOCATION;
import static com.mxmbro.sesame.helper.SesameSQLiteOpenHelper.TASK_NAME;
import static com.mxmbro.sesame.helper.SesameSQLiteOpenHelper.TASK_TIME;
import static com.mxmbro.sesame.helper.SesameSQLiteOpenHelper.USERS_TABLE;
import static com.mxmbro.sesame.helper.SesameSQLiteOpenHelper.USER_EMAIL;
import static com.mxmbro.sesame.helper.SesameSQLiteOpenHelper.USER_ID;
import static com.mxmbro.sesame.helper.SesameSQLiteOpenHelper.USER_NAME;
import static com.mxmbro.sesame.helper.SesameSQLiteOpenHelper.USER_PASSWORD;
import static com.mxmbro.sesame.helper.SesameSQLiteOpenHelper.USER_PHONE;

public class SesameApplication extends Application {

    private static String data_S;
    private static JSONArray JAr;
    private static boolean ES = true;
    private static ArrayList<Task> currentTasks;
    private static ArrayList<String> currentTasksId;
    private ArrayList<Task> viewTasks;
    private static SQLiteDatabase database_static;
    private SQLiteDatabase database;
    private static User user;
    private static String URL ="";
    static String user_name;

    public void onCreate() {
        super.onCreate();
        SesameSQLiteOpenHelper helper = new SesameSQLiteOpenHelper(this);
        user = new User("", "", "", "");
        user.setID("00000000-0000-0000-0000-000000000000");
        database = helper.getWritableDatabase();
        database_static = helper.getWritableDatabase();
    }

    void loadTasks() {
        currentTasks = new ArrayList<>();
        currentTasksId = new ArrayList<>();
        Cursor tasksCursor;
        tasksCursor = database.rawQuery("select * from " + TASKS_TABLE + " where " + USER_ID + " = " + "'" + user.getID() + "'" + " order by " + COMPLETE + ", " + TASK, null);
        Task t;
        tasksCursor.moveToFirst();
        if (! tasksCursor.isAfterLast()) {
            do {
                String task_id = tasksCursor.getString(0);
                String name = tasksCursor.getString(1);
                String task = tasksCursor.getString(2);
                String location = tasksCursor.getString(3);
                Long time = tasksCursor.getLong(4);
                String boolValue = tasksCursor.getString(5);
                String priority = tasksCursor.getString(6);
                String extra_info = tasksCursor.getString(7);
                boolean complete = Boolean.parseBoolean(boolValue);
                t = new Task(name, task, location, new Date(time));
                t.setId(task_id);
                t.setPriority(priority);
                t.setExtraInfo(extra_info);
                t.setComplete(complete);
                currentTasks.add(t);
                currentTasksId.add(task_id);
            } while(tasksCursor.moveToNext());
        }
        tasksCursor.close();
    }

    public void loadUser() {
        currentTasks = new ArrayList<>();
        Cursor userCursor;
        User u = new User("", "", "", "");
        u.setID("00000000-0000-0000-0000-000000000000");
        userCursor = database.rawQuery("select * from " + USERS_TABLE + " where " + USER_NAME + " = " + "'" + user_name + "'", null);
        userCursor.moveToFirst();
        if (! userCursor.isAfterLast()) {
            do {
                String user_id = userCursor.getString(0);
                String name = userCursor.getString(1);
                String email = userCursor.getString(2);
                String password = userCursor.getString(3);
                String phone = userCursor.getString(4);
                u = new User(name, password, email, phone);
                u.setID(user_id);
            } while(userCursor.moveToNext());
        }
        user = u;
        userCursor.close();
    }

    void viewTasks(String mode){
        viewTasks = new ArrayList<>();
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
                tasksCursor = database.rawQuery("select * from " + TASKS_TABLE + " where " + COMPLETE + " = " + c.getTimeInMillis() + " AND " + USER_ID + " = " + "'" + user.getID() + "'" + " order by " + COMPLETE + ", " + TASK, null);
                break;
            case "week":
                int dow = c.get(Calendar.DAY_OF_WEEK);
                c.set(Calendar.DAY_OF_YEAR, c.get(Calendar.DAY_OF_YEAR) - dow + 2);
                d.set(Calendar.DAY_OF_YEAR, d.get(Calendar.DAY_OF_YEAR) + (7 - dow + 2));
                tasksCursor = database.rawQuery("select * from " + TASKS_TABLE + " where " + COMPLETE + " >= " + c.getTimeInMillis() + " AND " + USER_ID + " = " + "'" + user.getID() + "'" + " order by " + COMPLETE + ", " + TASK, null);
                break;
            case "month":
                int moy = c.get(Calendar.MONTH);
                c.set(Calendar.DAY_OF_MONTH, 1);
                d.set(Calendar.DAY_OF_MONTH, 1);
                d.set(Calendar.MONTH, moy + 1);
                tasksCursor = database.rawQuery("select * from " + TASKS_TABLE + " where " + COMPLETE + " >= " + c.getTimeInMillis() + " AND " + USER_ID + " = " + "'" + user.getID() + "'" + " order by " + COMPLETE + ", " + TASK, null);
                break;
            default:
                tasksCursor = database.rawQuery("select * from " + TASKS_TABLE, null);
                break;
        }
        Task t;
        tasksCursor.moveToFirst();
        if (! tasksCursor.isAfterLast()) {
            do {
                String task_id = tasksCursor.getString(0);
                String name = tasksCursor.getString(1);
                String task = tasksCursor.getString(2);
                String location = tasksCursor.getString(3);
                Long time = tasksCursor.getLong(4);
                String boolValue = tasksCursor.getString(5);
                String priority = tasksCursor.getString(6);
                String extra_info = tasksCursor.getString(7);
                boolean complete = Boolean.parseBoolean(boolValue);
                t = new Task(name, task, location, new Date(time));
                t.setId(task_id);
                t.setPriority(priority);
                t.setExtraInfo(extra_info);
                t.setComplete(complete);
                viewTasks.add(t);
            } while(tasksCursor.moveToNext());
        }
        tasksCursor.close();
    }

    public void setCurrentTasks(ArrayList<Task> currentTasks) {
        SesameApplication.currentTasks = currentTasks;
        currentTasksId = new ArrayList<>();
        for (Task task:currentTasks){
            currentTasksId.add(task.getId());
        }
    }

    public ArrayList<Task> getCurrentTasks() {
        return currentTasks;
    }

    public ArrayList<Task> getViewTasks() {
        return viewTasks;
    }

    public static void addTask(Task t) {
        assert(null != t);

        ContentValues values = new ContentValues();
        values.put(TASK_ID, t.getId());
        values.put(TASK_NAME, t.getName());
        values.put(TASK, t.getTask());
        values.put(TASK_LOCATION, t.getLocation());
        values.put(TASK_TIME, t.getDate().getTime());
        values.put(COMPLETE, Boolean.toString(t.isComplete()));
        values.put(PRIORITY, t.getPriority());
        values.put(EXTRA_INFO, t.getExtraInfo());
        values.put(USER_ID, user.getID());

        database_static.insert(TASKS_TABLE, null, values);
        currentTasks.add(t);
    }

    public static void saveTask(Task t) {
        assert (null != t);

        ContentValues values = new ContentValues();
        values.put(TASK_NAME, t.getName());
        values.put(TASK, t.getTask());
        values.put(TASK_LOCATION, t.getLocation());
        values.put(TASK_TIME, t.getDate().getTime());
        values.put(COMPLETE, Boolean.toString(t.isComplete()));
        values.put(PRIORITY, t.getPriority());
        values.put(EXTRA_INFO, t.getExtraInfo());
        values.put(USER_ID, user.getID());

        String id = t.getId();
        String where = String.format("%s = '%s'", TASK_ID, id);
        database_static.update(TASKS_TABLE, values, where, null);
    }

    public void deleteTasks(String[] ids) {
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

    public void downloadData(){
        new DownloadUser().execute();
    }

    private static void connectURL(String url) throws IOException {
        data_S = "";
        BufferedReader buffRead = new BufferedReader(new InputStreamReader(new URL(url).openConnection().getInputStream()));
        String line = "";
        while(line != null){
            line = buffRead.readLine();
            data_S = String.format("%s%s", data_S, line);
        }
    }

    private static void generateJSONArray(String url) throws IOException, JSONException {
        data_S = "";
        BufferedReader buffRead = new BufferedReader(new InputStreamReader(new URL(url).openConnection().getInputStream()));
        String line = "";
        while(line != null){
            line = buffRead.readLine();
            data_S = String.format("%s%s", data_S, line);
        }
        if (data_S.equals("nullnull")) {
            JAr = new JSONArray("[]");
        }
        else {
            JAr = new JSONArray(data_S);
        }
        buffRead.close();

    }

    public static void addUser(User u) {
        ContentValues values = new ContentValues();
        values.put(USER_ID, u.getID());
        values.put(USER_NAME, u.getName());
        values.put(USER_EMAIL, u.getEmail());
        values.put(USER_PASSWORD, u.getPassword());
        values.put(USER_PHONE, u.getPhone());

        URL ="http://155.158.135.197/Sesame/insert.php?Users&user_id=" + u.getID() + "&name=" + u.getName()
                + "&email=" + u.getEmail() + "&password=" + u.getPassword() + "&phone=" + u.getPhone();
        new ConnectURL().execute();

        database_static.insert(USERS_TABLE, null, values);
    }

    static class DownloadUser extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                generateJSONArray("http://155.158.135.197/Sesame/select.php?Users&name="+user_name);
                userJAR();
                ES = true;
                new TasksDownload().execute();
            } catch (IOException | JSONException e) {
                ES = false;
            }
            return null;
        }

        private static void userJAR() throws JSONException {
            JSONObject JOb = JAr.getJSONObject(0);
            String user_id = JOb.getString("user_id");
            String name = JOb.getString("name");
            String email = JOb.getString("email");
            String password = JOb.getString("password");
            String phone = JOb.getString("phone");
            User u = new User(name, password, email, phone);
            u.setID(user_id);
            if (u.equals(user)){
                user = u;
            }
        }
    }

    static class TasksDownload extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                generateJSONArray("http://155.158.135.197/Sesame/select.php?Tasks&user_id=" + user.getID());
                for (int i = 0; i < JAr.length(); i++) {
                    tasksJAR(i);
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        private void tasksJAR(int i) throws JSONException {
            JSONObject JOb = JAr.getJSONObject(i);
            String task_id = JOb.getString("task_id");
            String name = JOb.getString("name");
            String task = JOb.getString("task");
            String location = JOb.getString("location");
            long time = JOb.getLong("time");
            String complete = JOb.getString("complete");
            String priority = JOb.getString("priority");
            String extraInfo = JOb.getString("extra_info");
            if (!currentTasksId.contains(task_id)) {
                Task t = new Task(name, task, location, new Date(time));
                t.setId(task_id);
                t.setComplete(Boolean.parseBoolean(complete));
                t.setPriority(priority);
                t.setExtraInfo(extraInfo);
                addTask(t);
            } else {
                Task t = currentTasks.get(currentTasksId.indexOf(task_id));
                t.setName(name);
                t.setTask(task);
                t.setLocation(location);
                t.setDate(new Date(time));
                t.setComplete(Boolean.parseBoolean(complete));
                t.setPriority(priority);
                t.setExtraInfo(extraInfo);
                saveTask(t);
            }
        }
    }

    static class ConnectURL extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                connectURL(URL);
                URL = "";
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
