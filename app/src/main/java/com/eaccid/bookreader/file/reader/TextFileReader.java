package com.eaccid.bookreader.file.reader;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eaccid.bookreader.activity.PagerActivity;
import com.eaccid.bookreader.R;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class TextFileReader extends ContextWrapper {

    private int pageLinesOnScreen;
    private String filePath;
    private TextPaint paint;
    private DisplayMetrics metrics;
    private ArrayList<String> pagesList = new ArrayList<String>();
    int currentChar = 0;
    boolean isLastPageRead = false;

    public TextFileReader(Context context, String filePath) {
        super(context);
        this.filePath = filePath;
        setScreenParameters();

        //TODO save and load current char
        loadPages();
    }

    private void setScreenParameters() {

        float verticalMargin = getBaseContext().getResources().getDimension(R.dimen.activity_vertical_margin) * 2;
        PagerActivity pagerActivity = (PagerActivity) getBaseContext();
        LayoutInflater inflater = pagerActivity.getLayoutInflater();

        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_page,
                (ViewGroup) pagerActivity.getWindow().getDecorView().findViewById(android.R.id.content), false);
        TextView textOnPage = (TextView) viewGroup.findViewById(R.id.text);

        paint = textOnPage.getPaint();
        Paint.FontMetrics paintFontMetrics = paint.getFontMetrics();
        float textSizeHeight = Math.abs(paintFontMetrics.top - paintFontMetrics.bottom);

        metrics = new DisplayMetrics();
        pagerActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        pageLinesOnScreen = (int) (Math.floor((metrics.heightPixels - verticalMargin) / textSizeHeight));

    }

    private void loadPages() {

        //TODO remove comment
        while (!isLastPageRead) {
//        int i = 0;
//        while (!isLastPageRead && i < 10) {
            pagesList.add(getPage());
//            i++;
        }
    }


    //TODO read to slow
    private String getPage() {

        StringBuilder sb = new StringBuilder();
        int lineCountOnScreen = 0;

        while (lineCountOnScreen < pageLinesOnScreen) {
            String lineFromFile = readLineFromFile();
            if (isLastPageRead) break;
            int numChars = 0;

            while ((lineCountOnScreen < pageLinesOnScreen) && (numChars < lineFromFile.length())) {
                numChars = numChars + paint.breakText(lineFromFile.substring(numChars), true, metrics.widthPixels, null);
                lineCountOnScreen++;
            }

            String displayedText = lineFromFile.substring(0, numChars);
            char nextChar = numChars < lineFromFile.length() ? lineFromFile.charAt(numChars) : ' ';
            if (!Character.isWhitespace(nextChar)) {
                displayedText = displayedText.substring(0, displayedText.lastIndexOf(" "));
            }

            currentChar = ++currentChar + displayedText.length();
            sb.append(displayedText.trim());
            if (!displayedText.trim().isEmpty()) {
                sb.append("\n\n");
                lineCountOnScreen++;
            }

        }
        return sb.toString();

    }

    private String readLineFromFile() {
        String line = null;
        BufferedReader bufferedReader = null;
        try {
//            bufferedReader = new BufferedReader(new FileReader(filePath));
            //"Windows-1251"
            //"ISO-8859-1"
            //"UTF-8"
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "Windows-1251"));
            bufferedReader.skip(currentChar);
            line = bufferedReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (Exception ignore) {
                }
            }
        }
        isLastPageRead = line == null;
        return line;
    }


    public ArrayList<String> getPages() {
        return pagesList;
    }

    public int getPagesAmount() {
//        int pagesAmount = 0;
//
//        try {
//            LineNumberReader lineNumberReader = new LineNumberReader(new FileReader(filePath));
//            lineNumberReader.skip(Long.MAX_VALUE);
//            pagesAmount = (int) (Math.ceil( (lineNumberReader.getLineNumber() + 1) / pageLinesOnScreen));
//            lineNumberReader.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        //TODO account total pages
//        return pagesAmount;
        return pagesList.size();
    }

}