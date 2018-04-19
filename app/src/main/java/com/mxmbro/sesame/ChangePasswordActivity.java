package com.mxmbro.sesame;

import android.content.Intent;
import android.preference.EditTextPreference;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class ChangePasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

       // EditTextPreference password = (EditTextPreference)getText(newpassword);
    }

    public void changePassword(View view) {
        //Intent intent = new Intent(getApplicationContext());
        //finish();
        //startActivity(intent);
    }

}
