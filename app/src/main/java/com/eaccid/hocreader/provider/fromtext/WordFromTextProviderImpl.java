package com.eaccid.hocreader.provider.fromtext;

import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.view.MotionEvent;
import android.widget.TextView;

import com.eaccid.hocreader.provider.fromtext.ins.TextManagerImpl;

public class WordFromTextProviderImpl implements WordFromTextProvider {

    @Override
    public WordFromText getWordByMotionEvent(TextView tv, MotionEvent event) {
        WordFromTextImpl wordFromText = new WordFromTextImpl();
        Layout textLayout = tv.getLayout();
        if (textLayout == null)
            return wordFromText;
        int x = (int) event.getX();
        int y = (int) event.getY();
        int currentLineOfClickedChar = textLayout.getLineForVertical(y);
        int charOffsetInLine = textLayout.getOffsetForHorizontal(currentLineOfClickedChar, x);
        int startOfLine = textLayout.getLineStart(currentLineOfClickedChar);
        int endOfLine = textLayout.getLineEnd(currentLineOfClickedChar);
        CharSequence text = tv.getText();
        WordInLineParser textParser = new WordInLineParser(text.toString(), startOfLine, charOffsetInLine, endOfLine);
        textParser.parse();
        String wordFromLine = textParser.getWord();
        wordFromText.setText(new TextManagerImpl().capitalizeFirsChar(wordFromLine));
        wordFromText.setSentence(textParser.getSentence());
        Spannable spanText = new SpannableString(text);
        spanText.setSpan(new WordClickableSpan(),
                textParser.getWordStart(), textParser.getWordEnd(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(spanText, TextView.BufferType.SPANNABLE);
        return wordFromText;
    }

}
