package com.mxmbro.sesame;

import android.app.Activity;

public class TaskManagerActivity extends Activity {
    protected TaskManagerApplication getStuffApplication() {
        return (TaskManagerApplication)getApplication();
    }
}
