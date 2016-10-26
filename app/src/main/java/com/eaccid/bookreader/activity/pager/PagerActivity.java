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
import com.eaccid.bookreader.fragment_1.ExampleDataProvider;
import com.eaccid.bookreader.fragment_1.ExampleDataProviderFragment;
import com.eaccid.bookreader.fragment_1.ItemPinnedMessageDialogFragment;
import com.eaccid.bookreader.pagerfragments.WordRecyclerViewFragment;
import com.eaccid.bookreader.dev.AppDatabaseManager;
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
        WordOutTranslatorDialogFragment.WordTranslationClickListener
{

    private static ArrayList<String> pagesList = new ArrayList<>();


    private void fillPagesListAndRefreshDatabase() {
        String filePath = getIntent().getStringExtra("filePath");
        String fileName = getIntent().getStringExtra("fileName");
        FileToPagesListReader fileToPagesListReader = new FileToPagesListReader(this, filePath);
        pagesList = fileToPagesListReader.getPages();

        AppDatabaseManager.createOrUpdateBook(filePath, fileName, pagesList.size());
        AppDatabaseManager.setCurrentBookForAddingWord(filePath);
    }

    public static ArrayList<String> getPagesList() {
        return pagesList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pager_fragment_activity);

        fillPagesListAndRefreshDatabase();

        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setPageTransformer(true, new ZoomOutPageTransformer());
        pager.setAdapter(pagerAdapter);

        CirclePageIndicator circleIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        circleIndicator.setViewPager(pager);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(new ExampleDataProviderFragment(), FragmentTags.FRAGMENT_TAG_DATA_PROVIDER)
                    .commit();
            getSupportFragmentManager().beginTransaction()
                    .add(new WordRecyclerViewFragment(), FragmentTags.FRAGMENT_LIST_VIEW)
                    .commit();
        }

    }


//fragment 2 change words

    public void onItemRemoved(int position) {
        Snackbar snackbar = Snackbar.make(
                findViewById(R.id.container),
                R.string.snack_bar_text_item_removed,
                Snackbar.LENGTH_LONG);

        snackbar.setAction(R.string.snack_bar_action_undo, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemUndoActionClicked();
            }
        });
        snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.snackbar_action_color_done));
        snackbar.show();
    }

    public void onItemPinned(int position) {
        final DialogFragment dialog = ItemPinnedMessageDialogFragment.newInstance(position);

        getSupportFragmentManager()
                .beginTransaction()
                .add(dialog, FragmentTags.FRAGMENT_TAG_ITEM_PINNED_DIALOG)
                .commit();
    }

    public void onItemClicked(int position) {
        final Fragment fragment = getSupportFragmentManager().findFragmentByTag(FragmentTags.FRAGMENT_LIST_VIEW);
        ExampleDataProvider.ConcreteData data = getDataProvider().getItem(position);

        if (data.isPinned()) {
            // unpin if tapped the pinned item
            data.setPinned(false);
            ((WordRecyclerViewFragment) fragment).notifyItemChanged(position);
        }
    }

    private void onItemUndoActionClicked() {
        int position = getDataProvider().undoLastRemoval();
        if (position >= 0) {
            final Fragment fragment = getSupportFragmentManager().findFragmentByTag(FragmentTags.FRAGMENT_LIST_VIEW);
            ((WordRecyclerViewFragment) fragment).notifyItemInserted(position);
        }
    }

    @Override
    public void onNotifyItemPinnedDialogDismissed(int itemPosition, boolean ok) {
        final Fragment fragment = getSupportFragmentManager().findFragmentByTag(FragmentTags.FRAGMENT_LIST_VIEW);

        getDataProvider().getItem(itemPosition).setPinned(ok);
        ((WordRecyclerViewFragment) fragment).notifyItemChanged(itemPosition);
    }

    @Override
    public void OnWordClicked(WordFromText wordFromText, int position) {

        AppDatabaseManager.setCurrentPageForAddingWord(position);

        final DialogFragment dialog = WordOutTranslatorDialogFragment.newInstance(wordFromText);
        getSupportFragmentManager()
                .beginTransaction()
                .add(dialog, FragmentTags.FRAGMENT_TAG_ITEM_PINNED_DIALOG)
                .commit();
    }

    public ExampleDataProvider getDataProvider() {
        final Fragment fragment = getSupportFragmentManager().findFragmentByTag(FragmentTags.FRAGMENT_TAG_DATA_PROVIDER);
        return ((ExampleDataProviderFragment) fragment).getDataProvider();
    }

    @Override
    public void onTranslatedWord(TranslatedWord translatedWord) {

        ReaderDictionary readerDictionary = new ReaderDictionary(this);

        boolean succeed = readerDictionary.addTranslatedWord(translatedWord);

        AppDatabaseManager.createOrUpdateWord(translatedWord.getWord(),
                translatedWord.getTranslation(),
                translatedWord.getContext(),
                succeed);
    }
}







