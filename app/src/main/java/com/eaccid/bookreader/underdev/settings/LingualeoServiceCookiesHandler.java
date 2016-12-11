package com.eaccid.bookreader.underdev.settings;


import android.content.Context;
import android.content.SharedPreferences;

import com.eaccid.hocreader.data.remote.libtranslator.lingualeo_impl.dictionary.LingualeoServiceCookies;

public class LingualeoServiceCookiesHandler implements LingualeoServiceCookies {

    private static final String LINGUALEO_COOKIES = "lingualeo_cookies";
    private SharedPreferences sp;

    public LingualeoServiceCookiesHandler(Context applicationContext) {
        sp = applicationContext.getSharedPreferences(LINGUALEO_COOKIES, Context.MODE_PRIVATE);
    }

    @Override
    public void storeCookies(String cookies) {
        storeCookiesOnDevice(cookies);
    }

    @Override
    public String loadCookies() {
        return loadCookiesFromDevice();
    }

    private void storeCookiesOnDevice(String cookies) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(LINGUALEO_COOKIES, cookies);
        editor.apply();
    }

    private String loadCookiesFromDevice() {
        return sp.getString(LINGUALEO_COOKIES, "");
    }

}