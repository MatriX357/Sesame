package com.mxmbro.sesame;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public class ViewPreference extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_set_password);
    }


}
