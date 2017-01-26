package com.eaccid.hocreader.presentation.translation;

import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.eaccid.hocreader.R;
import com.eaccid.hocreader.presentation.BasePresenter;
import com.eaccid.hocreader.provider.fromtext.WordFromText;
import com.eaccid.hocreader.provider.translator.TranslatedWord;
import com.eaccid.hocreader.presentation.weditor.IconTogglesResourcesProvider;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WordTranslationDialogFragment extends DialogFragment implements TranslationView {

    public interface OnWordTranslationClickListener {
        void onWordTranslated(TranslatedWord translatedWord);
    }

    public static WordTranslationDialogFragment newInstance(WordFromText wordFromText) {
        WordTranslationDialogFragment f = new WordTranslationDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable("wordFromText", wordFromText);
        f.setArguments(args);
        return f;
    }

    private WordTranslationDialogPresenter mPresenter;
    private ArrayAdapter<String> mAdapter;

    @BindView(R.id.word)
    TextView mBaseWord;
    @BindView(R.id.image_word_picture)
    ImageView mWordPicture;
    @BindView(R.id.transcription_speaker)
    ImageButton mSpeaker;
    @BindView(R.id.text_transcription)
    TextView mTranscription;
    @BindView(R.id.list_translations)
    ListView mTranslations;

    @Override
    public BasePresenter getPresenter() {
        return mPresenter;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mPresenter == null) mPresenter = new WordTranslationDialogPresenter();
        mPresenter.attachView(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.translation_dialog_frame, container);
        ButterKnife.bind(this, v);
        mBaseWord.setOnClickListener(view -> mPresenter.OnWordClicked());
        mSpeaker.setOnClickListener(view -> mPresenter.OnSpeakerClicked());
        mTranslations.setOnItemClickListener((adapterView, view, i, l) -> {
            TextView tv = (TextView) view;
            mPresenter.onTranslationClick((String) tv.getText());
        });
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.onViewCreated();
    }

    @Override
    public void showContextWord(String title) {
        getDialog().setTitle(title);
    }

    @Override
    public void showBaseWord(String text) {
        mBaseWord.setText(text);
    }

    public ImageView getWordPicture() {
        return mWordPicture;
    }

    @Override
    public void showWordTranscription(String text) {
        mTranscription.setText("[ " + text + " ]");
    }

    @Override
    public void showSpeaker(boolean isSpeaking) {
        mSpeaker.setImageResource(
                new IconTogglesResourcesProvider().getSpeakerResId(isSpeaking)
        );
    }

    @Override
    public void showTranslations(List<String> list) {
        mAdapter = new ArrayAdapter<String>(
                getView().getContext(), R.layout.translation_dialog_item, list);
        mTranslations.setAdapter(mAdapter);
    }

    public WordFromText getWordFromText() {
        return (WordFromText) getArguments().getSerializable("wordFromText");
    }

    public void notifyTranslationsChanged() {
        if (mAdapter != null)
            mAdapter.notifyDataSetChanged();
    }

}