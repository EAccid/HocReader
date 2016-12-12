package com.eaccid.hocreader.presentation.fragment.carousel;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eaccid.hocreader.R;
import com.eaccid.hocreader.data.local.db.entity.Word;

public class WordCarouselRecyclerViewAdapter extends OrmliteCursorRecyclerViewAdapter<Word, WordCarouselRecyclerViewAdapter.ViewHolder> {

    public WordCarouselRecyclerViewAdapter(Context context) {
        super(context);
    }

    @Override
    public WordCarouselRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.word_item_fragment_2, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, Word word) {
        holder.textView.setText(word.getName());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView textView;

        public ViewHolder(View drawerView) {
            super(drawerView);
            textView = (TextView) drawerView.findViewById(R.id.word);
        }
    }
}
