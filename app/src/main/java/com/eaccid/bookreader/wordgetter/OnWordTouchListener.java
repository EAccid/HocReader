package com.eaccid.bookreader.wordgetter;

import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.eaccid.bookreader.WordTranslatorViewer;
import com.eaccid.bookreader.dev.AppDatabaseManager;

public class OnWordTouchListener implements View.OnTouchListener {

    public OnWordTouchListener(int pageNumber) {
        AppDatabaseManager.setCurrentPageForAddingWord(pageNumber);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        switch (motionEvent.getAction()) {
            case MotionEvent.AXIS_Y:
                TextView tv = (TextView) view;
                WordFromText wordFromText = WordOnTexvViewFinder.getWordByMotionEvent(tv, motionEvent);
                if (!wordFromText.getText().isEmpty()) {
                    WordTranslatorViewer wordTranslatorViewer = new WordTranslatorViewer(view.getContext());
                    wordTranslatorViewer.showTranslationView(wordFromText);
                }
        }
        return true;
    }


}
