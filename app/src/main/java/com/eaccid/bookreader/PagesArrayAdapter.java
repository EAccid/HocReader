package com.eaccid.bookreader;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import java.util.List;


public class PagesArrayAdapter extends ArrayAdapter {

    private Context mContext;
    private int mTextOnPage;
    private List<String> mPagesList;
    private List<SpannableString> mSpannablePagesList;

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

            convertView.setTag(viewHolderItem);

        } else {
            viewHolderItem = (ViewHolderItem) convertView.getTag();
        }

        String textOnPage = mPagesList.get(position);
        viewHolderItem.textViewItem.setText(textOnPage);

        TextSpannHandler.setClickableWordsOnTextView(mContext,  viewHolderItem.textViewItem);

        return convertView;

    }


    static class ViewHolderItem {
        TextView textViewItem;
    }


}





