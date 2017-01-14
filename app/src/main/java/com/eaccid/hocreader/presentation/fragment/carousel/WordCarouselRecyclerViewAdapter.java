package com.eaccid.hocreader.presentation.fragment.carousel;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.eaccid.hocreader.R;
import com.eaccid.hocreader.data.local.db.entity.Word;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WordCarouselRecyclerViewAdapter extends OrmliteCursorRecyclerViewAdapter<Word, WordCarouselRecyclerViewAdapter.ViewHolder> {

    public WordCarouselRecyclerViewAdapter(Context context) {
        super(context);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.word)
        TextView word;
        @BindView(R.id.word_transcription)
        TextView transcription;
        @BindView(R.id.translation)
        TextView translation;

        public ViewHolder(View drawerView) {
            super(drawerView);
            ButterKnife.bind(this, drawerView);
        }
    }

    @Override
    public WordCarouselRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.word_item_fragment_2, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, Word word) {
        holder.word.setText(word.getName());
        holder.translation.setText(word.getTranslation());
//        holder.transcription.setText();
    }

}
