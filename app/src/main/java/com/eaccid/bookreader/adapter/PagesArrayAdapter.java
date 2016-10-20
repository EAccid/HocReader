package com.eaccid.bookreader.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.eaccid.bookreader.wordgetter.WordFromText;
import com.eaccid.bookreader.R;
import com.eaccid.bookreader.WordTranslatorViewer;
import com.eaccid.bookreader.wordgetter.WordOnTexvViewFinder;

import java.util.List;


public class PagesArrayAdapter extends ArrayAdapter{

    private Context mContext;
    private int mTextOnPage;
    private List<String> mPagesList;

    public PagesArrayAdapter(Context context, int textViewResourceId, List<String> pagesList) {
        super(context, textViewResourceId, pagesList);
        this.mContext = context;
        this.mTextOnPage = textViewResourceId;
        this.mPagesList = pagesList;
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

                    WordFromText wordFromText = WordOnTexvViewFinder.getWordByMotionEvent((TextView) v, event);

                    WordTranslatorViewer wordTranslatorViewer = new WordTranslatorViewer(mContext);
                    wordTranslatorViewer.showTranslationView(wordFromText);

                    return false;
                }
            });

            convertView.setTag(viewHolderItem);

        } else {
            viewHolderItem = (ViewHolderItem) convertView.getTag();
        }

        String textOnPage = mPagesList.get(position);
        viewHolderItem.textViewItem.setText(textOnPage);

        return convertView;

    }

    private static class ViewHolderItem {
        TextView textViewItem;
    }

}





