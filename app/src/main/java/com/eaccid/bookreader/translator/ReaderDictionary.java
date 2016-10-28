package com.eaccid.bookreader.translator;

import android.content.Context;

import com.eaccid.bookreader.dev.settings.LingualeoServiceCookiesHandler;
import com.eaccid.libtranslator.lingualeo.dictionary.LingualeoDictionary;


import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

public class ReaderDictionary {

    private Context context;

    public ReaderDictionary(Context context) {
        this.context = context;
    }

    public boolean addTranslatedWord(TranslatedWord word) {

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

        private TranslatedWord word;

        OuterDictionary(TranslatedWord word) {
            this.word = word;
        }

        @Override
        public Boolean call() throws Exception {

           LingualeoDictionary lingualeo = new LingualeoDictionary(new LingualeoServiceCookiesHandler(context));
           return lingualeo.addWord(word.getWordFromContext(), word.getTranslation(), word.getContext());

        }
    }

}


