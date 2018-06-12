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

import com.mxmbro.sesame.user.User;
import com.mxmbro.sesame.adapters.TaskListAdapter;
import com.mxmbro.sesame.tasks.Task;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class SesameActivity extends ListActivity implements NavigationView.OnNavigationItemSelectedListener {
    private SesameApplication app;
    private TaskListAdapter adapter;
    private boolean changesPending;
    private EditText btnDatePicker;
    private EditText taskNameEditText;
    private EditText taskEditText;
    private EditText taskLocationEditText;
    private EditText taskPriorityEditText;
    private EditText taskNotesEditText;
    private Date taskDate;
    private Dialog dialog;
    private boolean btnDateChanged;
    private boolean taskChanged;
    private boolean taskLocationChanged;
    private boolean taskNameChanged;
    private EditText userNameEditText;
    private EditText passwordEditText;
    private EditText registerNameEditText;
    private EditText registerPasswordEditText;
    private EditText repeatedPasswordEditText;
    private EditText registerEmailEditText;
    private EditText registerPhoneEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        startActivity(new Intent(getApplicationContext(), WelcomeActivity.class));
        loginDialog();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sesame);
        setUpViews();
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
                app.viewTasks("today");
                adapter = new TaskListAdapter(this, app.getViewTasks());
                setListAdapter(adapter);
                break;
            }
            case R.id.Week: {
                app.viewTasks("week");
                adapter = new TaskListAdapter(this, app.getViewTasks());
                setListAdapter(adapter);
                break;
            }
            case R.id.Month: {
                app.viewTasks("month");
                adapter = new TaskListAdapter(this, app.getViewTasks());
                setListAdapter(adapter);
                break;
            }
            case R.id.All: {
                app.loadTasks();
                adapter = new TaskListAdapter(this, app.getCurrentTasks());
                setListAdapter(adapter);
                break;
            }
            case R.id.Log_Out: {
                Intent intent = new Intent(getApplicationContext(), LoginDialog.class);
                finish();
                startActivity(intent);
                break;
            }
            case R.id.Settings: {
                Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
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
        SesameApplication.saveTask(t);
    }

    private void addDialog(){
        dialog = new AddDialog(this);
        dialog.show();
    }

    private void addTask() {
        if (taskNameChanged && btnDateChanged && taskChanged && taskLocationChanged) {
            String taskName = taskNameEditText.getText().toString();
            String task = taskEditText.getText().toString();
            String taskLocation = taskLocationEditText.getText().toString();
            String taskPriority = taskPriorityEditText.getText().toString();
            String taskNotes = taskNotesEditText.getText().toString();
            Task t = new Task(taskName, task, taskLocation, taskDate);
            t.setExtraInfo(taskNotes);
            t.setPriority(taskPriority);
            t.setId(UUID.randomUUID().toString());
            System.out.println(t.getId());
            SesameApplication.addTask(t);
            adapter = new TaskListAdapter(this, app.getCurrentTasks());
            setListAdapter(adapter);
            dialog.dismiss();
        } else {
            AlertDialog unsavedChangesDialog = new AlertDialog.Builder(this)
                    .setTitle(R.string.empty_field_title)
                    .setMessage(R.string.empty_field_message)
                    .setPositiveButton(R.string.ok, new AlertDialog.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .create();
            unsavedChangesDialog.show();
        }
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

    private void loadTasks(){
        app.loadTasks();
        adapter = new TaskListAdapter(this, app.getCurrentTasks());
        setListAdapter(adapter);
    }

    private void loginDialog() {
        dialog = new LoginDialog(this);
        dialog.show();
    }

    private void registerDialog() {
        dialog = new RegistrationDialog(this);
        dialog.show();
    }

    private void removeCompletedTasks() {
        String[] ids = adapter.removeCompletedTasks();
        app.deleteTasks(ids);
    }

    private void setUpViews() {
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
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        app = (SesameApplication) getApplication();
        loadTasks();
    }

    class AddDialog extends Dialog {

        AddDialog(@NonNull Context context) {
            super(context);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_add);
            setUpViews();
        }

        @Override
        public void onPointerCaptureChanged(boolean hasCapture) {

        }

        private void setUpViews() {
            taskNameEditText = findViewById(R.id.add_title);
            taskEditText = findViewById(R.id.add_task);
            taskLocationEditText = findViewById(R.id.add_location);
            btnDatePicker = findViewById(R.id.add_date);
            taskNotesEditText = findViewById(R.id.add_notes);
            taskPriorityEditText = findViewById(R.id.add_priority);
            Button addButton = findViewById(R.id.add_button);
            Button cancelButton = findViewById(R.id.add_cancel);
            btnDateChanged = false;
            taskChanged = false;
            taskLocationChanged = false;
            changesPending = false;

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

            taskNameEditText.addTextChangedListener(new TextWatcher() {

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    changesPending = true;
                    taskNameChanged = true;
                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                public void afterTextChanged(Editable s) {
                }
            });

            taskEditText.addTextChangedListener(new TextWatcher() {

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    changesPending = true;
                    taskChanged = true;
                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                public void afterTextChanged(Editable s) {
                }
            });

            taskLocationEditText.addTextChangedListener(new TextWatcher() {

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    changesPending = true;
                    taskLocationChanged = true;
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

    class LoginDialog extends Dialog  {

        LoginDialog(@NonNull Context context) {
            super(context);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_login);
            setUpViews();
        }

        private void setUpViews(){
            userNameEditText = findViewById(R.id.login_name);
            passwordEditText = findViewById(R.id.login_password);
            Button loginButton = findViewById(R.id.login_button);
            Button registerButton = findViewById(R.id.register_button);

            loginButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    SesameApplication.user_name = userNameEditText.getText().toString();
                    app.loadUser();
                    app.downloadData();
                    loadTasks();
                    dialog.dismiss();
                }
            });

            registerButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    registerDialog();
                }
            });

        }
    }

    class RegistrationDialog extends Dialog {

        RegistrationDialog(@NonNull Context context) {
            super(context);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_registration);
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
                    dialog.dismiss();
                }
            });

            cancelButton.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {

                }
            });


        }
    }
}
