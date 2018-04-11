package com.mxmbro.sesame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SettingTest extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_test);
    }

    public void changePassword(View view) {
        Intent intent = new Intent(getApplicationContext(), ChangePasswordActivity.class );
        finish();
        startActivity(intent);
    }

}
