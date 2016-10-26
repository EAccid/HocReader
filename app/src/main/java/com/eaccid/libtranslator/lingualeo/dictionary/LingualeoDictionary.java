package com.eaccid.libtranslator.lingualeo.dictionary;

import com.eaccid.libtranslator.dictionary.Dictionary;
import com.eaccid.libtranslator.lingualeo.connection.RequestHandler;
import com.eaccid.libtranslator.lingualeo.connection.RequestParameters;

public class LingualeoDictionary implements Dictionary {

    private LingualeoServiceCookies cookies;

    public LingualeoDictionary(LingualeoServiceCookies cookies) {
        this.cookies = cookies;
    }

    @Override
    public boolean authorize(String login, String password) {

        RequestParameters requestParameters = new RequestParameters();
        requestParameters.addParameter("email", login);
        requestParameters.addParameter("password", password);

        RequestHandler requestHandler = RequestHandler.newUnauthorizedRequestWithParameters("http://lingualeo.com/api/login", requestParameters);
        requestHandler.handleRequest();
        cookies.storeCookies(requestHandler.getResponseCookies());

        return requestHandler.isHandleRequestSucceeded();

    }

    @Override
    public boolean isAuth() {

        RequestHandler requestHandler = RequestHandler.newAuthorizedRequest("http://lingualeo.com/api/isauthorized", cookies.loadCookies());
        requestHandler.handleRequest();
        return requestHandler.isHandleRequestSucceeded();
    }

    @Override
    public boolean addWord(String word, String textTranslation, String context) {

        RequestParameters requestParameters = new RequestParameters();
        requestParameters.addParameter("word", word);
        requestParameters.addParameter("textTranslation", textTranslation);
        requestParameters.addParameter("context", context);

        RequestHandler requestHandler = RequestHandler.newAuthorizedRequestWithParameters("http://lingualeo.com/api/addword", cookies.loadCookies(), requestParameters);
        requestHandler.handleRequest();
        return requestHandler.isHandleRequestSucceeded();
    }

}
