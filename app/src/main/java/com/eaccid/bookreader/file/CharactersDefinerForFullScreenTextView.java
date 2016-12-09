package com.eaccid.bookreader.file;

import android.app.Activity;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import com.eaccid.bookreader.R;

public class CharactersDefinerForFullScreenTextView {

    private int charactersOnLine;
    private int charactersOnPage;

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

        LayoutInflater inflater = activity.getLayoutInflater();
        TextPaint paint = getTextPaint(inflater);

        Paint.FontMetrics paintFontMetrics = paint.getFontMetrics();
        float textSizeHeight = Math.abs(paintFontMetrics.top - paintFontMetrics.bottom);
        float textSizeWidth = paint.measureText("A");

        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int linesOnScreen = (int) (Math.floor((metrics.heightPixels) / textSizeHeight));

        charactersOnLine = (int) (Math.floor((metrics.widthPixels) / textSizeWidth) * 4 / 3);
        charactersOnPage = charactersOnLine * linesOnScreen;

    }

    private TextPaint getTextPaint(LayoutInflater inflater) {
        TextView textView = getTextView(inflater);
        return textView.getPaint();
    }

    private TextView getTextView(LayoutInflater inflater) {
        return PageTextViewFactory.getTextView(inflater);
    }

    private void setDefaultScreenParametersIncludeMargin(Activity activity) {

        float verticalMargin = activity.getResources().getDimension(R.dimen.activity_vertical_margin) * 2;
        float horizontalMargin = activity.getResources().getDimension(R.dimen.activity_horizontal_margin) * 2;

        LayoutInflater inflater = activity.getLayoutInflater();

        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.book_page_text_default, null, false);
        TextView textOnPage = (TextView) viewGroup.findViewById(R.id.text_on_page);

        TextPaint paint = textOnPage.getPaint();
        Paint.FontMetrics paintFontMetrics = paint.getFontMetrics();
        float textSizeHeight = Math.abs(paintFontMetrics.top - paintFontMetrics.bottom);

        DisplayMetrics metrics = new DisplayMetrics();

        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int linesOnScreen = (int) (Math.floor((metrics.heightPixels - verticalMargin) / textSizeHeight));

        float textSizeWidth = paint.measureText("A");

        charactersOnLine = (int) (Math.floor((metrics.widthPixels - horizontalMargin) / textSizeWidth) * 4 / 3);
        charactersOnPage = charactersOnLine * linesOnScreen;

    }

}
