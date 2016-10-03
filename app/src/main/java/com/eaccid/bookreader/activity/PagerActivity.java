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

import com.eaccid.bookreader.fileview.PagesArrayAdapter;
import com.eaccid.bookreader.R;
import com.eaccid.bookreader.file.reader.TextFileReader;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;


public class PagerActivity extends FragmentActivity {

    private static ArrayList<String> textPagesList = new ArrayList<String>();
    static final int NUM_ITEMS = 3;
    static String mfilePath;
    PagerAdapter mPagerAdapter;
    ViewPager mPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_pager);

        mfilePath = getIntent().getStringExtra("filePath");

        mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mPagerAdapter);

        CirclePageIndicator circleIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        circleIndicator.setViewPager(mPager);

        loadFileInTextPagesList();

    }

    private void loadFileInTextPagesList() {
        TextFileReader textFileReader = new TextFileReader(this, mfilePath);
        textPagesList = textFileReader.getPages();
    }

    /*******************************************************************************
     * TODO https://developer.android.com/reference/android/support/v13/app/FragmentStatePagerAdapter.html
     * When pages are not visible to the user, their entire fragment may be destroyed
     */

    public static class PagerAdapter extends FragmentStatePagerAdapter {

        PagesListFragment pages;


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
            Bundle outState = new Bundle();
            outState.putStringArrayList("textPagesList", textPagesList);
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
                textPagesList = bundle.getStringArrayList("textPagesList");
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

//                    //TODO -ing now
//                    ListView lv = (ListView) v.findViewById(android.R.id.list);
//                    lv.setOnTouchListener(this);

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

//        @Override
//        public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//
////            //TODO just for practice clickableSpann
////            switch (v.getId()) {
////                case android.R.id.list:
////                    ListView lv = (ListView) v;
//////                    deleteClickableWordsOnTextView((TextView) lv.getChildAt());
////                    int currentPosition = lv.getFirstVisiblePosition();
////                    TextSpannHandler.setClickableWordsOnTextView(getContext(), (TextView) lv.getChildAt(currentPosition));
////                    if (currentPosition != lv.getCount() - 1)
////                        TextSpannHandler.setClickableWordsOnTextView(getContext(), (TextView) lv.getChildAt(lv.getFirstVisiblePosition() + 1));
////            }
//        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            switch (mNum) {
                case 0:

                    if (textPagesList.size() > 0)
//                        setListAdapter(new ArrayAdapter<>(getActivity(), R.layout.text_on_page, textPagesList.subList(0,3)));

                        setListAdapter(
                                new PagesArrayAdapter(getContext(), R.layout.text_on_page, textPagesList)
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

//        @Override
//        public boolean onTouch(View v, MotionEvent event) {
//            switch (v.getId()) {
//                case R.id.text_on_page:
//                    Toast.makeText(getContext(), "text_on_page clicked", Toast.LENGTH_SHORT).show();
//                    break;
//                case android.R.id.list:
//
//                    if (event.getAction() == MotionEvent.ACTION_DOWN)
//                    {
//
//                        TextView tv = (TextView) ((ListView) v).getChildAt(0);
//                        Layout layout = tv.getLayout();
//
//                        int x = (int) event.getX();
//                        int y = (int) event.getY();
//
//                        if (layout!=null) {
//                            int line = layout.getLineForVertical(y);
//                            int offset = layout.getOffsetForHorizontal(line, x);
//
//
////                            int currentItem = ((PagesArrayAdapter )getListAdapter()).getCurrentItem();
//
//
////                            Toast.makeText(getContext(),"offset: " + offset + " -> x = " + x + ", y = " + y, Toast.LENGTH_SHORT).show();
//                            System.out.println("offset: " + offset + " -> x = " + x + ", y = " + y);
//
//                        }
//
//                    }
//
//                    break;
//                default:
//
//            }
//
//            return false;
//        }


    }

}




