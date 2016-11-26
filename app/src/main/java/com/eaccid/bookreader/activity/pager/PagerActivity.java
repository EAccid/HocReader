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

import com.eaccid.bookreader.pagerfragments.FragmentTags;
import com.eaccid.bookreader.fragment_0.OnWordFromTextViewTouchListener;
import com.eaccid.bookreader.fragment_0.WordOutTranslatorDialogFragment;
import com.eaccid.bookreader.db.AppDatabaseManager;
import com.eaccid.bookreader.provider.DataProvider;
import com.eaccid.bookreader.provider.WordDatabaseDataProvider;
import com.eaccid.bookreader.provider.WordDatabaseProviderFragment;
import com.eaccid.bookreader.fragment_1.ItemPinnedMessageDialogFragment;
import com.eaccid.bookreader.pagerfragments.WordsFromBookFragment;
import com.eaccid.bookreader.file.FileToPagesListReader;
import com.eaccid.bookreader.R;
import com.eaccid.bookreader.translator.ReaderDictionary;
import com.eaccid.bookreader.translator.TranslatedWord;
import com.eaccid.bookreader.wordgetter.WordFromText;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;

public class PagerActivity extends FragmentActivity implements
        ItemPinnedMessageDialogFragment.ItemPinnedEventListener,
        OnWordFromTextViewTouchListener.OnWordFromTextClickListener,
        WordOutTranslatorDialogFragment.WordTranslationClickListener {

    private static ArrayList<String> pagesList = new ArrayList<>();
    private PagerAdapter pagerAdapter;
    private WordsFromBookFragment wordsFromBookFragment;

    public static ArrayList<String> getPagesList() {
        return pagesList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pager_fragment_activity);

        fillPagesListAndRefreshDatabase();

        pagerAdapter = new PagerAdapter(getSupportFragmentManager());
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
    public void onNotifyItemPinnedDialogDismissed(int itemPosition, boolean ok) {
        getDataProvider().getItem(itemPosition).setPinned(ok);
        wordsFromBookFragment.notifyItemChanged(itemPosition);
    }

    @Override
    public void OnWordClicked(WordFromText wordFromText) {

        AppDatabaseManager.setCurrentPageForAddingWord(wordFromText.getPageNumber());

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
        AppDatabaseManager.createOrUpdateWord(translatedWord.getWordBaseForm(),
                translatedWord.getTranslation(),
                translatedWord.getContext(),
                succeed);

        getDataProvider().addWord(translatedWord.getWordBaseForm());
        wordsFromBookFragment.notifyItemChanged();
    }

    private void fillPagesListAndRefreshDatabase() {
        String filePath = getIntent().getStringExtra("filePath");
        String fileName = getIntent().getStringExtra("fileName");
        FileToPagesListReader fileToPagesListReader = new FileToPagesListReader(this, filePath);
        pagesList = fileToPagesListReader.getPages();

        //TODO del book updating
        AppDatabaseManager.createOrUpdateBook(filePath, fileName, pagesList.size());
        //TODO set as WordFilter
        AppDatabaseManager.setCurrentBookForAddingWord(filePath);
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

    public void onItemFragment1Pinned(int position) {
        final DialogFragment dialog = ItemPinnedMessageDialogFragment.newInstance(position);

        getSupportFragmentManager()
                .beginTransaction()
                .add(dialog, FragmentTags.ITEM_PINNED_DIALOG)
                .commit();
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

}







