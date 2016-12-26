package com.eaccid.hocreader.presentation.activity.settings;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.eaccid.hocreader.presentation.fragment.settings.SettingsFragment;


public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }

}
