package com.eaccid.hocreader.provider.translator;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.Size;

import com.eaccid.hocreader.data.remote.libtranslator.lingualeo_impl.dictionary.LingualeoDictionary;

public class HocDictionary {

    //TODO del, when preferences have been injected into LingualeoServiceCookiesSettings
    //refactor class

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

    public boolean authorize(String email, String password) {
        try {
            return new DictionaryAuthorization().execute(email, password).get();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private class OuterDictionary extends AsyncTask<TranslatedWord, Void, Boolean> {
        @Override
        protected Boolean doInBackground(@Size(min = 1) TranslatedWord... words) {
            TranslatedWord word = words[0];
            LingualeoServiceCookiesSettings cookiesHandler = new LingualeoServiceCookiesSettings(context);
            LingualeoDictionary lingualeo = new LingualeoDictionary(cookiesHandler);
            boolean succeed = lingualeo.addWord(word.getWordFromContext(), word.getTranslation(), word.getContext());
            return succeed;
        }
    }

    private class DictionaryAuthorization extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(@Size(min = 2) String... email_password) {
            String email = email_password[0];
            String password = email_password[1];
            LingualeoServiceCookiesSettings cookiesHandler = new LingualeoServiceCookiesSettings(context);
            LingualeoDictionary lingualeo = new LingualeoDictionary(cookiesHandler);
            boolean succeed = lingualeo.authorize(email, password);
            return succeed;
        }
    }

}


