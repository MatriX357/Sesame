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
import java.util.Date;

public class AddActivity extends TaskManagerActivity{

    private EditText btnDatePicker;
    private EditText taskWhatEditText;
    private EditText taskWhereEditText;
    private boolean changesPending;
    private AlertDialog unsavedChangesDialog;
    private Date taskDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        setUpViews();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
    private void addTask() {
        String taskWhat = taskWhatEditText.getText().toString();
        String taskWhere = taskWhereEditText.getText().toString();
        Task t = new Task(taskWhat,taskWhere, taskDate);
        getStuffApplication().addTask(t);
        finish();
    }

    private void datePicker() {
        final Calendar calendar = Calendar.getInstance();
        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        btnDatePicker.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        calendar.set(year,monthOfYear,dayOfMonth,0,0,0);
                        calendar.set(Calendar.MILLISECOND,0);
                        System.out.println(calendar.getTimeInMillis());
                        taskDate = calendar.getTime();
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void cancel() {
        if (changesPending) {
            unsavedChangesDialog = new AlertDialog.Builder(this)
                    .setTitle(R.string.unsaved_changes_title)
                    .setMessage(R.string.unsaved_changes_message)
                    .setPositiveButton(R.string.add_task, new AlertDialog.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            addTask();
                        }
                    })
                    .setNeutralButton(R.string.discard, new AlertDialog.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setNegativeButton(R.string.cancel, new AlertDialog.OnClickListener() {
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
        btnDatePicker = findViewById(R.id.btn_date);
        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker();
            }
        });

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

        taskWhereEditText.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                changesPending = true;
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            public void afterTextChanged(Editable s) { }
        });
    }
}

