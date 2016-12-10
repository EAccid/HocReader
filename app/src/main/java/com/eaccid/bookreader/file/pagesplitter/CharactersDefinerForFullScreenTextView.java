package com.eaccid.bookreader.file.pagesplitter;

import android.app.Activity;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.widget.TextView;

import com.eaccid.bookreader.exception.NotImplementedException;

public class CharactersDefinerForFullScreenTextView {

    private int charactersOnLine;
    private int charactersOnPage;

    public interface PageView {
        TextView getTextView();
    }

    public CharactersDefinerForFullScreenTextView(Activity activity) {
        setScreenParameters(activity);
    }

    public int getCharactersOnLine() {
        return charactersOnLine;
    }

    public int getCharactersOnPage() {
        return charactersOnPage;
    }

    private void setScreenParameters(Activity activity) {

        TextPaint paint = getTextPaint(activity);

        Paint.FontMetrics paintFontMetrics = paint.getFontMetrics();
        float textSizeHeight = Math.abs(paintFontMetrics.top - paintFontMetrics.bottom);
        float textSizeWidth = paint.measureText("A");

        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int linesOnScreen = (int) (Math.floor((metrics.heightPixels) / textSizeHeight));

        charactersOnLine = (int) (Math.floor((metrics.widthPixels) / textSizeWidth) * 4 / 3);
        charactersOnPage = charactersOnLine * linesOnScreen;

    }

    private TextPaint getTextPaint(Activity activity) {
        TextView textView = getTextView(activity);
        return textView.getPaint();
    }

    private TextView getTextView(Activity activity) {
        try {
            return ((PageView) activity).getTextView();
        } catch (ClassCastException e) {
            e.printStackTrace();
            throw new NotImplementedException("'interface CharactersDefinerForFullScreenTextView.PageView: ' " +
                    "method is not implemented in " + "'" + activity.getPackageName() + activity.getLocalClassName() + "'");
        }
    }

}
