package com.eaccid.hocreader.temp.presentation.preferences;

import com.eaccid.hocreader.temp.provider.translator.HocDictionaryProvider;
import rx.Observable;

public class LeoAuthenticationSettings {

    public Observable<Boolean> leoSignInObservable(final String email, final String password) {
        return new HocDictionaryProvider().authorize(email, password);
    }
}
