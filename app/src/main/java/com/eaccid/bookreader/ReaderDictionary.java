package com.eaccid.bookreader;


import com.eaccid.translator.lingualeo.dictionary.LingualeoDictionary;


import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

public class ReaderDictionary {

    public static boolean addTranslatedWord(TranslatedDictionaryWord word) {

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

    private static class OuterDictionary implements Callable<Boolean> {

        private TranslatedDictionaryWord word;

        OuterDictionary(TranslatedDictionaryWord word) {
            this.word = word;
        }

        @Override
        public Boolean call() throws Exception {

            LingualeoServiceCookiesHandler cookiesHandler = new LingualeoServiceCookiesHandler();
            LingualeoDictionary lingualeo = new LingualeoDictionary(cookiesHandler);
            lingualeo.authorize("accidental899@mail.ru","accid899");

            return lingualeo.addWord(word.getWord(), word.getTranslation(), word.getContext());

        }
    }

}


