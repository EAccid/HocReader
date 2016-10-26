package com.eaccid.bookreader.dev.settings;


import android.app.Application;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.eaccid.libtranslator.lingualeo.dictionary.LingualeoServiceCookies;

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