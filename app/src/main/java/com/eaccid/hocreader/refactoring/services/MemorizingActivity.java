package com.eaccid.hocreader.refactoring.services;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.eaccid.hocreader.R;

public class MemorizingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_memorizing);
        setFinishOnTouchOutside(true);

        String word = getIntent().getStringExtra("word");
        String translation = getIntent().getStringExtra("translation");

        TextView tv_word = (TextView) findViewById(R.id.word);
        TextView tv_translation = (TextView) findViewById(R.id.translation);

        tv_word.setText(word);
        tv_translation.setText(translation);

    }
}
