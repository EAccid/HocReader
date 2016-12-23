package com.eaccid.hocreader.presentation.activity.settings;

import android.content.Context;
import com.eaccid.hocreader.data.remote.libtranslator.lingualeo_impl.dictionary.LingualeoDictionary;
import rx.Observable;
import rx.Subscriber;

public class LeoAuthSettings {

    private Context context; //TODO del after inject

    public LeoAuthSettings(Context context) {
        this.context = context;
    }

    public Observable<Boolean> leoSignInObservable(final String email, final String password) {
        return Observable.create(
                new Observable.OnSubscribe<Boolean>() {
                    @Override
                    public void call(Subscriber<? super Boolean> subscriber) {
                        boolean isAuth = false;
                        try {
                            LingualeoServiceCookiesHandler cookiesHandler = new LingualeoServiceCookiesHandler(context);
                            LingualeoDictionary lingualeo = new LingualeoDictionary(cookiesHandler);
                            isAuth = lingualeo.authorize(email, password);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        subscriber.onNext(isAuth);
                        subscriber.onCompleted();
                    }
                });
    }
}
