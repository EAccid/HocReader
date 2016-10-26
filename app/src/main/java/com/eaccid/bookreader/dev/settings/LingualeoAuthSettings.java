package com.eaccid.bookreader.dev.settings;

import android.content.Context;

import com.eaccid.libtranslator.lingualeo.dictionary.LingualeoDictionary;

public class LingualeoAuthSettings implements Runnable {

    Context context;
    Thread thread;

    public LingualeoAuthSettings(Context context) {
        this.context = context;
    }

    public void setUp() {
        thread = new Thread(this, "lingualeo authentication");
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
