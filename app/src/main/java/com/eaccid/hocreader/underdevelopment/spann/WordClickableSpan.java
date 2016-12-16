package com.eaccid.hocreader.underdevelopment.spann;


import android.content.Context;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Toast;

public class WordClickableSpan extends ClickableSpan {

    private Context context;
    private String word ="";
    private int charNumberOnPage = 0;
    private boolean isClicked = false;

    public WordClickableSpan(Context context, String word, int currentCharOnPage) {
        super();
        this.context = context;
        this.word = word;
        this.charNumberOnPage = currentCharOnPage;
    }

    @Override
    public void onClick(View widget) {
        Toast.makeText(context, word, Toast.LENGTH_SHORT).show();
        isClicked = true;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
//        ds.setUnderlineText(false);
    }

}
