package com.eaccid.bookreader.wordview;

import android.text.Layout;
import android.view.MotionEvent;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordOnTexvViewFinder {

    public static Word getWordByMotionEvent(TextView tv, MotionEvent event) {

        Word word = new Word();

        Layout textLayout = tv.getLayout();
        int x = (int) event.getX();
        int y = (int) event.getY();

        if (textLayout != null) {

            int currentLineOfClickedChar = textLayout.getLineForVertical(y);
            int charOffsetInLine = textLayout.getOffsetForHorizontal(currentLineOfClickedChar, x);
            int startOfLine = textLayout.getLineStart(currentLineOfClickedChar);
            int endOfLine = textLayout.getLineEnd(currentLineOfClickedChar);

            CharSequence text = tv.getText();

            String wordFromLine = getWordFromLine(text, startOfLine, charOffsetInLine, endOfLine);
            word.setText(wordFromLine);
//            word.setSentance(
//                    getSentenceFromText(
//                            text, text.subSequence(startOfLine, endOfLine).toString()));

            word.setSentance(text.subSequence(startOfLine, endOfLine).toString());
        }

        return word;
    }

    private static String getWordFromLine(CharSequence text, int startOfLine, int offset, int endOfLine) {

        String firstPartOfWord = "";
        String lastPartOfWord = "";

        firstPartOfWord = getFirstPartOfWordInLine(text.subSequence(startOfLine, offset));
        lastPartOfWord = getLastPartOfWordInLine(text.subSequence(offset, endOfLine));

        return firstPartOfWord + lastPartOfWord;

    }

    private static String getFirstPartOfWordInLine(CharSequence sublineBeforeClickedChar) {
        return getMatchingResult(sublineBeforeClickedChar, Pattern.compile("(\\w+)$"));
    }

    private static String getLastPartOfWordInLine(CharSequence sublineAfterClickedChar) {
        return getMatchingResult(sublineAfterClickedChar, Pattern.compile("^([\\w\\-]+)"));
    }

    private static String getSentenceFromText(CharSequence text, CharSequence subtext) {

        //TODO does not work matcher.group(1) W/System.err: java.lang.ArrayIndexOutOfBoundsException: length=2; index=2
        return getMatchingResult(text, Pattern.compile("[A-Za-z,\" ]+" + subtext + "[A-Za-z,\" ]+"));

    }

    private static String getMatchingResult(CharSequence line, Pattern pattern) {
        Matcher matcher = pattern.matcher(line);
        try {
            if (matcher.find()) {
                return matcher.group(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
