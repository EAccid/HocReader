package com.eaccid.hocreader.temp.presentation.activity.notcard;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.eaccid.hocreader.R;
import com.eaccid.hocreader.presentation.BasePresenter;
import com.eaccid.hocreader.presentation.BaseView;
import com.eaccid.hocreader.temp.presentation.fragment.translation.semantic.ImageViewManager;
import com.eaccid.hocreader.temp.provider.db.words.WordItem;
import com.eaccid.hocreader.temp.provider.db.words.WordItemProvider;
import com.eaccid.hocreader.temp.underdevelopment.MemorizingCalculatorImpl;
import com.eaccid.hocreader.temp.underdevelopment.IconTogglesResourcesProvider;
import com.eaccid.hocreader.temp.underdevelopment.ReaderExceptionHandlerImpl;

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
    private static CardWordPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_item_fragment_2);
        if (mPresenter == null) mPresenter = new CardWordPresenter();
        mPresenter.attachView(this);
        setFinishOnTouchOutside(true);
        ButterKnife.bind(this);

        /**TODO data from presenter, temp solution*/
        WordItem wordItem = mPresenter.getWordItem();
        setDataToViewFromItem(wordItem);
        setListenersToViewFromItem();
    }

    private void setDataToViewFromItem(WordItem wordItem) {
        new WordItemProvider()
                .getWordItemWithTranslation(wordItem)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(item -> {
                    word.setText(item.getWordFromText());
                    new ImageViewManager().loadPictureFromUrl(wordImage, item.getPictureUrl());
                    transcription.setText("[" + item.getTranscription() + "]");
                    alreadyLearned.setImageResource(
                            new IconTogglesResourcesProvider().getAlreadyLearnedWordResId(
                                    new MemorizingCalculatorImpl(item)
                            )
                    );
                }, e -> {
                    new ReaderExceptionHandlerImpl().handleError(e);
                });
    }

    private void setListenersToViewFromItem() {
        deleteWord.setOnClickListener(v -> {
        });
        dontKnow.setOnClickListener(v -> {
            translation.setText(getTranslation());
            translation.setTextColor(Color.parseColor("#fff45f30"));
        });
        remember.setOnClickListener(v -> {
            translation.setText(getTranslation());
            translation.setTextColor(Color.parseColor("#ff2f8b2a"));
        });
        transcriptionSpeaker.setOnClickListener(v -> {
        });
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
        mPresenter.detachView();
    }

}
