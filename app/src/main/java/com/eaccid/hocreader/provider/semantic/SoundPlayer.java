package com.eaccid.hocreader.provider.semantic;

import com.eaccid.hocreader.underdevelopment.Releasable;

import rx.Observable;


public interface SoundPlayer extends Releasable {

    /**
     * @return Observable, that yield a single value: the  media source has completed successfully (true) or not (false)
     * and complete observer.
     */
    Observable<Boolean> play();

    @Override
    void release();
}
