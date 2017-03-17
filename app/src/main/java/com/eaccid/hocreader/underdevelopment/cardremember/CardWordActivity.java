package com.eaccid.hocreader.underdevelopment.cardremember;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.eaccid.hocreader.R;
import com.eaccid.hocreader.presentation.BasePresenter;
import com.eaccid.hocreader.presentation.BaseView;
import com.eaccid.hocreader.presentation.weditor.adapter.SwipeOnLongPressRecyclerViewAdapter;
import com.eaccid.hocreader.provider.semantic.ImageViewLoader;
import com.eaccid.hocreader.provider.semantic.SoundPlayer;
import com.eaccid.hocreader.provider.semantic.TranslationSoundPlayer;
import com.eaccid.hocreader.provider.db.words.WordItem;
import com.eaccid.hocreader.provider.db.words.WordItemProvider;
import com.eaccid.hocreader.underdevelopment.MemorizingCalculatorImpl;
import com.eaccid.hocreader.presentation.weditor.IconTogglesResourcesProvider;
import com.eaccid.hocreader.exceptions.ReaderExceptionHandlerImpl;
import com.eaccid.hocreader.underdevelopment.UnderDevelopment;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;

public class CardWordActivity extends AppCompatActivity implements BaseView {

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
    SoundPlayer<String> soundPlayer;
    @BindView(R.id.don_t_know)
    Button dontKnow;
    @BindView(R.id.remember)
    Button remember;
    boolean isSetToLearn;
    private static CardWordPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_item_fragment_2);
        if (mPresenter == null) mPresenter = new CardWordPresenter();
        mPresenter.attachView(this);
        setFinishOnTouchOutside(true);
        ButterKnife.bind(this);

        /**TODO delete getting data from presenter, temp solution*/
        WordItem wordItem = mPresenter.getWordItem();
        soundPlayer = new TranslationSoundPlayer();
        setDataToViewFromItem(wordItem);
        setListenersToViewFromItem();
    }

    private void setDataToViewFromItem(WordItem wordItem) {
        new WordItemProvider()
                .getWordItemWithTranslation(wordItem)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(item -> {
                    word.setText(item.getWordFromText());
                    new ImageViewLoader().loadPictureFromUrl(
                            wordImage,
                            item.getPictureUrl(),
                            R.drawable.empty_circle_background,
                            R.drawable.empty_circle_background,
                            false);
                    transcription.setText("[" + item.getTranscription() + "]");
                    alreadyLearned.setImageResource(
                            new IconTogglesResourcesProvider().getAlreadyLearnedWordResId(
                                    new MemorizingCalculatorImpl(item)
                            )
                    );
                    soundPlayer.release();
                    soundPlayer.preparePlayerFromSource(wordItem.getSoundUrl());
                    //Temp:
                    learnByHeart.setImageResource(
                            new IconTogglesResourcesProvider().getLearnByHeartResId(
                                    true
                            )
                    );
                    isSetToLearn = true;
                }, e -> {
                    new ReaderExceptionHandlerImpl().handleError(e);
                });
    }

    private void setListenersToViewFromItem() {
        learnByHeart.setOnClickListener(v -> {
            isSetToLearn = !isSetToLearn;
            learnByHeart.setImageResource(
                    new IconTogglesResourcesProvider().getLearnByHeartResId(
                            isSetToLearn
                    )
            );
            Toast.makeText(getBaseContext(), UnderDevelopment.TEXT, Toast.LENGTH_SHORT).show();
        });
        dontKnow.setOnClickListener(v -> {
            translation.setText(getTranslation());
            translation.setTextColor(Color.parseColor("#fff45f30"));
        });
        remember.setOnClickListener(v -> {
            translation.setText(getTranslation());
            translation.setTextColor(Color.parseColor("#ff2f8b2a"));
        });
        transcriptionSpeaker.setOnClickListener(
                speaker -> {
                    showSpeaker(transcriptionSpeaker, true);
                    soundPlayer.play().subscribe(completed -> {
                        showSpeaker(transcriptionSpeaker, false);
                    });
                }
        );
    }

    public String getWord() {
        return getIntent().getStringExtra("word");
    }

    public String getTranslation() {
        return getIntent().getStringExtra("translation");
    }

    @Override
    public BasePresenter getPresenter() {
        return mPresenter;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        soundPlayer.release();
        mPresenter.detachView();
        finish();
    }

    private void showSpeaker(ImageView iv, boolean isSpeaking) {
        iv.setImageResource(
                new IconTogglesResourcesProvider().getSpeakerResId(isSpeaking)
        );
    }


}
