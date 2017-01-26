package com.eaccid.hocreader.temp.provider.translator;

import android.content.SharedPreferences;

import com.eaccid.hocreader.injection.App;
import com.eaccid.hocreader.data.remote.libtranslator.lingualeo_impl.dictionary.LingualeoServiceCookies;

import javax.inject.Inject;

public class LingualeoServiceCookiesImpl implements LingualeoServiceCookies {

    private final String LINGUALEO_COOKIES = "lingualeo_cookies";
    @Inject
    SharedPreferences sp;

    public LingualeoServiceCookiesImpl() {
        App.getAppComponent().inject(this);
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