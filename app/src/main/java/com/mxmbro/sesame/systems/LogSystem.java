package com.mxmbro.sesame.systems;

import android.app.Application;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import static com.mxmbro.sesame.systems.PasswordSQLiteOpenHelper.TASKS_TABLE;

public class LogSystem extends Application {
    static SQLiteDatabase database;
    public void onCreate() {
        super.onCreate();
        PasswordSQLiteOpenHelper helper = new PasswordSQLiteOpenHelper(this);
        database = helper.getWritableDatabase();
    }


    public static char[] getPassword() throws Exception {
        char[] password = {};
        try (ObjectInputStream inP = new ObjectInputStream(new FileInputStream("user.save"))) {
            Cursor tasksCursor = database.rawQuery("select * from "+TASKS_TABLE, null);
            tasksCursor.moveToFirst();
            if (! tasksCursor.isAfterLast()) {
                password  = tasksCursor.getString(0).toCharArray();
            }
        }
        return password;

    }

    public static void setPassword(SQLiteDatabase database, char[] pss0) throws Exception {
        try (ObjectOutputStream outP = new ObjectOutputStream(new FileOutputStream("user.save"))) {
            outP.writeObject(pss0);
            outP.flush();
        }

    }
}
