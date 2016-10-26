package com.eaccid.libtranslator.lingualeo.connection;

import java.util.List;


class LingualeoCookies {

    private String cookies = "";

    void setCookies(String cookies) {
        if (cookies == null) return;
        this.cookies = cookies;
    }

    String getCookies() {
        return cookies;
    }

    void setCookies(List<String> cookies) {

        StringBuilder sbCookie = new StringBuilder();
        for (String cookie: cookies
             ) {
            sbCookie.append(cookie.concat(";"));
        }
        this.cookies = sbCookie.toString();
    }
}
