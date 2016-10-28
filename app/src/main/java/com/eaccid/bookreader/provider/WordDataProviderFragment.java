package com.eaccid.bookreader.provider;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class WordDataProviderFragment extends Fragment {
    private WordDataProvider mDataProvider;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);  // keep the mDataProvider instance
        mDataProvider = new WordDataProvider();

    }

    public WordDataProvider getDataProvider() {
        return mDataProvider;
    }
}