package com.mxmbro.sesame;

import android.content.Intent;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

public class WelcomeActivity extends AppCompatActivity {
    boolean czyHaslo = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);


        Thread thread = new Thread(){
            public void run() {
                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    if (czyHaslo == true) {
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                    }
                    else {
                        Intent intent = new Intent(getApplicationContext(), SesameActivity.class);
                        startActivity(intent);
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
