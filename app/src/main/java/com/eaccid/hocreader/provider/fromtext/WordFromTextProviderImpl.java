package com.eaccid.hocreader.provider.fromtext;

import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.view.MotionEvent;
import android.widget.TextView;

import com.eaccid.hocreader.provider.fromtext.ins.TextManagerImpl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordFromTextProviderImpl implements WordFromTextProvider {

    private int charStart = 0;

    @Override
    public WordFromText getWordByMotionEvent(TextView tv, MotionEvent event) {
        WordFromTextImpl wordFromText = new WordFromTextImpl();
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
            wordFromText.setText(new TextManagerImpl().capitalizeFirsChar(wordFromLine));
            wordFromText.setSentence(
                    getSentenceFromText(
                            (text.toString()).replace("\n\n", " ").trim(),
                                text.subSequence(startOfLine, endOfLine).toString()
                    )
            );
            Spannable spanText = new SpannableString(text);
            spanText.setSpan(new WordClickableSpan(),
                    charStart, charStart + wordFromLine.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tv.setText(spanText, TextView.BufferType.SPANNABLE);
        }
        return wordFromText;
    }

    private String getWordFromLine(CharSequence text, int startOfLine, int offset, int endOfLine) {
        String firstPartOfWord = getFirstPartOfWordInLine(text.subSequence(startOfLine, offset));
        String lastPartOfWord = getLastPartOfWordInLine(text.subSequence(offset, endOfLine));

        charStart = offset - firstPartOfWord.length();

        return firstPartOfWord + lastPartOfWord;
    }

    private String getFirstPartOfWordInLine(CharSequence sublineBeforeClickedChar) {
        return getMatchingResult(sublineBeforeClickedChar, Pattern.compile("(\\w+)$"));
    }

    private String getLastPartOfWordInLine(CharSequence sublineAfterClickedChar) {
        return getMatchingResult(sublineAfterClickedChar, Pattern.compile("^([\\w\\-]+)"));
    }

    private String getSentenceFromText(CharSequence text, CharSequence subtext) {
        return getMatchingResult(text,
                Pattern.compile("(?<=\\.)(\\w|\\s|,|'|`|-|–|\")+" + subtext + ".+?(\\.)"));
    }

    //todo take out in separate class
    private String getMatchingResult(CharSequence line, Pattern pattern) {
        Matcher matcher = pattern.matcher(line);
        try {
            if (matcher.find()) {
                return matcher.group(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}
