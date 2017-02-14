package com.eaccid.hocreader.presentation.pager;

import android.annotation.SuppressLint;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.eaccid.hocreader.presentation.book.BookFragment;
import com.eaccid.hocreader.presentation.weditor.WordsEditorFragment;
import com.eaccid.hocreader.presentation.weditor.WordsEditorView;
import com.eaccid.hocreader.provider.file.pagesplitter.CharactersDefinerForFullScreenTextView;
import com.eaccid.hocreader.provider.fromtext.WordFromText;
import com.eaccid.hocreader.provider.translator.TranslatedWord;
import com.eaccid.hocreader.presentation.FragmentTags;
import com.eaccid.hocreader.presentation.book.ins.OnWordFromPageViewTouchListener;
import com.eaccid.hocreader.R;
import com.eaccid.hocreader.presentation.translation.WordTranslationDialogFragment;
import com.eaccid.hocreader.underdevelopment.BookFileViaIntent;
import com.viewpagerindicator.CirclePageIndicator;

public class PagerActivity extends AppCompatActivity implements PagerView,
        CharactersDefinerForFullScreenTextView.PageView,
        OnWordFromPageViewTouchListener.OnWordFromTextClickListener,
        WordTranslationDialogFragment.OnWordTranslationClickListener {

    private PagerPresenter mPresenter;
    private WordsEditorView wordsEditorFragment;

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

        if (getIntent().getAction() != null && getIntent().getAction().equals("EDIT_WORDS")) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.pager_activity, WordsEditorFragment.newInstance(false), FragmentTags.WORDS_LIST_VIEW)
                    .commit();
            return;
        }

        //TODO delete after refactoring BookFileViaIntent
        BookFileViaIntent book = new BookFileViaIntent();
        if (!book.readFile(this)) {
            Toast.makeText(this, "The file cannot be opened", Toast.LENGTH_SHORT).show();
            finish();
        }
        mPresenter.createOrUpdateCurrentBook(book.getFileName(), book.getFilePath());
        if (getIntent().getData() != null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.pager_activity, BookFragment.newInstance(), FragmentTags.BOOK_READER)
                    .commit();
            return;
        }
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setPageTransformer(true, new ZoomOutPageTransformer());
        pager.setAdapter(pagerAdapter);
        CirclePageIndicator circleIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        circleIndicator.setViewPager(pager);
        if (savedInstanceState == null) {
            wordsEditorFragment = (WordsEditorView) pagerAdapter.getItem(1);
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
        @SuppressLint("InflateParams") ViewGroup viewGroup = (ViewGroup) getLayoutInflater().inflate(R.layout.book_page_text_item_0, null, false);
        return (TextView) viewGroup.findViewById(R.id.text_on_page);
    }

    @Override
    public void onWordClicked(WordFromText wordFromText) {
        mPresenter.OnWordFromTextViewClicked(wordFromText);
    }

    @Override
    public void onWordTranslated(TranslatedWord translatedWord) {
        mPresenter.onWordTranslated(translatedWord);
    }

    @Override
    public void showTranslationDialog(WordFromText wordFromText) {
        final DialogFragment dialog = WordTranslationDialogFragment.newInstance(wordFromText);
        getSupportFragmentManager()
                .beginTransaction()
                .add(dialog, FragmentTags.ITEM_PINNED_DIALOG)
                .commit();
    }

    public void notifyDataSetChanged() {
        wordsEditorFragment.notifyDataSetChanged();
    }

}







