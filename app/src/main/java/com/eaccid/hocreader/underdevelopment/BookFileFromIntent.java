package com.eaccid.hocreader.underdevelopment;

import android.app.Activity;

public interface BookFileFromIntent {
    boolean read(Activity context);

    String getName();

    String getPath();
}
