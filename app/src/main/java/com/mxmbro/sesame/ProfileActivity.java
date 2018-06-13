package com.mxmbro.sesame;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileActivity extends AppCompatActivity {

    private String oldPassword;
    private String newPassword;
    private String repeatedNewPassword;
    private Dialog dialog;
    private EditText oldPasswordEditText;
    private EditText newPasswordEditText;
    private EditText repeatedNewPasswordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setUpViews();
    }

    private void changePasswordDialog(){
        dialog = new ChangePasswordDialog(this);
        dialog.show();
    }

    private void setUpViews() {
        TextView profil_name = findViewById(R.id.profil_name);
        TextView profil_email = findViewById(R.id.profil_email);
        TextView profil_phone = findViewById(R.id.profil_phone);
        Button profil_arrow = findViewById(R.id.profile_arrow);
        Button edit_profile = findViewById(R.id.edit_profile_button);
        Button changePassword = findViewById(R.id.change_password_button);
        profil_name.setText(SesameApplication.user.getName());
        profil_email.setText(SesameApplication.user.getEmail());
        profil_phone.setText(SesameApplication.user.getPhone());

        profil_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), EditProfileActivity.class));
            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePasswordDialog();
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

    private class ChangePasswordDialog extends Dialog {
        ChangePasswordDialog(@NonNull Context context) {
            super(context);
        }

        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_change_password);
            setUpViews();
        }

        private void setUpViews() {
            oldPasswordEditText = findViewById(R.id.old_password);
            newPasswordEditText = findViewById(R.id.new_password);
            repeatedNewPasswordEditText = findViewById(R.id.repeated_new_password);
            FloatingActionButton changePassword = findViewById(R.id.change_password);
            FloatingActionButton cancelChangePassword = findViewById(R.id.change_password_cancel);

            changePassword.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    oldPassword = oldPasswordEditText.getText().toString();
                    newPassword = newPasswordEditText.getText().toString();
                    repeatedNewPassword = repeatedNewPasswordEditText.getText().toString();
                    if (!SesameApplication.user.getPassword().equals(oldPassword)) {
                        showToast("Niepoprawne stare hasło");
                    } else if (newPassword.length()<=8) {
                        showToast("Nowe hasło za krótkie minimum 9 znaków");
                    }else if (!newPassword.equals(repeatedNewPassword)) {
                        showToast("Źle powtórzone hasło");
                    }else {
                        SesameApplication.user.setPassword(newPassword);
                        SesameApplication.saveUser(SesameApplication.user);
                        dialog.dismiss();
                    }
                }
            });

            cancelChangePassword.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }
    }
}
