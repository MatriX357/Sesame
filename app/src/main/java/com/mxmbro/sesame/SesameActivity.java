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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.mxmbro.sesame.adapters.TaskListAdapter;
import com.mxmbro.sesame.tasks.Task;
import com.mxmbro.sesame.user.User;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class SesameActivity extends ListActivity implements NavigationView.OnNavigationItemSelectedListener {
    private boolean btnDateChanged;
    private boolean changesPending;
    private boolean taskChanged;
    private boolean taskLocationChanged;
    private boolean taskNameChanged;
    private Date taskDate;
    private Dialog dialog;
    private EditText btnDatePicker;
    private EditText taskNameEditText;
    private EditText taskEditText;
    private EditText taskLocationEditText;
    private EditText taskPriorityEditText;
    private EditText taskNotesEditText;
    private SesameApplication app;
    private Task selectedTask;
    private TaskListAdapter adapter;
    private int selectedPosition;
    private PriorityDialog chooser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        startActivity(new Intent(getApplicationContext(), WelcomeActivity.class));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sesame);
        setUpViews();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        selectedPosition = position;
        selectedTask = adapter.getItem(position);
        previewDialog();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTasks();
        adapter.forceReload();
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
            case R.id.Profile: {
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
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
            case R.id.Log_Out: {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                SesameApplication.user = new User("","","","");
                SesameApplication.user.setID("00000000-0000-0000-0000-000000000000");
                break;
            }
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

    private void addCancel() {
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
            t.setID(UUID.randomUUID().toString());
            System.out.println(t.getID());
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

    private void deleteTask(){
        AlertDialog unsavedChangesDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.delete_task_title)
                .setMessage(R.string.delete_task_message)
                .setPositiveButton(R.string.delete_task, new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        app.deleteTask(selectedTask);
                        loadTasks();
                        dialog.dismiss();
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
    }

    private void editCancel() {
        if (changesPending) {
            AlertDialog unsavedChangesDialog = new AlertDialog.Builder(this)
                    .setTitle(R.string.unsaved_changes_title)
                    .setMessage(R.string.unsaved_changes_message)
                    .setPositiveButton(R.string.edit_task, new AlertDialog.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            editTask();
                        }
                    })
                    .setNeutralButton(R.string.discard, new AlertDialog.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.cancel();
                            SesameActivity.this.dialog.cancel();
                            SesameActivity.this.previewDialog();
                        }
                    })
                    .setNegativeButton(R.string.cancel, new AlertDialog.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            SesameActivity.this.previewDialog();
                        }
                    })
                    .create();
            unsavedChangesDialog.show();
        } else {
            dialog.dismiss();
            dialog = new PreviewDialog(this);
            dialog.show();
        }
    }

    private void editDialog() {
        dialog = new EditDialog(this);
        dialog.show();
    }

    private void editTask() {
        if (changesPending) {
            String taskName = taskNameEditText.getText().toString();
            String task = taskEditText.getText().toString();
            String taskLocation = taskLocationEditText.getText().toString();
            String taskPriority = taskPriorityEditText.getText().toString();
            String taskNotes = taskNotesEditText.getText().toString();
            selectedTask.setName(taskName);
            selectedTask.setTask(task);
            selectedTask.setLocation(taskLocation);
            selectedTask.setDate(taskDate);
            selectedTask.setPriority(taskPriority);
            selectedTask.setExtraInfo(taskNotes);
            SesameApplication.saveTask(selectedTask);
            adapter = new TaskListAdapter(this, app.getCurrentTasks());
            setListAdapter(adapter);
            dialog.dismiss();
            previewDialog();
        } else {
            AlertDialog unsavedChangesDialog = new AlertDialog.Builder(this)
                    .setTitle(R.string.not_edit_field_title)
                    .setMessage(R.string.not_edit_field_message)
                    .setPositiveButton(R.string.ok, new AlertDialog.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .create();
            unsavedChangesDialog.show();
        }

    }

    private void loadTasks(){
        app.loadTasks();
        adapter = new TaskListAdapter(this, app.getCurrentTasks());
        setListAdapter(adapter);
    }

    private void previewDialog(){
        dialog = new PreviewDialog(this);
        dialog.show();
    }

    private void priorityChooser(){
        chooser = new PriorityDialog(this);
        chooser.show();
    }

    private void removeCompletedTasks() {
        String[] ids = adapter.removeCompletedTasks();
        app.deleteTasks(ids);
    }

    private void setUpViews() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        FloatingActionButton fab = findViewById(R.id.add_task_fab);
        FloatingActionButton reloadFab = findViewById(R.id.reload_fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDialog();
            }
        });

        reloadFab.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                loadTasks();
            }
        });

        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        app = (SesameApplication) getApplication();
        loadTasks();
    }

    private class AddDialog extends Dialog {

        AddDialog(@NonNull Context context) {
            super(context);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_add);
            setUpViews();
        }

        private void setUpViews() {
            taskNameEditText = findViewById(R.id.add_title);
            taskEditText = findViewById(R.id.add_task);
            taskLocationEditText = findViewById(R.id.add_location);
            btnDatePicker = findViewById(R.id.add_date);
            taskNotesEditText = findViewById(R.id.add_notes);
            taskPriorityEditText = findViewById(R.id.add_priority);
            FloatingActionButton addButton = findViewById(R.id.add_button);
            FloatingActionButton cancelButton = findViewById(R.id.add_cancel);
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
                    addCancel();
                }
            });

            taskPriorityEditText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    priorityChooser();
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

    private class EditDialog extends Dialog {

        EditDialog(@NonNull Context context) {
            super(context);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_add);
            setUpViews();
        }

        private void setUpViews() {
            taskNameEditText = findViewById(R.id.add_title);
            taskEditText = findViewById(R.id.add_task);
            taskLocationEditText = findViewById(R.id.add_location);
            btnDatePicker = findViewById(R.id.add_date);
            taskNotesEditText = findViewById(R.id.add_notes);
            taskPriorityEditText = findViewById(R.id.add_priority);
            FloatingActionButton editButton = findViewById(R.id.add_button);
            FloatingActionButton cancelButton = findViewById(R.id.add_cancel);
            taskNameEditText.setText(selectedTask.getName());
            taskEditText.setText(selectedTask.getTask());
            taskLocationEditText.setText(selectedTask.getLocation());
            btnDatePicker.setText(selectedTask.getDateString());
            taskDate = selectedTask.getDate();
            taskNotesEditText.setText(selectedTask.getExtraInfo());
            taskPriorityEditText.setText(selectedTask.getPriority());
            changesPending = false;

            btnDatePicker.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    datePicker();
                }
            });

            editButton.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    editTask();
                }
            });

            cancelButton.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    editCancel();
                }
            });

            taskPriorityEditText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    priorityChooser();
                }
            });

            taskNameEditText.addTextChangedListener(new TextWatcher() {

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    changesPending = true;
                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                public void afterTextChanged(Editable s) {
                }
            });

            taskEditText.addTextChangedListener(new TextWatcher() {

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    changesPending = true;
                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                public void afterTextChanged(Editable s) {
                }
            });

            taskLocationEditText.addTextChangedListener(new TextWatcher() {

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    changesPending = true;
                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                public void afterTextChanged(Editable s) {
                }
            });

            btnDatePicker.addTextChangedListener(new TextWatcher() {

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    changesPending = true;
                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                public void afterTextChanged(Editable s) {
                }
            });

            taskNotesEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    changesPending = true;
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            taskPriorityEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    changesPending = true;
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
    }

    private class PriorityDialog extends Dialog{

        PriorityDialog(@NonNull Context context) {
            super(context);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_chooser);
            setUpViews();
        }

        private void setUpViews(){
            final TextView low = findViewById(R.id.task_low);
            final TextView medium = findViewById(R.id.task_medium);
            final TextView high = findViewById(R.id.task_high);
            final TextView event = findViewById(R.id.event);

            low.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    taskPriorityEditText.setText(low.getText().toString());
                    chooser.dismiss();
                }
            });

            medium.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    taskPriorityEditText.setText(medium.getText().toString());
                    chooser.dismiss();
                }
            });

            high.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    taskPriorityEditText.setText(high.getText().toString());
                    chooser.dismiss();
                }
            });

            event.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    taskPriorityEditText.setText(event.getText().toString());
                    chooser.dismiss();
                }
            });
        }
    }

    private class PreviewDialog extends Dialog {
        PreviewDialog(Context context) {
            super(context);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_preview);
            setUpViews();
        }

        private void setUpViews() {
            TextView taskNameEditText = findViewById(R.id.preview_title);
            TextView taskEditText = findViewById(R.id.preview_task);
            TextView taskLocationEditText = findViewById(R.id.preview_location);
            TextView btnDatePicker = findViewById(R.id.preview_date);
            TextView taskNotesEditText = findViewById(R.id.preview_notes);
            TextView taskPriorityEditText = findViewById(R.id.preview_priority);
            FloatingActionButton editButton = findViewById(R.id.fab_edit);
            FloatingActionButton deleteButton = findViewById(R.id.fab_delete);
            final FloatingActionButton finishedButton = findViewById(R.id.fab_finished);
            taskNameEditText.setText(selectedTask.getName());
            taskEditText.setText(selectedTask.getTask());
            taskLocationEditText.setText(selectedTask.getLocation());
            btnDatePicker.setText(selectedTask.getDateString());
            taskNotesEditText.setText(selectedTask.getExtraInfo());
            taskPriorityEditText.setText(selectedTask.getPriority());
            if (selectedTask.getPriority().equals("Wydarzenie")){
                finishedButton.setVisibility(View.GONE);
            }else{
                if (selectedTask.isComplete()){
                    finishedButton.setImageResource(R.drawable.checkbox_on_background);
                }else{
                    finishedButton.setImageResource(R.drawable.checkbox_off_background);
                }
            }

            finishedButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    adapter.toggleTaskCompleteAtPosition(selectedPosition);
                    SesameApplication.saveTask(selectedTask);
                    if (selectedTask.isComplete()){
                        finishedButton.setImageResource(R.drawable.checkbox_on_background);
                    }else{
                        finishedButton.setImageResource(R.drawable.checkbox_off_background);
                    }
                }
            });

            editButton.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    dialog.dismiss();
                    editDialog();
                }
            });

            deleteButton.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    dialog.dismiss();
                    deleteTask();
                }
            });
        }
    }
}


