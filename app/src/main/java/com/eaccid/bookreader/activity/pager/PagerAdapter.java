package com.eaccid.bookreader.activity.pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

import com.eaccid.bookreader.pagerfragments.WordsCarouselTrainingFragment;
import com.eaccid.bookreader.pagerfragments.WordsFromBookFragment;
import com.eaccid.bookreader.pagerfragments.BookReaderListFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {

    private static final int NUM_ITEMS = 3;

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
                return new WordsFromBookFragment() ;
            case 2:
                return WordsCarouselTrainingFragment.newInstance();
            default:
                throw new IllegalStateException();
        }
    }

    @Override
    public int getItemPosition(Object object) {
        if (object instanceof WordsFromBookFragment) {
            return POSITION_NONE;
        }
        return super.getItemPosition(object);
    }

}