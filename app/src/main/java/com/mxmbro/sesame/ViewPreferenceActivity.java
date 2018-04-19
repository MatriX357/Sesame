package com.mxmbro.sesame;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

public class ViewPreferenceActivity extends Activity {

    SharedPreferences sharedPreferences;
    String newPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences sharedPreferences = null;


        newPassword = sharedPreferences.getString("newpassword", "brak");


        }


}
