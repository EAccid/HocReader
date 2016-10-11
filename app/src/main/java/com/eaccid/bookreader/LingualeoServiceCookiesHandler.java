package com.eaccid.bookreader;


import com.eaccid.translator.lingualeo.dictionary.LingualeoServiceCookies;

class LingualeoServiceCookiesHandler implements LingualeoServiceCookies {

    private String cookies;

    @Override
    public void storeCookies(String cookies) {
        this.cookies = cookies;
    }

    @Override
    public String loadCookies() {
        return cookies;
    }
}