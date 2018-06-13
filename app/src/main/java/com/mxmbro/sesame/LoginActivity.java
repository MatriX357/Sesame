package com.mxmbro.sesame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    private EditText userNameEditText;
    private EditText passwordEditText;
    private SesameApplication app;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setUpViews();
    }

    private void checkField() {
        try {
            if (SesameApplication.downloading) {
                throw new Exception();
            }
            if (SesameApplication.user.getID().equals("00000000-0000-0000-0000-000000000000")) {
                showToast("Podany użytkownik nie istnieje");
            } else if (!SesameApplication.user.getPassword().equals(password)) {
                showToast("Niepoprawne hasło");
            } else {
                finish();
            }
        }catch (Exception ignore){
            checkField();
        }
    }

    private void setUpViews() {
        userNameEditText = findViewById(R.id.login_name);
        passwordEditText = findViewById(R.id.login_password);
        Button loginButton = findViewById(R.id.login_button);
        Button registerButton = findViewById(R.id.register_button);
        app = (SesameApplication) getApplication();
        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String name = userNameEditText.getText().toString();
                password = passwordEditText.getText().toString();
                SesameApplication.user_name = name;
                app.loadUser();
                app.loadTasks();
                app.downloadData();
                if (name.equals("")||password.equals("")){
                    showToast("Uzupełnij pola");
                }else{
                    checkField();
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegistrationActivity.class));
            }
        });
    }

    private void showToast(String message){
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_message,
                (ViewGroup) findViewById(R.id.toast_message));

        TextView text = layout.findViewById(R.id.message);
        text.setText(message);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }
}
