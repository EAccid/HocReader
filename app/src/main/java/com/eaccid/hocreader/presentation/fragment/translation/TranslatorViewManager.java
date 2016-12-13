package com.eaccid.hocreader.presentation.fragment.translation;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.support.annotation.Size;
import android.widget.ImageView;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class TranslatorViewManager {


    public void loadPictureFromURL(ImageView wordPicture, String url) {
        new ImageWordPictureLoader(wordPicture).execute(url);
    }

    //TODO E/MediaPlayer: Should have subtitle controller already set
    public void loadSoundFromURL(MediaPlayer mMediaPlayer, String soundUrl) {
        new PlayerWordSoundLoader(mMediaPlayer).execute(soundUrl);
    }

    private class ImageWordPictureLoader extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        ImageWordPictureLoader(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(@Size(min = 1) String... urls) {
            String url = urls[0];
            Bitmap wordImage = null;
            InputStream is = null;
            try {
                is = new URL(url).openStream();
                wordImage = BitmapFactory.decodeStream(is);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (is != null)
                        is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return wordImage;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
        }

    }

    private class PlayerWordSoundLoader extends AsyncTask<String, Void, Void> {
        MediaPlayer mediaPlayer;

        PlayerWordSoundLoader(MediaPlayer mediaPlayer) {
            this.mediaPlayer = mediaPlayer;
        }

        @Override
        protected Void doInBackground(@Size(min = 1) String... urls) {
            String url = urls[0];
            try {
                mediaPlayer.setDataSource(url);
                mediaPlayer.prepare();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
