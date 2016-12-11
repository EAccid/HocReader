package com.eaccid.hocreader.data.remote;

import com.eaccid.bookreader.wordgetter.WordFromText;
import com.eaccid.hocreader.data.remote.libtranslator.translator.TextTranslation;
import com.eaccid.hocreader.data.remote.libtranslator.translator.Translator;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

public class ReaderTranslator {

    public static TextTranslation translate(WordFromText wordFromText) {

        String textWord = wordFromText.getText();

        try {

            ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
            OuterTranslation outerTranslation = new OuterTranslation(textWord);
            Future<TextTranslation> translationResult = executor.submit(outerTranslation);
            return translationResult.get();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    private static class OuterTranslation implements Callable<TextTranslation> {
        private String word;

        OuterTranslation(String word) {
            this.word = word;
        }

        @Override
        public TextTranslation call() throws Exception {

            Translator translator = TranslatorFactory.newTranslator(Translators.LINGUALEO);
            translator.translate(word);

            return translator.getTranslations();

        }
    }

}
