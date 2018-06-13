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

import com.mxmbro.sesame.user.User;

import java.util.UUID;

public class RegistrationActivity extends AppCompatActivity{
    private EditText registerNameEditText;
    private EditText registerPasswordEditText;
    private EditText repeatedPasswordEditText;
    private EditText registerEmailEditText;
    private EditText registerPhoneEditText;
    private String name;
    private String email;
    private String phone;

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
                name = registerNameEditText.getText().toString();
                String password = registerPasswordEditText.getText().toString();
                String repeat = repeatedPasswordEditText.getText().toString();
                email = registerEmailEditText.getText().toString();
                phone = registerPhoneEditText.getText().toString();
                if(name.equals("")||password.equals("")||repeat.equals("")||email.equals("")||phone.equals("")) {
                    showToast("Uzupełnij pola");
                }else if (password.length()!=9) {
                    showToast("Hasło za krótkie minimum 9 znaków");
                }else if (!password.equals(repeat)) {
                    showToast("Źle powtórzone hasło");
                }else if (!email.contains("@")) {
                    showToast("Wpisz poprawny email");
                }else if (phone.length()!=9) {
                    showToast("Wpisz poprawny numer telefonu");
                }else{
                    User u = new User(name, password, email, phone);
                    u.setID(UUID.randomUUID().toString());
                    SesameApplication.register(u);
                    showMessage();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener(){

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
