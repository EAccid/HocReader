package com.eaccid.bookreader.file;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eaccid.bookreader.R;

public class PageTextViewFactory {

    private PageTextViewFactory() {
    }

    public static TextView getTextView(LayoutInflater inflater) {
        //TODO settings: depends on user preferences

        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.book_page_text_default, null, false);
        TextView textView = (TextView) viewGroup.findViewById(R.id.text_on_page);
        return textView;
    }
}
