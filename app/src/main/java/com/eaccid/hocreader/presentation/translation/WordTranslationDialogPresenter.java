package com.eaccid.hocreader.presentation.translation;

import android.util.Log;

import com.eaccid.hocreader.data.remote.libtranslator.translator.TextTranslation;
import com.eaccid.hocreader.provider.semantic.SoundPlayer;
import com.eaccid.hocreader.provider.semantic.ImageViewLoader;
import com.eaccid.hocreader.provider.fromtext.WordFromText;
import com.eaccid.hocreader.provider.semantic.TranslationSoundPlayer;
import com.eaccid.hocreader.provider.translator.HocTranslatorProvider;
import com.eaccid.hocreader.provider.translator.TranslatedWord;
import com.eaccid.hocreader.provider.translator.TranslatedWordImpl;
import com.eaccid.hocreader.presentation.BasePresenter;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class WordTranslationDialogPresenter implements BasePresenter<WordTranslationDialogFragment> {
    private final String LOG_TAG = "TranslationPresenter";
    private WordTranslationDialogFragment mView;
    private TranslatedWord mTranslatedWord;
    private SoundPlayer<String> mSoundPlayer;
    private String mNextWordToTranslate;
    private Subscription mTranslationSubscription;

    @Override
    public void attachView(WordTranslationDialogFragment wordTranslationDialogFragment) {
        mView = wordTranslationDialogFragment;
        Log.i(LOG_TAG, "TranslationPresenter has been attached.");
    }

    @Override
    public void detachView() {
        mView = null;
        mSoundPlayer.release();
        if (mTranslationSubscription != null) mTranslationSubscription.unsubscribe();
        Log.i(LOG_TAG, "TranslationPresenter has been detached.");
    }

    public void onViewCreated() {
        mSoundPlayer = new TranslationSoundPlayer();
        translateText(mView.getWordFromText());
    }

    private void translateText(WordFromText wordFromText) {
        if (mTranslationSubscription != null && !mTranslationSubscription.isUnsubscribed())
            mTranslationSubscription.unsubscribe();
        mTranslationSubscription = new HocTranslatorProvider()
                .translate(wordFromText.getText())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<TextTranslation>() {
                    @Override
                    public void onCompleted() {
                        mView.notifyDataChanged();
                        unsubscribe();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        unsubscribe();
                    }

                    @Override
                    public void onNext(TextTranslation textTranslation) {
                        {
                            Log.i(LOG_TAG, "Text translated status: " + !textTranslation.isEmpty());
                            if (isNextWordToTranslateEmpty())
                                setNextWordToTranslate(textTranslation.getWord());
                            showTranslationsData(textTranslation);
                            mTranslatedWord = new TranslatedWordImpl(wordFromText.getText(), wordFromText.getSentence());
                        }
                    }
                });
    }

    private void showTranslationsData(TextTranslation textTranslation) {
        mSoundPlayer.preparePlayerFromSource(textTranslation.getSoundUrl());
        new ImageViewLoader()
                .loadPictureFromUrl(
                        mView.getWordPicture(),
                        textTranslation.getPicUrl(),
                        false
                );
        mView.showContextWord(mView.getWordFromText().getText());
        mView.showBaseWord(mNextWordToTranslate);
        mView.showWordTranscription(textTranslation.getTranscription());
        mView.showTranslations(textTranslation.getTranslates());
    }

    public void OnSpeakerClicked() {
        mView.showSpeaker(true);
        mSoundPlayer.play().subscribe(completed -> {
            mView.showSpeaker(false);
        });
    }

    public void OnWordClicked() {
        String nextWord = getNextWordToTranslate();
        WordFromText currentWord = mView.getWordFromText();
        setNextWordToTranslate(currentWord.getText());
        currentWord.setText(nextWord);
        translateText(currentWord);
    }

    public void onTranslationClick(String text) {
        mTranslatedWord.addTranslation(text);
        ((WordTranslationDialogFragment.OnWordTranslationClickListener) mView.getContext())
                .onWordTranslated(mTranslatedWord);
        mView.dismiss();
    }

    private void setNextWordToTranslate(String mNextWordToTranslate) {
        this.mNextWordToTranslate = mNextWordToTranslate;
    }

    private String getNextWordToTranslate() {
        return mNextWordToTranslate;
    }

    public boolean isNextWordToTranslateEmpty() {
        return mNextWordToTranslate == null || mNextWordToTranslate.isEmpty();
    }
}
