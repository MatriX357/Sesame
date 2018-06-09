package com.mxmbro.sesame;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class ProfilActivity extends AppCompatActivity {

    TaskManagerApplication app;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        app = (TaskManagerApplication) getApplication();
    }
}
