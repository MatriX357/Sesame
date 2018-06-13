package com.mxmbro.sesame.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class SesameSQLiteOpenHelper extends android.database.sqlite.SQLiteOpenHelper {

    private static final String DB_NAME = "tasks_db.sqllite";
    private static final int VERSION = 1;

    public static final String TASKS_TABLE = "Tasks";
    public static final String TASK_ID = "task_id";
    public static final String TASK_LOCATION = "location";
    public static final String COMPLETE = "complete";
    public static final String TASK_NAME = "name";
    public static final String TASK = "task";
    public static final String TASK_TIME = "time";
    public static final String PRIORITY = "priority";
    public static final String EXTRA_INFO = "extra_info";
    public static final String USER_ID = "user_id";
    public static final String USERS_TABLE = "Users";
    public static final String USER_NAME = "name";
    public static final String USER_EMAIL = "email";
    public static final String USER_PASSWORD = "password";
    public static final String USER_PHONE = "phone";
    public SesameSQLiteOpenHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTable(db);
    }

    private void createTable(SQLiteDatabase db) {
        createTableUsers(db);
        createTableTasks(db);
    }

    private void createTableUsers(SQLiteDatabase db) {
        db.execSQL("create table " + USERS_TABLE + " ( " +
                USER_ID + " text primary key not null, " +
                USER_NAME + " text unique, " +
                USER_EMAIL + " text unique, " +
                USER_PASSWORD + " text, " +
                USER_PHONE + " text unique" +
                ");"
        );
    }

    private void createTableTasks(SQLiteDatabase db) {
        db.execSQL("create table " + TASKS_TABLE + " ( " +
                TASK_ID + " text primary key not null, " +
                TASK_NAME + " text, " +
                TASK + " text, " +
                TASK_LOCATION + " text, " +
                TASK_TIME + " integer, " +
                COMPLETE + " text, " +
                PRIORITY + " text, " +
                EXTRA_INFO + " text, " +
                USER_ID + " text " +
                ");"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
