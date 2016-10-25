package com.eaccid.bookreader.aaaDEL;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.eaccid.bookreader.R;

public class WordsCursorAdapter extends CursorAdapter {

    Cursor cursor;

    @SuppressWarnings("unchecked")
    public WordsCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
//        return LayoutInflater.from(context).inflate(R.layout.word_item, viewGroup, false);
        return null;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

//        TextView tvWord = (TextView) view.findViewById(R.id.word);
//        TextView tvTranslation = (TextView) view.findViewById(R.id.translation);
//
//        tvWord.setText(cursor.getString(cursor.getColumnIndexOrThrow("word")));
//        tvTranslation.setText(cursor.getString(cursor.getColumnIndexOrThrow("translation")));

    }

}

//
//    @Override
//    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
//
//        LayoutInflater inflater = LayoutInflater.from(context);
//        View v = inflater.inflate(R.layout.word_item, viewGroup, false);
//        return v;
//
//    }


//
//    @Override
//    public void bindView(View itemView, Context context, Object item) {
//        Word word = (Word) item;
//
//        TextView tvWord = (TextView) itemView.findViewById(R.id.word);
//        TextView tvTranslation = (TextView) itemView.findViewById(R.id.translation);
//
//        tvWord.setText(word.getWord());
//        tvTranslation.setText(word.getTranslation());
//    }
//}
