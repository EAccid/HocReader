package com.eaccid.hocreader.provider.db;

import android.util.Log;
import android.util.SparseBooleanArray;

import com.eaccid.hocreader.data.local.db.entity.Word;

import com.eaccid.hocreader.provider.db.listprovider.DataListProvider;
import com.eaccid.hocreader.provider.db.listprovider.ItemDataProvider;
import com.eaccid.hocreader.provider.translator.HocTranslatorProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

public class WordListInteractor extends DataListProvider {

    private static final String logTAG = "WordListInteractor";
    private List<String> sessionWords;
    private WordListFromDatabaseFetcher wordListFromDatabaseFetcher;

    public WordListInteractor(WordListFromDatabaseFetcher wordListFromDatabaseFetcher) {
        this.sessionWords = new ArrayList<>();
        this.wordListFromDatabaseFetcher = wordListFromDatabaseFetcher;
    }

    @Override
    public void populateDataList() {
        setDataList(getWordList());
    }

    @Override
    public int undoLastRemoval() {
        WordProviderImpl word = (WordProviderImpl) getLastRemovedData();
        sessionWords.add(word.getWordFromText());
        wordListFromDatabaseFetcher.createItemWord(word);
        return super.undoLastRemoval();
    }

    @Override
    public void removeItem(int position) {
        ItemDataProvider item = getDataList().get(position);
        wordListFromDatabaseFetcher.removeItem(item);
        Word word = (Word) item.getObject();
        sessionWords.remove(word.getName());
        super.removeItem(position);
    }

    public void addItem(String wordBaseName) {
        if (sessionWords.contains(wordBaseName)) {
            return;
        }
        WordProviderImpl item = (WordProviderImpl) wordListFromDatabaseFetcher.createItemWord(wordBaseName, sessionWords.size());
        if (item == null) {
            Log.i(logTAG, "Word '" + wordBaseName + "' has not been added to database.");
            return;
        }
        sessionWords.add(wordBaseName);
        int insertedPosition = 0;
        item.setLastAdded(true);
        getDataList().add(insertedPosition, item);
    }

    private void clearSessionDataList() {
        sessionWords = new ArrayList<>();
    }

    public void fillSessionDataList() {
        setDataList(getDataListByBookAndSessionWords());
    }

    public void updateSessionDataList() {
        setDataList(getDataListByBookAndSessionWords());
        getDataList().addAll(addDataListByBookAndSessionWords());
    }

    private List<ItemDataProvider> getWordList() {
        return wordListFromDatabaseFetcher.getAll();
    }

    private List<ItemDataProvider> getDataListByBookAndSessionWords() {
        return wordListFromDatabaseFetcher.getAllFromDatabase(sessionWords);
    }

    private List<ItemDataProvider> addDataListByBookAndSessionWords() {
        return wordListFromDatabaseFetcher.addAllFromDatabase(sessionWords);
    }

    @Override
    public ItemDataProvider getItem(int index) {
        return super.getItem(index);
    }

    /**
     * Try RxJava in Android
     */

    public BehaviorSubject<WordProviderImpl> getWordProvider(final int index) {
        WordProviderImpl word = (WordProviderImpl) getItem(index);
        BehaviorSubject<WordProviderImpl> subject = BehaviorSubject.create();
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

    public Observable<Boolean> removeItems(SparseBooleanArray items) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                for (int i = (items.size() - 1); i >= 0; i--) {
                    removeItem(items.keyAt(i));
                }
                subscriber.onNext(true);
                subscriber.onCompleted();
            }
        });
    }

    public Observable<Boolean> removeAllItems() {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                boolean succeed = wordListFromDatabaseFetcher.removeItems();
                clearSessionDataList();
                clearDataList();
                subscriber.onNext(succeed);
                subscriber.onCompleted();
            }
        });
    }
}
