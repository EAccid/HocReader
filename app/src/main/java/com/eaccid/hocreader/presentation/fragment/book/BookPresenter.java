package com.eaccid.hocreader.presentation.fragment.book;

import android.util.Log;
import com.eaccid.bookreader.file.BaseFileImpl;
import com.eaccid.bookreader.file.pagesplitter.Page;
import com.eaccid.bookreader.file.pagesplitter.TxtFileToScreenReader;
import com.eaccid.bookreader.wordgetter.WordFromText;
import com.eaccid.hocreader.data.local.AppDatabaseManager;
import com.eaccid.hocreader.data.remote.ReaderDictionary;
import com.eaccid.hocreader.data.remote.TranslatedWord;
import com.eaccid.hocreader.presentation.BasePresenter;
import com.eaccid.hocreader.presentation.activity.pager.PagerActivity;
import com.eaccid.hocreader.presentation.activity.pager.PagerPresenter;
import java.util.ArrayList;
import java.util.List;
import rx.Subscriber;
import rx.schedulers.Schedulers;

public class BookPresenter implements BasePresenter<BookFragment> {
    private final String logTAG = "BookPresenter";
    private AppDatabaseManager dataManager;
    private BookFragment mView;
    private List<Page<String>> mPagesList;

    public BookPresenter() {
        mPagesList = new ArrayList<>();
    }

    @Override
    public void attachView(BookFragment bookFragment) {
        mView = bookFragment;
        Log.i(logTAG, mView.getClass().getName() + "' has been attached.");
        setDataManager();
        setDataToList();
    }

    @Override
    public void detachView() {
        Log.i(logTAG, mView.getClass().getName() + "' has been detached.");
        mView = null;
    }

    private void setDataManager() {
        PagerPresenter pp = ((PagerActivity) mView.getActivity()).getPresenter();
        dataManager = pp.getDataManager();
    }

    private void setDataToList() {

        TxtFileToScreenReader txtFileToScreenReader = new TxtFileToScreenReader(mView.getActivity());
        BaseFileImpl baseFile = new BaseFileImpl(dataManager.getCurrentBookPath());
        txtFileToScreenReader.getPageObservable(baseFile)
                .subscribeOn(Schedulers.io()).subscribe(new Subscriber<Page<String>>() {
            @Override
            public void onCompleted() {
                mView.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable e) {
                //TODO on error
            }

            @Override
            public void onNext(Page<String> page) {
                mPagesList.add(page);
            }
        });

    }

    public BookRecyclerViewAdapter createRecyclerViewAdapter() {
        return new BookRecyclerViewAdapter(mPagesList);
    }

    public void goToPageClicked(CharSequence input) {
        int currentPagePosition = dataManager.getCurrentPage() - 1;
        int fromPage = Integer.parseInt(input.toString()) - 1;
        mView.scrollToListPosition(fromPage, currentPagePosition);
        mView.showSnackBackToLastOpenedPage(currentPagePosition, fromPage);
    }

    public void onWordTranslated(TranslatedWord translatedWord) {
        ReaderDictionary readerDictionary = new ReaderDictionary(mView.getContext());
        boolean succeed = readerDictionary.addTranslatedWord(translatedWord);
        dataManager.createOrUpdateWord(translatedWord.getWordBaseForm(),
                translatedWord.getTranslation(),
                translatedWord.getContext(),
                succeed);

        //TODO del from here
        PagerPresenter pp = ((PagerActivity) mView.getActivity()).getPresenter();
        pp.addWordToWordListProvider(translatedWord.getWordBaseForm());
    }

    public void OnWordFromTextViewClicked(WordFromText wordFromText) {
        dataManager.setCurrentPageForAddingWord(wordFromText.getPageNumber());
    }
}
