package com.eaccid.hocreader.provider.semantic;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;

import rx.Observable;

public class TranslationSoundPlayer implements SoundPlayer {
    private final String LOG_TAG = "TranslationSoundPlayer";
    private MediaPlayer mediaPlayer;

    private TranslationSoundPlayer() {
    }

    public static SoundPlayer createAndPreparePlayerFromUrl(final String url) {
        TranslationSoundPlayer player = new TranslationSoundPlayer();
        player.preparePlayerFromUrl(url);
        return player;
    }

    /**
     * @return Observable, that yield a single value: the  media source has completed successfully (true) or not (false)
     * and complete observer.
     */
    @Override
    public Observable<Boolean> play() {
        return Observable.create(subscriber -> {
                    if (!isPlayerPrepared()) {
                        Log.e(LOG_TAG, "MediaPlayer has not been prepared correctly.");
                        subscriber.onNext(false);
                        subscriber.onCompleted();
                    }
                    if (mediaPlayer.isPlaying())
                        mediaPlayer.stop();
                    mediaPlayer.start();
                    mediaPlayer.setOnCompletionListener(mp -> {
                        subscriber.onNext(true);
                        subscriber.onCompleted();
                    });
                }
        );
    }

    @Override
    public void release() {
        if (isPlayerPrepared())
            mediaPlayer.release();
    }

    private void preparePlayerFromUrl(String url) {
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync();
            Log.i(LOG_TAG, "MediaPlayer has been created.");
        } catch (IOException e) {
            Log.e(LOG_TAG, "java.io.IOException -> setDataSource failed. Check internet connection.");
        }
    }

    private boolean isPlayerPrepared() {
        if (mediaPlayer == null) {
            return false;
        }
        return true;
    }

}
