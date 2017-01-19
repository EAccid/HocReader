package com.eaccid.hocreader.provider.db;

import com.eaccid.hocreader.provider.translator.HocTranslatorProvider;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

public class WordItemProvider {

    public BehaviorSubject<WordItem> getWordItemWithTranslation(final WordItem word) {
        BehaviorSubject<WordItem> subject = BehaviorSubject.create();
        new HocTranslatorProvider()
                .translate(word.getWordFromText())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(textTranslation -> {
                            word.setTranslationToText(textTranslation);
                            subject.onNext(word);
                        }, subject::onError
                );
        return subject;
    }

}
