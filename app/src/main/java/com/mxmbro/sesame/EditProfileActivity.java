package com.mxmbro.sesame;

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

public class EditProfileActivity extends AppCompatActivity {
    private EditText profileName;
    private EditText profileEmail;
    private EditText profilePhone;
    private String name;
    private String email;
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        setUpViews();
    }

    private void setUpViews() {
        profileName = findViewById(R.id.edit_profile_name);
        profileEmail = findViewById(R.id.edit_profile_email);
        profilePhone = findViewById(R.id.edit_profile_phone);
        Button editProfileCancel = findViewById(R.id.edit_profile_cancel);
        Button editProfile = findViewById(R.id.edit_profile);
        profileName.setText(SesameApplication.user.getName());
        profileEmail.setText(SesameApplication.user.getEmail());
        profilePhone.setText(SesameApplication.user.getPhone());

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = profileName.getText().toString();
                email = profileEmail.getText().toString();
                phone = profilePhone.getText().toString();

                if(name.equals("")||email.equals("")||phone.equals("")) {
                    showToast("Uzupełnij pola");
                }else if (!email.contains("@")) {
                    showToast("Wpisz poprawny email");
                }else if (phone.length()!=9) {
                    showToast("Wpisz poprawny numer telefonu");
                }else {
                    SesameApplication.user.setName(name);
                    SesameApplication.user.setEmail(email);
                    SesameApplication.user.setPhone(phone);
                    SesameApplication.saveUser(SesameApplication.user);
                    showMessage();
                }
            }
        });

        editProfileCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void showMessage() {
        try {
            if (SesameApplication.connect){
                throw new Exception();
            }
            switch (SesameApplication.message) {
                case "succesfullnull":
                    showToast("Pomyślna rejestracja");
                    finish();
                    break;
                default:
                    if (SesameApplication.message.equals("ErrorDuplicate entry '" + name + "' for key 'name'null")){
                        showToast("Użytkownik o podanej nazwie istnieję!");
                    } else if (SesameApplication.message.equals("ErrorDuplicate entry '" + email + "' for key 'email'null")){
                        showToast("Użytkownik z takim emailem istnieje!");
                    } else if (SesameApplication.message.equals("ErrorDuplicate entry '" + phone + "' for key 'phone'null")){
                        showToast("Użytkownik z takim numerem telefonu istnieje!");
                    } else {
                        showToast(SesameApplication.message);
                    }
                    break;
            }
        } catch (Exception e) {
            showMessage();
        }
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
