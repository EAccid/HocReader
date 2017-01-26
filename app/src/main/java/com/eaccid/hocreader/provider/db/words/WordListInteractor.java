package com.eaccid.hocreader.provider.db.words;

import android.util.Log;
import android.util.SparseBooleanArray;

import com.eaccid.hocreader.data.local.db.entity.Word;

import com.eaccid.hocreader.provider.translator.HocTranslatorProvider;
import com.eaccid.hocreader.provider.db.words.listprovider.DataListProvider;
import com.eaccid.hocreader.provider.db.words.listprovider.ItemDataProvider;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

public class WordListInteractor extends DataListProvider {

    private static final String logTAG = "WordListInteractor";
    private List<String> sessionWords;
    private WordListManager wordListManager;

    public WordListInteractor(WordListManager wordListManager) {
        this.sessionWords = new ArrayList<>();
        this.wordListManager = wordListManager;
    }

    @Override
    public void populateDataList() {
        setDataList(getWordList());
    }

    @Override
    public int undoLastRemoval() {
        WordItemImpl word = (WordItemImpl) getLastRemovedData();
        sessionWords.add(word.getWordFromText());
        wordListManager.createItemWord(word);
        return super.undoLastRemoval();
    }

    @Override
    public void removeItem(int position) {
        ItemDataProvider item = getDataList().get(position);
        wordListManager.removeItem(item);
        Word word = (Word) item.getObject();
        sessionWords.remove(word.getName());
        super.removeItem(position);
    }

    public void addItem(String wordBaseName, String translation, String context, Boolean succeed) {
        if (sessionWords.contains(wordBaseName)) {
            return;
        }
        WordItemImpl item = (WordItemImpl) wordListManager.createItemWord(
                wordBaseName, sessionWords.size(), translation, context, succeed);
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
        return wordListManager.getAll();
    }

    private List<ItemDataProvider> getDataListByBookAndSessionWords() {
        return wordListManager.getAllFromDatabase(sessionWords);
    }

    private List<ItemDataProvider> addDataListByBookAndSessionWords() {
        return wordListManager.addAllFromDatabase(sessionWords);
    }

    @Override
    public ItemDataProvider getItem(int index) {
        return super.getItem(index);
    }

    /**
     * Try RxJava in Android
     */

    public BehaviorSubject<WordItem> getWordItem(final int index) {
        WordItem word = (WordItem) getItem(index);
        BehaviorSubject<WordItem> subject = BehaviorSubject.create();
        new HocTranslatorProvider()
                .translate(word.getWordFromText())
                .subscribeOn(Schedulers.io())
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
                boolean succeed = wordListManager.removeItems();
                clearSessionDataList();
                clearDataList();
                subscriber.onNext(succeed);
                subscriber.onCompleted();
            }
        });
    }

}
