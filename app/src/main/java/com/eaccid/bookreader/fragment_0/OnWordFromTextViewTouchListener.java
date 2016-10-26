package com.eaccid.bookreader.fragment_0;

import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.eaccid.bookreader.wordgetter.WordFromText;
import com.eaccid.bookreader.wordgetter.WordOnTexvViewFinder;

public class OnWordFromTextViewTouchListener implements View.OnTouchListener {

    private int pageNumber ;

    public interface OnWordFromTextClickListener {
        void OnWordClicked(WordFromText wordFromText, int position);
    }

    public OnWordFromTextViewTouchListener(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        switch (motionEvent.getAction()) {
            case MotionEvent.AXIS_Y:
                TextView tv = (TextView) view;
                WordFromText wordFromText = WordOnTexvViewFinder.getWordByMotionEvent(tv, motionEvent);
                if (!wordFromText.getText().isEmpty()) {

                    ((OnWordFromTextClickListener) view.getContext()).OnWordClicked(wordFromText, pageNumber);
                }
        }
        return true;
    }

}
