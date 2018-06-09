package com.mxmbro.sesame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class Settings2Activity extends AppCompatActivity {

    TaskManagerApplication app;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        app = (TaskManagerApplication) getApplication();
    }

    public void editProfile(View view) {
        Intent intent = new Intent(getApplicationContext(), EditProfilActivity.class);
        startActivity(intent);
    }



    public void changePassword(View view) {
        Intent intent = new Intent(getApplicationContext(), ChangePasswordActivity.class);
        startActivity(intent);
    }


    public void back(View view) {
        Intent intent = new Intent(getApplicationContext(), SesameActivity.class);
        startActivity(intent);
    }
}