package com.eaccid.hocreader.provider.fromtext;

import android.view.MotionEvent;
import android.widget.TextView;

public interface WordFromTextProvider {
    WordFromText getWordByMotionEvent(TextView tv, MotionEvent event);
}
