package com.eaccid.hocreader.presentation.pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.eaccid.hocreader.presentation.book.BookFragment;
import com.eaccid.hocreader.presentation.training.WordTrainingFragment;
import com.eaccid.hocreader.presentation.weditor.WordsEditorFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {

    private Fragment wordsFromBookFragment;
    private static final int NUM_ITEMS = 3;

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
                return BookFragment.newInstance();
            case 1:
                if (wordsFromBookFragment == null) {
                    wordsFromBookFragment = WordsEditorFragment.newInstance(true);
                    return wordsFromBookFragment;
                }
                return wordsFromBookFragment;
            case 2:
                return WordTrainingFragment.newInstance(true);
            default:
                throw new IllegalStateException("There isn't such fragment position: " + position);
        }
    }

    @Override
    public int getItemPosition(Object object) {
        if (object instanceof WordsEditorFragment) {
            return POSITION_NONE;
        }
        return super.getItemPosition(object);
    }
}