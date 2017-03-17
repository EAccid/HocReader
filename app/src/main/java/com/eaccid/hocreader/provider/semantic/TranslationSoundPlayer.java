package com.eaccid.hocreader.provider.semantic;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;

import rx.Observable;

public class TranslationSoundPlayer implements SoundPlayer<String> {
    private final String LOG_TAG = "TranslationSoundPlayer";
    private final MediaPlayer mediaPlayer;

    public TranslationSoundPlayer() {
        mediaPlayer = new MediaPlayer();
    }

    @Override
    public void preparePlayerFromSource(final String url) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync();
            Log.i(LOG_TAG, "MediaPlayer has been created.");
        } catch (IOException e) {
            Log.e(LOG_TAG, "java.io.IOException -> setDataSource failed. Check internet connection.");
        }
    }

    /**
     * @return Observable, that yield a single value: the  media source has completed successfully (true) or not (false)
     * and complete observer.
     */
    @Override
    public Observable<Boolean> play() {
        return Observable.create(subscriber -> {
                    try {
                        if (mediaPlayer.isPlaying()) {
                            return;
                        }
                        mediaPlayer.start();
                        mediaPlayer.setOnCompletionListener(mp -> {
                            subscriber.onNext(true);
                            subscriber.onCompleted();
                        });
                    } catch (IllegalStateException e) {
                        Log.e(LOG_TAG, "MediaPlayer has not been prepared correctly.");
                        subscriber.onNext(false);
                        subscriber.onCompleted();
                    }
                }
        );
    }

    @Override
    public void release() {
        mediaPlayer.release();
    }
}
