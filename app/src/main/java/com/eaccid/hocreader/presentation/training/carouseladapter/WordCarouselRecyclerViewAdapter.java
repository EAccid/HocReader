package com.eaccid.hocreader.presentation.training.carouseladapter;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.eaccid.hocreader.R;
import com.eaccid.hocreader.data.local.db.entity.Word;
import com.eaccid.hocreader.provider.semantic.ImageViewManager;
import com.eaccid.hocreader.provider.semantic.MediaPlayerManager;
import com.eaccid.hocreader.provider.NetworkAvailablenessImpl;
import com.eaccid.hocreader.provider.db.words.WordItemProvider;
import com.eaccid.hocreader.underdevelopment.MemorizingCalculatorImpl;
import com.eaccid.hocreader.underdevelopment.ReaderExceptionHandlerImpl;
import com.eaccid.hocreader.presentation.weditor.IconTogglesResourcesProvider;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;

public class WordCarouselRecyclerViewAdapter extends OrmLiteCursorRecyclerViewAdapter<Word, WordCarouselRecyclerViewAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.word)
        TextView word;
        @BindView(R.id.word_transcription)
        TextView transcription;
        @BindView(R.id.translation)
        TextView translation;
        @BindView(R.id.word_image)
        ImageView wordImage;
        @BindView(R.id.action_delete)
        ImageView deleteWord;
        @BindView(R.id.already_learned)
        ImageView alreadyLearned;
        @BindView(R.id.transcription_speaker)
        ImageView transcriptionSpeaker;
        MediaPlayer mediaPlayer;
        @BindView(R.id.don_t_know)
        Button dontKnow;
        @BindView(R.id.remember)
        Button remember;

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
        setDataToViewFromItem(holder, word);
        setListenersToViewFromItem(holder, word);
    }

    public String getCurrentContext(int position) {
        String context = "";
        try {
            Word word = getTypedItem(position);
            context = word.getContext();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return context;
    }

    private void setDataToViewFromItem(WordCarouselRecyclerViewAdapter.ViewHolder holder, Word word) {
        if (!new NetworkAvailablenessImpl().isNetworkAvailable()) {
            holder.word.setText(word.getName());
            return;
        }
        new WordItemProvider()
                .getWordItemWithTranslation(word)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(wordItem -> {
                            holder.word.setText(wordItem.getWordFromText());
                            new ImageViewManager().loadPictureFromUrl(
                                    holder.wordImage,
                                    wordItem.getPictureUrl(),
                                    R.drawable.empty_circle_background,
                                    R.drawable.empty_circle_background);
                            holder.translation.setText("*?");
                            holder.transcription.setText("[" + wordItem.getTranscription() + "]");
                            holder.alreadyLearned.setImageResource(
                                    new IconTogglesResourcesProvider().getAlreadyLearnedWordResId(
                                            new MemorizingCalculatorImpl(wordItem)
                                    )
                            );
                            if (holder.mediaPlayer != null) //delete, after todo release method in MediaPlayerManager
                                holder.mediaPlayer.release();
                            holder.mediaPlayer = new MediaPlayerManager().createAndPreparePlayerFromURL(wordItem.getSoundUrl());

                        }, e -> {
                            new ReaderExceptionHandlerImpl().handleError(e);

                        }
                );
    }

    //TODO on click listeners handler (carousel)
    private void setListenersToViewFromItem(ViewHolder holder, Word word) {
        holder.deleteWord.setOnClickListener(v -> {
        });
        holder.dontKnow.setOnClickListener(v -> {
            holder.translation.setText(word.getTranslation());
            holder.translation.setTextColor(Color.parseColor("#fff45f30"));
        });
        holder.remember.setOnClickListener(v -> {
            holder.translation.setText(word.getTranslation());
            holder.translation.setTextColor(Color.parseColor("#ff2f8b2a"));
        });
        holder.transcriptionSpeaker.setOnClickListener(v -> {
        });
        holder.transcriptionSpeaker.setOnClickListener(
                speaker -> {
                    if (holder.mediaPlayer == null) return;
                    showSpeaker(holder.transcriptionSpeaker, true);
                    holder.mediaPlayer.setOnCompletionListener(mp ->
                            showSpeaker(holder.transcriptionSpeaker, false));
                    new MediaPlayerManager().play(holder.mediaPlayer);
                }
        );
    }

    private void showSpeaker(ImageView iv, boolean isSpeaking) {
        iv.setImageResource(
                new IconTogglesResourcesProvider().getSpeakerResId(isSpeaking)
        );
    }
}
