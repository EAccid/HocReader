package com.eaccid.hocreader.data.remote.libtranslator.lingualeo_impl.dictionary;

import com.eaccid.hocreader.App;
import com.eaccid.hocreader.data.remote.libtranslator.dictionary.DictionaryRx;
import com.eaccid.hocreader.data.remote.libtranslator.lingualeo_impl.connection.RequestHandler;
import com.eaccid.hocreader.data.remote.libtranslator.lingualeo_impl.connection.RequestParameters;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

public class LingualeoDictionaryRx implements DictionaryRx {
    //TODO refactor with retrofit using

    @Inject
    LingualeoServiceCookies cookies;

    public LingualeoDictionaryRx() {
        App.getAppComponent().inject(this);
    }

    @Override
    public Observable<Boolean> authorize(final String login, final String password) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                RequestParameters requestParameters = new RequestParameters();
                requestParameters.addParameter("email", login);
                requestParameters.addParameter("password", password);
                RequestHandler requestHandler = RequestHandler.newUnauthorizedRequestWithParameters("http://lingualeo.com/api/login", requestParameters);
                requestHandler.handleRequest();
                cookies.storeCookies(requestHandler.getCookies());
                subscriber.onNext(requestHandler.isHandleRequestSucceeded());
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
                requestHandler.handleRequest();
                subscriber.onNext(requestHandler.isHandleRequestSucceeded());
                subscriber.onCompleted();
            }
        });
    }
}
