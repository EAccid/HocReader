package com.eaccid.bookreader.activity;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.eaccid.bookreader.adapter.PagesArrayAdapter;
import com.eaccid.bookreader.file.FileToPagesReader;
import com.eaccid.bookreader.R;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;

public class PagerActivity extends FragmentActivity {

    private static ArrayList<String> pagesList = new ArrayList<String>();
    static final int NUM_ITEMS = 3;
    PagerAdapter pagerAdapter;
    ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_pager);

        //TODO file handler in separate class
        String mfilePath = getIntent().getStringExtra("filePath");
        FileToPagesReader fileToPagesReader = new FileToPagesReader(this, mfilePath);
        pagesList = fileToPagesReader.getPages();

        pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(pagerAdapter);

        CirclePageIndicator circleIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        circleIndicator.setViewPager(pager);

    }

    public static class PagerAdapter extends FragmentStatePagerAdapter {

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
            return PagesListFragment.newInstance(position);
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

        @Override
        public Parcelable saveState() {

            //TODO save state

            Bundle outState = new Bundle();
            outState.putStringArrayList("pagesList", pagesList);
//            ListView list = getListView();
//            outState.putInt("someVarB", list.getFirstVisiblePosition());
            return outState;
        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
            super.restoreState(state, loader);
            if (state != null) {
                Bundle bundle = (Bundle) state;
                bundle.setClassLoader(loader);
                pagesList = bundle.getStringArrayList("pagesList");
            }
        }
    }

    public static class PagesListFragment extends ListFragment {
        int mNum;

        static PagesListFragment newInstance(int num) {
            PagesListFragment f = new PagesListFragment();

            // Supply num input as an argument.
            Bundle args = new Bundle();
            args.putInt("num", num);
            f.setArguments(args);

            return f;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mNum = getArguments() != null ? getArguments().getInt("num") : 1;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View v = inflater.inflate(R.layout.fragment_page, container, false);
            View tv = v.findViewById(R.id.text);

            //TODO separate fragment mNum
            switch (mNum) {
                case 0:
                    ((TextView) tv).setText("Fragment #" + mNum + ": book");
                    break;
                case 1:
                    ((TextView) tv).setText("Fragment #" + mNum + ": added words");
                    break;
                case 2:
                    ((TextView) tv).setText("Fragment #" + mNum + ": word training");
                    break;
                default:
                    ((TextView) tv).setText("Fragment #" + mNum);
                    Toast.makeText(getContext(), "smth goes wrong!", Toast.LENGTH_SHORT).show();
                    break;
            }

            return v;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            switch (mNum) {
                case 0:

                    if (pagesList.size() > 0)
                        setListAdapter(
                                new PagesArrayAdapter(getContext(), R.layout.text_on_page, pagesList)
                        );

                    break;
                case 1:
                    break;
                case 2:
                    break;
                default:
                    Toast.makeText(getContext(), "smth goes wrong!", Toast.LENGTH_SHORT).show();
                    break;
            }

        }


    }

}






