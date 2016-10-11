package com.eaccid.bookreader;

import android.content.Context;

import com.eaccid.bookreader.settings.LingualeoServiceCookiesHandler;
import com.eaccid.translator.lingualeo.dictionary.LingualeoDictionary;


import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

class ReaderDictionary {

    private Context context;

    ReaderDictionary(Context context) {
        this.context = context;
    }

    boolean addTranslatedWord(TranslatedDictionaryWord word) {

        try {

            ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
            ReaderDictionary.OuterDictionary outerTranslation = new ReaderDictionary.OuterDictionary(word);
            Future<Boolean> translationResult = executor.submit(outerTranslation);
            return translationResult.get();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    private class OuterDictionary implements Callable<Boolean> {

        private TranslatedDictionaryWord word;

        OuterDictionary(TranslatedDictionaryWord word) {
            this.word = word;
        }

        @Override
        public Boolean call() throws Exception {

           LingualeoDictionary lingualeo = new LingualeoDictionary(new LingualeoServiceCookiesHandler(context));
           return lingualeo.addWord(word.getWord(), word.getTranslation(), word.getContext());

        }
    }

}


