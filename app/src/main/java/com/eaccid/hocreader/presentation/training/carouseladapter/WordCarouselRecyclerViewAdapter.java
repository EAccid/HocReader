package com.eaccid.hocreader.presentation.training.carouseladapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.eaccid.hocreader.R;
import com.eaccid.hocreader.data.local.db.entity.Word;
import com.eaccid.hocreader.provider.semantic.ImageViewLoader;
import com.eaccid.hocreader.provider.semantic.SoundPlayer;
import com.eaccid.hocreader.provider.semantic.TranslationSoundPlayer;
import com.eaccid.hocreader.provider.NetworkAvailablenessImpl;
import com.eaccid.hocreader.provider.db.words.WordItemProvider;
import com.eaccid.hocreader.underdevelopment.MemorizingCalculatorImpl;
import com.eaccid.hocreader.underdevelopment.ReaderExceptionHandlerImpl;
import com.eaccid.hocreader.presentation.weditor.IconTogglesResourcesProvider;
import com.eaccid.hocreader.underdevelopment.UnderDevelopment;

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
        @BindView(R.id.learn_by_heart_false)
        ImageView learnByHeart;
        @BindView(R.id.already_learned)
        ImageView alreadyLearned;
        @BindView(R.id.transcription_speaker)
        ImageView transcriptionSpeaker;
        SoundPlayer soundPlayer;
        @BindView(R.id.don_t_know)
        Button dontKnow;
        @BindView(R.id.remember)
        Button remember;
        boolean isSetToLearn;

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

    @Override
    public void onViewDetachedFromWindow(WordCarouselRecyclerViewAdapter.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        if (holder.soundPlayer != null)
            holder.soundPlayer.release();
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
        if (!new NetworkAvailablenessImpl(holder.itemView.getContext()).isNetworkAvailable()) {
            holder.word.setText(word.getName());
            return;
        }
        new WordItemProvider()
                .getWordItemWithTranslation(word)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(wordItem -> {
                            holder.word.setText(wordItem.getWordFromText());
                            new ImageViewLoader().loadPictureFromUrl(
                                    holder.wordImage,
                                    wordItem.getPictureUrl(),
                                    R.drawable.empty_circle_background,
                                    R.drawable.empty_circle_background,
                                    false);
                            holder.translation.setText("*?");
                            holder.transcription.setText("[" + wordItem.getTranscription() + "]");
                            holder.alreadyLearned.setImageResource(
                                    new IconTogglesResourcesProvider().getAlreadyLearnedWordResId(
                                            new MemorizingCalculatorImpl(wordItem)
                                    )
                            );
                            holder.soundPlayer = TranslationSoundPlayer.createAndPreparePlayerFromUrl(wordItem.getSoundUrl());
                            //Temp:
                            holder.learnByHeart.setImageResource(
                                    new IconTogglesResourcesProvider().getLearnByHeartResId(
                                            true
                                    )
                            );
                            holder.isSetToLearn = true;
                        }, e -> {
                            new ReaderExceptionHandlerImpl().handleError(e);

                        }
                );
    }

    //TODO on click listeners handler (carousel)
    private void setListenersToViewFromItem(ViewHolder holder, Word word) {
        holder.learnByHeart.setOnClickListener(v -> {
            holder.isSetToLearn = !holder.isSetToLearn;
            holder.learnByHeart.setImageResource(
                    new IconTogglesResourcesProvider().getLearnByHeartResId(
                            holder.isSetToLearn //getWordListItemProvider(position).isSetToLearn()
                    )
            );
            Toast.makeText(holder.itemView.getContext(), UnderDevelopment.TEXT, Toast.LENGTH_SHORT).show();
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
                    showSpeaker(holder.transcriptionSpeaker, true);
                    holder.soundPlayer.play().subscribe(completed -> {
                        showSpeaker(holder.transcriptionSpeaker, false);
                    });
                }
        );
        holder.alreadyLearned.setOnClickListener(v -> {
            Toast.makeText(holder.itemView.getContext(), UnderDevelopment.TEXT, Toast.LENGTH_SHORT).show();
        });
    }

    private void showSpeaker(ImageView iv, boolean isSpeaking) {
        iv.setImageResource(
                new IconTogglesResourcesProvider().getSpeakerResId(isSpeaking)
        );
    }
}
