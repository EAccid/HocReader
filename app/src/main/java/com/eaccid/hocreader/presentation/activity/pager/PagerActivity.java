package com.eaccid.hocreader.presentation.activity.pager;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;
import com.eaccid.bookreader.file.pagesplitter.CharactersDefinerForFullScreenTextView;
import com.eaccid.bookreader.wordgetter.WordFromText;
import com.eaccid.hocreader.data.remote.TranslatedWord;
import com.eaccid.hocreader.presentation.fragment.FragmentTags;
import com.eaccid.hocreader.presentation.fragment.book.OnWordFromPageViewTouchListener;
import com.eaccid.hocreader.presentation.fragment.editor.WordsEditorFragment;
import com.eaccid.bookreader.R;
import com.eaccid.hocreader.presentation.BaseView;
import com.eaccid.hocreader.presentation.fragment.translation.WordTranslationDialogFragment;
import com.viewpagerindicator.CirclePageIndicator;

public class PagerActivity extends FragmentActivity implements BaseView,
        CharactersDefinerForFullScreenTextView.PageView,
        OnWordFromPageViewTouchListener.OnWordFromTextClickListener,
        WordTranslationDialogFragment.WordTranslationClickListener {

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

    @Override
    public void OnWordClicked(WordFromText wordFromText) {
        mPresenter.OnWordFromTextViewClicked(wordFromText);
        final DialogFragment dialog = WordTranslationDialogFragment.newInstance(wordFromText);
        getSupportFragmentManager()
                .beginTransaction()
                .add(dialog, FragmentTags.ITEM_PINNED_DIALOG)
                .commit();
    }

    @Override
    public void onWordTranslated(TranslatedWord translatedWord) {
        mPresenter.onWordTranslated(translatedWord);
    }
}







