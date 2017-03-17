package com.eaccid.hocreader.underdevelopment;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.eaccid.hocreader.provider.semantic.SoundPlayer;
import com.ms.square.android.expandabletextview.ExpandableTextView;

public interface WordViewElements {

    TextView word();

    TextView transcription();

    TextView translation();

    ImageView wordImage();

    int defaultImageResId();

    ImageView learnByHeart();

    ImageView alreadyLearned();

    ImageView transcriptionSpeaker();

    SoundPlayer<String> soundPlayer();

    View container();

    @Nullable
    ExpandableTextView context();

}
