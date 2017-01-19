package com.eaccid.hocreader.presentation.activity.pager;

import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eaccid.hocreader.provider.file.pagesplitter.CharactersDefinerForFullScreenTextView;
import com.eaccid.hocreader.provider.fromtext.WordFromText;
import com.eaccid.hocreader.provider.translator.TranslatedWord;
import com.eaccid.hocreader.presentation.fragment.FragmentTags;
import com.eaccid.hocreader.presentation.fragment.book.OnWordFromPageViewTouchListener;
import com.eaccid.hocreader.presentation.fragment.weditor.WordsEditorFragment;
import com.eaccid.hocreader.R;
import com.eaccid.hocreader.presentation.BaseView;
import com.eaccid.hocreader.presentation.fragment.translation.WordTranslationDialogFragment;
import com.viewpagerindicator.CirclePageIndicator;

public class PagerActivity extends AppCompatActivity implements BaseView,
        CharactersDefinerForFullScreenTextView.PageView,
        OnWordFromPageViewTouchListener.OnWordFromTextClickListener,
        WordTranslationDialogFragment.OnWordTranslationClickListener {

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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public TextView getTextView() {
        //TODO settings: depends on user preferences
        ViewGroup viewGroup = (ViewGroup) getLayoutInflater().inflate(R.layout.book_page_text_item_0, null, false);
        return (TextView) viewGroup.findViewById(R.id.text_on_page);
    }

    public void notifyItemChanged() {
        wordsEditorFragment.notifyDataSetChanged();
    }

    @Override
    public void onWordClicked(WordFromText wordFromText) {
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







