package com.mxmbro.sesame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.mxmbro.sesame.systems.LogSystem;

import java.util.Arrays;


public class LoginActivity extends AppCompatActivity  {
    EditText taskPassword = findViewById(R.id.password);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }


    public void logIn(View view) throws Exception {
        char[] pssw = taskPassword.getText().toString().toCharArray();
        char[] pss0 = LogSystem.getPassword();
        if (Arrays.equals(pssw, pss0)) {
            Intent intent = new Intent(getApplicationContext(), SesameActivity.class);
            startActivity(intent);
        }
    }
}

