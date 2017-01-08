package com.eaccid.hocreader.presentation.fragment.translation.semantic;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class ImageViewManager {

    Context context;

    //TODO inject
    public ImageViewManager(Context context) {
        this.context = context;
    }

    public void loadPictureFromUrl(ImageView imageView, String url) {
        Picasso.with(context)
                .load(url)
//                .placeholder(R.drawable.ic_placeholder)
//                .error(R.drawable.ic_error_fallback)
                .into(imageView);
    }

}
