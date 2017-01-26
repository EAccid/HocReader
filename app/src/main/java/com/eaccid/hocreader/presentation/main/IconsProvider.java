package com.eaccid.hocreader.presentation.main;

import android.support.annotation.DrawableRes;

import com.eaccid.hocreader.R;
import com.eaccid.hocreader.temp.provider.file.findner.FileExtensions;

public class IconsProvider {

    public
    @DrawableRes
    int getFileExtensionsIconResId(FileExtensions extension) {
        if (extension == FileExtensions.TXT) {
            return R.drawable.ic_txt;
        } else {
            return R.drawable.ic_pdf;
        }
    }
}
