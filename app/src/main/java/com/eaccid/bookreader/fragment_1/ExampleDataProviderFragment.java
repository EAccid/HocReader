package com.eaccid.bookreader.fragment_1;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class ExampleDataProviderFragment extends Fragment {
    private ExampleDataProvider mDataProvider;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);  // keep the mDataProvider instance
        mDataProvider = new ExampleDataProvider();
    }

    public ExampleDataProvider getDataProvider() {
        return mDataProvider;
    }
}