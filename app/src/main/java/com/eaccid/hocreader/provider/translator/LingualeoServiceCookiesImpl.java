package com.eaccid.hocreader.provider.translator;

import android.content.Context;
import android.content.SharedPreferences;

import com.eaccid.hocreader.data.remote.libtranslator.lingualeo_impl.dictionary.LingualeoServiceCookies;
import com.eaccid.hocreader.presentation.settings.Preference;

public class LingualeoServiceCookiesImpl implements LingualeoServiceCookies {
    public static final String LINGUALEO_COOKIES = "lingualeo_cookies";
    private SharedPreferences sp;

    public LingualeoServiceCookiesImpl(Context context) {
        sp = context.getSharedPreferences(Preference.SHP_NAME_APP, Context.MODE_PRIVATE);
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