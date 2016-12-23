package com.eaccid.hocreader.presentation.activity.settings;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import com.eaccid.hocreader.R;

public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
