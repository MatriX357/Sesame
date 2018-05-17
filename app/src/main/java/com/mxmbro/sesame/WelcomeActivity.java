package com.mxmbro.sesame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.Arrays;

public class WelcomeActivity extends AppCompatActivity {
    char[] pss = "0".toCharArray();
    private TaskManagerApplication app;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println(pss);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        app = (TaskManagerApplication) getApplication();

        final Thread thread = new Thread(){
            public void run() {
                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (Arrays.equals(app.getPassword(), pss)) {
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
