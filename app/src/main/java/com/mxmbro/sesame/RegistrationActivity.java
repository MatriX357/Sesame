package com.mxmbro.sesame;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.mxmbro.sesame.user.User;

import java.util.UUID;

public class RegistrationActivity extends AppCompatActivity{
    private EditText registerNameEditText;
    private EditText registerPasswordEditText;
    private EditText repeatedPasswordEditText;
    private EditText registerEmailEditText;
    private EditText registerPhoneEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        setUpViews();
    }

    private void setUpViews(){
        registerNameEditText = findViewById(R.id.register_name);
        registerPasswordEditText = findViewById(R.id.register_password);
        repeatedPasswordEditText = findViewById(R.id.repeated_password);
        registerEmailEditText = findViewById(R.id.register_email);
        registerPhoneEditText = findViewById(R.id.register_phone);
        final Button registerButton = findViewById(R.id.register);
        Button cancelButton = findViewById(R.id.register_cancel);

        registerButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String name = registerNameEditText.getText().toString();
                String password = registerPasswordEditText.getText().toString();
                String email = registerEmailEditText.getText().toString();
                String phone = registerPhoneEditText.getText().toString();
                User u = new User(name, password, email, phone);
                u.setID(UUID.randomUUID().toString());
                SesameApplication.addUser(u);
                finish();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
