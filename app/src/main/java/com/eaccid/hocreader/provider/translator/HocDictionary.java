package com.eaccid.hocreader.provider.translator;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.Size;
import com.eaccid.hocreader.underdevelopment.settings.LingualeoServiceCookiesHandler;
import com.eaccid.hocreader.data.remote.libtranslator.lingualeo_impl.dictionary.LingualeoDictionary;

public class HocDictionary {

    //TODO del, when preferences have been injected into LingualeoServiceCookiesHandler
    private Context context;

    public HocDictionary(Context context) {
        this.context = context;
    }

    public boolean addTranslatedWord(TranslatedWord word) {
        try {
            return new OuterDictionary().execute(word).get();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private class OuterDictionary extends AsyncTask<TranslatedWord, Void, Boolean> {
        @Override
        protected Boolean doInBackground(@Size(min = 1) TranslatedWord... words) {
            TranslatedWord word = words[0];
            LingualeoServiceCookiesHandler cookiesHandler = new LingualeoServiceCookiesHandler(context);
            LingualeoDictionary lingualeo = new LingualeoDictionary(cookiesHandler);
            boolean succeed = lingualeo.addWord(word.getWordFromContext(), word.getTranslation(), word.getContext());
            return succeed;
        }
    }

}


