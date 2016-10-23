package com.eaccid.bookreader.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.eaccid.bookreader.wordgetter.OnWordTouchListener;
import com.eaccid.bookreader.R;

import java.util.List;


public class PagesAdapter extends ArrayAdapter<String> {

    private Context mContext;
    private int mTextOnPage;
    private int viewItemLayout;
    private List<String> mPagesList;
    private List<String> mPagesListAll;



    public PagesAdapter(Context context, int textViewResourceId, List<String> pagesList) {
        super(context, R.layout.text_on_page, textViewResourceId, pagesList);
        this.mContext = context;
        this.mTextOnPage = textViewResourceId;
        this.viewItemLayout = R.layout.text_on_page;
        this.mPagesList = pagesList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolderItem viewHolderItem;

        if (convertView == null) {

            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(viewItemLayout, parent, false);


            viewHolderItem = new ViewHolderItem();
            viewHolderItem.textViewItem = (TextView) convertView.findViewById(mTextOnPage);
            viewHolderItem.pageNumber = (TextView) convertView.findViewById(R.id.page_number);

            viewHolderItem.textViewItem.setOnTouchListener(new OnWordTouchListener(position + 1));

            convertView.setTag(viewHolderItem);

        } else {
            viewHolderItem = (ViewHolderItem) convertView.getTag();
        }

        String textOnPage = mPagesList.get(position);
        viewHolderItem.textViewItem.setText(textOnPage);
        viewHolderItem.pageNumber.setText(
                String.valueOf(position + 1) +
                        " - " +
                        String.valueOf(mPagesList.size())
        );

        return convertView;

    }

    private static class ViewHolderItem {
        TextView textViewItem;
        TextView pageNumber;
    }


}





