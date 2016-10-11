package com.eaccid.bookreader.wordview;

import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.eaccid.TranslatorFactory;
import com.eaccid.translator.lingualeo.translator.WordTranslation;
import com.eaccid.translator.translator.TextTranslation;
import com.eaccid.translator.translator.Translator;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;


public class WordTranslator {


    private Context context;

    public WordTranslator(Context context) {
        this.context = context;
    }

    public void showTranslationView(Word word) {

        WordTranslation wordTranslation = (WordTranslation) translate(word);
        TranslationDialogFragment translationDialogFragment =  TranslationDialogFragment.newInstance(wordTranslation);
        translationDialogFragment.show();
    }

    private static TextTranslation translate(Word word) {

        String textWord = word.getText();

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

            Translator translator = TranslatorFactory.newTranslator();
            translator.translate(word);

            return translator.getTranslations();

        }
    }

}
