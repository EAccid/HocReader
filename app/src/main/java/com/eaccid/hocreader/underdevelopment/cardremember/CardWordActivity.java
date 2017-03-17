package com.eaccid.hocreader.underdevelopment.cardremember;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.eaccid.hocreader.R;
import com.eaccid.hocreader.presentation.BasePresenter;
import com.eaccid.hocreader.presentation.BaseView;
import com.eaccid.hocreader.provider.semantic.ImageViewLoader;
import com.eaccid.hocreader.provider.semantic.SoundPlayer;
import com.eaccid.hocreader.provider.semantic.TranslationSoundPlayer;
import com.eaccid.hocreader.provider.db.words.WordItem;
import com.eaccid.hocreader.provider.db.words.WordItemProvider;
import com.eaccid.hocreader.underdevelopment.MemorizingCalculatorImpl;
import com.eaccid.hocreader.underdevelopment.IconTogglesResourcesProvider;
import com.eaccid.hocreader.exceptions.ReaderExceptionHandlerImpl;
import com.eaccid.hocreader.underdevelopment.UnderDevelopment;
import com.eaccid.hocreader.underdevelopment.WordViewElements;
import com.eaccid.hocreader.underdevelopment.WordViewHandler;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;

public class CardWordActivity extends AppCompatActivity implements CardWordView, WordViewElements {
    private CardWordPresenter mPresenter;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_item_fragment_2);
        if (mPresenter == null) mPresenter = new CardWordPresenter();
        mPresenter.attachView(this);
        setFinishOnTouchOutside(true);
        ButterKnife.bind(this);
        soundPlayer = new TranslationSoundPlayer();
        mPresenter.onViewCreate();
        dontKnow.setOnClickListener(v -> {
            translation.setText(getTranslation());
            translation.setTextColor(Color.parseColor("#fff45f30"));
        });
        remember.setOnClickListener(v -> {
            translation.setText(getTranslation());
            translation.setTextColor(Color.parseColor("#ff2f8b2a"));
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
        soundPlayer.release();
        mPresenter.detachView();
        finish();
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
        return findViewById(R.id.container);
    }

    @Override
    public ExpandableTextView context() {
        return null;
    }

    @Override
    public void setDataToView(WordItem wordItem) {
        new WordItemProvider()
                .getWordItemWithTranslation(wordItem)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(item -> {
                            new WordViewHandler().loadDataToViewFromWordItem(this, item);
                            translation.setText("*?");
                        },
                        e -> new ReaderExceptionHandlerImpl().handleError(e)
                );
    }
}
