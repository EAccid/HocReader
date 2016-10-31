package com.eaccid.bookreader.activity.pager;

import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
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

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
//                switch (position) {
//                    case 0:
//                    case 2:
//                        getDataProvider().fillDataList();
//                        final Fragment fragment = getSupportFragmentManager().findFragmentByTag(FragmentTags.FRAGMENT_WORDS_LIST_VIEW);
//                        getSupportFragmentManager().beginTransaction()
//                                .remove(fragment).commit();
//
//                        ((WordsFromBookFragment) fragment).notifyDataChanged();
//
//
//                        getSupportFragmentManager().beginTransaction()
//                                .add(new WordsFromBookFragment(), "RAGMENT_WORDS_LIST_VIEW1")
//                                .commit();
////                        final Fragment fragment1 = getSupportFragmentManager().findFragmentByTag(FragmentTags.FRAGMENT_WORDS_LIST_VIEW);
////                        ((WordsFromBookFragment) fragment1).notifyDataChanged();
//
////                        getSupportFragmentManager().beginTransaction().attach(fragment).commit();
//                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }


        });

        CirclePageIndicator circleIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        circleIndicator.setViewPager(pager);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(new WordDatabaseProviderFragment(), FragmentTags.FRAGMENT_TAG_DATA_PROVIDER)
                    .commit();
            getSupportFragmentManager().beginTransaction()
                    .add(new WordsFromBookFragment(), FragmentTags.FRAGMENT_WORDS_LIST_VIEW)
                    .commit();
        }

    }


    @Override
    public void onNotifyItemPinnedDialogDismissed(int itemPosition, boolean ok) {
        final Fragment fragment = getSupportFragmentManager().findFragmentByTag(FragmentTags.FRAGMENT_WORDS_LIST_VIEW);

        getDataProvider().getItem(itemPosition).setPinned(ok);
        ((WordsFromBookFragment) fragment).notifyItemChanged(itemPosition);
    }

    @Override
    public void OnWordClicked(WordFromText wordFromText) {

        AppDatabaseManager.setCurrentPageForAddingWord(wordFromText.getPageNumber());

        final DialogFragment dialog = WordOutTranslatorDialogFragment.newInstance(wordFromText);
        getSupportFragmentManager()
                .beginTransaction()
                .add(dialog, FragmentTags.FRAGMENT_TAG_ITEM_PINNED_DIALOG)
                .commit();

    }

    public WordDatabaseDataProvider getDataProvider() {
        final Fragment fragment = getSupportFragmentManager().findFragmentByTag(FragmentTags.FRAGMENT_TAG_DATA_PROVIDER);
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
        getDataProvider().fillSessionDataList(); //todo / tem refilling
        pagerAdapter.notifyDataSetChanged();

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
                .add(dialog, FragmentTags.FRAGMENT_TAG_ITEM_PINNED_DIALOG)
                .commit();
    }

    public void onItemFragment1Clicked(int position) {
        final Fragment fragment = getSupportFragmentManager().findFragmentByTag(FragmentTags.FRAGMENT_WORDS_LIST_VIEW);
        DataProvider.ItemDataProvider data = getDataProvider().getItem(position);

        if (data.isPinned()) {
            // unpin if tapped the pinned item
            data.setPinned(false);
            ((WordsFromBookFragment) fragment).notifyItemChanged(position);
        }
    }

    private void onItemFragment1UndoActionClicked() {
        int position = getDataProvider().undoLastRemoval();
        if (position >= 0) {
            final Fragment fragment = getSupportFragmentManager().findFragmentByTag(FragmentTags.FRAGMENT_WORDS_LIST_VIEW);
            ((WordsFromBookFragment) fragment).notifyItemInserted(position);
        }
    }

}








