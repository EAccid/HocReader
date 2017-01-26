package com.eaccid.hocreader.temp.presentation.activity.training;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.eaccid.hocreader.R;
import com.eaccid.hocreader.temp.presentation.fragment.FragmentTags;
import com.eaccid.hocreader.temp.presentation.fragment.carousel.WordsCarouselFragment;

public class TrainingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.activity_training, WordsCarouselFragment.newInstance(false), FragmentTags.WORDS_CAROUSEL)
                .commit();
    }
}
