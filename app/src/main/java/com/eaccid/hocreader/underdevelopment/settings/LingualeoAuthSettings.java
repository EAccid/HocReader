package com.eaccid.hocreader.underdevelopment.settings;

import android.content.Context;

import com.eaccid.hocreader.data.remote.libtranslator.lingualeo_impl.dictionary.LingualeoDictionary;

public class LingualeoAuthSettings implements Runnable {

    private Context context;

    public LingualeoAuthSettings(Context context) {
        this.context = context;
    }

    public void setUp() {
        Thread thread = new Thread(this, "lingualeo authentication");
        thread.start();
    }

    @Override
    public void run() {
        try {

            LingualeoServiceCookiesHandler cookiesHandler = new LingualeoServiceCookiesHandler(context);
            LingualeoDictionary lingualeo = new LingualeoDictionary(cookiesHandler);
            lingualeo.authorize("accidental899@mail.ru", "accid899");

            if (!lingualeo.isAuth()) {
                System.err.println("Fail Lingualeo authentication: " + LingualeoAuthSettings.class.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
