package com.eaccid.hocreader.temp.presentation.fragment.settings;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import com.eaccid.hocreader.R;
import com.eaccid.hocreader.temp.presentation.service.OnAlarmManagerScheduleListener;

public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        bindPreferenceSummaryToValue(
                findPreference(
                        getString(
                                R.string.notifications_frequency_key)
                )
        );
        bindPreferenceSummaryToValue(
                findPreference(
                        getString(
                                R.string.pref_leo_sign_in_key)
                )
        );
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object objectValue) {
        if (preference == null) return false;
        updatePreferencesSummary(preference, objectValue);
        updatePreferencesValue(preference, objectValue);
        return true;
    }

    private void bindPreferenceSummaryToValue(Preference preference) {
        preference.setOnPreferenceChangeListener(this);
        updatePreferencesSummary(preference, PreferenceManager
                .getDefaultSharedPreferences(preference.getContext())
                .getString(preference.getKey(), getString(R.string.default_pref_value)));
    }

    private void updatePreferencesSummary(Preference preference, Object value) {
        String stringValue = value.toString();
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

    private void updatePreferencesValue(Preference preference, Object value) {
        String stringValue = value.toString();
        String notificationsFrequency = getString(R.string.notifications_frequency_key);
        if (preference.getKey().equals(notificationsFrequency)
                ) {
            long interval = Long.valueOf(stringValue) * 60 * 1000;
            ((OnAlarmManagerScheduleListener) preference.getContext()).onSchedule(Math.max(-1, interval));
        }
    }
}
