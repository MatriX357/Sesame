package com.mxmbro.sesame;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;

import com.mxmbro.sesame.adapters.TaskListAdapter;
import com.mxmbro.sesame.tasks.Task;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SesameActivity extends ListActivity implements NavigationView.OnNavigationItemSelectedListener {
    private SesameApplication app;
    private TaskListAdapter adapter;
    private boolean changesPending;
    private EditText btnDatePicker;
    private EditText taskWhatEditText;
    private EditText taskWhereEditText;
    private Date taskDate;
    Dialog dialog;
    private boolean btnDateChanged;
    private boolean taskWhatChanged;
    private boolean taskWhereChanged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
        startActivity(intent);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sesame);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDialog();
            }
        });
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        app = (SesameApplication) getApplication();
        app.loadTasks("all");
        adapter = new TaskListAdapter(this, app.getCurrentTasks());
        setListAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.DeleteFinished: {
                removeCompletedTasks();
                break;
            }
            case R.id.Profil: {
                Intent intent = new Intent(getApplicationContext(), ProfilActivity.class);
                finish();
                startActivity(intent);
                break;
            }

            case R.id.Today: {
                app.loadTasks("today");
                adapter = new TaskListAdapter(this, app.getCurrentTasks());
                setListAdapter(adapter);
                break;
            }
            case R.id.Week: {
                app.loadTasks("week");
                adapter = new TaskListAdapter(this, app.getCurrentTasks());
                setListAdapter(adapter);
                break;
            }
            case R.id.Month: {
                app.loadTasks("month");
                adapter = new TaskListAdapter(this, app.getCurrentTasks());
                setListAdapter(adapter);
                break;
            }
            case R.id.All: {
                app.loadTasks("all");
                adapter = new TaskListAdapter(this, app.getCurrentTasks());
                setListAdapter(adapter);
                break;
            }
            case R.id.Log_Out: {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                finish();
                startActivity(intent);


                break;
            }
            case R.id.Settings: {
                Intent intent = new Intent(getApplicationContext(), Settings2Activity.class);
                finish();
                startActivity(intent);
                break;
            }
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.forceReload();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        adapter.toggleTaskCompleteAtPosition(position);
        Task t = adapter.getItem(position);
        app.saveTask(t);
    }

    private void removeCompletedTasks() {
        Long[] ids = adapter.removeCompletedTasks();
        app.deleteTasks(ids);
    }

    private void addDialog(){
        dialog = new AddDialog(this);
        dialog.show();
    }

    private void addTask() {
        String taskWhat = taskWhatEditText.getText().toString();
        String taskWhere = taskWhereEditText.getText().toString();
        Task t = new Task(taskWhat, taskWhere, taskDate);
        app.addTask(t);
        adapter = new TaskListAdapter(this, app.getCurrentTasks());
        setListAdapter(adapter);
        dialog.dismiss();
    }

    private void datePicker() {
        final Calendar calendar = Calendar.getInstance();
        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        btnDatePicker.setText(String.format(Locale.getDefault(), "%d-%d-%d", dayOfMonth, monthOfYear + 1, year));
                        calendar.set(year, monthOfYear, dayOfMonth, 0, 0, 0);
                        calendar.set(Calendar.MILLISECOND, 0);
                        System.out.println(calendar.getTimeInMillis());
                        taskDate = calendar.getTime();
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void cancel() {
        if (changesPending) {
            AlertDialog unsavedChangesDialog = new AlertDialog.Builder(this)
                    .setTitle(R.string.unsaved_changes_title)
                    .setMessage(R.string.unsaved_changes_message)
                    .setPositiveButton(R.string.add_task, new AlertDialog.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            addTask();
                        }
                    })
                    .setNeutralButton(R.string.discard, new AlertDialog.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.cancel();
                            SesameActivity.this.dialog.cancel();
                        }
                    })
                    .setNegativeButton(R.string.cancel, new AlertDialog.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create();
            unsavedChangesDialog.show();
        } else {
            dialog.dismiss();
        }
    }

    public class AddDialog extends Dialog {

        AddDialog(@NonNull Context context) {
            super(context);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_add);
            setUpViews();
        }

        @Override
        public void onPointerCaptureChanged(boolean hasCapture) {

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
                    SesameActivity.this.cancel();
                }
            });
            taskWhatEditText.addTextChangedListener(new TextWatcher() {
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    changesPending = true;
                    taskWhatChanged = true;
                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                public void afterTextChanged(Editable s) {
                }
            });

            taskWhereEditText.addTextChangedListener(new TextWatcher() {
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    changesPending = true;
                    taskWhereChanged = true;
                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                public void afterTextChanged(Editable s) {
                }
            });

            btnDatePicker.addTextChangedListener(new TextWatcher() {
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    changesPending = true;
                    btnDateChanged = true;
                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                public void afterTextChanged(Editable s) {
                }
            });
        }
    }
}
