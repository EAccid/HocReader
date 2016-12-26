package com.eaccid.hocreader.presentation.preferences;

import android.content.Context;
import com.eaccid.hocreader.provider.translator.HocDictionary;
import rx.Observable;
import rx.Subscriber;

public class LeoAuthenticationSettings {

    private Context context; //TODO del after inject

    public LeoAuthenticationSettings(Context context) {
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
