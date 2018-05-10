package com.mxmbro.sesame;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;

import java.util.Objects;

public class ViewPreferenceActivity extends Activity {

    SharedPreferences sharedPreferences;
    private String newPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences sharedPreferences = null;


        //noinspection ConstantConditions
        //newPassword = Objects.requireNonNull(null).getString("newpassword", "brak");


        }


}
