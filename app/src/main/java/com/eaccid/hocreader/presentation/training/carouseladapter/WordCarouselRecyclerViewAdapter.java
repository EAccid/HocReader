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
import com.eaccid.hocreader.provider.db.words.WordItem;
import com.eaccid.hocreader.provider.semantic.ImageViewLoader;
import com.eaccid.hocreader.provider.semantic.SoundPlayer;
import com.eaccid.hocreader.provider.semantic.TranslationSoundPlayer;
import com.eaccid.hocreader.provider.NetworkAvailablenessImpl;
import com.eaccid.hocreader.provider.db.words.WordItemProvider;
import com.eaccid.hocreader.underdevelopment.IconTogglesResources;
import com.eaccid.hocreader.underdevelopment.Learning;
import com.eaccid.hocreader.underdevelopment.LearningImpl;
import com.eaccid.hocreader.underdevelopment.MemorizingCalculatorImpl;
import com.eaccid.hocreader.exceptions.ReaderExceptionHandlerImpl;
import com.eaccid.hocreader.underdevelopment.IconTogglesResourcesProvider;
import com.eaccid.hocreader.underdevelopment.UnderDevelopment;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;

public class WordCarouselRecyclerViewAdapter extends OrmLiteCursorRecyclerViewAdapter<Word, WordCarouselRecyclerViewAdapter.ViewHolder> {
    private CompositeSubscription compositeSubscription = new CompositeSubscription();

    static class ViewHolder extends RecyclerView.ViewHolder {
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
        @BindView(R.id.don_t_know)
        Button dontKnow;
        @BindView(R.id.remember)
        Button remember;
        SoundPlayer<String> soundPlayer;
        Subscription subscription;

        ViewHolder(View drawerView) {
            super(drawerView);
            ButterKnife.bind(this, drawerView);
            soundPlayer = new TranslationSoundPlayer();
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
        holder.dontKnow.setOnClickListener(v -> {
            holder.translation.setText(word.getTranslation());
            holder.translation.setTextColor(Color.parseColor("#fff45f30"));
        });
        holder.remember.setOnClickListener(v -> {
            holder.translation.setText(word.getTranslation());
            holder.translation.setTextColor(Color.parseColor("#ff2f8b2a"));
        });
        boolean isNetworkAvailable = new NetworkAvailablenessImpl(holder.itemView.getContext()).isNetworkAvailable();
        if (!isNetworkAvailable)
            return;
        loadDataToViewFromWordItem(holder, word);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        compositeSubscription.unsubscribe();
    }

    public String getCurrentWordContext(int position) {
        String context = "";
        try {
            Word word = getTypedItem(position);
            context = word.getContext();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return context;
    }

    private void loadDataToViewFromWordItem(WordCarouselRecyclerViewAdapter.ViewHolder holder, Word word) {
        if (holder.subscription != null && !holder.subscription.isUnsubscribed())
            compositeSubscription.remove(holder.subscription);
        holder.subscription = new WordItemProvider()
                .getWordItemWithTranslation(word)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(wordItem -> {
                            holder.word.setText(wordItem.getWordFromText());
                            holder.translation.setText("*?");
                            holder.transcription.setText("[" + wordItem.getTranscription() + "]");
                            new ImageViewLoader().loadPictureFromUrl(
                                    holder.wordImage,
                                    wordItem.getPictureUrl(),
                                    R.drawable.empty_circle_background,
                                    R.drawable.empty_circle_background,
                                    false
                            );
                            holder.soundPlayer.preparePlayerFromSource(wordItem.getSoundUrl());
                            holder.transcriptionSpeaker.setOnClickListener(
                                    speaker -> {
                                        showSpeaker(holder.transcriptionSpeaker, true);
                                        holder.soundPlayer.play().subscribe(completed -> {
                                            showSpeaker(holder.transcriptionSpeaker, false);
                                        });
                                    }
                            );
                            handleLearningToggles(holder, wordItem);
                        }, e -> new ReaderExceptionHandlerImpl().handleError(e)
                );
        compositeSubscription.add(holder.subscription);
    }

    private void handleLearningToggles(ViewHolder holder, WordItem wordItem) {
        final Learning learning = getLearningHandler();
        final IconTogglesResources togglesResources = getIconTogglesResources();
        holder.alreadyLearned.setImageResource(
                togglesResources.getAlreadyLearnedWordResId(
                        new MemorizingCalculatorImpl(wordItem)
                )
        );
        holder.alreadyLearned.setOnClickListener(v -> {
            learning.isAlreadyLearned(wordItem);
            Toast.makeText(holder.itemView.getContext(), UnderDevelopment.TEXT, Toast.LENGTH_SHORT).show();
        });
        holder.learnByHeart.setImageResource(
                togglesResources.getLearnByHeartResId(
                        learning.isLearnByHart(wordItem)
                )
        );
        holder.learnByHeart.setOnClickListener(v -> {
            boolean learn = learning.isLearnByHart(wordItem);
            learning.setToLearn(wordItem, !learn);
            holder.learnByHeart.setImageResource(
                    togglesResources.getLearnByHeartResId(!learn)
            );
            Toast.makeText(holder.itemView.getContext(), UnderDevelopment.TEXT, Toast.LENGTH_SHORT).show();
        });
    }

    private void showSpeaker(ImageView iv, boolean isSpeaking) {
        iv.setImageResource(
                getIconTogglesResources().getSpeakerResId(isSpeaking)
        );
    }

    private Learning getLearningHandler() {
        return new LearningImpl();
    }

    private IconTogglesResources getIconTogglesResources() {
        return new IconTogglesResourcesProvider();
    }

}
