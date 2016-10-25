package com.eaccid.bookreader.aaaDEL;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eaccid.bookreader.R;
import com.eaccid.bookreader.db.entity.Word;
import com.j256.ormlite.android.apptools.OrmLiteCursorAdapter;

public class WordsOrmLiteCursorAdapter extends OrmLiteCursorAdapter {

    public WordsOrmLiteCursorAdapter(Context context) {
        super(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {

//        LayoutInflater inflater = LayoutInflater.from(context);
//        View v = inflater.inflate(R.layout.word_item, viewGroup, false);
//        return v;
        return null;
    }

    @Override
    public void bindView(View itemView, Context context, Object item) {
//        Word word = (Word) item;
//
//        TextView tvWord = (TextView) itemView.findViewById(R.id.word);
//        TextView tvTranslation = (TextView) itemView.findViewById(R.id.translation);
//
//        tvWord.setText(word.getWord());
//        tvTranslation.setText(word.getTranslation());
    }
}
