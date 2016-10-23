package com.eaccid.bookreader.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.eaccid.bookreader.adapter.PagesAdapter;
import com.eaccid.bookreader.dev.AppDatabaseManager;
import com.eaccid.bookreader.file.FileToPagesReader;
import com.eaccid.bookreader.R;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;

public class MainFragmentActivity extends FragmentActivity {

    private static ArrayList<String> pagesList = new ArrayList<String>();
    static final int NUM_ITEMS = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_pager);


        fillPagesListAndRefreshDatabase();

        MainPagerAdapter mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(mainPagerAdapter);

        CirclePageIndicator circleIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        circleIndicator.setViewPager(pager);


    }

    private void fillPagesListAndRefreshDatabase() {
        String filePath = getIntent().getStringExtra("filePath");
        String fileName = getIntent().getStringExtra("fileName");
        FileToPagesReader fileToPagesReader = new FileToPagesReader(this, filePath);
        pagesList = fileToPagesReader.getPages();

        AppDatabaseManager.createOrUpdateBook(filePath, fileName, pagesList.size());
        AppDatabaseManager.setCurrentBookForAddingWord(filePath);
    }

    public static class MainPagerAdapter extends FragmentStatePagerAdapter {

        /*******************************************************************************
         * TODO https://developer.android.com/reference/android/support/v13/app/FragmentStatePagerAdapter.html
         * When pages are not visible to the user, their entire fragment may be destroyed
         */

        MainPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Override
        public Fragment getItem(int position) {
            return MainListFragment.newInstance(position);
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

    }


    public static class MainListFragment extends ListFragment {
        private int mNum;
        private int curentBookPage = 0;

        static MainListFragment newInstance(int num) {
            MainListFragment f = new MainListFragment();

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
                    PagesAdapter pagesAdapter = new PagesAdapter(getContext(), R.id.text_on_page, pagesList);
                    if (pagesList.size() > 0)
                        setListAdapter(pagesAdapter);
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


        @Override
        public void onSaveInstanceState(Bundle outState) {
            ListView list = getListView();
            outState.putInt("firstVisiblePosition", list.getFirstVisiblePosition());
            super.onSaveInstanceState(outState);
        }

    }

}






