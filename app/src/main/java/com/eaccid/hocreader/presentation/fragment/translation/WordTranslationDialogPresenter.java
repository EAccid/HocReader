package com.eaccid.hocreader.presentation.fragment.translation;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.eaccid.hocreader.provider.translator.HocTranslator;
import com.eaccid.hocreader.provider.translator.TranslatedWord;
import com.eaccid.hocreader.data.remote.libtranslator.translator.TextTranslation;
import com.eaccid.hocreader.presentation.BasePresenter;
import com.eaccid.hocreader.provider.fromtext.WordFromText;

public class WordTranslationDialogPresenter implements BasePresenter<WordTranslationDialogFragment> {
    private final String logTAG = "TranslationPresenter";
    private WordTranslationDialogFragment mView;

    /**
     * TODO refactor: make methods more readable
     * add non context translation
     * disable adding non-base words
     */

    private TranslatedWord mTranslatedWord;
    private TextTranslation mWordTranslation;
    private MediaPlayer mMediaPlayer;
    private String nextWordToTranslate;

    public WordTranslationDialogPresenter() {
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    @Override
    public void attachView(WordTranslationDialogFragment wordTranslationDialogFragment) {
        mView = wordTranslationDialogFragment;
        Log.i(logTAG, "TranslationPresenter has been attached.");

        translateText(getWordFromText());
        setNextWordToTranslate(mTranslatedWord.getWordBaseForm());

    }

    @Override
    public void detachView() {
        Log.i(logTAG, "TranslationPresenter has been detached.");
        mMediaPlayer.release();
        mView = null;
    }

    public void showTranslations() {

        mView.setWordText(nextWordToTranslate);//mWordTranslation.getWord()

        TranslatorViewManager viewManager = new TranslatorViewManager();
        viewManager.loadPictureFromURL(mView.getWordPicture(), mWordTranslation.getPicUrl());
        viewManager.loadSoundFromURL(mMediaPlayer, mWordTranslation.getSoundUrl());

        mView.setWordTranscription(mWordTranslation.getTranscription());

        mView.loadTranslations(mWordTranslation.getTranslates());

        mView.setDialogTitle(mTranslatedWord.getWordFromContext());
        mView.getTranslationsView().setOnItemClickListener(new OnItemTranslationClickListener());

    }

    public void OnSpeakerClicked() {
        if (!mMediaPlayer.isPlaying()) {
            mMediaPlayer.start();
            mView.setImageSpeaker(true); //TODO image to speak
        }
    }

    public void OnWordClicked() {
        String nextWord = getNextWordToTranslate();
        WordFromText currentWord = getWordFromText();

        setNextWordToTranslate(currentWord.getText());
        currentWord.setText(nextWord);

        translateText(currentWord);
        showTranslations();
    }

    private class OnItemTranslationClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            TextView tv = (TextView) view;
            String chosenTranslation = (String) tv.getText();
            mTranslatedWord.setTranslation(chosenTranslation);
            ((WordTranslationDialogFragment.WordTranslationClickListener) mView.getContext()).onWordTranslated(mTranslatedWord);
            mView.dismiss();
        }
    }

    private void translateText(WordFromText wordFromText) {
        mWordTranslation = HocTranslator.translate(wordFromText);
        mTranslatedWord = new TranslatedWord();
        mTranslatedWord.setWordBaseForm(mWordTranslation.getWord());
        mTranslatedWord.setWordFromContext(wordFromText.getText());
        mTranslatedWord.setContext(wordFromText.getSentence());
    }

    private void setNextWordToTranslate(String nextWordToTranslate) {
        this.nextWordToTranslate = nextWordToTranslate;
    }

    private String getNextWordToTranslate() {
        return nextWordToTranslate;
    }

    private WordFromText getWordFromText() {
        return (WordFromText) mView.getArguments().getSerializable("wordFromText");
    }

}
