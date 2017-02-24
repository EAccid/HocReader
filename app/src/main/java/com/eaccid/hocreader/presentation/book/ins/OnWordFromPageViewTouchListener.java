package com.eaccid.hocreader.presentation.book.ins;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.eaccid.hocreader.exceptions.NotImplementedException;
import com.eaccid.hocreader.provider.fromtext.WordFromText;
import com.eaccid.hocreader.provider.fromtext.WordFromTextProvider;
import com.eaccid.hocreader.provider.fromtext.WordFromTextProviderImpl;

public class OnWordFromPageViewTouchListener implements View.OnTouchListener {

    private final int pageNumber;

    public interface OnWordFromTextClickListener {
        void onWordClicked(WordFromText wordFromText);
    }

    public OnWordFromPageViewTouchListener(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.AXIS_Y) {
            TextView tv = (TextView) view;
            WordFromTextProvider wordFromTextProvider = new WordFromTextProviderImpl();
            WordFromText wordFromText = wordFromTextProvider.getWordByMotionEvent(tv, motionEvent);
            wordFromText.setPageNumber(pageNumber);
            return !wordFromText.getText().isEmpty()
                    && provideOnWordClick(view.getContext(), wordFromText);
        }
        return true;
    }

    private boolean provideOnWordClick(Context context, WordFromText wordFromText) {
        try {
            ((OnWordFromTextClickListener) context).onWordClicked(wordFromText);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new NotImplementedException("'interface " +
                    "OnWordFromPageViewTouchListener.OnWordFromTextClickListener: ' " +
                    "method is not implemented in " + "'" +
                    context.getPackageName() + "." +
                    context.getClass().getName() + "'");
        }
    }

}
