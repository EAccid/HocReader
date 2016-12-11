package com.eaccid.bookreader.activity.pager;

import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.eaccid.bookreader.file.pagesplitter.CharactersDefinerForFullScreenTextView;
import com.eaccid.bookreader.pagerfragments.FragmentTags;
import com.eaccid.bookreader.pagerfragments.fragment_0.OnWordFromTextViewTouchListener;
import com.eaccid.bookreader.pagerfragments.fragment_0.WordOutTranslatorDialogFragment;
import com.eaccid.bookreader.provider.DataProvider;
import com.eaccid.bookreader.provider.WordDatabaseDataProvider;
import com.eaccid.bookreader.provider.WordDatabaseProviderFragment;
import com.eaccid.bookreader.pagerfragments.fragment_1.WordsFromBookFragment;
import com.eaccid.bookreader.R;
import com.eaccid.hocreader.data.local.WordManager;
import com.eaccid.hocreader.data.remote.ReaderDictionary;
import com.eaccid.hocreader.data.remote.TranslatedWord;
import com.eaccid.bookreader.wordgetter.WordFromText;
import com.viewpagerindicator.CirclePageIndicator;

public class PagerActivity extends FragmentActivity implements
        OnWordFromTextViewTouchListener.OnWordFromTextClickListener,
        WordOutTranslatorDialogFragment.WordTranslationClickListener,
        CharactersDefinerForFullScreenTextView.PageView {

    private WordManager wordManager;
    private WordsFromBookFragment wordsFromBookFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pager_fragment_activity);
        wordManager = new WordManager();
        wordManager.loadDatabaseManager(this);
        RefreshDatabase();

        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setPageTransformer(true, new ZoomOutPageTransformer());
        pager.setAdapter(pagerAdapter);

        CirclePageIndicator circleIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        circleIndicator.setViewPager(pager);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(new WordDatabaseProviderFragment(), FragmentTags.DATA_PROVIDER)
                    .commit();
            //todo del fragment / add to transaction
            wordsFromBookFragment = (WordsFromBookFragment) pagerAdapter.getItem(1);
        }

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            //TODO del
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (wordsFromBookFragment != null && (positionOffset == 0)
                        && (position == 0 || position == 2)) {
                    Log.i("words list", "Word list has been filled by session words.");
                    getDataProvider().fillSessionDataList();
                    wordsFromBookFragment.notifyItemChanged();
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
    public void OnWordClicked(WordFromText wordFromText) {

        wordManager.setCurrentPageForAddingWord(wordFromText.getPageNumber());

        final DialogFragment dialog = WordOutTranslatorDialogFragment.newInstance(wordFromText);
        getSupportFragmentManager()
                .beginTransaction()
                .add(dialog, FragmentTags.ITEM_PINNED_DIALOG)
                .commit();

    }

    public WordDatabaseDataProvider getDataProvider() {
        final Fragment fragment = getSupportFragmentManager().findFragmentByTag(FragmentTags.DATA_PROVIDER);
        return ((WordDatabaseProviderFragment) fragment).getDataProvider();
    }

    @Override
    public void onTranslatedWord(TranslatedWord translatedWord) {
        ReaderDictionary readerDictionary = new ReaderDictionary(getApplicationContext());
        boolean succeed = readerDictionary.addTranslatedWord(translatedWord);

        //TODO del word updating
        wordManager.createOrUpdateWord(translatedWord.getWordBaseForm(),
                translatedWord.getTranslation(),
                translatedWord.getContext(),
                succeed);

        getDataProvider().addWord(translatedWord.getWordBaseForm());
        wordsFromBookFragment.notifyItemChanged();
    }

    private void RefreshDatabase() {
        String filePath = getIntent().getStringExtra("filePath");
        String fileName = getIntent().getStringExtra("fileName");

        //TODO set as WordFilter
        wordManager.setCurrentBookForAddingWord(filePath);
    }

    public void onItemFragment1Removed(int position) {
        Snackbar snackbar = Snackbar.make(
                findViewById(R.id.container),
                R.string.snack_bar_text_item_removed,
                Snackbar.LENGTH_LONG);

        snackbar.setAction(R.string.snack_bar_action_undo, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemFragment1UndoActionClicked();
            }
        });
        snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.snackbar_action_color_done));
        snackbar.show();
    }

    public void onItemFragment1Clicked(int position) {
        DataProvider.ItemDataProvider data = getDataProvider().getItem(position);
        if (data.isPinned()) {
            // unpin if tapped the pinned item
            data.setPinned(false);
            wordsFromBookFragment.notifyItemChanged(position);
        }
    }

    private void onItemFragment1UndoActionClicked() {
        int position = getDataProvider().undoLastRemoval();
        if (position >= 0) {
            wordsFromBookFragment.notifyItemInserted(position);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        wordManager.releaseDatabaseManager();
    }

    @Override
    public TextView getTextView() {
        //TODO settings: depends on user preferences
        ViewGroup viewGroup = (ViewGroup) getLayoutInflater().inflate(R.layout.book_page_text_default, null, false);
        return (TextView) viewGroup.findViewById(R.id.text_on_page);
    }

    public WordManager getWordData() {
        return wordManager;
    }
}







