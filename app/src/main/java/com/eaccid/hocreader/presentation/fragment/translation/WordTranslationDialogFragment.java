package com.eaccid.hocreader.presentation.fragment.translation;

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
import com.eaccid.hocreader.provider.translator.TranslatedWord;
import com.eaccid.hocreader.provider.fromtext.WordFromText;
import com.eaccid.hocreader.presentation.BasePresenter;
import com.eaccid.hocreader.presentation.BaseView;

import java.util.List;

public class WordTranslationDialogFragment extends DialogFragment implements BaseView {

    public interface WordTranslationClickListener {
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

    private TextView textViewWord;
    private ImageView imageWordPicture;
    private ImageButton imageButtonTranscriptionSpeaker;
    private TextView textViewTranscription;
    private ListView listViewTranslations;

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
        imageWordPicture = (ImageView) v.findViewById(R.id.image_word_picture);
        textViewWord = (TextView) v.findViewById(R.id.word);
        textViewTranscription = (TextView) v.findViewById(R.id.text_transcription);
        listViewTranslations = (ListView) v.findViewById(R.id.list_translations);
        imageButtonTranscriptionSpeaker = (ImageButton) v.findViewById(R.id.transcription_speaker);

        textViewWord.setOnClickListener(view -> mPresenter.OnWordClicked());
        imageButtonTranscriptionSpeaker.setOnClickListener(view -> mPresenter.OnSpeakerClicked());

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.showTranslations();
    }

    public void setDialogTitle(String title) {
        getDialog().setTitle(title);
    }

    public void setWordText(String text) {
        textViewWord.setText(text);
    }

    public ImageView getWordPicture() {
        return imageWordPicture;
    }

    public ImageButton getTranscriptionSpeaker() {
        return imageButtonTranscriptionSpeaker;
    }

    public void setWordTranscription(String text) {
        textViewTranscription.setText("[ " + text + " ]");
    }

    public void loadTranslations(List<String> translations) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getView().getContext(), R.layout.translation_dialog_item, translations);
        listViewTranslations.setAdapter(adapter);
    }

    public ListView getTranslationsView() {
        return listViewTranslations;
    }

    public void setImageSpeaker(boolean isSpeak) {
        //imageButtonTranscriptionSpeaker.setImageResource(R.drawable.ic_hearing_blue_24px);
    }
}