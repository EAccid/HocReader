package com.eaccid.hocreader.presentation.training.carouseladapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.eaccid.hocreader.R;
import com.eaccid.hocreader.data.local.db.entity.Word;
import com.eaccid.hocreader.provider.semantic.SoundPlayer;
import com.eaccid.hocreader.provider.semantic.TranslationSoundPlayer;
import com.eaccid.hocreader.provider.NetworkAvailablenessImpl;
import com.eaccid.hocreader.provider.db.words.WordItemProvider;
import com.eaccid.hocreader.exceptions.ReaderExceptionHandlerImpl;
import com.eaccid.hocreader.underdevelopment.WordViewElements;
import com.eaccid.hocreader.underdevelopment.WordViewHandler;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;

public class WordCarouselRecyclerViewAdapter extends OrmLiteCursorRecyclerViewAdapter<Word, WordCarouselRecyclerViewAdapter.ViewHolder> {
    private CompositeSubscription compositeSubscription = new CompositeSubscription();

    static class ViewHolder extends RecyclerView.ViewHolder implements WordViewElements {
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

        @Override
        public TextView word() {
            return word;
        }

        @Override
        public TextView transcription() {
            return transcription;
        }

        @Override
        public TextView translation() {
            return translation;
        }

        @Override
        public ImageView wordImage() {
            return wordImage;
        }

        @Override
        public int defaultImageResId() {
            return R.drawable.empty_circle_background;
        }

        @Override
        public ImageView learnByHeart() {
            return learnByHeart;
        }

        @Override
        public ImageView alreadyLearned() {
            return alreadyLearned;
        }

        @Override
        public ImageView transcriptionSpeaker() {
            return transcriptionSpeaker;
        }

        @Override
        public SoundPlayer<String> soundPlayer() {
            return soundPlayer;
        }

        @Override
        public View container() {
            return itemView;
        }

        @Override
        public ExpandableTextView context() {
            return null;
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
        if (holder.subscription != null && !holder.subscription.isUnsubscribed())
            compositeSubscription.remove(holder.subscription);
        holder.subscription = new WordItemProvider()
                .getWordItemWithTranslation(word)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(wordItem -> {
                            new WordViewHandler().loadDataToViewFromWordItem(holder, wordItem);
                            holder.translation.setText("*?");
                        },
                        e -> new ReaderExceptionHandlerImpl().handleError(e)
                );
        compositeSubscription.add(holder.subscription);
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

}
