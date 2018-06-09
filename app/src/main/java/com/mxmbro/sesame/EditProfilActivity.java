package com.mxmbro.sesame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class EditProfilActivity extends AppCompatActivity {


    SesameApplication app;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profil);
        app = (SesameApplication) getApplication();
    }

    public void back(View view) {
        Intent intent = new Intent(getApplicationContext(), Settings2Activity.class);
        startActivity(intent);
    }

    public void cancel(View view){
        Intent intent = new Intent(getApplicationContext(), Settings2Activity.class);
        startActivity(intent);
    }

}
