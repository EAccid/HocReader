package com.eaccid.hocreader.presentation.fragment.translation.semantic;

import android.content.Context;
import android.widget.ImageView;

import com.eaccid.hocreader.injection.App;
import com.eaccid.hocreader.injection.ApplicationContext;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import de.hdodenhof.circleimageview.CircleImageView;

public class ImageViewManager {

    @Inject
    @ApplicationContext
    Context context;

    public ImageViewManager(Context context) {
        App.getAppComponent().inject(this);
    }

    public void loadPictureFromUrl(ImageView imageView, String url) {
        Picasso.with(context)
                .load(url)
//                .placeholder(R.drawable.ic_placeholder)
//                .error(R.drawable.ic_error_fallback)
                .into(imageView);
    }


}
