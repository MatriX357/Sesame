package com.mxmbro.sesame;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mxmbro.sesame.systems.LogSystem;
import com.mxmbro.sesame.systems.PasswordSQLiteOpenHelper;
import com.mxmbro.sesame.tasks.TasksSQLiteOpenHelper;

import java.io.File;
import java.util.Arrays;

public class WelcomeActivity extends AppCompatActivity {
    char[] pss = {};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        final Thread thread = new Thread(){
            public void run() {
                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (Arrays.equals(LogSystem.getPassword(database), pss)) {
                            Intent intent = new Intent(getApplicationContext(), SesameActivity.class);
                            startActivity(intent);
                        }
                        else {
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }


        };
        thread.start();
    }

    @Override
    public void onPause(){
        super.onPause();
        finish();
    }
}
