package com.eaccid.hocreader.provider.semantic;

import android.content.Context;
import android.widget.ImageView;

import com.eaccid.hocreader.injection.App;
import com.eaccid.hocreader.injection.ApplicationContext;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

public class ImageViewManager {

    @Inject
    @ApplicationContext
    Context context;

    public ImageViewManager() {
        App.getAppComponent().inject(this);
    }

    public void loadPictureFromUrl(ImageView imageView, String url) {
        if (url.isEmpty())
            return;
        Picasso.with(context)
                .load(url)
//                .placeholder(R.drawable.ic_placeholder)
//                .error(R.drawable.ic_error_fallback)
                .into(imageView);

    }
}
