package com.eaccid.hocreader.presentation.book;

import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.eaccid.hocreader.exceptions.NotImplementedException;
import com.eaccid.hocreader.provider.fromtext.WordFromText;
import com.eaccid.hocreader.provider.fromtext.WordFromTextImpl;
import com.eaccid.hocreader.provider.fromtext.WordFromTextProvider;
import com.eaccid.hocreader.provider.fromtext.WordFromTextProviderImpl;

public class OnWordFromPageViewTouchListener implements View.OnTouchListener{

    private int pageNumber;

    public interface OnWordFromTextClickListener {
        void onWordClicked(WordFromText wordFromText);
    }

    public OnWordFromPageViewTouchListener(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.AXIS_Y:
                TextView tv = (TextView) view;
                WordFromTextProvider wordFromTextProvider = new WordFromTextProviderImpl();
                WordFromText wordFromText = wordFromTextProvider.getWordByMotionEvent(tv, motionEvent);
                wordFromText.setPageNumber(pageNumber);
                if (!wordFromText.getText().isEmpty()) {
                    try {
                        ((OnWordFromTextClickListener) view.getContext()).onWordClicked(wordFromText);
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new NotImplementedException("'interface " +
                                "OnWordFromPageViewTouchListener.OnWordFromTextClickListener: ' " +
                                "method is not implemented in " + "'" +
                                view.getContext().getPackageName() + "." +
                                view.getContext().getClass().getName() + "'");
                    }
                }
        }
        return true;
    }

}
