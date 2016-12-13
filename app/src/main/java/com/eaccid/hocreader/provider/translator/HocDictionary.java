package com.eaccid.hocreader.provider.translator;

import android.content.Context;

import com.eaccid.hocreader.refactoring.settings.LingualeoServiceCookiesHandler;
import com.eaccid.hocreader.data.remote.libtranslator.lingualeo_impl.dictionary.LingualeoDictionary;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

public class HocDictionary {

    //TODO del, when preferences have been injected into LingualeoServiceCookiesHandler
    private Context context;

    public HocDictionary(Context context) {
        this.context = context;
    }

    public boolean addTranslatedWord(TranslatedWord word) {

        try {

            ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
            HocDictionary.OuterDictionary outerTranslation = new HocDictionary.OuterDictionary(word);
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

            LingualeoServiceCookiesHandler cookiesHandler = new LingualeoServiceCookiesHandler(context);
            LingualeoDictionary lingualeo = new LingualeoDictionary(cookiesHandler);
            boolean succeed = lingualeo.addWord(word.getWordFromContext(), word.getTranslation(), word.getContext());

            return succeed;

        }
    }

}


