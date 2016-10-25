package com.eaccid.bookreader.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.eaccid.bookreader.apagersfragment.CarouselPreviewFragment;
import com.eaccid.bookreader.apagersfragment.SwipeOnLongPressExampleFragment;
import com.eaccid.bookreader.apagersfragment.BookReaderListFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {

    static final int NUM_ITEMS = 3;

    /*******************************************************************************
     * TODO https://developer.android.com/reference/android/support/v13/app/FragmentStatePagerAdapter.html
     * When pages are not visible to the user, their entire fragment may be destroyed
     */

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return BookReaderListFragment.newInstance(position);
            case 1:
                return SwipeOnLongPressExampleFragment.newInstance();
            case 2:
                return CarouselPreviewFragment.newInstance();
            default:
                throw new IllegalStateException();
        }

    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

}