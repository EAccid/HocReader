package com.eaccid.hocreader.presentation.training;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.eaccid.hocreader.R;
import com.eaccid.hocreader.presentation.FragmentTags;

public class TrainingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.activity_training, WordTrainingFragment.newInstance(false), FragmentTags.WORDS_CAROUSEL)
                .commit();
    }

}
