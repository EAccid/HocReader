package com.eaccid.hocreader.provider.semantic;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.util.Log;
import android.widget.ImageView;

import com.eaccid.hocreader.injection.App;
import com.eaccid.hocreader.injection.ApplicationContext;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

public class ImageViewManager {

    @Inject
    @ApplicationContext
    Context context;
    private final String LOG_TAG = "ImageViewManager";

    public ImageViewManager() {
        App.getAppComponent().inject(this);
    }

    public void loadPictureFromUrl(
            final ImageView imageView,
            final String url,
            final @DrawableRes int placeholderId,
            final @DrawableRes int errorId) {

        if (url.isEmpty())
            return;
        Picasso
                .with(context)
                .load(url)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .placeholder(placeholderId)
                .error(errorId)
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        Log.v(LOG_TAG, "Picasso has fetched the image from cache");
                    }

                    @Override
                    public void onError() {
                        Picasso.with(context)
                                .load(url)
                                .error(errorId)
                                .into(imageView, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                        Log.v(LOG_TAG, "Picasso has fetched the image from url online");
                                    }

                                    @Override
                                    public void onError() {
                                        Log.v(LOG_TAG, "Picasso could not fetch the image from cache and url online");
                                    }
                                });
                    }
                });
    }
}
