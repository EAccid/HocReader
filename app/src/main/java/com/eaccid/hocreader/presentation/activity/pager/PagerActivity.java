package com.eaccid.hocreader.presentation.activity.pager;

import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;
import com.eaccid.bookreader.file.pagesplitter.CharactersDefinerForFullScreenTextView;
import com.eaccid.hocreader.presentation.fragment.editor.WordsEditorFragment;
import com.eaccid.bookreader.R;
import com.eaccid.hocreader.presentation.BaseView;
import com.viewpagerindicator.CirclePageIndicator;

public class PagerActivity extends FragmentActivity implements BaseView,
        CharactersDefinerForFullScreenTextView.PageView {

    private PagerPresenter mPresenter;
    private WordsEditorFragment wordsEditorFragment; //TODO refactor: del from here

    @Override
    public PagerPresenter getPresenter() {
        return mPresenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pager_fragment_activity);
        if (mPresenter == null) mPresenter = new PagerPresenter();
        mPresenter.attachView(this);

        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setPageTransformer(true, new ZoomOutPageTransformer());
        pager.setAdapter(pagerAdapter);

        CirclePageIndicator circleIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        circleIndicator.setViewPager(pager);

        if (savedInstanceState == null) {
            wordsEditorFragment = (WordsEditorFragment) pagerAdapter.getItem(1);
        }

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            //TODO del, temp solution
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (wordsEditorFragment != null && (positionOffset == 0)
                        && (position == 0 || position == 2)) {
                    Log.i("words list", "Word list has been filled by session words.");
                    mPresenter.getDataProvider().fillSessionDataList();
                    wordsEditorFragment.notifyItemChanged();
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public TextView getTextView() {
        //TODO settings: depends on user preferences
        ViewGroup viewGroup = (ViewGroup) getLayoutInflater().inflate(R.layout.book_page_text_default, null, false);
        return (TextView) viewGroup.findViewById(R.id.text_on_page);
    }

    public void notifyItemChanged() {
        wordsEditorFragment.notifyItemChanged();
    }
}







