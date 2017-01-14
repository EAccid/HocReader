package com.eaccid.hocreader.data.remote.libtranslator.lingualeo_impl.translator;

import com.eaccid.hocreader.data.remote.libtranslator.lingualeo_impl.connection.RequestHandler;
import com.eaccid.hocreader.data.remote.libtranslator.lingualeo_impl.connection.RequestParameters;
import com.eaccid.hocreader.data.remote.libtranslator.translator.TextTranslation;
import com.eaccid.hocreader.data.remote.libtranslator.translator.TranslatorRx;

import rx.Observable;
import rx.Subscriber;

public class LingualeoTranslatorRx implements TranslatorRx {

    @Override
    public Observable<TextTranslation> translate(final String word) {
        return Observable.create(new Observable.OnSubscribe<TextTranslation>() {
            @Override
            public void call(Subscriber<? super TextTranslation> subscriber) {
                RequestParameters requestParameters = new RequestParameters();
                requestParameters.addParameter("word", word);
                RequestHandler requestHandler = RequestHandler.newUnauthorizedRequestWithParameters("http://lingualeo.com/api/gettranslates", requestParameters);
                requestHandler.handleRequest();
                subscriber.onNext(new WordTranslation(requestHandler.getResponse()));
                subscriber.onCompleted();
            }
        });
    }
}
