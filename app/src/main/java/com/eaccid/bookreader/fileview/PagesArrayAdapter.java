package com.eaccid.bookreader.fileview;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Layout;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.eaccid.bookreader.R;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class PagesArrayAdapter extends ArrayAdapter {

    private Context mContext;
    private int mTextOnPage;
    private List<String> mPagesList;
    private int currentItem = 0;

    public PagesArrayAdapter(Context context, int textViewResourceId, List<String> textPagesList) {
        super(context, textViewResourceId, textPagesList);
        this.mContext = context;
        this.mTextOnPage = textViewResourceId;
        this.mPagesList = textPagesList;
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolderItem viewHolderItem;

        if (convertView == null) {

            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(mTextOnPage, parent, false);


            viewHolderItem = new ViewHolderItem();
            viewHolderItem.textViewItem = (TextView) convertView.findViewById(R.id.text_on_page);


            viewHolderItem.textViewItem.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    TextView tv = (TextView) v;

                    Layout layout = tv.getLayout();

                    int x = (int) event.getX();
                    int y = (int) event.getY();

                    if (layout != null) {

                        int line = layout.getLineForVertical(y);
                        int offset = layout.getOffsetForHorizontal(line, x);


                        int startOfLine = layout.getLineStart(line);
                        int endOfLine = layout.getLineEnd(line);


                        Pattern patternWordStart = Pattern.compile("(\\w+)$");
                        Pattern patternWordEnd = Pattern.compile("^([\\w\\-]+)");

                        String firstPartOfWord = "";
                        String lastPartOfWord = "";

                        Matcher matcher = patternWordStart.matcher(tv.getText().subSequence(startOfLine, offset));
                        if (matcher.find()) {
                            firstPartOfWord = matcher.group(1);
                        }

                        matcher = patternWordEnd.matcher(tv.getText().subSequence(offset, endOfLine));
                        if (matcher.find()) {
                            lastPartOfWord = matcher.group(1);
                        }

                        System.out.println(firstPartOfWord + lastPartOfWord);

                    }

                    return false;
                }
            });


            convertView.setTag(viewHolderItem);

        } else {
            viewHolderItem = (ViewHolderItem) convertView.getTag();
        }

        String textOnPage = mPagesList.get(position);
        viewHolderItem.textViewItem.setText(textOnPage);

//        TextSpannHandler.setClickableWordsOnTextView(mContext,  viewHolderItem.textViewItem);

        return convertView;

    }

//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        currentItem = position;
//    }

    static class ViewHolderItem {
        TextView textViewItem;

        }

//    public int getCurrentItem() {
//        return currentItem;
//    }
}





