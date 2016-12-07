package com.eaccid.bookreader.activity.pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.eaccid.bookreader.pagerfragments.WordsCarouselTrainingFragment;
import com.eaccid.bookreader.pagerfragments.WordsFromBookFragment;
import com.eaccid.bookreader.pagerfragments.BookReaderListFragment;
import com.eaccid.bookreader.underdev.bookfragment_0.RecyclerViewFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {

    private Fragment wordsFromBookFragment;
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
//                return BookReaderListFragment.newInstance(position);
                return RecyclerViewFragment.newInstance();
            case 1:
                if (wordsFromBookFragment != null) {
                    return wordsFromBookFragment;
                }
                wordsFromBookFragment = new WordsFromBookFragment();
                return wordsFromBookFragment;
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