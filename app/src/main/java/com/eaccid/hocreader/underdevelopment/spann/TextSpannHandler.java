package com.eaccid.hocreader.underdevelopment.spann;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import java.util.Scanner;

public class TextSpannHandler {

    public static void deleteClickableWordsOnTextView(TextView tv) {
        if (tv == null) return;
        tv.setText(tv.getText().toString());
        /**
         SpannableString ss = getYourSpannableString();
         UnderlineSpan[] uspans = ss.getSpans(0, ss.length(), UnderlineSpan.class);
         for (UnderlineSpan us : uspans) {
         ss.removeSpan(us);
         }*/
    }

    public static void setClickableWordsOnTextView(Context context, TextView tv) {
        if (tv == null) return;
        String text = tv.getText().toString();
        Scanner sc = new Scanner(text);
        Spannable spanText = new SpannableString(text);
        int currentCharInText = 0;
        while (sc.hasNextLine()) {
            int currentCharInLine = currentCharInText;
            String line = sc.nextLine();
            for (String word : line.split(" ")) {

                if (word.length() > 0 && Character.isLetter(word.charAt(0))) {
                    try {
                        spanText.setSpan(new WordClickableSpan(), 0, currentCharInLine + word.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                currentCharInLine = ++currentCharInLine + word.length();
            }
            currentCharInText = currentCharInLine;
        }
        sc.close();
        tv.setText(spanText, TextView.BufferType.SPANNABLE);
        tv.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
