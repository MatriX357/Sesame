package com.mxmbro.sesame.systems;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mxmbro.sesame.SettingsActivity;

public class PasswordSQLiteOpenHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "tasks_db.sqllite";
    private static final int VERSION = 1;
    public static final String TASKS_TABLE = "Password";
    public static final String PASSWORD = "Password";
    public PasswordSQLiteOpenHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void createTable(SQLiteDatabase db) {
        db.execSQL("create table " + TASKS_TABLE + " ( " + PASSWORD + ");"
        );
        db.execSQL("insert into " + TASKS_TABLE + " values ( " + PASSWORD + ");"
        );
    }
}
