package com.eaccid.hocreader.temp.underdevelopment.spann;

import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

public class WordClickableSpan extends ClickableSpan {

    @Override
    public void onClick(View widget) {

    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setColor(Color.parseColor("#7C4DFF"));
        ds.setUnderlineText(false);
    }

}
