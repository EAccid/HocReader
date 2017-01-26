package com.eaccid.hocreader.presentation.settings.preferences;

import com.eaccid.hocreader.provider.translator.HocDictionaryProvider;
import rx.Observable;

public class LeoAuthenticationSettings {

    public Observable<Boolean> leoSignInObservable(final String email, final String password) {
        return new HocDictionaryProvider().authorize(email, password);
    }
}
