package com.eaccid.hocreader.provider.translator;

import android.os.AsyncTask;
import android.support.annotation.Size;

import com.eaccid.hocreader.data.remote.TranslatorFactory;
import com.eaccid.hocreader.data.remote.Translators;
import com.eaccid.hocreader.provider.fromtext.WordFromText;
import com.eaccid.hocreader.data.remote.libtranslator.translator.TextTranslation;
import com.eaccid.hocreader.data.remote.libtranslator.translator.Translator;

public class HocTranslator {

    public TextTranslation translate(WordFromText wordFromText) {
        try {
            return new OuterTranslation().execute(wordFromText).get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    private class OuterTranslation extends AsyncTask<WordFromText, Void, TextTranslation> {
        @Override
        protected TextTranslation doInBackground(@Size(min = 1) WordFromText... words) {
            WordFromText word = words[0];
            Translator translator = TranslatorFactory.newTranslator(Translators.LINGUALEO);
            translator.translate(word.getText());
            return translator.getTranslations();
        }
    }

}
