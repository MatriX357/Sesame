package com.mxmbro.sesame;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.mxmbro.sesame.tasks.Task;

import java.util.Calendar;

public class AddActivity extends TaskManagerActivity implements View.OnClickListener {

    Button btnDatePicker;
    EditText txtDate;

    private EditText taskWhatEditText;
    private EditText taskWhereEditText;
    protected boolean changesPending;
    private AlertDialog unsavedChangesDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        setUpViews();

        btnDatePicker= findViewById(R.id.btn_date);
        txtDate= findViewById(R.id.in_date);

        btnDatePicker.setOnClickListener(this);

    }

    public void onClick(View v) {

        if (v == btnDatePicker) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
    protected void addTask() {
        String taskWhat = taskWhatEditText.getText().toString();
        String taskWhere = taskWhatEditText.getText().toString();
        Task t = new Task(taskWhat,taskWhere);
        getStuffApplication().addTask(t);
        finish();
    }

    protected void cancel() {
        if (changesPending) {
            unsavedChangesDialog = new AlertDialog.Builder(this)
                    .setTitle(R.string.unsaved_changes_title)
                    .setMessage(R.string.unsaved_changes_message)
                    .setPositiveButton(R.string.dodaj_zadanie, new AlertDialog.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            addTask();
                        }
                    })
                    .setNeutralButton(R.string.discard, new AlertDialog.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, new AlertDialog.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            unsavedChangesDialog.cancel();
                        }
                    })
                    .create();
            unsavedChangesDialog.show();
        } else {
            finish();
        }
    }
    private void setUpViews() {
        taskWhatEditText = findViewById(R.id.Co_Text);
        taskWhereEditText = findViewById(R.id.Gdzie_Text);
        Button addButton = findViewById(R.id.add_b);
        Button cancelButton = findViewById(R.id.cancel);

        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addTask();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                cancel();
            }
        });
        taskWhatEditText.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                changesPending = true;
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            public void afterTextChanged(Editable s) { }
        });
    }
}

