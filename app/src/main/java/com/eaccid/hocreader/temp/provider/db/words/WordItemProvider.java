package com.eaccid.hocreader.temp.provider.db.words;

import com.eaccid.hocreader.data.local.db.entity.Word;
import com.eaccid.hocreader.data.remote.libtranslator.translator.TextTranslation;
import com.eaccid.hocreader.temp.provider.translator.HocTranslatorProvider;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

public class WordItemProvider {

    public Observable<WordItem> getWordItemWithTranslation(final Word word) {
        return new HocTranslatorProvider()
                .translate(word.getName())
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<TextTranslation, Observable<WordItem>>() {
                    @Override
                    public Observable<WordItem> call(TextTranslation textTranslation) {
                        return Observable.create(new Observable.OnSubscribe<WordItem>() {
                            @Override
                            public void call(Subscriber<? super WordItem> subscriber) {
                                WordItem wordItem = new WordItemImpl(word);
                                wordItem.setTranslationToText(textTranslation);
                                subscriber.onNext(wordItem);
                                subscriber.onCompleted();
                            }
                        });
                    }
                });
    }

    public BehaviorSubject<WordItem> getWordItemWithTranslation(final WordItem word) {
        BehaviorSubject<WordItem> subject = BehaviorSubject.create();
        new HocTranslatorProvider()
                .translate(word.getWordFromText())
                .subscribeOn(Schedulers.io())
                .subscribe(textTranslation -> {
                            word.setTranslationToText(textTranslation);
                            subject.onNext(word);
                            subject.onCompleted();
                        }
                );
        return subject;
    }

}
