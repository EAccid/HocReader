package com.eaccid.hocreader.presentation.translation.semantic;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;

public class MediaPlayerManager {

    private final String LOG_TAG = "MediaPlayerManager";
    //TODO 'create and release player' methods
    //it is needed ot avoid: 'MediaPlayer finalized without being released'
    // when fragment has been destroyed and mp hasn't been released

    @Nullable
    public MediaPlayer createAndPreparePlayerFromURL(final String url) {
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync();
            Log.i(LOG_TAG, "MediaPlayer has been created.");
        } catch (IOException e) {
            Log.e(LOG_TAG, "W/System.err: java.io.IOException: setDataSource failed. Check internet connection.");
        }
        return mediaPlayer;
    }

    public void play(final MediaPlayer mediaPlayer) {
        if (mediaPlayer == null) {
            Log.e(LOG_TAG, "MediaPlayer is null. Check creation.");
            return;
        }
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        mediaPlayer.start();
    }
}
