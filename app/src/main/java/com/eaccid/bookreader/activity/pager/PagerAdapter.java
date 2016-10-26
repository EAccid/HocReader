package com.eaccid.bookreader.activity.pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.eaccid.bookreader.pagerfragments.CarouselPreviewFragment;
import com.eaccid.bookreader.pagerfragments.WordRecyclerViewFragment;
import com.eaccid.bookreader.pagerfragments.BookReaderListFragment;

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
                return WordRecyclerViewFragment.newInstance();
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