package com.eaccid.hocreader.data.remote.libtranslator.lingualeo_impl.dictionary;

import com.eaccid.hocreader.data.remote.libtranslator.lingualeo_impl.RequestExceptionHandlerImpl;
import com.eaccid.hocreader.App;
import com.eaccid.hocreader.data.remote.libtranslator.dictionary.DictionaryRx;
import com.eaccid.hocreader.data.remote.libtranslator.lingualeo_impl.connection.RequestHandler;
import com.eaccid.hocreader.data.remote.libtranslator.lingualeo_impl.connection.RequestParameters;

import rx.Observable;
import rx.Subscriber;

public class LingualeoDictionaryRx implements DictionaryRx {
    private LingualeoServiceCookies cookies;

    public LingualeoDictionaryRx() {
        this.cookies = App.getLeoCookies();
    }

    @Override
    public Observable<Boolean> authorize(final String login, final String password) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                RequestHandler requestHandler = createAndHandleAuthRequestHandler(login, password);
                subscriber.onNext(requestHandler.isHandleRequestSucceeded());
                subscriber.onCompleted();
            }
        });
    }

    @Override
    public Observable<AuthParameters> authorizeAndReturn(final String login, final String password) {
        return Observable.create(new Observable.OnSubscribe<AuthParameters>() {
            @Override
            public void call(Subscriber<? super AuthParameters> subscriber) {
                RequestHandler requestHandler = createAndHandleAuthRequestHandler(login, password);
                AuthParameters authParameters = new AuthParameters(requestHandler.getResponse());
                authParameters.setAuth(requestHandler.isHandleRequestSucceeded());
                authParameters.setEmail(login);
                subscriber.onNext(authParameters);
                subscriber.onCompleted();
            }
        });
    }

    @Override
    public Observable<Boolean> isAuth() {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                RequestHandler requestHandler = RequestHandler.newAuthorizedRequest("http://lingualeo.com/api/isauthorized", cookies.loadCookies());
                requestHandler.setRequestExceptionHandler(new RequestExceptionHandlerImpl());
                requestHandler.handleRequest();
                subscriber.onNext(requestHandler.isHandleRequestSucceeded());
                subscriber.onCompleted();
            }
        });
    }

    @Override
    public Observable<Boolean> addWord(final String word, final String textTranslation, final String context) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                RequestParameters requestParameters = new RequestParameters();
                requestParameters.addParameter("word", word);
                requestParameters.addParameter("textTranslation", textTranslation);
                requestParameters.addParameter("context", context);
                RequestHandler requestHandler = RequestHandler.newAuthorizedRequestWithParameters("http://lingualeo.com/api/addword", cookies.loadCookies(), requestParameters);
                requestHandler.setRequestExceptionHandler(new RequestExceptionHandlerImpl());
                requestHandler.handleRequest();
                subscriber.onNext(requestHandler.isHandleRequestSucceeded());
                subscriber.onCompleted();
            }
        });
    }

    private RequestHandler createAndHandleAuthRequestHandler(final String login, final String password) {
        RequestParameters requestParameters = new RequestParameters();
        requestParameters.addParameter("email", login);
        requestParameters.addParameter("password", password);
        RequestHandler requestHandler = RequestHandler.newUnauthorizedRequestWithParameters("http://lingualeo.com/api/login", requestParameters);
        requestHandler.setRequestExceptionHandler(new RequestExceptionHandlerImpl());
        requestHandler.handleRequest();
        cookies.storeCookies(requestHandler.getCookies());
        return requestHandler;
    }

}
