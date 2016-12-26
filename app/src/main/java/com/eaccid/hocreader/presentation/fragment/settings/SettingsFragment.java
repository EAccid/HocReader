package com.eaccid.hocreader.presentation.fragment.settings;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import com.eaccid.hocreader.R;

public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        bindPreferenceSummaryToValue(findPreference("notifications_frequency"));
        bindPreferenceSummaryToValue(findPreference("pref_leo_sign_in"));
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object o) {
        updatePreference(preference, o);
        return true;
    }

    private void updatePreference(Preference preference, Object value) {
        String stringValue = value.toString();
        if (preference == null) return;
        if (preference instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) preference;
            int index = listPreference.findIndexOfValue(stringValue);
            preference.setSummary(
                    index >= 0
                            ? listPreference.getEntries()[index]
                            : null);
        } else {
            preference.setSummary(stringValue);
        }
    }

    private void bindPreferenceSummaryToValue(Preference preference) {
        preference.setOnPreferenceChangeListener(this);
        updatePreference(preference,PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }
}
