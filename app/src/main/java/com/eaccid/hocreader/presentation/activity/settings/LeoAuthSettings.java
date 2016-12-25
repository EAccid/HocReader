package com.eaccid.hocreader.presentation.activity.settings;

import android.content.Context;

import com.eaccid.hocreader.provider.translator.HocDictionary;

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
                        HocDictionary hocDictionary = new HocDictionary(context);
                        boolean isAuth = hocDictionary.authorize(email, password);
                        subscriber.onNext(isAuth);
                        subscriber.onCompleted();
                    }
                });
    }
}
