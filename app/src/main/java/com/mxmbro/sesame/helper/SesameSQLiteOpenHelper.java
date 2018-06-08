package com.mxmbro.sesame.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class SesameSQLiteOpenHelper extends android.database.sqlite.SQLiteOpenHelper {

    private static final String DB_NAME = "tasks_db.sqllite";
    private static final int VERSION = 1;

    public static final String TASKS_TABLE = "tasks";
    public static final String TASK_ID = "id";
    public static final String TASK_DATE = "long";
    public static final String TASK_WHAT = "what";
    public static final String TASK_WHERE = "wher";
    public static final String TASK_COMPLETE = "complete";
    public static final String TASKS_TABLE2 = "Password";
    public static final String PASSWORD = "Password";

    public SesameSQLiteOpenHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTable(db);
    }

    private void createTable(SQLiteDatabase db) {
        db.execSQL("create table " + TASKS_TABLE + " ( " +
                TASK_ID + " integer primary key autoincrement not null, " +
                TASK_DATE + " integer, " +
                TASK_WHAT + " text, " +
                TASK_WHERE + " text, " +
                TASK_COMPLETE + " text " +
                ");"
        );
        db.execSQL("create table " + TASKS_TABLE2 + " ( " + PASSWORD + ");"
        );
        db.execSQL("insert into " + TASKS_TABLE2 + " values ( " + "'0'" + ");"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
